# Research: API de Calendario Anual Paroquial

## Decision 1: Runtime and Framework Baseline
- Decision: Implementar como web service Java 21 com Spring Boot 3.x.
- Rationale: Alinha com a constituicao do projeto (Java + Spring), acelera entrega de REST, validacao, seguranca e observabilidade.
- Alternatives considered:
  - Manter app Java puro (Gradle init): rejeitado por alto custo de infraestrutura (HTTP, validacao, seguranca) sem ganho de dominio.
  - Micronaut/Quarkus: rejeitado por divergencia da baseline constitucional e custo de curva.

## Decision 2: Persistence in Shared Database
- Decision: Usar PostgreSQL com schema da API para calendario e leitura somente de `usuarios`, `organizacoes`, `membros_organizacao`.
- Rationale: Especificacao exige banco compartilhado e proibe escrita nas tabelas externas; PostgreSQL oferece integridade relacional e lock/concurrency robustos.
- Alternatives considered:
  - Banco dedicado para a API: rejeitado por contrariar diretriz de banco compartilhado.
  - Persistencia em documento (NoSQL): rejeitado por relacoes fortes entre evento/projeto/organizacoes e necessidade de integridade transacional.

## Decision 3: Event-to-Organization Model
- Decision: Cada evento tera exatamente 1 `organizacao_responsavel_id` (obrigatorio) e, opcionalmente, participantes em `eventos_envolvidos`; a API deve suportar operacao explicita de limpeza total para estado valido sem participantes.
- Rationale: Reflete regra de negocio final: autoridade deriva da organizacao responsavel; participantes colaboram sem governanca total e podem ser removidos integralmente sem invalidar o evento.
- Alternatives considered:
  - Evento sem organizacao responsavel: rejeitado por quebrar regra de autorizacao e rastreabilidade.
  - Evento vinculado diretamente a usuario: rejeitado explicitamente pelo requisito FR-042.

## Decision 9: Access Policy (Anonymous vs Authenticated)
- Decision: Aplicar `bearerAuth` por padrao em endpoints, com excecao apenas para consulta de calendario publico (`GET /eventos`) retornando eventos `CONFIRMADO` para acesso anonimo.
- Rationale: Atende requisito de transparencia publica do calendario sem expor dados internos e preserva RBAC para leituras internas e mutacoes.
- Alternatives considered:
  - Exigir autenticacao para todas consultas: rejeitado por impedir acesso publico ao calendario oficial.
  - Liberar acesso anonimo para detalhes internos: rejeitado por risco de exposicao indevida de dados nao publicos.

## Decision 10: RBAC by Organization and Role Catalog Validation
- Decision: Avaliar permissao em duas etapas: (1) validar papel no catalogo permitido da organizacao; (2) aplicar regra de escopo do evento (`organizacao_responsavel_id`) e hierarquia de autoridade. Rejeitar explicitamente `secretario` fora de organizacao do tipo Conselho.
- Rationale: Garante consistencia entre identidade organizacional e autorizacao operacional, evitando elevacao indevida de privilegios.
- Alternatives considered:
  - RBAC apenas por nome de papel sem contexto de organizacao: rejeitado por permitir falsos positivos de permissao.
  - Regra hardcoded por endpoint sem catalogo: rejeitado por baixa manutenibilidade e maior risco de drift.

## Decision 11: Dedicated Contract/Integration Tests for Visibility Lifecycle
- Decision: Tratar visibilidade por status como contrato verificavel com testes dedicados para tres cenarios obrigatorios: `RASCUNHO` fora do publico, `CONFIRMADO` visivel no publico anonimo, `CANCELADO` visivel em historico com motivo.
- Rationale: Regras de visibilidade sao parte critica da governanca paroquial e nao podem ficar implícitas somente em implementacao.
- Alternatives considered:
  - Cobrir apenas em testes unitarios de dominio: rejeitado por nao validar comportamento HTTP/publico.
  - Cobrir apenas em smoke manual: rejeitado por baixa repetibilidade e risco de regressao.

## Decision 12: Domain Rejection for ADICIONADO_EXTRA Without Justification
- Decision: Impor regra de dominio obrigatoria para `ADICIONADO_EXTRA`: deve existir justificativa textual nao vazia e rastreavel via observacao/auditoria no mesmo fluxo transacional.
- Rationale: Mantem integridade de governanca do planejamento anual e viabiliza indicador de retrabalho com causa auditavel.
- Alternatives considered:
  - Permitir justificativa opcional: rejeitado por fragilizar trilha de auditoria.
  - Aceitar justificativa posterior assíncrona: rejeitado por janela de inconsistencia de dados.

## Decision 4: Conflict Handling and Approval Flow
- Decision: Conflitos de agenda sao nao-bloqueantes na escrita e resolvidos por decisao administrativa posterior (paroco, vigario, coordenador do conselho).
- Rationale: Evita travar operacao pastoral e preserva trilha de decisao com governanca.
- Alternatives considered:
  - Bloquear criacao em conflito: rejeitado por impactar operacao e contrariar FR-022.
  - Resolver automaticamente por prioridade fixa: rejeitado por risco pastoral e falta de contexto humano.

## Decision 5: Temporal Strategy and Status Lifecycle
- Decision: Persistir timestamps em UTC e responder em America/Sao_Paulo; usar status RASCUNHO, CONFIRMADO, CANCELADO, ADICIONADO_EXTRA.
- Rationale: Evita ambiguidade temporal, atende calendario local e regras de visibilidade/auditoria da especificacao.
- Alternatives considered:
  - Persistir direto em horario local: rejeitado por ambiguidade em ajuste civil de horario.
  - Dois status apenas (ativo/cancelado): rejeitado por nao cobrir governanca de rascunho e auditoria de extras.

## Decision 6: API Contract and Versioning
- Decision: Definir contrato OpenAPI para eventos, projetos, observacoes e aprovacoes, com codigos de erro deterministicos e compatibilidade por 1 minor.
- Rationale: Atende principio API-first e reduz regressao para consumidores.
- Alternatives considered:
  - Documentacao ad-hoc sem OpenAPI: rejeitado por baixa rastreabilidade de contrato.
  - Versionamento breaking imediato: rejeitado por violar requisito de compatibilidade.

## Decision 7: Testing Strategy
- Decision: Executar testes em 3 niveis: unit (dominio/use cases), integration (repositorio + banco), contract (MockMvc + OpenAPI assertions).
- Rationale: Cobre qualidade por historia, validacao de regras e estabilidade de contrato.
- Alternatives considered:
  - Somente unit tests: rejeitado por nao validar serializacao/contrato/transacao.
  - Somente integration tests: rejeitado por feedback lento e baixa precisao de falha.

## Decision 8: Observability and Audit
- Decision: Padronizar logging estruturado com correlation-id, ator, acao, alvo, resultado e motivo de falha de negocio.
- Rationale: Necessario para auditoria paroquial e troubleshooting sem expor dados sensiveis.
- Alternatives considered:
  - Logs textuais livres: rejeitado por baixa capacidade de busca e auditoria.
  - Auditoria apenas em banco sem log de operacao: rejeitado por dificultar investigacao operacional.
