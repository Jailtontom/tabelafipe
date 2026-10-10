package br.com.tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Item de /modelos/{modelo}/anos  ->  {"codigo":"2014-3","nome":"2014 Diesel"} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Ano(String codigo, String nome) {

    /** Código 32000 é como a FIPE representa veículo "Zero KM". */
    public static final int ZERO_KM = 32000;

    /** "2014-3" -> 2014 */
    public int anoModelo() {
        return Integer.parseInt(codigo.split("-")[0]);
    }

    public boolean isZeroKm() {
        return anoModelo() == ZERO_KM;
    }
}
