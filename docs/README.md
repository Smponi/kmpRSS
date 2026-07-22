# kmpRSS documentation

This directory is the product and engineering memory of kmpRSS. Agents must read
the relevant documents before changing product behaviour or architecture.

Every Markdown document has a matching HTML document for human readers. Markdown
is the canonical source; when it changes, its HTML counterpart must be updated in
the same commit.

## Current documents

| Area | Agent source | Human view | Status |
|---|---|---|---|
| Product | [Core product](product/core-product.md) | [Core product](product/core-product.html) | Accepted |
| Domain | [Glossary](domain/glossary.md) | [Glossary](domain/glossary.html) | Accepted |
| Decision | [ADR-0001: v1 product foundation](adr/0001-v1-product-foundation.md) | [ADR-0001](adr/0001-v1-product-foundation.html) | Accepted |

## Status vocabulary

- **Proposed:** a working hypothesis awaiting an explicit product decision.
- **Accepted:** binding until superseded by a newer decision.
- **Deprecated:** retained for history but no longer binding.

## Documentation rule

Requirements use stable IDs (`PRD-*`). Open decisions use stable IDs (`Q-*`).
Tests, issues, ADRs, and commits should reference those IDs rather than copying
requirements into new, potentially divergent prose.
