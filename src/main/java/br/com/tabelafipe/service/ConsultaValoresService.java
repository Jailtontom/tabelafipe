package br.com.tabelafipe.service;

import br.com.tabelafipe.gateway.FipeGateway;
import br.com.tabelafipe.model.Ano;
import br.com.tabelafipe.model.Marca;
import br.com.tabelafipe.model.Modelo;
import br.com.tabelafipe.model.ModeloComValor;
import br.com.tabelafipe.model.ResultadoConsulta;
import br.com.tabelafipe.model.Veiculo;
import br.com.tabelafipe.strategy.SelecaoAnoStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Facade: um único método esconde as 4 chamadas da FIPE
 * (marcas -> modelos -> anos -> valor) e devolve o resultado pronto.
 */
public class ConsultaValoresService {

    /** Poucas threads de propósito: a API pública tem limite de requisições. */
    private static final int MAX_CHAMADAS_SIMULTANEAS = 4;
    private static final String VALOR_INDISPONIVEL = "indisponível";

    private final FipeGateway fipe;
    private final SelecaoAnoStrategy selecaoAno;

    private List<Marca> marcasCache;

    public ConsultaValoresService(FipeGateway fipe, SelecaoAnoStrategy selecaoAno) {
        this.fipe = fipe;
        this.selecaoAno = selecaoAno;
    }

    /**
     * @param marcaInformada código ("59") ou parte do nome ("volks")
     * @param modeloInformado parte do nome do modelo ("amarok")
     */
    public ResultadoConsulta consultar(String marcaInformada, String modeloInformado) {
        Marca marca = encontrarMarca(marcaInformada);

        List<Modelo> modelos = fipe.listarModelos(marca.codigo()).stream()
                .filter(m -> contemIgnorandoCaixa(m.nome(), modeloInformado))
                .toList();

        if (modelos.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nenhum modelo com \"" + modeloInformado + "\" na marca " + marca.nome());
        }

        List<ModeloComValor> comValores = buscarValoresEmParalelo(marca.codigo(), modelos);
        return new ResultadoConsulta(marca.codigo(), marca.nome(), comValores);
    }

    private Marca encontrarMarca(String marcaInformada) {
        String termo = marcaInformada.trim();
        List<Marca> marcas = fipe.listarMarcas();

        return marcas.stream()
                .filter(m -> m.codigo().equals(termo))                       // 1º: pelo código
                .findFirst()
                .or(() -> marcas.stream()
                        .filter(m -> contemIgnorandoCaixa(m.nome(), termo))   // 2º: pelo nome
                        .findFirst())
                .orElseThrow(() -> new IllegalArgumentException("Marca não encontrada: " + marcaInformada));
    }

    private List<ModeloComValor> buscarValoresEmParalelo(String codigoMarca, List<Modelo> modelos) {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CHAMADAS_SIMULTANEAS);
        try {
            List<CompletableFuture<ModeloComValor>> futuros = modelos.stream()
                    .map(m -> CompletableFuture.supplyAsync(() -> buscarValor(codigoMarca, m), executor))
                    .toList();

            return futuros.stream()
                    .map(CompletableFuture::join)
                    .sorted(Comparator.comparing(ModeloComValor::nome))
                    .toList();
        } finally {
            executor.shutdown();
        }
    }

    /** Uma falha em um modelo não derruba a consulta inteira. */
    private ModeloComValor buscarValor(String codigoMarca, Modelo modelo) {
        try {
            Optional<Ano> ano = selecaoAno.selecionar(fipe.listarAnos(codigoMarca, modelo.codigo()));
            String valor = ano
                    .map(a -> fipe.buscarVeiculo(codigoMarca, modelo.codigo(), a.codigo()))
                    .map(Veiculo::valor)
                    .orElse(VALOR_INDISPONIVEL);
            return new ModeloComValor(modelo.codigo(), modelo.nome(), valor);
        } catch (RuntimeException e) {
            System.err.println("Falha ao buscar valor de " + modelo.nome() + ": " + e.getMessage());
            return new ModeloComValor(modelo.codigo(), modelo.nome(), VALOR_INDISPONIVEL);
        }
    }

    private static boolean contemIgnorandoCaixa(String texto, String termo) {
        return texto.toLowerCase().contains(termo.trim().toLowerCase());
    }

    /** Lista de marcas ordenada por código, para exibir ao usuário. */
    public List<Marca> listarMarcas() {
        if (marcasCache == null) {
            marcasCache = fipe.listarMarcas().stream()
                    .sorted(Comparator.comparingInt(m -> Integer.parseInt(m.codigo())))
                    .toList();
        }
        return marcasCache;
    }
}
