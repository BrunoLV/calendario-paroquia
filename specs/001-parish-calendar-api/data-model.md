# Data Model: API de Calendario Anual Paroquial

## 1. EventoParoquial
- Purpose: Entidade central do calendario.
- Fields:
  - `id` (UUID, PK)
  - `titulo` (string, 1..160, obrigatorio)
  - `descricao` (string, 0..4000)
  - `tipo_atividade` (enum/string, obrigatorio)
  - `categoria_id` (UUID, FK -> Categoria.id, obrigatorio)
  - `local_id` (UUID, FK -> Local.id, obrigatorio)
  - `projeto_id` (UUID, FK -> ProjetoEvento.id, opcional)
  - `organizacao_responsavel_id` (UUID, FK externa -> Organizacao.id, obrigatorio)
  - `inicio_utc` (timestamp with time zone, obrigatorio)
  - `fim_utc` (timestamp with time zone, obrigatorio)
  - `status` (enum: RASCUNHO, CONFIRMADO, CANCELADO, ADICIONADO_EXTRA)
  - `tem_conflito` (boolean, default false)
  - `motivo_conflito` (string, opcional)
  - `origem_alteracao` (enum, obrigatorio)
  - `cancelado_motivo` (string, obrigatorio quando status=CANCELADO)
  - `adicionado_extra_justificativa` (string, obrigatorio quando status=ADICIONADO_EXTRA)
  - `criado_em_utc` (timestamp, obrigatorio)
  - `atualizado_em_utc` (timestamp, obrigatorio)
- Validation rules:
  - `fim_utc > inicio_utc`
  - `organizacao_responsavel_id` sempre presente
  - `status=ADICIONADO_EXTRA` exige justificativa textual nao vazia e rastreavel em observacao/auditoria
  - `status=CANCELADO` exige `cancelado_motivo`
- State transitions:
  - `RASCUNHO -> CONFIRMADO`
  - `RASCUNHO -> CANCELADO`
  - `CONFIRMADO -> CANCELADO`
  - `RASCUNHO|CONFIRMADO -> ADICIONADO_EXTRA` (quando origem fora do planejamento)

## 2. ProjetoEvento
- Purpose: Agrupador tematico de eventos.
- Fields:
  - `id` (UUID, PK)
  - `nome` (string, 1..160, obrigatorio)
  - `descricao` (string, 0..2000)
  - `inicio_previsto_utc` (timestamp, opcional)
  - `fim_previsto_utc` (timestamp, opcional)
  - `status` (enum: ATIVO, ARQUIVADO)
  - `criado_em_utc`, `atualizado_em_utc`
- Validation rules:
  - Se ambos presentes, `fim_previsto_utc >= inicio_previsto_utc`

## 3. EventoEnvolvido
- Purpose: Relacao N:N de organizacoes participantes em eventos.
- Fields:
  - `evento_id` (UUID, FK -> EventoParoquial.id)
  - `organizacao_id` (UUID, FK externa -> Organizacao.id)
  - `papel_participacao` (enum: APOIO, COEXECUCAO, COMUNICACAO, LITURGIA, OUTRO)
  - `incluido_em_utc`
- Constraints:
  - PK composta (`evento_id`, `organizacao_id`)
  - Relacao opcional por evento: um evento pode nao ter participantes
  - Operacao explicita de limpeza total remove todos participantes e mantem o evento valido

## 4. ObservacaoEvento
- Purpose: Trilha imutavel de comunicacao e auditoria.
- Fields:
  - `id` (UUID, PK)
  - `evento_id` (UUID, FK -> EventoParoquial.id, obrigatorio)
  - `usuario_id` (UUID, FK externa -> Usuario.id, obrigatorio)
  - `tipo` (enum: NOTA, JUSTIFICATIVA, APROVACAO, REPROVACAO, CANCELAMENTO, AJUSTE_HORARIO)
  - `conteudo` (string, 1..4000, obrigatorio)
  - `criado_em_utc` (timestamp, obrigatorio)
- Constraints:
  - Append-only: sem update/delete funcional
  - Conteudo obrigatorio

## 5. EventoRecorrencia
- Purpose: Definir regras de repeticao (ex: missas fixas).
- Fields:
  - `id` (UUID, PK)
  - `evento_base_id` (UUID, FK -> EventoParoquial.id)
  - `frequencia` (enum: DIARIA, SEMANAL, MENSAL)
  - `intervalo` (int > 0)
  - `dias_semana` (array<int>, opcional)
  - `ocorrencias_max` (int, opcional)
  - `ate_utc` (timestamp, opcional)
- Validation rules:
  - `ocorrencias_max` xor `ate_utc` permitido, ambos opcionais

## 6. Local
- Purpose: Recurso de realizacao do evento.
- Fields:
  - `id` (UUID, PK)
  - `nome` (string, obrigatorio)
  - `tipo` (enum: FISICO, VIRTUAL, ITINERANTE)
  - `capacidade` (int, opcional)
  - `ativo` (boolean)

## 7. Categoria
- Purpose: Classificacao funcional/liturgica.
- Fields:
  - `id` (UUID, PK)
  - `nome` (string, obrigatorio)
  - `descricao` (string, opcional)
  - `ativo` (boolean)

## 8. Entidades Externas (Read-Only)
- `Usuario` (externa): usada para autoria de observacao e contexto de seguranca.
- `Organizacao` (externa): usada para responsabilidade e participacao de evento.
- `MembroOrganizacao` (externa): usada para derivar papel/permissao.

## 9. PapelOrganizacionalCatalogo (Regra Derivada)
- Purpose: Representar validacoes de papel por tipo de organizacao para RBAC em runtime.
- Source: Derivado de `membros_organizacao` + metadados de `organizacoes` (somente leitura).
- Rules:
  - Pastorais/Laicato: `coordenador`, `vice-coordenador`, `membro`
  - Clero: `paroco`, `vigario`, `padre`
  - Conselho: `coordenador`, `vice-coordenador`, `secretario`, `membro`
  - Regra negativa mandatória: `secretario` em organizacao diferente de Conselho deve resultar em negacao de acesso (`ACCESS_DENIED`).

## 10. VisibilityProjection (Regra de Exposicao)
- Purpose: Definir subconjuntos de campos e alcance de visibilidade conforme status e contexto de autenticacao.
- Rules:
  - Publico anonimo (`GET /eventos`): apenas eventos `CONFIRMADO`.
  - Interno autenticado: acesso conforme RBAC; inclui `RASCUNHO` e `CANCELADO` conforme permissao.
  - Historico de `CANCELADO`: obrigatorio expor motivo de cancelamento para usuarios com escopo interno autorizado.

## Relationship Summary
- `ProjetoEvento (1) -> (N) EventoParoquial`
- `EventoParoquial (1) -> (N) ObservacaoEvento`
- `EventoParoquial (1) -> (N) EventoRecorrencia`
- `EventoParoquial (N) -> (1) Organizacao` via `organizacao_responsavel_id` (externa)
- `EventoParoquial (N) <-> (N) Organizacao` via `EventoEnvolvido` (externa, opcional; pode ficar vazio apos limpeza total)
- `ObservacaoEvento (N) -> (1) Usuario` (externa)
