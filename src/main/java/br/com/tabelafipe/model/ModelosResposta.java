package br.com.tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/** /marcas/{marca}/modelos devolve um OBJETO: {"modelos":[...], "anos":[...]} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ModelosResposta(List<Modelo> modelos) {
}
