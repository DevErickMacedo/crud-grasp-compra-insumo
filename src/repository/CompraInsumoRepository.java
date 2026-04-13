package repository;

import domain.CompraInsumo;
import domain.StatusCompra;

import java.util.List;
import java.util.Optional;

public interface CompraInsumoRepository {

    /**
     * Persiste uma nova CompraInsumo no repositório.
     *
     * @param compra entidade a ser salva
     */
    void salvar(CompraInsumo compra);

    /**
     * Retorna todas as CompraInsumo armazenadas.
     *
     * @return lista completa de compras
     */
    List<CompraInsumo> listarTodas();

    /**
     * Retorna todas as CompraInsumo que possuem o status informado.
     *
     * @param status status a filtrar
     * @return lista de compras com o status especificado
     */
    List<CompraInsumo> listarPorStatus(StatusCompra status);

    /**
     * Busca uma CompraInsumo pelo seu identificador único.
     *
     * @param id identificador da compra
     * @return {@code Optional} contendo a compra, ou vazio se não encontrada
     */
    Optional<CompraInsumo> buscarPorId(String id);

    /**
     * Atualiza os dados de uma CompraInsumo existente.
     *
     * <p>Utilizado tanto para atualização de dados quanto para
     * persistir a transição de status.</p>
     *
     * @param compra entidade com dados atualizados
     */
    void atualizar(CompraInsumo compra);

    /**
     * Remove uma CompraInsumo pelo seu identificador único.
     *
     * @param id identificador da compra a remover
     */
    void remover(String id);

}
