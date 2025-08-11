# Sistema de Propostas de Crédito (Backend)

Este projeto é um sistema backend desenvolvido para gerenciar propostas de crédito. 

É uma API RESTful capaz de:

- Registrar propostas de crédito com validações específicas.
- Gerar parcelas associadas às propostas.
- Consultar propostas e suas parcelas.
- Registrar pagamentos de parcelas.

Tudo isso com persistência de dados em PostgreSQL.

---

# Tecnologias Utilizadas

- Java 17
- Spring Boot 3.1.2
- Maven (Gerenciamento de dependências e build)
- PostgreSQL (Banco de dados relacional via Docker Compose)
- Spring Data JPA / Hibernate (Mapeamento objeto-relacional)
- Jakarta Validation + Hibernate Validator (Validação de dados)
- Docker Compose (Para orquestração do banco PostgreSQL)

---

# Como Rodar a Aplicação

## 1. Subir o banco de dados PostgreSQL via Docker Compose

Na raiz do projeto, execute:

```bash
docker-compose up -d
```

Isso iniciará um container PostgreSQL na porta 5433.

---

## 2. Executar a aplicação Spring Boot

Na raiz do projeto, rode:

```bash
./mvnw spring-boot:run
```

---

## 3. Acesso

- A API ficará disponível em: `http://localhost:8080`

---

# Endpoints da API

## 1. Criar Proposta (POST)

- URL: `/propostas`
- Payload JSON:

```json
{
  "cpf": "00000000000",
  "valorSolicitado": 100.50,
  "quantidadeParcelas": 12,
  "dataSolicitacao": "2025-05-01"
}
```

- Exemplo cURL:

```bash
curl -X POST http://localhost:8080/propostas -H "Content-Type: application/json" -d "{\"cpf\":\"00000000000\",\"valorSolicitado\":100.50,\"quantidadeParcelas\":12,\"dataSolicitacao\":\"2025-05-01\"}"
```

---

## 2. Listar Propostas Paginadas (GET)

- URL: `/propostas?page=0&size=10`
- Exemplo cURL:

```bash
curl "http://localhost:8080/propostas?page=0&size=10"
```

---

## 3. Buscar Proposta por ID (GET)

- URL: `/propostas/{id}`
- Exemplo cURL:

```bash
curl http://localhost:8080/propostas/1
```

---

### 4. Pagar Parcela (POST)

- URL: `/propostas/{idProposta}/parcelas/{numeroParcela}/pagar`
- Exemplo cURL:

```bash
curl -X POST http://localhost:8080/propostas/1/parcelas/3/pagar
```

---

# Funcionalidades Implementadas

- Cadastro de Proposta: valida CPF, valor mínimo (R$100,00), parcelas entre 1 e 24.
- Geração automática de parcelas com status inicial "Em Aberto".
- Listagem paginada das propostas com parcelas.
- Consulta detalhada da proposta pelo ID.
- Pagamento de parcelas, atualizando status para "Paga".
- Persistência em PostgreSQL, configurada para rodar com Docker Compose.
- Validação com Jakarta Validation e Hibernate Validator.
- Tratamento de erros com respostas HTTP adequadas.

---

# Regras de Negócio

- CPF deve ser válido e obrigatório.
- Valor solicitado deve ser igual ou maior que R$ 100,00.
- Quantidade de parcelas entre 1 e 24.
- Parcela só pode ser paga se existir e estiver vinculada à proposta.
- Erros específicos retornam mensagens claras e status HTTP apropriados.