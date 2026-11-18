# Sombra Certa API

API REST feita em Spring Boot para a Global Solution 2026.2 da FIAP (Domain Driven Design - Java), no desafio de calor extremo. A ideia é ajudar idosos e trabalhadores de rua de São Paulo a achar um lugar para se proteger do calor.

A API guarda dois tipos de dado:

- **Pontos de refúgio**: praças arborizadas, pontos de hidratação, abrigos climatizados e unidades de saúde.
- **Registros de temperatura**: temperatura e umidade medidas em cada ponto. A API devolve também o nível de risco (BAIXO, MODERADO, ALTO ou EXTREMO).

## Tecnologias

Java 21, Spring Boot 3.3.5, Spring Data JPA, Bean Validation, Oracle (ojdbc11), Docker e Render.

## Como o projeto está organizado

```
br.com.fiap.sombracerta
├── model        entidades JPA (PontoRefugio, RegistroTemperatura, TipoPonto)
├── dao          repositórios Spring Data JPA
├── bo           regras de negócio
├── controller   endpoints REST
├── dto          classes de entrada e saída com validação
└── exception    exceções próprias e o GlobalExceptionHandler
```

## Banco de dados

Duas tabelas, ligadas por chave estrangeira (um ponto tem vários registros):

- `TB_PONTO_REFUGIO`: ID_PONTO, NM_PONTO, DS_ENDERECO, NM_CIDADE, TP_PONTO, DT_CADASTRO, ST_ATIVO
- `TB_REGISTRO_TEMPERATURA`: ID_REGISTRO, ID_PONTO, DT_REGISTRO, NR_TEMPERATURA_C, NR_UMIDADE_PCT

As tabelas são criadas sozinhas na primeira execução (`ddl-auto=update`).

Valores aceitos em `tipo`: `PRACA_ARBORIZADA`, `PONTO_HIDRATACAO`, `ABRIGO_CLIMATIZADO` e `UNIDADE_SAUDE`.

## Endpoints

| Método | Rota | O que faz |
|---|---|---|
| GET | `/` | mostra que a API está no ar |
| GET | `/pontos` | lista os pontos (aceita `?cidade=`) |
| GET | `/pontos/{id}` | busca um ponto |
| POST | `/pontos` | cria um ponto |
| PUT | `/pontos/{id}` | atualiza um ponto |
| DELETE | `/pontos/{id}` | exclui um ponto |
| GET | `/registros` | lista os registros (aceita `?pontoId=`) |
| GET | `/registros/{id}` | busca um registro |
| POST | `/registros` | cria um registro |
| PUT | `/registros/{id}` | atualiza um registro |
| DELETE | `/registros/{id}` | exclui um registro |

O arquivo `requests.http` tem um exemplo de cada chamada.

## Regras de negócio

- Só dá para registrar temperatura em um ponto que esteja ativo.
- A data do registro não pode ser no futuro.
- A temperatura tem que ficar entre -10 e 60 °C e a umidade entre 0 e 100%.
- Um ponto que já tem registros não pode ser excluído.
- O nível de risco depende só da temperatura: abaixo de 30 °C é BAIXO, de 30 a 34,99 é MODERADO, de 35 a 39,99 é ALTO e de 40 em diante é EXTREMO.

## Erros

| Quando | Código |
|---|---|
| Id não existe | 404 |
| Dados inválidos ou JSON mal formado | 400 |
| Regra de negócio violada | 422 |
| Conflito no banco (registro em uso) | 409 |
| Erro inesperado | 500 |

Todos os erros voltam no mesmo formato: `timestamp`, `status`, `erro`, `mensagem`, `caminho` e `detalhes`.

## Como rodar

A senha do banco não fica no código. Antes de rodar, defina estas variáveis de ambiente:

- `DB_URL`, por exemplo `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL`
- `DB_USER` (seu RM)
- `DB_PASSWORD`

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

O deploy está no Render, usando o `Dockerfile` do projeto. No Render, crie um Web Service com runtime Docker e cadastre `DB_URL`, `DB_USER` e `DB_PASSWORD` nas variáveis de ambiente. Como o plano gratuito hiberna, a primeira requisição depois de um tempo parado pode demorar cerca de um minuto.
