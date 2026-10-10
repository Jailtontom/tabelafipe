package br.com.tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Item de /carros/marcas  ->  {"codigo":"59","nome":"VW - VolksWagen"} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Marca(String codigo, String nome) {
}
