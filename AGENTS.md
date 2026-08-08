# AGENTS.md

This file guides coding agents and automated contributors working in `atom-common`.

## Start here

Read `README.md`, `CHANGELOG.md`, the closest implementation, and its tests before changing code. Use
`llms.txt` as the compact repository index.

## Build and test

```bash
sh ./mvnw clean verify -Dgpg.skip=true
sh ./mvnw test-compile dependency:analyze -DfailOnWarning=true
```

Use the checked-in Maven Wrapper. The project publishes Java 25 bytecode and enforces Maven 3.9.16
or newer.

## Library rules

- Keep this module framework-neutral and free of application or business-domain concepts.
- Treat public classes, methods, serialized field names, error-code formats, and null behavior as compatibility
  contracts.
- Reject invalid input explicitly; do not catch `Throwable`, silently replace invalid values, or expose secrets in
  error messages.
- Prefer the smallest coherent change and add regression tests for every behavior change or bug fix.
- Preserve source and binary compatibility in patch releases unless the old behavior is demonstrably unsafe; call
  out deliberate behavior tightening in `CHANGELOG.md`.
- Keep generated source, Javadoc, and binary artifacts reproducible and publishable together.

## Release rules

- Never claim or consume a release only installed in a developer's local Maven repository.
- Before a release, update the POM version and dated changelog, run the full verification suite, and use the
  protected `Release to Maven Central` workflow from `main`.
- A release is complete only after the exact coordinate resolves from Maven Central using a clean local repository.
- Maven Central versions are immutable; never retry publication by overwriting an existing coordinate.

## Change workflow

1. Find the current contract and callers with `rg`.
2. Change the implementation and tests together.
3. Run the focused tests, then the full verification commands above.
4. Update `README.md`, `CHANGELOG.md`, and `llms.txt` when contracts, configuration, or release behavior changes.
5. Review the diff for accidental API breaks, secrets, stale versions, and local-only assumptions.
