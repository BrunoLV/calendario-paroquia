# Feature Specification: API de Calendario Anual Paroquial

**Feature Branch**: `001-parish-calendar-api`  
**Created**: 2026-03-14  
**Status**: Draft  
**Input**: User description: "Desenvolver uma API para o gerenciamento de um calendário anual de uma paróquia, permitindo a organização de eventos, missas fixas e atividades pastorais com controle hierárquico de acesso. O sistema deve permitir a organização de eventos individuais ou agrupados sob um projeto (ex: Novenas, Semana Santa), garantindo a rastreabilidade de observações."

## Clarifications

### Session 2026-03-14

- Q: Qual modelo de autorizacao hierarquica deve ser adotado? -> A: RBAC hierarquico com papeis clericais e organizacionais, com regras de permissao por responsabilidade no evento e colaboracao via observacoes.
- Q: Qual estrategia de exclusao de eventos deve ser adotada? -> A: Soft delete com status "cancelado".
- Q: Qual estrategia de timezone deve ser adotada? -> A: Armazenar em UTC e converter para America/Sao_Paulo nas respostas.
- Q: Como tratar conflitos de agenda? -> A: Permitir inclusao do evento e marcar conflito para discussao posterior, com decisao administrativa de qual evento permanece.
- Q: Quem tem autoridade final para decidir conflitos de agenda? -> A: Paroco, vigario ou coordenador do conselho (representando o conselho pastoral).
- Q: Quem aprova mudancas de horario e cancelamento? -> A: Paroco, vigario ou coordenador do conselho, com autorizacao obrigatoria antes da efetivacao.
- Q: Quem autoriza remocao, cancelamento e edicao de horario? -> A: Conselho na figura do coordenador ou Clero na figura do paroco.
- Q: Qual deve ser o ciclo de vida de status do evento? -> A: RASCUNHO, CONFIRMADO, CANCELADO e ADICIONADO_EXTRA, com regras especificas de visibilidade e comportamento.
- Q: Quem possui autoridade administrativa total? -> A: Clero na figura do paroco ou coordenador do conselho.
- Q: Quem pode incluir eventos no calendario? -> A: Admin Geral (paroco, vigario, conselho pastoral) e Admin de Pastoral (coordenador e vice da pastoral/laicato responsavel).
- Q: Como detalhar os papeis de acesso? -> A: Admin Geral, Admin de Pastoral, Colaborador e Leitor, com permissoes distintas por escopo.
- Q: Como estruturar o sistema inicial por blocos de capacidade? -> A: Base de governanca, infraestrutura/classificacao, coracao do calendario (MVP), complexidade/colaboracao e rastreabilidade/comunicacao.
- Q: O papel Secretario pode existir em qualquer organizacao? -> A: Nao. Secretario e papel exclusivo do Conselho.
- Q: Quais papeis existem por organizacao? -> A: Pastorais/Laicato (coordenador, vice-coordenador, membro), Clero (paroco, vigario, padre), Conselho (coordenador, vice-coordenador, secretario, membro).
- Q: Esta API gerencia organizacoes, usuarios e membros_organizacao? -> A: Nao. Essa API e responsavel apenas pelo calendario paroquial; organizacoes, usuarios e membros_organizacao sao gerenciados por outras aplicacoes.
- Q: O banco de dados e exclusivo desta API? -> A: Nao. O banco e compartilhado com outras apps; esta API le as tabelas organizacoes, usuarios e membros_organizacao para controle de acesso e auditoria, mas nao escreve nelas.
- Q: Eventos devem ser vinculados a usuarios? -> A: Nao. Eventos sao vinculados a organizacoes. Apenas observacoes podem referenciar o identificador do usuario (autoria/auditoria).
- Q: Qual e a estrutura de vinculo de organizacoes em um evento? -> A: Um evento possui exatamente uma organizacao responsavel (N:1, obrigatoria) e, opcionalmente, uma ou mais organizacoes participantes/colaboradoras (N:N via eventos_envolvidos).
- Q: Como tratar a dependencia de US2 em relacao aos artefatos do evento base? -> A: US2 depende explicitamente dos artefatos de evento base (modelo/endpoint de eventos), disponibilizados na Foundation+US1 ou antecipados na Foundation quando necessario.
- Q: A operacao de limpeza total de participantes e valida? -> A: Sim. A API deve permitir limpar totalmente os participantes de um evento, mantendo estado valido sem organizacoes participantes.
- Q: Como medir resultados de usabilidade e desempenho da API? -> A: O plano deve incluir tarefas e instrumentacao para tempo de cadastro, latencia sob carga e indicador de retrabalho.
- Q: Qual politica de acesso anonimo vs autenticado deve ser adotada? -> A: Leitura de calendario publico (eventos CONFIRMADO) permite acesso anonimo; demais leituras internas e todas as mutacoes exigem autenticacao com RBAC.
- Q: Como validar RBAC por organizacao e catalogo de papeis? -> A: Devem existir testes de contrato/integracao para catalogo de papeis e regra por organizacao, incluindo caso negativo que rejeita Secretario fora do Conselho.
- Q: Como validar visibilidade publica por status e historico de cancelamento? -> A: Devem existir testes dedicados para garantir RASCUNHO fora do publico, CONFIRMADO visivel no publico e CANCELADO exibido no historico com motivo.
- Q: Como tratar ADICIONADO_EXTRA sem justificativa? -> A: O dominio deve rejeitar ADICIONADO_EXTRA sem justificativa obrigatoria, com teste de contrato/integracao dedicado.
- Q: Como remover ambiguidade entre rastreabilidade e auditoria de observacoes? -> A: Rastreabilidade append-only fica em requisito funcional de historico imutavel; auditoria de autoria e evidencias verificaveis fica em requisito de governanca/auditoria.
- Q: Como tornar mensuravel a qualidade de codigo e aderencia arquitetural? -> A: Definir limites objetivos de complexidade, regras de dependencias entre camadas validadas por tooling e cobertura minima de testes para componentes criticos.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Gerir eventos do calendario (Priority: P1)

Como secretaria paroquial, quero cadastrar, consultar, editar e remover eventos para manter o calendario anual atualizado e confiavel.

**Why this priority**: Sem gestao basica de eventos (CRUD), o calendario nao entrega valor operacional para a paroquia.

**Independent Test**: Pode ser testada de forma independente ao criar um evento, listar no periodo, atualizar dados e confirmar remocao com regras de validacao aplicadas.

**Acceptance Scenarios**:

1. **Given** um usuario no papel de Admin Geral (paroco, vigario, conselho pastoral) ou Admin de Pastoral (coordenador/vice da pastoral responsavel), **When** cadastra um evento com dados obrigatorios validos, **Then** o evento e persistido e retornado com identificador unico.
2. **Given** um evento existente, **When** o usuario solicita alteracao de data/horario, **Then** o sistema registra solicitacao pendente e so efetiva apos autorizacao de paroco, vigario ou coordenador do conselho.
3. **Given** um evento cancelado com autorizacao de paroco, vigario ou coordenador do conselho, **When** o calendario do periodo e consultado, **Then** o evento nao aparece como ativo.

---

### User Story 2 - Organizar eventos por projeto pastoral (Priority: P2)

Como coordenador pastoral, quero agrupar eventos sob projetos (ex: Novenas, Semana Santa) para planejar e acompanhar iniciativas com contexto comum.

**Why this priority**: O agrupamento por projeto aumenta a capacidade de planejamento tematico e reduz trabalho manual na organizacao das atividades.

**Independent Test**: Pode ser testada de forma independente criando um projeto, vinculando eventos e consultando calendario filtrado por projeto, desde que os artefatos de evento base (modelo/endpoint de eventos) estejam disponiveis.

**Acceptance Scenarios**:

1. **Given** um projeto pastoral existente, **When** o usuario vincula eventos ao projeto, **Then** todos os eventos vinculados ficam consultaveis por projeto e por periodo.
2. **Given** um evento vinculado a projeto, **When** o usuario remove o vinculo, **Then** o evento permanece no calendario como evento individual.
3. **Given** um evento com organizacoes participantes, **When** o usuario autorizado realiza limpeza total de participantes, **Then** o evento permanece valido sem participantes e preserva a organizacao responsavel.

---

### User Story 3 - Controlar acesso hierarquico e rastrear observacoes (Priority: P3)

Como administrador paroquial, quero aplicar permissoes hierarquicas e registrar observacoes rastreaveis para garantir governanca e historico das decisoes.

**Why this priority**: O controle de acesso e a rastreabilidade reduzem risco operacional e aumentam confianca no processo de planejamento.

**Independent Test**: Pode ser testada de forma independente ao validar restricoes de permissao por perfil e registrar observacoes com autoria, data e historico de alteracoes.

**Acceptance Scenarios**:

1. **Given** um usuario sem permissao de edicao, **When** tenta alterar um evento, **Then** o sistema bloqueia a operacao e retorna erro de autorizacao.
2. **Given** um evento existente, **When** um usuario autorizado adiciona observacao, **Then** a observacao fica vinculada ao evento com autor e data/hora, sem alteracao posterior do conteudo.
3. **Given** um usuario autenticado com papel valido no catalogo da sua organizacao, **When** executa operacao permitida pelo seu perfil, **Then** o sistema autoriza a operacao conforme RBAC hierarquico e escopo organizacional.
4. **Given** um usuario com papel `secretario` vinculado a organizacao diferente de Conselho, **When** tenta executar operacao reservada ao papel, **Then** o sistema rejeita a requisicao por violacao da regra de papel por organizacao.

### Edge Cases

- Tentativa de criar evento duplicado no mesmo horario, local e categoria.
- Tentativa de atualizar ou remover evento inexistente.
- Tentativa de excluir definitivamente (hard delete) um evento ja referenciado por projeto ou observacao.
- Conflito de horario entre missa fixa e atividade pastoral no mesmo recurso critico.
- Conflito marcado sem decisao administrativa registrada.
- Solicitacao de alteracao de horario, cancelamento ou remocao sem autorizacao de paroco, vigario ou coordenador do conselho.
- Tentativa de inclusao de evento por perfil Leitor.
- Evento em RASCUNHO publicado indevidamente no calendario publico.
- Evento ADICIONADO_EXTRA sem justificativa registrada em observacao.
- Remocao de projeto contendo eventos ativos sem perder historico dos eventos.
- Inclusao de observacao vazia ou acima do limite permitido.
- Mudanca de horario em data com ajuste civil de fuso/horario local.

## API Contract & Validation *(mandatory)*

- Endpoints novos para gerenciamento de eventos: criar, listar por periodo, obter por identificador, atualizar e remover.
- Endpoints novos para gerenciamento de projetos: criar, listar, atualizar e remover projeto, alem de vincular e desvincular eventos.
- Endpoints novos para observacoes: incluir e listar historico de observacoes imutaveis.
- Endpoints novos para fluxo de aprovacao: submeter solicitacao de alteracao de horario/cancelamento, aprovar/reprovar e consultar status.
- Endpoints de autorizacao administrativa devem registrar o autorizador (paroco, vigario ou coordenador do conselho) para remocao, cancelamento e edicao de horario.
- Endpoint de criacao de eventos deve aceitar apenas perfis de Admin Geral e Admin de Pastoral no escopo permitido.
- Endpoint de consulta do calendario publico deve permitir acesso anonimo apenas para eventos no status CONFIRMADO.
- Endpoints de leitura interna (incluindo detalhes nao publicos) e todos os endpoints de mutacao devem exigir autenticacao e aplicar RBAC hierarquico.
- A validacao RBAC deve incluir regra de papel por organizacao, com rejeicao explicita de `secretario` fora do Conselho.
- Regras de validacao obrigatorias: titulo, tipo de atividade, data/hora inicial e final, status e origem da alteracao.
- Regras de validacao obrigatorias para ciclo de vida: `ADICIONADO_EXTRA` exige justificativa textual nao vazia e rastreavel em observacao/auditoria.
- Erros devem ser retornados com codigo de negocio deterministico para: validacao, recurso nao encontrado, acesso negado e violacao de regra de vinculo.
- Conflitos de agenda devem ser sinalizados com codigo/status deterministico de conflito para tratativa posterior, sem bloquear cadastro/atualizacao.
- Mudancas de contrato devem manter compatibilidade para consumidores existentes por pelo menos um ciclo de versao menor, com aviso de deprecacao quando aplicavel.
- O contrato deve possuir testes dedicados de visibilidade por status: `RASCUNHO` fora do publico, `CONFIRMADO` visivel no publico e `CANCELADO` exibido com motivo no historico.
- O contrato deve possuir teste dedicado para rejeicao de `ADICIONADO_EXTRA` sem justificativa.

## Calendar Integrity Rules *(mandatory for calendar/event features)*

- A API deve usar estrategia canonica unica para data/hora em todas as operacoes e respostas.
- Persistencia temporal deve ocorrer em UTC; respostas para consumidores paroquiais devem ser convertidas para America/Sao_Paulo.
- Eventos devem ter regras consistentes para evitar intervalos invalidos (fim antes de inicio) e identificar sobreposicoes com marcacao de conflito.
- Missas fixas devem ser representadas de forma previsivel para todo o calendario anual, com comportamento explicito para excecoes.
- A listagem deve ser deterministicamente ordenada por data/hora inicial e criterio secundario estavel.
- Filtros por periodo, tipo e projeto devem produzir resultados consistentes e reproduziveis.
- Exclusao de evento deve ser logica (soft delete), atualizando status para "cancelado" e preservando vinculos historicos.
- A operacao de atualizacao de participantes deve suportar limpeza total (lista vazia) como estado valido, preservando a organizacao responsavel obrigatoria do evento.

## Event Lifecycle and Status Rules *(mandatory)*

- **RASCUNHO**:
	- Uso: planejamento inicial pelo coordenador da pastoral responsavel.
	- Visibilidade: apenas pastoral responsavel, padres e conselho.
	- Regra: nao aparece no calendario publico.
- **CONFIRMADO**:
	- Uso: evento aprovado ou integrante do calendario oficial.
	- Visibilidade: publico para todos os fieis.
- **CANCELADO**:
	- Uso: evento que nao ocorrera.
	- Comportamento: mantido para historico (soft delete logico), exibido com indicacao de cancelamento no calendario e com observacao de motivo.
- **ADICIONADO_EXTRA**:
	- Uso: evento surgido fora do planejamento anual inicial.
	- Regra: deve ser marcado explicitamente para auditoria de desvio do planejamento anual e exige justificativa textual obrigatoria.

## Operational Observability *(mandatory)*

- Toda operacao de criacao, atualizacao, remocao, vinculacao e observacao deve registrar trilha operacional estruturada.
- Registros operacionais devem incluir, no minimo: identificador de correlacao, ator, acao, alvo, data/hora e resultado.
- Falhas devem registrar motivo de negocio e contexto suficiente para diagnostico sem expor dados sensiveis.
- A trilha de observacoes deve permitir auditoria de autoria e historico append-only.

## Architecture and Code Standards *(mandatory)*

- A funcionalidade deve respeitar camadas de dominio, aplicacao e infraestrutura com responsabilidades claras.
- Adaptadores de entrada e saida devem encapsular detalhes externos, preservando o nucleo de regras de negocio.
- Regras de negocio de calendario, projetos e observacoes devem permanecer independentes de detalhes de transporte.
- O codigo alterado deve manter legibilidade, baixo acoplamento e tratamento explicito de erros de negocio.
- A implementacao deve aplicar boas praticas de Java e Spring para injecao de dependencias, validacao, fronteiras transacionais e mapeamento de excecoes.
- Conversao de timezone deve ocorrer em adaptadores de entrada/saida, preservando UTC no nucleo de dominio.
- Critério mensurável de complexidade: metodos alterados em dominio/aplicacao devem manter complexidade ciclomática <= 10, sem violacoes em ferramenta de analise estatica adotada pelo projeto.
- Critério mensurável de arquitetura: regras de fronteira clean/hexagonal devem ser validadas por teste automatizado de arquitetura (ex.: ArchUnit), com 0 violacoes para dependencias proibidas entre `domain`, `application`, `api` e `infrastructure`.
- Critério mensurável de testes: cobertura minima de 80% em linhas para pacotes `domain` e `application`, e cobertura minima de 90% para politicas/regras criticas (`AuthorizationPolicy`, `CalendarIntegrityPolicy`, validacao de status de evento).

## Domain Model and Relationships *(mandatory)*

- Modelo conceitual principal:
	- Usuarios (1:N) Membros_Organizacao (N:1) Organizacoes. *(tabelas externas — banco compartilhado; gerenciadas por outras apps; esta API apenas le)*
	- Projetos_Eventos (1:N) Eventos (N:1) Locais.
	- Eventos (N:1) Categorias.
	- Eventos (1:N) Observacoes_Evento.
	- Eventos (N:1) Organizacao_Responsavel. *(exatamente uma organizacao responsavel por evento)*
	- Eventos (N:N) Organizacoes_Participantes via Eventos_Envolvidos. *(relacao opcional; quando presente, uma ou mais organizacoes colaboradoras)*
	- Observacoes_Evento (N:1) Usuarios. *(referencia somente leitura ao usuario autor — banco compartilhado)*
- Restricao de vinculo organizacional:
	- Todo evento DEVE ter exatamente uma `organizacao_responsavel_id` (chave estrangeira obrigatoria).
	- Um evento PODE nao ter organizacoes participantes; quando houver participantes, DEVE haver uma ou mais organizacoes em `eventos_envolvidos`.
	- A organizacao responsavel determina o escopo de autoridade para operacoes sensiveis (Admin de Pastoral so opera no escopo da propria organizacao responsavel).
	- Organizacoes participantes conferem visibilidade e colaboracao ao evento, mas nao conferem autoridade de gestao.
	- Eventos NAO SAO vinculados a usuarios. Observacoes podem referenciar o identificador do usuario para autoria e rastreabilidade.
- Hierarquia de eventos:
	- projetos_eventos como guarda-chuva tematico (ex: Novena, Semana Santa).
	- eventos como unidade operacional (missa, reuniao, procissao), com ou sem projeto_id.
	- eventos_recorrencia para regras de repeticao (ex: missas fixas).
	- observacoes_evento como historico de comunicacao e decisoes.
- Regra de permissao organizacional:
	- **Admin Geral**: Paroco, Vigario e Conselho Pastoral.
	  - Criar, editar e excluir qualquer evento ou projeto.
	  - Autorizar mudanca de horario, cancelamento e remocao administrativa.
	  - Deliberar decisao final de conflitos (na figura de paroco, vigario ou coordenador do conselho).
	- **Admin de Pastoral**: Coordenador e Vice da Pastoral/Laicato responsavel.
	  - Criar e editar eventos no escopo da propria pastoral.
	  - Submeter solicitacoes para alteracoes sensiveis (horario/cancelamento/remocao).
	- **Colaborador**: Membros da pastoral responsavel ou envolvida.
	  - Adicionar observacoes e editar detalhes nao sensiveis conforme regras vigentes.
	- **Leitor**: Demais usuarios e fieis.
	  - Apenas visualizar calendario conforme visibilidade do status do evento.

- Catalogo de papeis por organizacao:
	- **Pastorais**: coordenador, vice-coordenador, membro.
	- **Laicato**: coordenador, vice-coordenador, membro.
	- **Clero**: paroco, vigario, padre.
	- **Conselho**: coordenador, vice-coordenador, secretario, membro.
	- Regra de consistencia: o papel secretario e exclusivo do Conselho.
	- Regra de autorizacao: possuir papel organizacional nao implica automaticamente autoridade administrativa total; prevalecem as regras de autorizacao desta especificacao.
- Hierarquia institucional de referencia para autorizacao:
	- Clero > Conselho > Coordenador > Vice > Secretario > Membro.

## Initial System Scope (Phased)

- **Dependencias Externas — Banco Compartilhado (fora do escopo desta API)**:
	- `organizacoes`: gerenciada por outra app; consumida por esta API para vincular eventos a organizacoes responsaveis e colaboradoras.
	- `usuarios`: gerenciada por outra app; consumida por esta API para autoria de observacoes e aplicacao de RBAC.
	- `membros_organizacao`: gerenciada por outra app; consumida por esta API para validacao de papeis e permissoes no controle de acesso.
	- Esta API nao cria, nao edita e nao remove registros nestas tabelas.
- **Fase 1 - Infraestrutura e Classificacao (Onde e O Que)**:
	- `locais`: matriz, salao, sala, link virtual, praca e outros recursos.
	- `categorias`: missa, formacao, reuniao, festa e demais classificacoes.
- **Fase 2 - Coracao do Calendario (Evento Base / MVP)**:
	- `projetos_eventos`: agrupadores como Novena 2026 e Semana Santa.
	- `eventos`: entidade principal com projeto opcional, local, categoria e organizacao responsavel (nunca vinculada diretamente ao usuario).
	- Entrega de valor do MVP: criar evento unico, vincular projeto opcional e consultar no calendario.
- **Fase 3 - Complexidade e Colaboracao (Plus)**:
	- `eventos_envolvidos`: associacao N:N para organizacoes colaboradoras.
	- `eventos_recorrencia`: regras para missas fixas e eventos repetitivos.
	- Dependencia explicita: capacidades de US2 dependem dos artefatos de evento base (`eventos`) entregues em Foundation+US1 ou promovidos para Foundation conforme estrategia de execucao.
- **Fase 4 - Rastreabilidade e Comunicacao (Historico)**:
	- `observacoes_evento`: notas operacionais e historico humano de ajustes.
	- Observacoes com trilha append-only, autoria rastreavel via usuario_id (referencia ao banco compartilhado) e auditoria.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: O sistema MUST permitir criar, consultar, atualizar e remover eventos do calendario anual paroquial.
- **FR-002**: O sistema MUST permitir classificar eventos por tipo (missa fixa, evento individual, atividade pastoral).
- **FR-003**: O sistema MUST permitir criar e manter projetos pastorais para agrupamento de eventos relacionados.
- **FR-004**: O sistema MUST permitir vincular e desvincular eventos a um projeto sem perda de historico do evento.
- **FR-005**: O sistema MUST aplicar controle hierarquico de acesso por perfil para operacoes de leitura e escrita.
- **FR-006**: O sistema MUST bloquear alteracoes quando o usuario nao possuir permissao suficiente.
- **FR-007**: O sistema MUST validar campos obrigatorios, coerencia temporal e regras de conflito antes de persistir alteracoes.
- **FR-008**: O sistema MUST registrar observacoes vinculadas ao evento ou projeto com autoria e carimbo temporal.
- **FR-009**: O sistema MUST manter rastreabilidade das observacoes em historico imutavel e append-only.
- **FR-010**: O sistema MUST disponibilizar consulta de calendario por intervalo de datas com filtros por tipo e por projeto.
- **FR-011**: O sistema MUST retornar codigos padronizados para validacao, autorizacao, recurso nao encontrado e sinalizacao de conflito de agenda.
- **FR-012**: O sistema MUST preservar consistencia de dados quando houver operacoes concorrentes sobre o mesmo evento.
- **FR-013**: O sistema MUST suportar eventos recorrentes por meio de regra de recorrencia associada ao evento base.
- **FR-014**: O sistema MUST suportar relacionamento N:N entre eventos e organizacoes envolvidas na execucao.
- **FR-015**: O sistema MUST aplicar autorizacao baseada na hierarquia Clero > Conselho > Coordenador > Vice > Secretario > Membro.
- **FR-016**: O sistema MUST restringir membros e pastorais envolvidas a colaboracao por observacoes quando nao forem responsaveis pela gestao do evento.
- **FR-017**: O sistema MUST manter trilha de auditoria verificavel para observacoes, incluindo autoria, carimbo temporal, ator da acao e identificador de correlacao.
- **FR-018**: O sistema MUST aplicar soft delete em eventos, marcando status "cancelado" em vez de remocao fisica.
- **FR-019**: O sistema MUST preservar referencias historicas de projetos, organizacoes envolvidas e observacoes apos cancelamento do evento.
- **FR-020**: O sistema MUST armazenar timestamps de eventos em UTC no backend.
- **FR-021**: O sistema MUST retornar data/hora convertidas para America/Sao_Paulo nas respostas da API voltadas ao calendario paroquial.
- **FR-022**: O sistema MUST permitir criacao/atualizacao de evento mesmo com conflito de agenda, marcando status de conflito e motivo.
- **FR-023**: O sistema MUST registrar decisao administrativa posterior para cada conflito, incluindo qual evento permanece e qual e cancelado/ajustado.
- **FR-024**: O sistema MUST restringir a decisao final de conflitos de agenda aos perfis paroco, vigario ou coordenador do conselho.
- **FR-025**: O sistema MUST registrar auditoria da decisao final de conflito com decisor, data/hora e justificativa.
- **FR-026**: O sistema MUST exigir autorizacao de paroco, vigario ou coordenador do conselho para efetivar mudancas de horario de eventos.
- **FR-027**: O sistema MUST exigir autorizacao de paroco, vigario ou coordenador do conselho para efetivar cancelamento de eventos.
- **FR-028**: O sistema MUST manter fluxo auditavel de solicitacao, aprovacao/reprovacao e efetivacao para mudancas de horario, cancelamento e remocao administrativa.
- **FR-029**: O sistema MUST suportar os status de evento RASCUNHO, CONFIRMADO, CANCELADO e ADICIONADO_EXTRA.
- **FR-030**: O sistema MUST restringir a visibilidade de eventos em RASCUNHO para pastoral responsavel, padres e conselho.
- **FR-031**: O sistema MUST expor eventos CONFIRMADO no calendario publico.
- **FR-032**: O sistema MUST manter eventos CANCELADO no historico e exibi-los com indicacao de cancelamento e motivo.
- **FR-033**: O sistema MUST exigir marcacao explicita de ADICIONADO_EXTRA para eventos fora do planejamento anual.
- **FR-034**: O sistema MUST disponibilizar consulta/auditoria da proporcao de eventos ADICIONADO_EXTRA no periodo.
- **FR-035**: O sistema MUST restringir autoridade administrativa total de eventos aos perfis de Admin Geral (paroco, vigario e conselho pastoral).
- **FR-036**: O sistema MUST restringir inclusao/criacao de eventos aos perfis de Admin Geral e Admin de Pastoral no escopo autorizado.
- **FR-037**: O sistema MUST restringir perfil Colaborador a observacoes e edicoes nao sensiveis conforme regra de negocio vigente.
- **FR-038**: O sistema MUST restringir perfil Leitor a visualizacao do calendario e detalhes publicos.
- **FR-039**: O sistema MUST restringir o papel Secretario exclusivamente a membros vinculados ao Conselho.
- **FR-040**: O sistema MUST suportar e validar o catalogo de papeis por organizacao: Pastorais/Laicato (coordenador, vice-coordenador, membro), Clero (paroco, vigario, padre), Conselho (coordenador, vice-coordenador, secretario, membro).
- **FR-041**: O sistema MUST acessar as tabelas compartilhadas organizacoes, usuarios e membros_organizacao exclusivamente em modo de leitura, sem realizar insercao, atualizacao ou remocao de registros nessas tabelas.
- **FR-042**: O sistema MUST vincular cada evento a exatamente uma organizacao responsavel (campo obrigatorio organizacao_responsavel_id). O sistema MAY manter evento sem organizacoes participantes; quando participantes forem informadas, MUST haver uma ou mais organizacoes em `eventos_envolvidos`. O sistema MUST NOT vincular eventos a usuarios. Observacoes MUST referenciar o identificador do usuario autor proveniente do banco compartilhado.
- **FR-043**: O sistema MUST aplicar o escopo de autoridade de Admin de Pastoral com base na organizacao responsavel do evento: um Admin de Pastoral so pode criar ou editar eventos cuja organizacao responsavel seja a sua propria organizacao.
- **FR-044**: O sistema MUST permitir que organizacoes participantes (eventos_envolvidos) sejam adicionadas ou removidas por Admin Geral ou pelo Admin de Pastoral da organizacao responsavel do evento.
- **FR-045**: O sistema MUST permitir acesso anonimo somente para consulta do calendario publico de eventos CONFIRMADO; consultas internas com dados nao publicos MUST exigir autenticacao.
- **FR-046**: O sistema MUST aceitar operacao explicita de limpeza total de participantes de evento (lista vazia), mantendo o evento em estado valido sem participantes e preservando `organizacao_responsavel_id` obrigatoria.
- **FR-047**: O sistema MUST coletar e disponibilizar metricas operacionais para validar SC-001 (tempo de cadastro), SC-002 (latencia sob carga) e SC-005 (indicador de retrabalho).
- **FR-048**: O sistema MUST validar catalogo de papeis por organizacao em tempo de autorizacao e MUST rejeitar o papel `secretario` quando nao estiver vinculado ao Conselho.
- **FR-049**: O sistema MUST rejeitar eventos no status `ADICIONADO_EXTRA` quando a justificativa obrigatoria estiver ausente, vazia ou nao rastreavel para auditoria.
- **FR-050**: O sistema MUST manter testes de contrato/integracao dedicados para regras de visibilidade por status (`RASCUNHO`, `CONFIRMADO`, `CANCELADO`) e para a rejeicao de `ADICIONADO_EXTRA` sem justificativa.

### Key Entities *(include if feature involves data)*

- **EventoParoquial**: Representa um compromisso no calendario (titulo, descricao, tipo, inicio, fim, local, status, projetoVinculado, organizacaoResponsavelId). Possui exatamente uma organizacao responsavel (N:1, obrigatoria) e, opcionalmente, organizacoes participantes (N:N via EventoEnvolvido), com cardinalidade minima de 1 quando presentes. Nunca vinculado diretamente a usuario.
- **ProjetoEvento**: Representa agrupamento guarda-chuva de eventos (nome, periodo, descricao, status).
- **PerfilAcessoHierarquico**: Representa papel e nivel de permissao derivado das tabelas compartilhadas (papel, nivel, escopo de atuacao, permissoes). Lido do banco compartilhado; nao gerenciado por esta API.
- **Usuario** *(entidade externa — banco compartilhado)*: Identidade autenticada; referenciada em ObservacaoEvento para autoria e auditoria. Esta API nao cria nem edita usuarios.
- **Organizacao** *(entidade externa — banco compartilhado)*: Unidade paroquial (pastorais, conselho, clero); usada como chave de responsabilidade e colaboracao em eventos. Esta API nao cria nem edita organizacoes.
- **MembroOrganizacao** *(entidade externa — banco compartilhado)*: Vinculo usuario-organizacao-funcao; consumido por esta API para derivar perfil de acesso. Esta API nao cria nem edita membros.
- **Local**: Recurso fisico, virtual ou itinerante associado ao evento.
- **Categoria**: Classificacao para filtragem e visao do calendario.
- **EventoRecorrencia**: Regra de repeticao de eventos fixos.
- **EventoEnvolvido**: Associacao N:N entre evento e organizacoes participantes/colaboradoras. Representa organizacoes que participam do evento sem serem responsaveis pela sua gestao. A organizacao responsavel e gerenciada diretamente em EventoParoquial (organizacaoResponsavelId). O conjunto de participantes pode ser vazio apos operacao explicita de limpeza total.
- **ObservacaoEvento**: Registro imutavel de comunicacao no contexto do evento (apenas inclusao, sem edicao de conteudo). Referencia o identificador do usuario autor (banco compartilhado) para rastreabilidade e auditoria.
- **StatusEvento**: Enumeracao de ciclo de vida (RASCUNHO, CONFIRMADO, CANCELADO, ADICIONADO_EXTRA) que governa visibilidade e comportamento no calendario.

## Assumptions

- Esta API e responsavel exclusivamente pelo gerenciamento do calendario paroquial (eventos, projetos e observacoes); ela nao gerencia organizacoes, usuarios ou membros_organizacao.
- O banco de dados e compartilhado com outras aplicacoes; as tabelas organizacoes, usuarios e membros_organizacao sao de responsabilidade de outra app e consumidas por esta API apenas em modo de leitura.
- Eventos sao sempre vinculados a organizacoes; nenhum evento e vinculado diretamente a um usuario.
- Observacoes podem referenciar o identificador do usuario (campo usuario_id) do banco compartilhado para fins de autoria e auditoria.
- O ano pastoral e tratado em ciclos anuais, com possibilidade de consulta por qualquer intervalo dentro do ciclo.
- O controle hierarquico seguira os niveis Clero > Conselho > Coordenador > Vice > Secretario > Membro.
- O papel Secretario sera permitido apenas para membros do Conselho.
- A inclusao de novos eventos no calendario sera permitida apenas para perfis Admin Geral e Admin de Pastoral no escopo autorizado.
- A consulta do calendario publico de eventos CONFIRMADO pode ser consumida anonimamente; acessos internos e mutacoes exigem usuario autenticado.
- A decisao final sobre conflitos de agenda sera sempre de paroco, vigario ou coordenador do conselho.
- Remocao, cancelamento e mudancas de horario so sao efetivados apos autorizacao de paroco, vigario ou coordenador do conselho.
- Um evento pode existir sem projeto; projeto e opcional para acomodar eventos avulsos.
- Observacoes sao de uso administrativo interno e devem ser auditaveis.
- Missas fixas podem ser representadas por regras recorrentes ou por instancias planejadas, desde que o comportamento seja consistente para o usuario final.
- Eventos ADICIONADO_EXTRA devem conter justificativa institucional para analise do conselho.

## Dependencies

- Definicao oficial da matriz de papeis e permissoes da paróquia.
- Validacao institucional das categorias de evento e nomenclaturas pastorais.
- Politica de auditoria e retencao de historico aprovada pela administracao paroquial.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Usuarios autorizados conseguem registrar um novo evento completo em ate 2 minutos, em 90% das tentativas monitoradas.
- **SC-002**: 95% das consultas de calendario por periodo retornam resultado em ate 2 segundos sob carga operacional prevista da paroquia.
- **SC-003**: 100% das tentativas de escrita por usuarios sem permissao adequada sao bloqueadas e auditadas.
- **SC-004**: 100% das inclusoes de observacoes ficam rastreaveis com autor e data/hora em auditoria funcional.
- **SC-005**: A equipe administrativa reduz em pelo menos 40% o retrabalho de consolidacao manual do calendario apos 2 ciclos mensais de uso.
- **SC-006**: 100% dos conflitos detectados ficam marcados e associados a uma decisao administrativa posterior.
- **SC-007**: 100% das mudancas de horario, cancelamentos e remocoes administrativas efetivadas possuem autorizacao registrada de paroco, vigario ou coordenador do conselho.
- **SC-011**: 100% das tentativas de inclusao de eventos por perfis fora de Admin Geral/Admin de Pastoral sao bloqueadas e auditadas.
- **SC-008**: 100% dos eventos em RASCUNHO permanecem fora do calendario publico.
- **SC-009**: 100% dos eventos CANCELADO exibem motivo de cancelamento no historico/calendario.
- **SC-010**: O painel de auditoria anual apresenta taxa de eventos ADICIONADO_EXTRA por periodo e por organizacao.
- **SC-012**: O processo de medicao registra semanalmente baseline e tendencia para tempo medio de cadastro de evento, latencia P95 sob carga e indicador de retrabalho administrativo.
- **SC-013**: 100% das builds de CI para a feature passam com complexidade ciclomática <= 10 nos metodos alterados de `domain` e `application`.
- **SC-014**: 100% das execucoes do teste de arquitetura da feature aprovam sem violacoes de dependencias proibidas entre camadas.
- **SC-015**: A suíte automatizada da feature comprova cobertura minima de 80% para `domain` e `application` e 90% para regras criticas de autorizacao, integridade de calendario e ciclo de vida de status.
