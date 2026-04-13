import controller.CompraInsumoController;
import repository.CompraInsumoRepository;
import repository.json.CompraInsumoRepositoryJson;
import service.CompraInsumoService;
import util.JsonMini;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // 1. Utilitário de JSON (Pure Fabrication)
        JsonMini jsonMini = new JsonMini();

        // 2. Implementação concreta do repositório (Pure Fabrication)
        //    Carrega automaticamente os dados do arquivo JSON na inicialização.
        CompraInsumoRepository compraInsumoRepository =
                new CompraInsumoRepositoryJson(jsonMini);

        // 3. Serviço — recebe a interface, nunca a implementação concreta (Low Coupling)
        CompraInsumoService compraInsumoService =
                new CompraInsumoService(compraInsumoRepository);

        // 4. Controller — recebe o serviço e o scanner do terminal
        Scanner scanner = new Scanner(System.in);
        CompraInsumoController controller =
                new CompraInsumoController(compraInsumoService, scanner);

        // 5. Inicia o loop do menu textual
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   Bem-vindo ao Sistema de Compras de       ║");
        System.out.println("║   Insumo — Feira Livre · GRASP CRUD        ║");
        System.out.println("╚════════════════════════════════════════════╝");
        controller.iniciar();

        scanner.close();

    }
}