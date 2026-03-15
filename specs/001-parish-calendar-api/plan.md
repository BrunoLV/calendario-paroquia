# Implementation Plan: API de Calendario Anual Paroquial

**Branch**: `001-parish-calendar-api` | **Date**: 2026-03-15 | **Spec**: `/home/bruno/DEV/WORKSPACES/calendario-paroquia/specs/001-parish-calendar-api/spec.md`
**Input**: Feature specification from `/home/bruno/DEV/WORKSPACES/calendario-paroquia/specs/001-parish-calendar-api/spec.md`

## Summary

Implementar uma API Java 21 + Spring Boot 3.x para gestao do calendario anual paroquial com foco em: CRUD de eventos com ciclo de vida (`RASCUNHO`, `CONFIRMADO`, `CANCELADO`, `ADICIONADO_EXTRA`), agrupamento por projetos, RBAC hierarquico por organizacao, observacoes append-only auditaveis, tratamento nao bloqueante de conflitos de agenda e visibilidade publica controlada por status. A abordagem tecnica combina OpenAPI 3.0.3 como contrato, persistencia PostgreSQL compartilhada (com leitura somente de tabelas externas), arquitetura clean/hexagonal e estrategia de testes em camadas (unit/integration/contract) para garantir rastreabilidade e conformidade constitucional.

## Technical Context

**Language/Version**: Java 21  
**Primary Dependencies**: Spring Boot 3.x (Web, Validation, Security, Data JPA), Flyway, PostgreSQL driver, OpenAPI 3.0.3 tooling  
**Storage**: PostgreSQL compartilhado; escrita apenas nas tabelas da API de calendario e leitura somente de `organizacoes`, `usuarios`, `membros_organizacao`  
**Testing**: JUnit 5 + Spring Boot Test + MockMvc + suites separadas de unit/integration/contract  
**Target Platform**: Linux server (API REST stateless)  
**Project Type**: Web service backend (single service)  
**Performance Goals**: SC-001 (cadastro <= 2 min em 90%), SC-002 (p95 de consulta <= 2s), SC-012 (baseline semanal de tempo/latencia/retrabalho)  
**Constraints**: UTC em persistencia e conversao para `America/Sao_Paulo`; soft delete por `CANCELADO`; conflitos nao bloqueantes; compatibilidade de contrato por 1 ciclo minor; sem escrita em tabelas externas; complexidade ciclomatica <= 10 em `domain`/`application`; 0 violacoes de arquitetura; cobertura minima 80% (`domain`/`application`) e 90% em regras criticas  
**Scale/Scope**: Calendario anual paroquial com eventos, projetos, recorrencia, observacoes, aprovacoes e RBAC por organizacao

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Design Gate Review

- API Contract Gate: PASS. Endpoints e semantica de erro estao definidos na especificacao e mapeados no contrato OpenAPI (`/eventos`, `/projetos`, `/eventos/{eventoId}/observacoes`, `/aprovacoes`).
- Calendar Integrity Gate: PASS. Regras de timezone (UTC -> `America/Sao_Paulo`), conflitos nao bloqueantes, ordenacao deterministica e soft delete estao explicitadas.
- Testability Gate: PASS. US1/US2/US3 possuem cenarios independentes e caminhos executaveis de teste.
- Observability Gate: PASS. Log estruturado com correlation-id, ator, acao, alvo e resultado foi definido como obrigatorio.
- Simplicity Gate: PASS. Stack segue baseline constitucional (Java + Spring Boot + PostgreSQL) sem dependencia extra injustificada.
- Architecture Gate: PASS. Fronteiras clean/hexagonal exigidas (`domain`, `application`, `api`, `infrastructure`) e verificacao automatizada prevista.
- Java/Spring Gate: PASS. Praticas de DI, validacao, transacoes e mapeamento de excecoes foram definidas no plano da feature.

### Post-Design Gate Review

- API Contract Gate: PASS. Contrato OpenAPI em `contracts/calendar-api.openapi.yaml` cobre regras criticas, incluindo visibilidade publica e `ADICIONADO_EXTRA`.
- Calendar Integrity Gate: PASS. `data-model.md` consolida validacoes de intervalo, status e integridade organizacional.
- Testability Gate: PASS. `quickstart.md` define suites dedicadas para RBAC por organizacao, visibilidade por status e rejeicao de `ADICIONADO_EXTRA` sem justificativa.
- Observability Gate: PASS. `research.md` e `spec.md` mantem decisao por logging estruturado e trilha auditavel append-only.
- Simplicity Gate: PASS. Nao houve desvio da baseline; decisoes mantiveram menor complexidade para o escopo.
- Architecture Gate: PASS. Modelo e tarefas reforcam isolamento de dominio/aplicacao vs adaptadores, com regra objetiva de 0 violacoes.
- Java/Spring Gate: PASS. Design permanece aderente ao baseline Java 21 + Spring Boot 3.x.

## Phase 0: Research Output

Arquivo gerado: `/home/bruno/DEV/WORKSPACES/calendario-paroquia/specs/001-parish-calendar-api/research.md`

- Todos os itens inicialmente indefinidos foram resolvidos.
- Decisoes consolidadas: baseline Java/Spring, persistencia compartilhada somente leitura para tabelas externas, modelo evento-organizacao, RBAC por catalogo+escopo, visibilidade por status, obrigatoriedade de justificativa para `ADICIONADO_EXTRA`, estrategia de testes em camadas e observabilidade estruturada.
- Resultado: nenhum `NEEDS CLARIFICATION` pendente.

## Phase 1: Design & Contracts Output

### Data Model

Arquivo gerado: `/home/bruno/DEV/WORKSPACES/calendario-paroquia/specs/001-parish-calendar-api/data-model.md`

- Entidades principais: `EventoParoquial`, `ProjetoEvento`, `EventoEnvolvido`, `ObservacaoEvento`, `EventoRecorrencia`, `Local`, `Categoria`.
- Regras criticas modeladas: `organizacao_responsavel_id` obrigatoria, limpeza total de participantes como estado valido, `ADICIONADO_EXTRA` com justificativa obrigatoria, `CANCELADO` com motivo obrigatorio, observacoes append-only.

### Contracts

Arquivo gerado: `/home/bruno/DEV/WORKSPACES/calendario-paroquia/specs/001-parish-calendar-api/contracts/calendar-api.openapi.yaml`

- Interface externa da API definida via OpenAPI 3.0.3.
- Contratos cobrem eventos, projetos, observacoes e aprovacoes.
- Validacoes obrigatorias refletidas no contrato: RBAC, visibilidade por status, limpeza de participantes e rejeicao de `ADICIONADO_EXTRA` sem justificativa.

### Quickstart

Arquivo gerado: `/home/bruno/DEV/WORKSPACES/calendario-paroquia/specs/001-parish-calendar-api/quickstart.md`

- Fluxo de setup local, build e execucao.
- Suites dedicadas de verificacao:
  - `RoleCatalogContractTest` + `RbacOrganizationIntegrationTest`
  - `PublicVisibilityContractTest` + `StatusVisibilityIntegrationTest`
  - `AddedExtraValidationContractTest` + `AddedExtraDomainIntegrationTest`

### Agent Context

- Script de sincronizacao executado: `.specify/scripts/bash/update-agent-context.sh copilot`
- Arquivo alvo: `/home/bruno/DEV/WORKSPACES/calendario-paroquia/.github/agents/copilot-instructions.md`
- Observacao: apos preencher este plano, o contexto deve refletir linguagem/dependencias reais sem placeholders.

## Project Structure

### Documentation (this feature)

```text
specs/001-parish-calendar-api/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── calendar-api.openapi.yaml
└── tasks.md
```

### Source Code (repository root)

```text
app/
├── build.gradle.kts
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
│       ├── java/
│       └── resources/
└── bin/

gradle/
├── libs.versions.toml
└── wrapper/

gradle.properties
settings.gradle.kts
gradlew
gradlew.bat
```

**Structure Decision**: Projeto backend unico em modulo Gradle (`app`) com testes no mesmo modulo e artefatos de especificacao em `specs/001-parish-calendar-api`.

## Complexity Tracking

Sem violacoes constitucionais que exijam justificativa nesta fase.
