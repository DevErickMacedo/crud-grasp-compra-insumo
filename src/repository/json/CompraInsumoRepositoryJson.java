package repository.json;

import domain.CompraInsumo;
import domain.StatusCompra;
import repository.CompraInsumoRepository;
import util.JsonMini;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CompraInsumoRepositoryJson implements CompraInsumoRepository {

    private static final String CAMINHO = "data/compras_insumo.json";

    private final JsonMini jsonMini;
    private final List<CompraInsumo> cache;

    /**
     * Cria a implementação e carrega os dados existentes do arquivo JSON.
     *
     * @param jsonMini utilitário de leitura e escrita JSON
     */
    public CompraInsumoRepositoryJson(JsonMini jsonMini) {
        this.jsonMini = jsonMini;
        this.cache = new ArrayList<>();
        carregarDoArquivo();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Adiciona a compra ao cache em memória e persiste imediatamente no arquivo.</p>
     */
    @Override
    public void salvar(CompraInsumo compra) {
        cache.add(compra);
        persistir();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CompraInsumo> listarTodas() {
        return Collections.unmodifiableList(cache);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CompraInsumo> listarPorStatus(StatusCompra status) {
        return cache.stream()
                .filter(c -> c.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CompraInsumo> buscarPorId(String id) {
        return cache.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Localiza a compra pelo id e substitui o objeto no cache pelo atualizado,
     * persistindo imediatamente no arquivo.</p>
     */
    @Override
    public void atualizar(CompraInsumo compra) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equals(compra.getId())) {
                cache.set(i, compra);
                break;
            }
        }
        persistir();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Remove a compra do cache em memória e persiste imediatamente no arquivo.</p>
     */
    @Override
    public void remover(String id) {
        cache.removeIf(c -> c.getId().equals(id));
        persistir();
    }

    /**
     * Carrega as CompraInsumo do arquivo JSON para o cache em memória.
     * Executado apenas na inicialização da aplicação.
     */
    private void carregarDoArquivo() {
        List<Map<String, String>> lista = jsonMini.lerLista(CAMINHO);
        for (Map<String, String> mapa : lista) {
            CompraInsumo c = new CompraInsumo();
            c.setId(mapa.get("id"));
            c.setDescricao(mapa.get("descricao"));
            c.setValorTotal(Double.parseDouble(mapa.get("valorTotal")));
            c.setDataPedido(LocalDate.parse(mapa.get("dataPedido")));
            c.setStatus(StatusCompra.valueOf(mapa.get("status")));

            String dataRec = mapa.get("dataRecebimento");
            if (dataRec != null && !dataRec.equals("null")) {
                c.setDataRecebimento(LocalDate.parse(dataRec));
            }
            cache.add(c);
        }
    }

    /**
     * Serializa o cache atual e sobrescreve o arquivo JSON.
     * Chamado após cada operação de escrita para garantir consistência.
     */
    private void persistir() {
        List<Map<String, String>> lista = new ArrayList<>();
        for (CompraInsumo c : cache) {
            Map<String, String> mapa = new LinkedHashMap<>();
            mapa.put("id", c.getId());
            mapa.put("descricao", c.getDescricao());
            mapa.put("valorTotal", String.valueOf(c.getValorTotal()));
            mapa.put("dataPedido", c.getDataPedido().toString());
            mapa.put("dataRecebimento",
                    c.getDataRecebimento() != null ? c.getDataRecebimento().toString() : "null");
            mapa.put("status", c.getStatus().name());
            lista.add(mapa);
        }
        jsonMini.escreverLista(CAMINHO, lista);
    }

}
