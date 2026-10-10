package br.com.tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** Item de saída: {"codigo":5940,"nome":"AMAROK ...","Valor":"R$ 84.753,00"} */
@JsonPropertyOrder({"codigo", "nome", "Valor"})
public record ModeloComValor(Integer codigo,
                             String nome,
                             @JsonProperty("Valor") String valor) {
}
