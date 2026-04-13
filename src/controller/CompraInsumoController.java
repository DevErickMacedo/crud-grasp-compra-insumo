package controller;

import domain.CompraInsumo;
import domain.StatusCompra;
import dto.CompraInsumoRequest;
import exception.RegraNegocioException;
import service.CompraInsumoService;

import java.util.List;
import java.util.Scanner;

public class CompraInsumoController {

    private final CompraInsumoService compraInsumoService;
    private final Scanner scanner;

    /**
     * Cria o controller com o serviço e o scanner injetados pelo {@code Main}.
     *
     * @param compraInsumoService serviço de CompraInsumo
     * @param scanner             leitor de entrada do terminal
     */
    public CompraInsumoController(CompraInsumoService compraInsumoService, Scanner scanner) {
        this.compraInsumoService = compraInsumoService;
        this.scanner = scanner;
    }

    /**
     * Inicia o loop principal do menu textual.
     *
     * <p>Exibe as opções disponíveis, lê a escolha do usuário e delega para
     * o método correspondente. O loop encerra quando o usuário escolhe a
     * opção de saída.</p>
     */
    public void iniciar() {
        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Opção: ");
            processarOpcao(opcao);
        } while (opcao != 0);
    }

    /**
     * Exibe o menu principal no terminal.
     */
    private void exibirMenu() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE COMPRAS DE INSUMO           ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Cadastrar CompraInsumo               ║");
        System.out.println("║  2. Listar todas as compras              ║");
        System.out.println("║  3. Listar compras por status            ║");
        System.out.println("║  4. Buscar compra por ID                 ║");
        System.out.println("║  5. Avançar status da compra             ║");
        System.out.println("║  6. Atualizar dados da compra            ║");
        System.out.println("║  7. Remover compra                       ║");
        System.out.println("║  0. Sair                                 ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    /**
     * Processa a opção escolhida pelo usuário, delegando para o método adequado.
     *
     * @param opcao número da opção selecionada no menu
     */
    private void processarOpcao(int opcao) {
        try {
            switch (opcao) {
                case 1 -> cadastrarCompra();
                case 2 -> listarTodas();
                case 3 -> listarPorStatus();
                case 4 -> buscarPorId();
                case 5 -> avancarStatus();
                case 6 -> atualizarCompra();
                case 7 -> removerCompra();
                case 0 -> System.out.println("\nEncerrando o sistema. Até logo!");
                default -> System.out.println("\n[AVISO] Opção inválida. Digite um número entre 0 e 7.");
            }
        } catch (RegraNegocioException | IllegalArgumentException e) {
            System.out.println("\n[ERRO] " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  Operações de CompraInsumo
    // ─────────────────────────────────────────────

    /**
     * Coleta os dados do terminal e delega o cadastro ao serviço.
     *
     * <p>A data do pedido e o status inicial são preenchidos automaticamente
     * pelo serviço e pelo construtor da entidade.</p>
     */
    private void cadastrarCompra() {
        System.out.println("\n--- Cadastrar CompraInsumo ---");
        String descricao = lerTexto("Descrição dos insumos: ");
        double valor = lerDouble("Valor total (ex: 150.00): ");

        CompraInsumo compra = compraInsumoService.cadastrar(
                new CompraInsumoRequest(descricao, valor));

        System.out.println("\n[OK] Compra cadastrada com sucesso!");
        exibirCompra(compra);
    }

    /**
     * Exibe todas as CompraInsumo cadastradas no terminal.
     */
    private void listarTodas() {
        System.out.println("\n--- Todas as CompraInsumo ---");
        List<CompraInsumo> lista = compraInsumoService.listarTodas();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma compra cadastrada.");
            return;
        }
        lista.forEach(this::exibirCompra);
    }

    /**
     * Solicita um status ao usuário e lista as compras filtradas.
     */
    private void listarPorStatus() {
        System.out.println("\n--- Listar por Status ---");
        System.out.println("Status disponíveis: SOLICITADO, APROVADO, RECEBIDO");
        StatusCompra status = lerStatus("Status desejado: ");
        List<CompraInsumo> lista = compraInsumoService.listarPorStatus(status);
        System.out.println("\n--- Compras com status " + status + " ---");
        if (lista.isEmpty()) {
            System.out.println("Nenhuma compra com esse status.");
            return;
        }
        lista.forEach(this::exibirCompra);
    }

    /**
     * Coleta o id do terminal e exibe os dados da compra encontrada.
     */
    private void buscarPorId() {
        System.out.println("\n--- Buscar CompraInsumo por ID ---");
        String id = lerTexto("ID da compra: ");
        CompraInsumo compra = compraInsumoService.buscarPorId(id);
        System.out.println("\n[OK] Compra encontrada:");
        exibirCompra(compra);
    }

    /**
     * Coleta o id do terminal e delega o avanço de status ao serviço.
     *
     * <p><b>Regra de negócio (Tema 9)</b>: a transição segue obrigatoriamente
     * a ordem {@code SOLICITADO → APROVADO → RECEBIDO}. Não é permitido
     * retroceder nem pular etapas.</p>
     */
    private void avancarStatus() {
        System.out.println("\n--- Avançar Status da Compra ---");
        listarTodas();
        String id = lerTexto("ID da compra para avançar status: ");
        CompraInsumo compra = compraInsumoService.avancarStatus(id);
        System.out.println("\n[OK] Status avançado com sucesso!");
        exibirCompra(compra);
    }

    /**
     * Coleta os novos dados do terminal e delega a atualização ao serviço.
     */
    private void atualizarCompra() {
        System.out.println("\n--- Atualizar CompraInsumo ---");
        listarTodas();
        String id = lerTexto("ID da compra a atualizar: ");

        CompraInsumo atual = compraInsumoService.buscarPorId(id);
        System.out.println("Dados atuais: descrição='" + atual.getDescricao()
                + "', valor=" + atual.getValorTotal());
        System.out.println("(Pressione Enter para manter o valor atual)");

        String descricao = lerTextoOpcional(
                "Nova descrição [" + atual.getDescricao() + "]: ", atual.getDescricao());
        String valorStr = lerTextoOpcional(
                "Novo valor [" + atual.getValorTotal() + "]: ",
                String.valueOf(atual.getValorTotal()));

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            System.out.println("[ERRO] Valor inválido. Operação cancelada.");
            return;
        }

        CompraInsumo atualizada = compraInsumoService.atualizar(
                id, new CompraInsumoRequest(descricao, valor));
        System.out.println("\n[OK] Compra atualizada com sucesso!");
        exibirCompra(atualizada);
    }

    /**
     * Coleta o id do terminal e delega a remoção ao serviço.
     */
    private void removerCompra() {
        System.out.println("\n--- Remover CompraInsumo ---");
        listarTodas();
        String id = lerTexto("ID da compra a remover: ");
        compraInsumoService.remover(id);
        System.out.println("\n[OK] Compra removida com sucesso!");
    }

    // ─────────────────────────────────────────────
    //  Utilitários de I/O
    // ─────────────────────────────────────────────

    /**
     * Exibe os dados formatados de uma CompraInsumo no terminal.
     *
     * @param c compra a exibir
     */
    private void exibirCompra(CompraInsumo c) {
        System.out.println("  ┌────────────────────────────────────────────");
        System.out.println("  │ ID:               " + c.getId());
        System.out.println("  │ Descrição:        " + c.getDescricao());
        System.out.printf ("  │ Valor total:      R$ %.2f%n", c.getValorTotal());
        System.out.println("  │ Data do pedido:   " + c.getDataPedido());
        System.out.println("  │ Data recebimento: " +
                (c.getDataRecebimento() != null ? c.getDataRecebimento() : "Pendente"));
        System.out.println("  │ Status:           " + c.getStatus());
        System.out.println("  └────────────────────────────────────────────");
    }

    /**
     * Lê uma linha de texto não vazia do terminal.
     *
     * @param prompt mensagem exibida ao usuário antes da leitura
     * @return texto digitado pelo usuário (sem espaços nas extremidades)
     */
    private String lerTexto(String prompt) {
        String valor;
        do {
            System.out.print(prompt);
            valor = scanner.nextLine().trim();
            if (valor.isEmpty()) System.out.println("[AVISO] Campo não pode ser vazio.");
        } while (valor.isEmpty());
        return valor;
    }

    /**
     * Lê uma linha de texto do terminal; se vazia, retorna o valor padrão.
     *
     * @param prompt      mensagem exibida ao usuário
     * @param valorPadrao valor retornado se o usuário pressionar Enter
     * @return texto digitado ou valor padrão
     */
    private String lerTextoOpcional(String prompt, String valorPadrao) {
        System.out.print(prompt);
        String valor = scanner.nextLine().trim();
        return valor.isEmpty() ? valorPadrao : valor;
    }

    /**
     * Lê um número inteiro do terminal, repetindo a leitura em caso de entrada inválida.
     *
     * @param prompt mensagem exibida ao usuário
     * @return inteiro digitado pelo usuário
     */
    private int lerInteiro(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Digite apenas números inteiros.");
            }
        }
    }

    /**
     * Lê um número decimal do terminal, repetindo a leitura em caso de entrada inválida.
     *
     * @param prompt mensagem exibida ao usuário
     * @return double digitado pelo usuário
     */
    private double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = scanner.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(linha);
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Digite um valor numérico válido (ex: 150.00).");
            }
        }
    }

    /**
     * Lê um {@link StatusCompra} do terminal pelo nome, repetindo em caso de entrada inválida.
     *
     * @param prompt mensagem exibida ao usuário
     * @return StatusCompra correspondente ao texto digitado
     */
    private StatusCompra lerStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = scanner.nextLine().trim().toUpperCase();
            try {
                return StatusCompra.valueOf(linha);
            } catch (IllegalArgumentException e) {
                System.out.println("[AVISO] Status inválido. Use: SOLICITADO, APROVADO ou RECEBIDO.");
            }
        }
    }

}
