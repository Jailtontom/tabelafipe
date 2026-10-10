package br.com.tabelafipe.gateway;

import br.com.tabelafipe.model.Ano;
import br.com.tabelafipe.model.Marca;
import br.com.tabelafipe.model.Modelo;
import br.com.tabelafipe.model.ModelosResposta;
import br.com.tabelafipe.model.Veiculo;
import br.com.tabelafipe.service.ConsumoApi;
import br.com.tabelafipe.service.IConverteDados;

import java.util.List;

/** Implementação real do gateway, usando a API pública parallelum. */
public class FipeApiGateway implements FipeGateway {

    private static final String BASE = "https://parallelum.com.br/fipe/api/v1/carros/marcas";

    private final ConsumoApi consumo;
    private final IConverteDados conversor;

    public FipeApiGateway(ConsumoApi consumo, IConverteDados conversor) {
        this.consumo = consumo;
        this.conversor = conversor;
    }

    @Override
    public List<Marca> listarMarcas() {
        return conversor.obterLista(consumo.obterDados(BASE), Marca.class);          // JSON começa com [
    }

    @Override
    public List<Modelo> listarModelos(String codigoMarca) {
        String json = consumo.obterDados(BASE + "/" + codigoMarca + "/modelos");
        return conversor.obterDados(json, ModelosResposta.class).modelos();           // JSON começa com {
    }

    @Override
    public List<Ano> listarAnos(String codigoMarca, Integer codigoModelo) {
        String url = BASE + "/" + codigoMarca + "/modelos/" + codigoModelo + "/anos";
        return conversor.obterLista(consumo.obterDados(url), Ano.class);              // JSON começa com [
    }

    @Override
    public Veiculo buscarVeiculo(String codigoMarca, Integer codigoModelo, String codigoAno) {
        String url = BASE + "/" + codigoMarca + "/modelos/" + codigoModelo + "/anos/" + codigoAno;
        return conversor.obterDados(consumo.obterDados(url), Veiculo.class);          // JSON começa com {
    }
}
