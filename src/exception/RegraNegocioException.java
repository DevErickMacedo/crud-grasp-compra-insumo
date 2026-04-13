package exception;

public class RegraNegocioException extends RuntimeException {

    /**
     * Cria uma exceção de negócio com a mensagem descritiva do erro.
     *
     * @param mensagem descrição clara da regra de negócio violada
     */
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }

}
