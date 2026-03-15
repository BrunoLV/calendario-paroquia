<!--
Sync Impact Report
- Version change: 1.1.0 -> 1.2.0
- Modified principles:
	- III. Testability Is Non-Negotiable -> III. Testability Is Non-Negotiable
	- IV. Traceable Operations and Auditability -> IV. Traceable Operations and Auditability
- Added sections:
	- Operational Metrics and SLO Discipline
- Removed sections:
	- None
- Templates requiring updates:
	- ✅ updated: .specify/templates/plan-template.md
	- ✅ updated: .specify/templates/spec-template.md
	- ✅ updated: .specify/templates/tasks-template.md
	- ⚠ pending (path not present in repository): .specify/templates/commands/*.md
	- ✅ updated: .github/agents/copilot-instructions.md
- Follow-up TODOs:
	- TODO(COMMAND_TEMPLATES_DIR): criar `.specify/templates/commands/` para validacao completa de comandos.
-->

# Calendario Paroquial Constitution

## Core Principles

### I. API-First Domain Contract
All feature changes MUST start by defining or updating API behavior and domain rules for parish events.
Every endpoint MUST have explicit request/response contracts, validation rules, and error semantics.
CRUD operations MUST preserve event identity, schedule semantics, and consistency across create, read,
update, and delete flows.
Rationale: Stable contracts reduce regressions and keep annual planning integrations predictable.

### II. Liturgical Calendar Integrity
The system MUST enforce date/time integrity for the annual parish calendar, including conflict handling,
required event fields, and deterministic ordering of events.
Business rules for recurring celebrations, exceptional dates, and planning constraints MUST be declared in
specification artifacts before implementation.
Rationale: Parish operations depend on reliable calendar data and conflict-free planning.

### III. Testability Is Non-Negotiable
Each user story MUST define independent acceptance scenarios and at least one executable test path before
code is merged.
Changes affecting API contracts or validation rules MUST include automated tests that fail before the fix
and pass after implementation.
Contract and integration suites MUST explicitly cover at least: status-based public visibility,
RBAC by organizational scope, and rejection paths for invalid lifecycle transitions.
Rationale: The calendar API is operationally critical and must remain trustworthy release over release.

### IV. Traceable Operations and Auditability
All mutating operations (create, update, delete) MUST emit structured logs with correlation identifiers,
actor context when available, and outcome status.
Error responses MUST be diagnosable without exposing sensitive details.
Audit trails for domain-justified exceptions (such as out-of-plan event additions) MUST be immutable and
queryable by period and organization.
Rationale: Operational traceability is required to support parish administration and incident resolution.

### V. Clean and Hexagonal Architecture by Default
The project MUST adopt Clean Architecture and Hexagonal Architecture boundaries: domain/application logic
in core layers, transport and persistence in adapters, and framework-specific details kept at the edge.
Code MUST follow clean code practices (small cohesive units, expressive names, low cyclomatic complexity,
and explicit error handling) with maintainability favored over clever implementations.
Rationale: Architectural boundaries keep domain logic stable and reduce coupling to framework and infra.

## Technology Baseline

- The implementation language MUST be Java.
- The service framework MUST be Spring Boot.
- Spring components MUST follow best practices for configuration, dependency injection, validation,
	transaction boundaries, and error handling.
- Java code MUST follow modern Java best practices compatible with project target version and build tool.
- Any deviation from Java + Spring Boot baseline MUST be documented, justified, and approved in plan
	complexity tracking.

## Operational Metrics and SLO Discipline

- Every feature affecting event lifecycle, visibility, or authorization MUST define measurable success
	criteria and evidence collection strategy.
- Runtime instrumentation MUST cover at least: event registration lead time, calendar query latency, and
	administrative rework indicator.
- The project MUST maintain a recurring baseline snapshot cadence for operational metrics and preserve
	history for trend analysis.
- Pull requests changing critical flows MUST include metric impact notes and verification evidence.

## Domain and Data Constraints

- Event payloads MUST include title, date/time, type/category, and status fields required for planning.
- Date/time values MUST use a single canonical timezone strategy documented in the feature specification.
- Deletions MUST define whether operation is hard-delete or soft-delete and preserve required audit traces.
- Input validation errors MUST return deterministic machine-readable codes.
- Backward-incompatible contract changes MUST include migration notes and versioning impact.

## Delivery Workflow and Quality Gates

1. Specification phase MUST document user stories, edge cases, functional requirements, and measurable
	success criteria.
2. Planning phase MUST pass Constitution Check gates before implementation starts.
3. Tasks MUST be organized by user story, preserving independent delivery and testability.
4. Pull requests MUST include evidence of tests executed, API contract impact, and logging/observability
	updates where applicable.
5. Pull requests MUST document architecture impact, confirming layer boundaries and port/adapter alignment.
6. A feature is complete only when acceptance scenarios and success criteria are verifiably satisfied.

## Governance

This constitution supersedes project-level delivery conventions for feature specification, planning, and
task execution.

Amendment Procedure:
1. Propose changes through a documented update to this file with rationale and impact analysis.
2. Confirm template synchronization across `.specify/templates/` before ratification.
3. Record the version bump using semantic versioning policy defined below.

Versioning Policy:
1. MAJOR: Removal or redefinition of principles that changes governance guarantees.
2. MINOR: New principle/section or materially expanded mandatory guidance.
3. PATCH: Clarifications, wording improvements, and non-semantic refinements.

Compliance Review Expectations:
1. Every plan MUST include a Constitution Check result.
2. Every spec MUST declare testable acceptance scenarios and measurable outcomes.
3. Every task list MUST preserve traceability to user stories and quality gates.
4. Reviewers MUST block merges when mandatory constitutional gates are not satisfied.

**Version**: 1.2.0 | **Ratified**: 2026-03-14 | **Last Amended**: 2026-03-15
