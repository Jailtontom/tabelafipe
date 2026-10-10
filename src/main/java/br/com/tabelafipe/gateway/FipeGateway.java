package br.com.tabelafipe.gateway;

import br.com.tabelafipe.model.Ano;
import br.com.tabelafipe.model.Marca;
import br.com.tabelafipe.model.Modelo;
import br.com.tabelafipe.model.Veiculo;

import java.util.List;

/**
 * Gateway: o resto do sistema conversa com a FIPE só por esta interface.
 * Não sabe nada de HTTP, URL ou JSON — isso facilita testar (basta um fake)
 * e trocar a fonte de dados no futuro.
 */
public interface FipeGateway {

    List<Marca> listarMarcas();

    List<Modelo> listarModelos(String codigoMarca);

    List<Ano> listarAnos(String codigoMarca, Integer codigoModelo);

    Veiculo buscarVeiculo(String codigoMarca, Integer codigoModelo, String codigoAno);
}
