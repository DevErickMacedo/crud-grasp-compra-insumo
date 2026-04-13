package domain;

public enum StatusCompra {

    /** Compra recém-criada, aguardando aprovação. */
    SOLICITADO,

    /** Compra aprovada, aguardando recebimento dos insumos. */
    APROVADO,

    /** Insumos recebidos. Estado final da compra. */
    RECEBIDO;

    /**
     * Retorna o próximo status permitido na progressão linear de estados.
     *
     * <p><b>Regra de negócio (Tema 9)</b>: a transição de status deve seguir
     * a ordem {@code SOLICITADO → APROVADO → RECEBIDO}. Não é permitido
     * retroceder nem pular etapas.</p>
     *
     * @return próximo {@link StatusCompra} na sequência
     * @throws IllegalStateException se o status atual já for o estado final ({@code RECEBIDO})
     */
    public StatusCompra proximoPermitido() {
        return switch (this) {
            case SOLICITADO -> APROVADO;
            case APROVADO   -> RECEBIDO;
            case RECEBIDO   -> throw new IllegalStateException(
                    "A compra já está no estado final RECEBIDO e não pode avançar.");
        };
    }

    /**
     * Verifica se este status representa o estado final da compra.
     *
     * @return {@code true} se o status for {@code RECEBIDO}
     */
    public boolean isFinal() {
        return this == RECEBIDO;
    }

}
