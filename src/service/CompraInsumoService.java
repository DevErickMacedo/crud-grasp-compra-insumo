package service;

import domain.CompraInsumo;
import domain.StatusCompra;
import dto.CompraInsumoRequest;
import exception.RegraNegocioException;
import repository.CompraInsumoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CompraInsumoService {

    private final CompraInsumoRepository compraInsumoRepository;

    /**
     * Cria o serviço com a dependência necessária injetada manualmente.
     *
     * @param compraInsumoRepository repositório de CompraInsumo
     */
    public CompraInsumoService(CompraInsumoRepository compraInsumoRepository) {
        this.compraInsumoRepository = compraInsumoRepository;
    }

    /**
     * Cadastra uma nova CompraInsumo aplicando as regras de negócio:
     * <ul>
     *   <li>Descrição obrigatória e mínimo de 3 caracteres (validado no domínio)</li>
     *   <li>Valor total maior que zero (validado no domínio)</li>
     *   <li>Data do pedido preenchida automaticamente (pelo construtor da entidade)</li>
     *   <li>Status inicial definido automaticamente como {@link StatusCompra#SOLICITADO}</li>
     * </ul>
     *
     * <p><b>Padrão GRASP: Creator</b> — este método cria a instância de
     * {@link CompraInsumo} pois possui os dados do DTO e o id gerado.</p>
     *
     * @param request dados da nova compra
     * @return CompraInsumo criada e persistida
     * @throws IllegalArgumentException se descrição ou valor violarem regras do domínio
     */
    public CompraInsumo cadastrar(CompraInsumoRequest request) {
        CompraInsumo compra = new CompraInsumo(
                UUID.randomUUID().toString(),
                request.getDescricao(),
                request.getValorTotal()
        );
        compraInsumoRepository.salvar(compra);
        return compra;
    }

    /**
     * Retorna todas as CompraInsumo cadastradas.
     *
     * @return lista completa de compras
     */
    public List<CompraInsumo> listarTodas() {
        return compraInsumoRepository.listarTodas();
    }

    /**
     * Retorna todas as CompraInsumo que possuem o status informado.
     *
     * @param status status a filtrar
     * @return lista de compras com o status especificado
     */
    public List<CompraInsumo> listarPorStatus(StatusCompra status) {
        return compraInsumoRepository.listarPorStatus(status);
    }

    /**
     * Busca uma CompraInsumo pelo seu identificador único.
     *
     * @param id identificador da compra
     * @return CompraInsumo encontrada
     * @throws RegraNegocioException se não existir compra com o id informado
     */
    public CompraInsumo buscarPorId(String id) {
        return compraInsumoRepository.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException(
                        "CompraInsumo com id '" + id + "' não encontrada."));
    }

    /**
     * Avança o status de uma CompraInsumo para o próximo estado permitido
     * na sequência {@code SOLICITADO → APROVADO → RECEBIDO}.
     *
     * <p><b>Regra de negócio (Tema 9)</b>: a transição de status deve seguir
     * a ordem definida. Não é permitido voltar ao status anterior nem
     * pular etapas. Ao atingir {@code RECEBIDO}, a data de recebimento é
     * preenchida automaticamente com a data atual.</p>
     *
     * <p><b>Regra de negócio (Tema 9)</b>: não é permitido receber
     * ({@code RECEBIDO}) uma compra que não foi aprovada ({@code APROVADO}).
     * Esta regra é garantida pela própria sequência da máquina de estados.</p>
     *
     * @param id identificador da compra a avançar
     * @return CompraInsumo com o novo status aplicado
     * @throws RegraNegocioException se a compra não for encontrada ou já estiver no estado final
     */
    public CompraInsumo avancarStatus(String id) {
        CompraInsumo compra = buscarPorId(id);

        if (compra.getStatus().isFinal()) {
            throw new RegraNegocioException(
                    "A compra já está no estado final RECEBIDO e não pode avançar.");
        }

        StatusCompra novoStatus = compra.getStatus().proximoPermitido();
        compra.setStatus(novoStatus);

        if (novoStatus == StatusCompra.RECEBIDO) {
            compra.setDataRecebimento(LocalDate.now());
        }

        compraInsumoRepository.atualizar(compra);
        return compra;
    }

    /**
     * Atualiza a descrição e o valor total de uma CompraInsumo existente.
     *
     * <p>Apenas compras no status {@code SOLICITADO} podem ter seus dados
     * editados, pois compras aprovadas ou recebidas já estão em processamento.</p>
     *
     * @param id      identificador da compra a atualizar
     * @param request novos dados da compra
     * @return CompraInsumo atualizada
     * @throws RegraNegocioException    se a compra não for encontrada ou não estiver como SOLICITADO
     * @throws IllegalArgumentException se os novos dados violarem regras do domínio
     */
    public CompraInsumo atualizar(String id, CompraInsumoRequest request) {
        CompraInsumo existente = buscarPorId(id);

        if (existente.getStatus() != StatusCompra.SOLICITADO) {
            throw new RegraNegocioException(
                    "Somente compras com status SOLICITADO podem ser editadas. "
                            + "Status atual: " + existente.getStatus());
        }

        existente.setDescricao(request.getDescricao());
        existente.setValorTotal(request.getValorTotal());
        compraInsumoRepository.atualizar(existente);
        return existente;
    }

    /**
     * Remove uma CompraInsumo pelo seu identificador único.
     *
     * <p>Apenas compras no status {@code SOLICITADO} podem ser removidas.</p>
     *
     * @param id identificador da compra a remover
     * @throws RegraNegocioException se a compra não for encontrada ou não estiver como SOLICITADO
     */
    public void remover(String id) {
        CompraInsumo compra = buscarPorId(id);
        if (compra.getStatus() != StatusCompra.SOLICITADO) {
            throw new RegraNegocioException(
                    "Somente compras com status SOLICITADO podem ser removidas. "
                            + "Status atual: " + compra.getStatus());
        }
        compraInsumoRepository.remover(id);
    }

}
