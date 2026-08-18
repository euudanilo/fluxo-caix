# Fluxo Caixa

Sistema de controle de fluxo de caixa e cobrança recorrente, desenvolvido com Java e Spring Boot. Projetado para resolver um problema real de pequenas e médias empresas: falta de visibilidade sobre o caixa futuro, o que causa tanto ruptura de caixa (não sobra dinheiro para pagar contas) quanto decisões financeiras tomadas "no escuro".

## O problema que o projeto resolve

Pequenas empresas costumam controlar contas a pagar e a receber em planilhas, sem visão consolidada do que vai entrar e sair de caixa nos próximos dias. Isso dificulta decisões básicas como "posso fazer esse investimento esse mês?" ou "vou conseguir pagar os fornecedores na próxima semana?".

Este sistema resolve isso com:
- Cadastro de clientes e lançamentos financeiros (contas a pagar e a receber)
- Cálculo automático de juros e multa por atraso
- **Projeção de fluxo de caixa**: dado um período, calcula quanto vai entrar, quanto vai sair, e o saldo projetado
- Job agendado que marca lançamentos vencidos automaticamente, sem depender de intervenção manual

## Stack técnica

- **Java 21**
- **Spring Boot 4** (Web, Data JPA, Validation, Scheduler)
- **PostgreSQL** como banco de dados
- **Flyway** para versionamento de schema
- **JUnit 5 + Mockito + AssertJ** para testes unitários
- **MockMvc** para testes de integração
- **JaCoCo** para cobertura de testes
- **Docker Compose** para o ambiente de banco local
- **GitHub Actions** para CI (build + testes contra Postgres real em cada Pull Request)

## Principais decisões de arquitetura

### Flyway em vez de `ddl-auto=update`
O schema do banco é gerenciado inteiramente por migrations versionadas (`src/main/resources/db/migration`), não pelo Hibernate. Isso garante rastreabilidade (toda mudança de banco fica registrada e revisável em Pull Request) e elimina o risco de alterações automáticas indesejadas em produção. `spring.jpa.hibernate.ddl-auto=validate` garante que a aplicação nem sobe se as entidades Java estiverem dessincronizadas do schema real.

### Single Table Inheritance para `LancamentoFinanceiro`
`ContaAPagar` e `ContaAReceber` herdam de uma classe abstrata `LancamentoFinanceiro`, mapeadas na mesma tabela do banco (`@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`). As duas variações compartilham quase todo o comportamento — a diferença está só na direção do dinheiro — e isso simplifica bastante as queries de fluxo de caixa, que precisam olhar pagar e receber juntos sem `UNION` entre tabelas.

### `BigDecimal` em toda operação monetária
Nenhum valor financeiro usa `float`/`double`, que têm erro de arredondamento binário. Toda soma, multiplicação e divisão envolvendo dinheiro usa `BigDecimal` com `RoundingMode` explícito, evitando erro de centavo acumulado.

### DTOs separados da entidade JPA
Nenhuma entidade é exposta diretamente pela API. Isso desacopla o contrato HTTP da estrutura do banco, evita expor campos internos, e permite que os DTOs de resposta incluam campos calculados (como `valorAtualizado`, que aplica juros/multa em tempo real e não existe como coluna no banco).

### Tratamento global de exceções
Um `@RestControllerAdvice` centraliza a conversão de exceções de negócio em respostas HTTP padronizadas (404 para recursos não encontrados, 409 para conflitos, 400 para dados inválidos), evitando duplicação de `try/catch` em cada Controller.

### `spring.jpa.open-in-view=false`
Desabilitado deliberadamente. Os Services carregam explicitamente os dados necessários antes de montar os DTOs, em vez de depender de lazy-loading implícito durante a serialização da resposta — isso libera conexões do pool de banco mais rápido e deixa o comportamento mais previsível.

## Como rodar localmente

### Pré-requisitos
- Java 21+
- Docker

### Passos

1. Suba o banco de dados:
```bash
docker compose up -d
```

2. Rode a aplicação:
```bash
./mvnw spring-boot:run
```

O Flyway aplica as migrations automaticamente na primeira execução. A API sobe em `http://localhost:8080`.

### Rodando os testes
```bash
./mvnw test
```

Um relatório de cobertura é gerado em `target/site/jacoco/index.html`.

## Endpoints principais

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/clientes` | Cadastra um cliente |
| `GET` | `/api/clientes` | Lista todos os clientes |
| `GET` | `/api/clientes/{id}` | Busca cliente por ID |
| `POST` | `/api/lancamentos` | Cadastra uma conta a pagar ou a receber |
| `PATCH` | `/api/lancamentos/{id}/pagamento` | Registra o pagamento de um lançamento |
| `GET` | `/api/projecao-caixa?dataInicio=...&dataFim=...` | Retorna a projeção de saldo (entradas - saídas) para o período |

### Exemplo: cadastrando um lançamento
```bash
curl -X POST http://localhost:8080/api/lancamentos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "tipo": "RECEBER",
    "descricao": "Servico de consultoria",
    "valorOriginal": 1500.00,
    "dataVencimento": "2026-09-01"
  }'
```

### Exemplo: consultando a projeção de caixa
```bash
curl "http://localhost:8080/api/projecao-caixa?dataInicio=2026-09-01&dataFim=2026-09-30"
```

## Testes e qualidade

- **14 testes** automatizados, combinando testes unitários (com Mockito, isolando a lógica de negócio) e testes de integração (com MockMvc, validando a API contra um banco Postgres real)
- Cobertura de testes priorizada onde a lógica de negócio é mais crítica: pacote `domain` (cálculo de juros e multa) em torno de 80%, pacote `job` (automação) em 100%
- CI configurado via GitHub Actions: todo Pull Request roda o build completo e a suíte de testes contra um container Postgres real antes de permitir o merge

## Estrutura do projeto

src/main/java/com/danilo/fluxocaixa/
├── domain/ # Entidades JPA e regras de negocio (Cliente, LancamentoFinanceiro, etc)
├── repository/ # Interfaces Spring Data JPA
├── service/ # Regras de negocio e orquestracao
├── controller/ # Endpoints REST
├── dto/ # Objetos de transferencia (request/response)
├── exception/ # Tratamento global de excecoes
└── job/ # Tarefas agendadas

## Histórico de desenvolvimento

O projeto foi construído seguindo o fluxo GitHub Flow completo: 
toda funcionalidade nasceu em uma branch própria, passou por Pull Request com CI 
automatizado rodando testes contra um banco real, e só então foi mesclada à 
`main`. O histórico de commits segue o padrão Conventional Commits (`feat:`, 
`fix:`, `test:`, `chore:`, `ci:`, `docs:`), e o repositório documenta, através dos 
Pull Requests mesclados, a evolução incremental de cada camada do sistema — da 
modelagem do banco até a funcionalidade de projeção de caixa.