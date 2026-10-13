# API Oficina Mecânica

API REST para gerenciar clientes, veículos, serviços e ordens de serviço de uma oficina mecânica. Trabalho da disciplina de Engenharia de Software Orientada a Serviços (CEAVI/UDESC).

## Tecnologias

- Java 21
- Spring Boot 3.5 (`spring-boot-starter-web`)
- Maven

Os dados ficam em memória: não há banco de dados e tudo é perdido quando a aplicação reinicia.

## Como executar

Requisitos: JDK 21 e Maven instalados.

```
mvn spring-boot:run
```

A API sobe em `http://localhost:8080/api`.

## Autenticação

Todas as rotas exigem token, menos o login. Usuário de demonstração:

```
POST /api/auth/login
{
  "email": "usuario@email.com",
  "senha": "123456"
}
```

A resposta traz o token, que deve ser enviado nas demais requisições:

```
Authorization: Bearer SEU_TOKEN
```

## Rotas

Todas com o prefixo `/api`.

| Recurso | Rotas |
| --- | --- |
| Autenticação | `POST /auth/login` |
| Clientes | `GET /clientes` · `GET /clientes/{id}` · `POST /clientes` · `PUT /clientes/{id}` · `DELETE /clientes/{id}` · `GET /clientes/{id}/veiculos` |
| Veículos | `GET /veiculos` · `GET /veiculos/{id}` · `POST /veiculos` · `PUT /veiculos/{id}` · `DELETE /veiculos/{id}` · `GET /veiculos/{id}/ordens-servico` |
| Serviços | `GET /servicos` · `GET /servicos/{id}` · `POST /servicos` · `PUT /servicos/{id}` · `DELETE /servicos/{id}` |
| Ordens de serviço | `GET /ordens-servico` · `GET /ordens-servico/{id}` · `POST /ordens-servico` · `PUT /ordens-servico/{id}` · `DELETE /ordens-servico/{id}` |
| Serviços da OS | `GET /ordens-servico/{id}/servicos` · `POST /ordens-servico/{id}/servicos` · `DELETE /ordens-servico/{id}/servicos/{servicoId}` |

### Filtros e paginação

| Rota | Parâmetros |
| --- | --- |
| `GET /clientes` | `nome`, `page`, `limit` |
| `GET /veiculos` | `placa`, `page`, `limit` |
| `GET /ordens-servico` | `status`, `page`, `limit` |
| `GET /servicos` | `preco_min`, `preco_max` |

`page` começa em 1 (padrão 1) e `limit` vai de 1 a 100 (padrão 10).

### Regras de negócio da ordem de serviço

- Uma OS é aberta para um veículo existente e começa com status `ABERTA`.
- Os status possíveis são `ABERTA`, `EM_ANDAMENTO` e `CONCLUIDA`.
- Só é possível excluir uma OS com status `ABERTA`.
- O mesmo serviço não pode ser adicionado duas vezes na mesma OS.

### Formato de erro

Todos os erros seguem o mesmo formato:

```json
{
  "status": 404,
  "erro": "RECURSO_NAO_ENCONTRADO",
  "mensagem": "Ordem de serviço não encontrada."
}
```

| Status | Código |
| --- | --- |
| 400 | `DADOS_INVALIDOS` |
| 401 | `NAO_AUTENTICADO`, `CREDENCIAIS_INVALIDAS` |
| 404 | `RECURSO_NAO_ENCONTRADO` |
| 409 | `RECURSO_EM_USO`, `SERVICO_DUPLICADO` |

## Arquitetura

Arquitetura em camadas, em que cada pacote tem uma responsabilidade:

```
requisição → security → controller → service → repository
```

| Pacote | Responsabilidade |
| --- | --- |
| `controller` | Rotas HTTP e status de resposta; repassa para o service |
| `service` | Regras de negócio e validações |
| `repository` | Armazenamento em memória, com uma classe base genérica |
| `model` | Entidades do domínio |
| `dto` | Formatos de entrada e saída da API |
| `exception` | Exceção da API e tratamento global de erros |
| `security` | Interceptor que valida o token |
| `config` | Registro do interceptor e dados iniciais |

## Testando com o Postman

Importe o arquivo `oficina-mecanica-api.postman_collection.json` no Postman para ter as requisições prontas.
