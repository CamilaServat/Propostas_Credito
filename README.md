# Sistema de Propostas de Crédito (Backend)

Este projeto é um **sistema backend** para gerenciamento de propostas de crédito, desenvolvido em **Java 17 + Spring Boot**, com persistência no **PostgreSQL** e execução simplificada via **Docker Compose**.

## Funcionalidades
- Registrar propostas de crédito com validações.
- Gerar parcelas associadas às propostas.
- Consultar propostas e suas parcelas.
- Registrar pagamentos de parcelas.
- Persistir dados em banco relacional.

---

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.1.2**
- **Maven**
- **PostgreSQL** (via Docker Compose)
- **Spring Data JPA / Hibernate**
- **Jakarta Validation + Hibernate Validator**
- **Docker Compose**

---

## Como Rodar a Aplicação

### Clonar o repositório

### Subir aplicação + banco de dados
O projeto já vem com um docker-compose.yml configurado para subir **o PostgreSQL** e **a aplicação** juntos.  

Basta executar:

docker-compose up -d --build

> O parâmetro `--build` só é necessário se for a primeira vez rodando ou se você alterou o código e gerou um novo `.jar`.

---

## Se houver alterações no código
Se você mexeu no código Java, antes de rodar o docker-compose será necessário gerar o `.jar` novamente:

mvn clean package -DskipTests
docker-compose up -d --build

---

## Acesso à API
- Base URL: `http://localhost:8080`

---

## Endpoints e Exemplos (via cURL no CMD/PowerShell)

## 1. Criar Proposta (POST)

- URL: /propostas
- JSON:

json
{
  "cpf": "00000000000",
  "valorSolicitado": 100.50,
  "quantidadeParcelas": 12,
  "dataSolicitacao": "2025-05-01"
}


- Exemplo cURL:

```bash
curl -X POST http://localhost:8080/propostas -H "Content-Type: application/json" -d "{\"cpf\":\"00000000000\",\"valorSolicitado\":100.50,\"quantidadeParcelas\":12,\"dataSolicitacao\":\"2025-05-01\"}"
```
---

## 2. Listar Propostas Paginadas (GET)

- URL: /propostas?page=0&size=10
- Exemplo cURL:

```bash
curl "http://localhost:8080/propostas?page=0&size=10"
```
---

## 3. Buscar Proposta por ID (GET)

- URL: /propostas/{id}
- Exemplo cURL:

```bash
curl http://localhost:8080/propostas/1
```

---

### 4. Pagar Parcela (POST)

- URL: /propostas/{idProposta}/parcelas/{numeroParcela}/pagar
- Exemplo cURL:

```bash
curl -X POST http://localhost:8080/propostas/1/parcelas/3/pagar
```

---

## Regras de Negócio
- CPF válido e obrigatório.
- Valor solicitado **mínimo de R$ 100,00**.
- Quantidade de parcelas entre **1 e 24**.
- Parcela só pode ser paga se existir e pertencer à proposta.
- Respostas com mensagens claras e códigos HTTP adequados.

---

## Parar a aplicação
docker-compose down