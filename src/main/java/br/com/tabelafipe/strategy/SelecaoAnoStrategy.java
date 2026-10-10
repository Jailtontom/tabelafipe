package br.com.tabelafipe.strategy;

import br.com.tabelafipe.model.Ano;

import java.util.List;
import java.util.Optional;

/**
 * Strategy: cada modelo tem vários anos (e um valor por ano).
 * A regra de QUAL ano usar fica isolada aqui — dá para trocar sem mexer no serviço.
 */
@FunctionalInterface
public interface SelecaoAnoStrategy {
    Optional<Ano> selecionar(List<Ano> anos);
}
