package br.com.tabelafipe.model;

import java.util.List;

/** Saída final: marca + lista de modelos com valor. */
public record ResultadoConsulta(String codigo,
                                String nome,
                                List<ModeloComValor> dadosComValores) {
}
