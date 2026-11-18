# Sombra Certa API

API REST feita em Spring Boot para a **Global Solution 2026.2 da FIAP** (CitizenTech · Domain Driven Design - Java). O tema é o calor extremo nas grandes cidades. O foco são idosos e trabalhadores de rua, que ficam mais expostos nas ondas de calor, principalmente na região da Avenida Paulista e da Bela Vista, em São Paulo.

A API guarda dois tipos de dado:

- **Pontos de refúgio**: lugares onde a pessoa consegue se proteger do calor (praças arborizadas, pontos de hidratação, abrigos climatizados e unidades de saúde).
- **Registros de temperatura**: temperatura e umidade medidas em cada ponto. O nível de risco (BAIXO, MODERADO, ALTO ou EXTREMO) é calculado automaticamente a partir da temperatura.

## Links

| Item | Link |
|---|---|
| Repositório GitHub | https://github.com/migueldsa20071/sombracerta-api |
| Deploy (Render) | https://sombracerta-api.onrender.com |

## Integrantes

| Nome completo | RM |
|---|---|
| João Gabriel C.M. Santos | RM563953 |
| Miguel dos Santos de Andrade | RM563516 |
| Pedro Henrique da Silva | RM566235 |

## Tecnologias

| Tecnologia | Uso no projeto |
|---|---|
| Java 21 | Linguagem da aplicação |
| Spring Boot 3.3.5 | Framework base, servidor web embutido e injeção de dependências |
| Spring Web | Criação dos endpoints REST (controllers) |
| Spring Data JPA / Hibernate | Mapeamento objeto-relacional e acesso aos dados (DAO) |
| Bean Validation | Validação dos dados de entrada (DTOs) |
| Oracle (ojdbc11) | Banco de dados relacional da FIAP |
| Maven | Gerenciamento de dependências e build do `.jar` |
| Docker | Empacotamento da aplicação para o deploy |
| Render | Servidor onde a API está publicada |

## Arquitetura em camadas

O caminho de uma requisição é **Controller → BO → DAO → Banco de dados**.

```
br.com.fiap.sombracerta
├── model        entidades JPA mapeadas para o Oracle (PontoRefugio, RegistroTemperatura, TipoPonto)
├── dao          acesso a dados (Spring Data JPA)
├── bo           regras de negócio e transações
├── controller   endpoints REST (GET, POST, PUT, DELETE)
├── dto          objetos de entrada e saída, com validações
└── exception    exceções de domínio e GlobalExceptionHandler
```

| Camada | Responsabilidade |
|---|---|
| Model | Classes que representam as tabelas do Oracle (`@Entity`, `@Table`, `@Column`) e a relação 1:N entre ponto e registros. |
| DAO | Interfaces `PontoRefugioDAO` e `RegistroTemperaturaDAO`, que estendem `JpaRepository` e fazem as consultas ao banco. |
| BO | `PontoRefugioBO` e `RegistroTemperaturaBO`: aplicam as regras de negócio, controlam as transações e lançam as exceções de domínio. |
| Controller | `PontoRefugioController`, `RegistroTemperaturaController` e `HomeController`: recebem as requisições HTTP, validam os dados e devolvem a resposta. |
| DTO | Records de Request e Response, que isolam o que entra e sai da API das entidades do banco. |
| Exception | Exceções de domínio e `GlobalExceptionHandler`, que padroniza todas as respostas de erro. |

## Banco de dados

Duas tabelas, ligadas por chave estrangeira (um ponto tem vários registros, relação 1:N). Elas são criadas automaticamente pelo Hibernate na primeira execução (`ddl-auto=update`).

**`TB_PONTO_REFUGIO`**

| Coluna | Atributo Java | Descrição |
|---|---|---|
| `ID_PONTO` (PK) | `id` | Identificador gerado automaticamente |
| `NM_PONTO` | `nome` | Nome do ponto (obrigatório, até 120 caracteres) |
| `DS_ENDERECO` | `endereco` | Endereço (obrigatório, até 200 caracteres) |
| `NM_CIDADE` | `cidade` | Cidade (obrigatório, até 80 caracteres) |
| `TP_PONTO` | `tipo` | Tipo do ponto (enum `TipoPonto`) |
| `DT_CADASTRO` | `dataCadastro` | Data de cadastro, preenchida pela API |
| `ST_ATIVO` | `ativo` | Indica se o ponto está ativo |

**`TB_REGISTRO_TEMPERATURA`**

| Coluna | Atributo Java | Descrição |
|---|---|---|
| `ID_REGISTRO` (PK) | `id` | Identificador gerado automaticamente |
| `ID_PONTO` (FK) | `ponto` | Ponto de refúgio ao qual o registro pertence |
| `DT_REGISTRO` | `dataRegistro` | Data da medição |
| `NR_TEMPERATURA_C` | `temperaturaC` | Temperatura em °C |
| `NR_UMIDADE_PCT` | `umidadePct` | Umidade relativa do ar em % |

Valores aceitos em `tipo`: `PRACA_ARBORIZADA`, `PONTO_HIDRATACAO`, `ABRIGO_CLIMATIZADO` e `UNIDADE_SAUDE`.

## Endpoints

| Método | Rota | O que faz | Resposta |
|---|---|---|---|
| GET | `/` | Mostra que a API está no ar | 200 |
| GET | `/pontos` | Lista os pontos (aceita `?cidade=`) | 200 |
| GET | `/pontos/{id}` | Busca um ponto | 200 / 404 |
| POST | `/pontos` | Cadastra um ponto | 201 + `Location` |
| PUT | `/pontos/{id}` | Atualiza um ponto | 200 / 404 |
| DELETE | `/pontos/{id}` | Exclui um ponto | 204 / 404 / 422 |
| GET | `/registros` | Lista os registros (aceita `?pontoId=`) | 200 |
| GET | `/registros/{id}` | Busca um registro | 200 / 404 |
| POST | `/registros` | Registra temperatura e umidade | 201 + `Location` |
| PUT | `/registros/{id}` | Atualiza um registro | 200 / 404 |
| DELETE | `/registros/{id}` | Exclui um registro | 204 / 404 |


## Regras de negócio

- Só dá para registrar temperatura em um ponto que esteja ativo.
- A data do registro não pode ser no futuro.
- A temperatura tem que ficar entre -10 e 60 °C e a umidade entre 0 e 100%.
- Um ponto que já tem registros não pode ser excluído.
- O nível de risco depende só da temperatura:

| Temperatura | Nível de risco |
|---|---|
| Abaixo de 30 °C | BAIXO |
| De 30 °C a 34,99 °C | MODERADO |
| De 35 °C a 39,99 °C | ALTO |
| 40 °C ou mais | EXTREMO |

## Tratamento de erros

A classe `GlobalExceptionHandler` (`@RestControllerAdvice`) centraliza o tratamento de erros.

| Situação | Exceção tratada | HTTP |
|---|---|---|
| Recurso não encontrado | `RecursoNaoEncontradoException` | 404 |
| Dados inválidos ou JSON malformado | `MethodArgumentNotValidException`, `HttpMessageNotReadableException` | 400 |
| Parâmetro com tipo inválido | `MethodArgumentTypeMismatchException` | 400 |
| Regra de negócio violada | `RegraNegocioException` | 422 |
| Violação de integridade no banco | `DataIntegrityViolationException` | 409 |
| Erro inesperado | `Exception` | 500 |

Todos os erros voltam no mesmo formato JSON: `timestamp`, `status`, `erro`, `mensagem`, `caminho` e `detalhes`. Exemplo de um `POST /pontos` sem os campos obrigatórios (400):

```json
{
  "timestamp": "2026-11-18T16:46:16.763191991",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Dados da requisição inválidos",
  "caminho": "/pontos",
  "detalhes": [
    "endereco: O endereço é obrigatório",
    "tipo: O tipo é obrigatório (PRACA_ARBORIZADA, PONTO_HIDRATACAO, ABRIGO_CLIMATIZADO ou UNIDADE_SAUDE)",
    "nome: O nome é obrigatório"
  ]
}
```

##  rodar


- `DB_URL`, `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL`
- `DB_USER` ( RM)
- `DB_PASSWORD` (sua senha no oracle)

No Windows (cmd), dentro da pasta do projeto:

```
set DB_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
set DB_USER=SEU_RM
set DB_PASSWORD=SUA_SENHA
mvnw.cmd clean -DskipTests package
java -jar target/sombracerta-0.0.1-SNAPSHOT.jar
```

No Linux ou macOS use `export` no lugar de `set` e `./mvnw` no lugar de `mvnw.cmd`. A API sobe em `http://localhost:8080`.

## Deploy

A API está publicada no Render em **https://sombracerta-api.onrender.com**, como Web Service, usando o `Dockerfile` do projeto.

- Runtime: Docker · Instância: Free · Branch: `main`
- O build é feito em dois estágios: o primeiro compila o projeto com Maven e gera o `.jar`; o segundo cria uma imagem leve, só com o Java, para executar a API.
- As credenciais do Oracle (`DB_URL`, `DB_USER`, `DB_PASSWORD`) ficam em variáveis de ambiente do Render. 
- Como o plano gratuito hiberna por inatividade, a primeira requisição depois de um tempo parado pode demorar cerca de um minuto.


## Testes em produção

Os testes foram feitos na URL pública do Render, com a extensão REST Client do VS Code. Foram validados: `GET /` (API online), CRUD completo de `/pontos` e `/registros`, filtro `?pontoId=`, cálculo automático do nível de risco  e as respostas de erro 400 e 404 no formato padronizado.
