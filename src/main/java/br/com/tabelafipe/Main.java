package br.com.tabelafipe;

import br.com.tabelafipe.gateway.FipeApiGateway;
import br.com.tabelafipe.model.ResultadoConsulta;
import br.com.tabelafipe.service.ConsultaValoresService;
import br.com.tabelafipe.service.ConsumoApi;
import br.com.tabelafipe.service.ConverteDados;
import br.com.tabelafipe.strategy.AnoMaisRecenteStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {
        // Montagem das dependências (injeção manual pelo construtor)
        var service = new ConsultaValoresService(
                new FipeApiGateway(new ConsumoApi(), new ConverteDados()),
                new AnoMaisRecenteStrategy());

        System.out.println("=== MARCAS DISPONÍVEIS ===");
        service.listarMarcas()
                .forEach(m -> System.out.printf("Cód: %-4s | %s%n", m.codigo(), m.nome()));

        Scanner leitura = new Scanner(System.in);
        System.out.print("\nInforme o código da marca: ");
        String marca = leitura.nextLine();
        System.out.print("Modelo (ex.: AMAROK): ");
        String modelo = leitura.nextLine();

        ResultadoConsulta resultado = service.consultar(marca, modelo);

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(resultado);
        System.out.println(json);
    }
}
