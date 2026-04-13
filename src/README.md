# CRUD GRASP Compra Insumo

Equipe composta por: Erick Araújo Macedo e Henrique Varela Mouta.

### Como Compilar e Executar o Projeto

- Coloque todos os arquivos, respeitando a estrutura de pacotes;
- Compile com *javac*;
- Execute a *Main*;
- O diretório *data/* é criado automaticamente na primeira escrita.

### Estrutura do Projeto e Padrões GRASP
| Arquivo                           | Camada            | Papel GRASP                            |
|-----------------------------------|-------------------|----------------------------------------|
| `Main.java`                       | `Raiz`            | **Creator**                            |
| `StatusCompra.java`               | `Domain`          | **Information Expert**                 |
| `CompraInsumo.java`               | `Domain`          | **Information Expert**                 |
| `RegraNegocioException.java`      | `Exception`       | **High Cohesion**                      |
| `CompraInsumoRequest.java`        | `DTO`             | **High Cohesion**                      |
| `CompraInsumoRepository.java`     | `Repository`      | **Indirection + Protected Variations** |
| `CompraInsumoRepositoryJson.java` | `Repository/Json` | **Pure Fabrication**                   |
| `CompraInsumoService.java`        | `Service`         | **Alta Coesão + Baixo Acomplamento**   |
| `CompraInsumoController.java`     | `Controller`      | **Controller**                         |
| `JsonMini.java`                   | `Util`            | **Pure Fabrication**                   |
