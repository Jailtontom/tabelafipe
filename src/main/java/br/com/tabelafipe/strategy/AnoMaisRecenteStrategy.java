package br.com.tabelafipe.strategy;

import br.com.tabelafipe.model.Ano;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Usa o ano-modelo mais recente; "Zero KM" (32000) só se for a única opção. */
public class AnoMaisRecenteStrategy implements SelecaoAnoStrategy {

    @Override
    public Optional<Ano> selecionar(List<Ano> anos) {
        Comparator<Ano> porAno = Comparator.comparingInt(Ano::anoModelo);
        return anos.stream()
                .filter(a -> !a.isZeroKm())
                .max(porAno)
                .or(() -> anos.stream().max(porAno));
    }
}
