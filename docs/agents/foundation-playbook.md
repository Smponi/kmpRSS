# Foundation playbook for agents

- **Status:** Accepted
- **Last updated:** 2026-07-22
- **Audience:** AI agents changing the `reader/` project

## Required reading

Before architecture or feature work, read `Agents.md`, `docs/README.md`, the relevant product document and glossary,
ADR-0001, [platform foundation](../architecture/platform-foundation.md), and
[build contract](../engineering/build-and-quality.md). Markdown is canonical.

## Placement decision

```mermaid
flowchart TD
    N[New code] --> Q{Same behaviour and meaning?}
    Q -- yes --> C[commonMain feature/core package]
    Q -- Android UX/system --> A[androidMain]
    Q -- iOS UX/system --> I[iosMain]
    Q -- lifecycle/packaging only --> H[androidApp or iosApp]
```

| Answer | Destination |
|---|---|
| Same behaviour and meaning on both platforms | `commonMain`, behind a small interface when behaviour varies |
| Android Material/system behaviour | `androidMain` |
| iOS Apple/system behaviour | `iosMain` |
| Activity, SwiftUI host, signing or packaging | Host app module |

Use feature packages for product capabilities and `core` only for narrow cross-feature foundations. A module interface
must hide meaningful behaviour; do not create pass-through abstractions. One adapter is hypothetical, two adapters
make a real seam.

## TDD workflow

Work in vertical slices: one observable test fails, implement the minimum interface to make it pass, then refactor only
while green. Tests cross the same public interface as callers. Do not test private functions, implementation shape or
mock internal collaborators. Run the smallest relevant task after each cycle and the complete canonical gates before
handoff.

## Dependency admission

Before adding a library, record what non-trivial behaviour it owns, why the platform/standard library cannot provide
it, its KMP/platform support and maintenance risk. Implement one or two trivial functions locally. New versions belong
in `reader/gradle/libs.versions.toml`; never scatter versions through build scripts.

## Documentation parity

Every `docs/**/*.md` file has the same-path `.html` human view in the same commit. Markdown is normative. HTML may be
denser, but it must preserve every MUST/DO-NOT rule, stable ID, command, path, status, risk, consequence and graph edge.
Graphs require an adjacent text or table equivalent and must not be the only source of information. Run
`./scripts/verify-doc-pairs.sh`; semantic parity remains a review responsibility.

## Commit recipe

Prefer small explanatory commits such as: build reproducibility and gates; tested design-system seam; CI; canonical
documentation. Each commit should compile or document an intentional intermediate state, contain no generated build
output, and preserve unrelated user changes.

## ADR-0001 guardrails

Do not add account/sync ownership fields, nested tags, full-content extraction, a notification backend, dwell-time read
heuristics or Material-identical iOS UI. Reference accepted IDs instead of restating product decisions in code.

## Handoff evidence

Report commit IDs, exact commands and outcomes, which platform builds were real, any toolchain limitation, remaining
accessibility/device risks, and the next smallest test-first feature slice.
