package dto;

public class CompraInsumoRequest {

    private String descricao;
    private double valorTotal;

    /**
     * Construtor padrão.
     */
    public CompraInsumoRequest() {}

    /**
     * Cria um CompraInsumoRequest com todos os dados necessários.
     *
     * @param descricao  descrição dos insumos
     * @param valorTotal valor total da compra
     */
    public CompraInsumoRequest(String descricao, double valorTotal) {
        this.descricao = descricao;
        this.valorTotal = valorTotal;
    }

    /**
     * Retorna a descrição informada para a CompraInsumo.
     *
     * @return descricao
     */
    public String getDescricao() { return descricao; }

    /**
     * Define a descrição da compra.
     *
     * @param descricao descrição dos insumos
     */
    public void setDescricao(String descricao) { this.descricao = descricao; }

    /**
     * Retorna o valor total informado para a compra.
     *
     * @return valorTotal
     */
    public double getValorTotal() { return valorTotal; }

    /**
     * Define o valor total da compra.
     *
     * @param valorTotal valor total
     */
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

}
