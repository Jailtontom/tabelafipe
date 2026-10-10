package br.com.tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Item da lista "modelos" de /marcas/{marca}/modelos  ->  {"codigo":5940,"nome":"AMAROK ..."} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Modelo(Integer codigo, String nome) {
}
