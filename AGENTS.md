# AGENTS.md

`atom-common` is a shared Java library. Keep it framework-neutral and free of application or business-domain concepts.

## Context and execution

- Use `llms.txt` as the index. Read relevant `README.md` and `CHANGELOG.md` sections, the affected implementation,
  tests, and callers found with `rg`. Use `pom.xml`, the Maven Wrapper, and `.github/workflows/` for current build facts;
  changelog entries describe historical releases.
- Complete the requested work through verification. Resolve routine, reversible choices from repository evidence;
  ask only when unresolved ambiguity materially affects correctness, scope, or an irreversible action.
  Continue independent authorized work while waiting, and preserve unrelated user changes.
- When available and useful, delegate independent investigation or review to subagents; coordinate file ownership
  before parallel edits.

## Compatibility

- Preserve public APIs (including Lombok-generated methods), serialized fields, error-code formats, and null behavior.
  Patch releases preserve source and binary compatibility unless existing behavior is demonstrably unsafe;
  document deliberate behavior tightening in `CHANGELOG.md`.
- Reject invalid input explicitly in new APIs. For existing APIs, preserve established validation and fallback
  contracts unless changing them is part of the task; for example, `Pager.setTotalNum(null)` uses `NO_TOTAL_NUM`.
  Do not catch `Throwable` or expose secrets in errors.
- Make the smallest coherent change; add regression tests for runtime behavior changes and bug fixes.
- Keep generated source, Javadoc, and binary artifacts reproducible and publishable together.

## Verification

Use the checked-in Maven Wrapper and the JDK/Maven requirements in `pom.xml`.

- Documentation/instruction-only edits: check factual accuracy, referenced paths, and `git diff --check`;
  Maven verification is unnecessary unless build behavior or executable examples are affected.
- Code changes: run the affected tests first, e.g. `sh ./mvnw -Dtest=ErrorCodeTest test`, then the full checks below.
- Build, dependency, CI, or release changes: run both full checks and validate the affected configuration.

```bash
sh ./mvnw clean verify -Dgpg.skip=true
sh ./mvnw test-compile dependency:analyze -DfailOnWarning=true
```

After required checks pass, repeat or broaden them only for new changes, failures, or unresolved concerns.
Report blocked or skipped checks with the reason; do not present them as passing.

## Skills

- Load skills when explicitly requested or when their stated scope fits the task; read only the needed references.
- User instructions take precedence over skill guidelines. If a skill blocks or redirects the requested work,
  link the exact `SKILL.md`, quote the relevant instruction, and explain its applicability.
- Keep repository-wide rules here; use `.agents/skills/<name>/SKILL.md` for reusable workflows with clear triggers,
  without duplicating these rules or pinning model-specific prompting advice.

## Completion and releases

- Update `README.md` for user-facing usage/configuration changes, `CHANGELOG.md` for behavior/compatibility changes,
  and `llms.txt` when repository navigation changes. Review the diff for API breaks, secrets, and stale references.
- Report the outcome, verification results, and remaining blockers concisely in the user's language.
- For an authorized release, update the POM version and dated changelog, run both full checks, and use
  `.github/workflows/release.yml` (`Release to Maven Central`) from `main`.
- A local Maven install is not proof of publication. A release is complete only after the exact coordinate resolves
  from Maven Central using a clean local repository. Never overwrite an existing Central coordinate.
