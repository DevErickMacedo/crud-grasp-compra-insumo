package domain;

import java.time.LocalDate;

public class CompraInsumo {

    private String id;
    private String descricao;
    private double valorTotal;
    private LocalDate dataPedido;
    private LocalDate dataRecebimento;
    private StatusCompra status;

    /**
     * Construtor padrão exigido para desserialização JSON.
     */
    public CompraInsumo() {}

    /**
     * Cria uma CompraInsumo com os dados fornecidos, preenchendo automaticamente
     * a data do pedido e iniciando o status como {@link StatusCompra#SOLICITADO}.
     *
     * <p><b>Regra de negócio</b>: a {@code dataPedido} é preenchida com
     * {@code LocalDate.now()} sem intervenção do usuário.</p>
     *
     * @param id          identificador único da compra
     * @param descricao   descrição dos insumos (obrigatória, mínimo 3 caracteres)
     * @param valorTotal  valor total da compra (deve ser maior que zero)
     * @throws IllegalArgumentException se descrição ou valor forem inválidos
     */
    public CompraInsumo(String id, String descricao, double valorTotal) {
        this.id = id;
        setDescricao(descricao);
        setValorTotal(valorTotal);
        this.dataPedido = LocalDate.now();
        this.status = StatusCompra.SOLICITADO;
        this.dataRecebimento = null;
    }

    /**
     * Retorna o identificador único da CompraInsumo.
     *
     * @return id
     */
    public String getId() { return id; }

    /**
     * Define o identificador único da CompraInsumo.
     *
     * @param id identificador
     */
    public void setId(String id) { this.id = id; }

    /**
     * Retorna a descrição dos insumos da compra.
     *
     * @return descricao
     */
    public String getDescricao() { return descricao; }

    /**
     * Define a descrição da compra aplicando as regras de domínio:
     * <ul>
     *   <li>descrição não pode ser nula ou vazia</li>
     *   <li>descrição deve ter no mínimo 3 caracteres</li>
     * </ul>
     *
     * @param descricao descrição dos insumos
     * @throws IllegalArgumentException se a descrição for inválida
     */
    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição da CompraInsumo é obrigatória.");
        }
        if (descricao.trim().length() < 3) {
            throw new IllegalArgumentException("A descrição deve ter no mínimo 3 caracteres.");
        }
        this.descricao = descricao.trim();
    }

    /**
     * Retorna o valor total da compra.
     *
     * @return valorTotal
     */
    public double getValorTotal() { return valorTotal; }

    /**
     * Define o valor total da compra, garantindo que seja maior que zero.
     *
     * <p><b>Regra de negócio (Tema 9)</b>: o valor total da compra deve
     * ser maior que zero.</p>
     *
     * @param valorTotal valor da compra
     * @throws IllegalArgumentException se o valor for menor ou igual a zero
     */
    public void setValorTotal(double valorTotal) {
        if (valorTotal <= 0) {
            throw new IllegalArgumentException("O valor total da compra deve ser maior que zero.");
        }
        this.valorTotal = valorTotal;
    }

    /**
     * Retorna a data em que o pedido de compra foi criado.
     *
     * <p>Preenchida automaticamente no momento do cadastro.</p>
     *
     * @return dataPedido
     */
    public LocalDate getDataPedido() { return dataPedido; }

    /**
     * Define a data do pedido (usado na desserialização JSON).
     *
     * @param dataPedido data do pedido
     */
    public void setDataPedido(LocalDate dataPedido) { this.dataPedido = dataPedido; }

    /**
     * Retorna a data em que os insumos foram recebidos, ou {@code null}
     * caso ainda não tenham sido recebidos.
     *
     * @return dataRecebimento ou {@code null}
     */
    public LocalDate getDataRecebimento() { return dataRecebimento; }

    /**
     * Define a data de recebimento dos insumos.
     *
     * @param dataRecebimento data efetiva de recebimento
     */
    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    /**
     * Retorna o status atual da compra.
     *
     * @return status
     */
    public StatusCompra getStatus() { return status; }

    /**
     * Define o status da compra (usado na desserialização JSON e pelo serviço
     * ao aplicar transições de estado).
     *
     * @param status novo status
     */
    public void setStatus(StatusCompra status) { this.status = status; }

    @Override
    public String toString() {
        return "CompraInsumo{id='" + id + "', descricao='" + descricao
                + "', valorTotal=" + valorTotal
                + ", dataPedido=" + dataPedido
                + ", dataRecebimento=" + dataRecebimento
                + ", status=" + status + "}";
    }

}
