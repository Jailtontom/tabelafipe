package br.com.tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Resposta de /anos/{ano}  ->  {"Valor":"R$ 84.753,00","Marca":"VW - VolksWagen",...} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Veiculo(@JsonAlias("Valor") String valor,
                      @JsonAlias("Modelo") String modelo,
                      @JsonAlias("AnoModelo") Integer anoModelo,
                      @JsonAlias("Combustivel") String combustivel,
                      @JsonAlias("MesReferencia") String mesReferencia) {
}
