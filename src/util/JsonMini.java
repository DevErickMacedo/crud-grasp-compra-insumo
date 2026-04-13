package util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonMini {

    public List<Map<String, String>> lerLista(String caminho) {
        List<Map<String, String>> resultado = new ArrayList<>();
        File arquivo = new File(caminho);
        if (!arquivo.exists()) return resultado;

        try {
            String conteudo = new String(Files.readAllBytes(arquivo.toPath())).trim();
            if (conteudo.isEmpty() || conteudo.equals("[]")) return resultado;

            conteudo = conteudo.substring(1, conteudo.lastIndexOf(']')).trim();
            List<String> objetos = separarObjetos(conteudo);

            for (String obj : objetos) {
                Map<String, String> mapa = new LinkedHashMap<>();
                obj = obj.trim();
                if (obj.startsWith("{")) obj = obj.substring(1);
                if (obj.endsWith("}")) obj = obj.substring(0, obj.length() - 1);

                for (String par : separarPares(obj)) {
                    String[] kv = par.split(":", 2);
                    if (kv.length < 2) continue;
                    String chave = kv[0].trim().replaceAll("\"", "");
                    String valor = kv[1].trim();
                    // Remove aspas de strings, mantém literais JSON (null, true, false, números)
                    if (valor.startsWith("\"") && valor.endsWith("\"")) {
                        valor = valor.substring(1, valor.length() - 1);
                    }
                    mapa.put(chave, valor);
                }
                resultado.add(mapa);
            }
        } catch (IOException e) {
            System.err.println("[JsonMini] Erro ao ler arquivo: " + caminho);
        }
        return resultado;
    }

    /**
     * Escreve uma lista de mapas em um arquivo JSON, sobrescrevendo o conteúdo anterior.
     *
     * <p>Valores {@code "null"} são escritos sem aspas no JSON. Valores numéricos
     * e booleanos também são escritos sem aspas.</p>
     *
     * <p>Cria os diretórios necessários caso não existam.</p>
     *
     * @param caminho caminho do arquivo JSON de destino
     * @param lista   lista de mapas a ser serializada
     */
    public void escreverLista(String caminho, List<Map<String, String>> lista) {
        try {
            File arquivo = new File(caminho);
            arquivo.getParentFile().mkdirs();

            StringBuilder sb = new StringBuilder("[\n");
            for (int i = 0; i < lista.size(); i++) {
                sb.append("  {");
                Map<String, String> mapa = lista.get(i);
                List<String> chaves = new ArrayList<>(mapa.keySet());
                for (int j = 0; j < chaves.size(); j++) {
                    String chave = chaves.get(j);
                    String valor = mapa.get(chave);
                    sb.append("\"").append(chave).append("\":");
                    if (ehLiteralJson(valor)) {
                        sb.append(valor);
                    } else {
                        sb.append("\"").append(valor).append("\"");
                    }
                    if (j < chaves.size() - 1) sb.append(",");
                }
                sb.append("}");
                if (i < lista.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("]");
            Files.write(arquivo.toPath(), sb.toString().getBytes());
        } catch (IOException e) {
            System.err.println("[JsonMini] Erro ao escrever arquivo: " + caminho);
        }
    }

    /**
     * Verifica se o valor deve ser escrito como literal JSON (sem aspas).
     * São literais: {@code null}, {@code true}, {@code false} e números.
     *
     * @param valor string a verificar
     * @return {@code true} se for um literal JSON
     */
    private boolean ehLiteralJson(String valor) {
        if (valor == null) return true;
        if (valor.equals("null") || valor.equals("true") || valor.equals("false")) return true;
        try { Double.parseDouble(valor); return true; } catch (NumberFormatException e) { return false; }
    }

    /**
     * Separa a string JSON em blocos correspondentes a cada objeto {@code {...}}.
     *
     * @param conteudo string sem os colchetes externos do array
     * @return lista de strings, cada uma representando um objeto JSON
     */
    private List<String> separarObjetos(String conteudo) {
        List<String> objetos = new ArrayList<>();
        int profundidade = 0;
        StringBuilder atual = new StringBuilder();
        for (char c : conteudo.toCharArray()) {
            if (c == '{') profundidade++;
            if (c == '}') profundidade--;
            atual.append(c);
            if (profundidade == 0 && atual.toString().trim().startsWith("{")) {
                objetos.add(atual.toString().trim());
                atual = new StringBuilder();
            }
        }
        return objetos;
    }

    /**
     * Separa os pares chave-valor de um objeto JSON já sem as chaves externas.
     *
     * @param conteudo string de pares separados por vírgula
     * @return lista de strings no formato {@code "chave":"valor"}
     */
    private List<String> separarPares(String conteudo) {
        List<String> pares = new ArrayList<>();
        boolean dentroAspas = false;
        StringBuilder atual = new StringBuilder();
        for (char c : conteudo.toCharArray()) {
            if (c == '"') dentroAspas = !dentroAspas;
            if (c == ',' && !dentroAspas) {
                pares.add(atual.toString().trim());
                atual = new StringBuilder();
            } else {
                atual.append(c);
            }
        }
        if (!atual.toString().trim().isEmpty()) pares.add(atual.toString().trim());
        return pares;
    }

}
