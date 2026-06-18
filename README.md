# 💰 Investment Simulator API

API RESTful para simulação de investimentos em renda fixa (CDB e Tesouro Selic), desenvolvida com Spring Boot e PostgreSQL.

---

## 🛠️ Tecnologias

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- PostgreSQL 16
- Docker
- Lombok
- Maven

---

## 📋 Pré-requisitos

- Java 21 instalado
- Docker Desktop instalado e rodando
- Maven instalado (ou usar o wrapper `./mvnw`)

---

## 🚀 Como rodar o projeto

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/investment-simulator.git
cd investment-simulator
```

### 2. Suba o banco de dados com Docker

Na raiz do projeto, execute:

```bash
docker compose up -d
```

Verifique se o container está rodando:

```bash
docker ps
```

Deve aparecer o container `investment-simulator-db` com status `Up`.

### 3. Rode a aplicação

```bash
./mvnw spring-boot:run
```

Ou pela IDE, rodando a classe principal `InvestmentSimulatorApplication.java`.

### 4. Teste os endpoints

Com a aplicação rodando, utilize o **Postman** para testar as requisições na URL base:

```
http://localhost:8080
```

---

## 🗂️ Estrutura do projeto

```
src/main/java/com/testetecnico/investmentsimulator/
├── controller/
│   └── SimulationController.java
├── domain/
│   ├── entity/
│   │   ├── InvestmentSimulation.java
│   │   └── SimulationResult.java
│   └── enums/
│       └── InvestmentType.java
├── dto/
│   ├── SimulationRequestDTO.java
│   └── SimulationResponseDTO.java
├── mapper/
│   └── SimulationMapper.java
├── repository/
│   ├── InvestmentSimulationRepository.java
│   └── SimulationResultRepository.java
└── service/
    ├── impl
    │   ├── SimulationService.java           ← implementação
    │   └── SimulationCalculatorService.java ← implementação
    ├── SimulationCalculator.java        ← interface
    └── Simulation.java                  ← interface
```

---

## 🏛️ Decisões de arquitetura

### Separação em camadas

O projeto segue uma arquitetura em camadas com responsabilidades bem definidas:

- **Controller** — recebe a requisição HTTP e delega pro service. Sem lógica de negócio
- **Service** — orquestra o fluxo: monta a entidade, delega o cálculo, salva no banco e retorna o DTO
- **Calculator** — responsável exclusivamente pelo cálculo financeiro (juros compostos e IR)
- **Mapper** — converte entidades para DTOs. Mantém essa responsabilidade fora do service
- **Repository** — acesso ao banco de dados via Spring Data JPA

### Interfaces

O projeto utiliza interfaces para desacoplar contratos de implementações, seguindo o **Dependency Inversion Principle**:

- `Simulation` — contrato do `SimulationService`
- `SimulationCalculator` — contrato do `SimulationCalculatorService`

O `SimulationService` depende da interface `SimulationCalculator`, não da implementação concreta — facilitando testes e futuras trocas de implementação.

### Separação do cálculo

O `SimulationCalculatorService` foi organizado em métodos privados com responsabilidades únicas:

- `calculateMonthlyRate` — converte taxa anual para mensal
- `calculateFinalValue` — executa o loop de juros compostos
- `calculateTotalInvested` — soma aporte inicial e aportes mensais
- `taxRate` — retorna a alíquota de IR pela tabela regressiva
- `buildResult` — monta o objeto `SimulationResult`

### DTOs

A API nunca expõe entidades diretamente — toda comunicação usa DTOs:

- `SimulationRequestDTO` — entrada com validações (`@NotNull`, `@Positive`, etc.)
- `SimulationResponseDTO` — saída com dados da simulação e do resultado calculado, achatando duas entidades em um único objeto

### Mapper

A conversão de entidade para DTO foi extraída para o `SimulationMapper`, mantendo o service focado em orquestração.

---

## 📡 Endpoints

| Método | Rota | Descrição | Status |
|---|---|---|---|
| `POST` | `/simulations` | Cria uma nova simulação | `201 Created` |
| `GET` | `/simulations` | Lista todas as simulações | `200 OK` |
| `GET` | `/simulations/{id}` | Busca uma simulação por ID | `200 OK` |
| `DELETE` | `/simulations/{id}` | Remove uma simulação | `204 No Content` |

---

## 📨 Exemplos de requisição

### POST /simulations

**Body (JSON):**
```json
{
    "investorName": "Laura",
    "investmentType": "CDB",
    "initialContribution": 1000.00,
    "monthlyContribution": 500.00,
    "timeMonths": 24,
    "annualRate": 12.0
}
```

> `initialContribution` é opcional — caso não informado, a simulação considera apenas os aportes mensais.

**Tipos de investimento aceitos:**
- `CDB` — taxa prefixada informada pelo usuário
- `TESOURO_SELIC` — taxa pós-fixada atrelada à Selic, informada pelo usuário

**Response (201 Created):**
```json
{
    "id": "3f2a1b4c-...",
    "investorName": "Laura",
    "investmentType": "CDB",
    "initialContribution": 1000.00,
    "monthlyContribution": 500.00,
    "timeMonths": 24,
    "annualRate": 12.0,
    "createdAt": "2026-06-17T20:00:00",
    "finalValue": 15234.50,
    "totalInvested": 13000.00,
    "totalReturn": 2234.50,
    "estimatedTax": 391.04,
    "netValue": 14843.46
}
```

### GET /simulations/{id}

```
GET http://localhost:8080/simulations/3f2a1b4c-0000-0000-0000-000000000000
```

### DELETE /simulations/{id}

```
DELETE http://localhost:8080/simulations/3f2a1b4c-0000-0000-0000-000000000000
```

---

## ⚙️ Variáveis de configuração

As configurações de banco estão em `src/main/resources/application.properties` e devem bater com o `docker-compose.yml`:

| Propriedade | Valor padrão |
|---|---|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/investment_simulator` |
| `spring.datasource.username` | `admin` |
| `spring.datasource.password` | `admin123` |
| `server.port` | `8080` |

---

## 🧮 Lógica de cálculo

A simulação utiliza **juros compostos mensais** com base na taxa anual informada:

```
taxa mensal = taxa anual ÷ 100 ÷ 12
saldo = saldo × (1 + taxa mensal) + aporte mensal
```

O cálculo itera mês a mês, acumulando rendimentos sobre o saldo crescente — incluindo os aportes mensais anteriores.

O imposto de renda é estimado com base na **tabela regressiva da Receita Federal** para renda fixa:

| Prazo | Alíquota |
|---|---|
| Até 6 meses | 22,5% |
| 6 a 12 meses | 20,0% |
| 12 a 24 meses | 17,5% |
| Acima de 24 meses | 15,0% |

> O IR incide apenas sobre o rendimento (`totalReturn`), não sobre o valor total investido.

---

## 🗄️ Modelo de dados

### investment_simulation

| Coluna | Tipo | Descrição |
|---|---|---|
| `id` | UUID | Identificador único |
| `name_investor` | VARCHAR | Nome do investidor |
| `investment_type` | VARCHAR | Tipo: `CDB` ou `TESOURO_SELIC` |
| `initial_contribution` | NUMERIC | Aporte inicial (opcional) |
| `monthly_contribution` | NUMERIC | Aporte mensal |
| `time_months` | INTEGER | Prazo em meses |
| `annual_rate` | NUMERIC | Taxa anual (%) |
| `created_at` | TIMESTAMP | Data da simulação |

### simulation_result

| Coluna | Tipo | Descrição |
|---|---|---|
| `id` | UUID | Identificador único |
| `simulation_id` | UUID | FK para `investment_simulation` |
| `final_value` | NUMERIC | Valor bruto ao final |
| `total_invested` | NUMERIC | Total aportado |
| `total_return` | NUMERIC | Rendimento acumulado |
| `estimated_tax` | NUMERIC | IR estimado |
| `net_value` | NUMERIC | Valor líquido após IR |

---

## 🔮 Próximos passos

### Testes unitários

Adicionar cobertura de testes com JUnit 5 e Mockito:

- Testes unitários do `SimulationCalculatorService` — validando o cálculo de juros compostos, total investido e alíquotas de IR para cada faixa de prazo
- Testes unitários do `SimulationService` — mockando o repositório e o calculator para testar a orquestração isoladamente

### Taxas automáticas de mercado

Hoje o usuário informa a taxa manualmente. A evolução natural seria buscar as taxas automaticamente, com duas abordagens possíveis:

**Opção 1 — Integração com API externa**
Consumir uma API de mercado financeiro (como a API do Banco Central ou da B3) para buscar a taxa Selic e taxas de CDB em tempo real. A taxa chegaria automaticamente na simulação, sem o usuário precisar informar.

**Opção 2 — Tabela de produtos no banco**
Cadastrar os produtos de investimento disponíveis diretamente no banco de dados, com suas respectivas taxas:

```
investment_product
├── id
├── name          (ex: "CDB Banco Inter 120% CDI")
├── type          (CDB / TESOURO_SELIC)
├── rate          (taxa anual)
└── updated_at
```

O usuário escolheria o produto e a taxa seria preenchida automaticamente. Essa abordagem também permite atualizar as taxas periodicamente via job agendado (`@Scheduled` do Spring).

### Outras melhorias

- **Versionamento de API** — prefixo `/v1/simulations` para permitir evolução sem quebrar clientes existentes
- **Variáveis de ambiente** — externalizar credenciais do banco via perfis Spring (`application-dev.properties`, `application-prod.properties`)
- **Autenticação** — Spring Security com JWT para proteger os endpoints