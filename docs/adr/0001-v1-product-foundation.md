# ADR-0001: v1 product foundation

- **Status:** Accepted
- **Decision date:** 2026-07-22
- **Decider:** Product owner
- **Supersedes:** None

## Context

kmpRSS needs an explicit product centre before architecture and UI implementation
begin. The app must choose an initial audience, ownership model, organisation
model, reading-completion rule, and v1 boundaries. Leaving these implicit would
allow independent agents to build incompatible data models and UX assumptions.

## Decision

1. **Audience:** v1 is designed first for newcomers. Primary UI copy describes
   following websites and reading articles; protocol terms such as RSS and Atom
   appear only where they help. Expert OPML import remains supported.
2. **Ownership:** v1 stores data locally, requires no account, and offers no
   cross-device sync. OPML provides subscription portability.
3. **Organisation:** subscriptions and tags have a many-to-many relationship. A
   subscription may have zero or many tags. “Untagged” is a computed view.
4. **Saved:** saved/unsaved is a core v1 state independent of read/unread.
5. **Automatic read:** an entry becomes read when the reader viewport has reached
   at least 80% of laid-out article content.
6. **Reading day:** the same first 80% event grants credit to the current local
   calendar day. If a short entry is already at least 80% visible when opened, it
   qualifies immediately. No time threshold is added.
7. **Full content:** v1 renders content supplied by the feed and can open the
   original page. Extracting full article content from arbitrary pages is deferred.
8. **Notifications:** v1 has no notification backend. Any initial notification is
   best effort and can only concern entries discovered by local refresh. A timely,
   server-backed service may be reconsidered for v2 or later.
9. **Tag structure:** tags are a flat list and cannot contain other tags. A
   subscription can still belong to multiple tags.

## Consequences

### Positive

- Onboarding has one clear mental model and does not require RSS knowledge.
- Core use remains private, offline-capable, and free of backend operations.
- Tags support overlapping interests without copying subscriptions.
- Read state and reading rhythm share one deterministic, testable event.
- Reader quality can be prioritised over brittle page extraction.

### Costs and accepted trade-offs

- A person using two devices has independent state and must import subscriptions
  separately; OPML does not preserve read/saved state.
- Multi-tag OPML round-tripping is limited by the format and consuming apps.
- Short entries can count as read and grant a reading day immediately on open.
- Background notifications can be late or absent under OS scheduling constraints.
- Some truncated feeds require opening the original page in v1.

## Guardrails for implementation

- Do not add account identifiers, remote ownership, or sync conflict fields “for
  later” without an accepted sync design.
- Model `Subscription ↔ Tag` as many-to-many and enforce unique tag names according
  to a later naming/case decision.
- Keep tags flat; do not add parent IDs, hierarchy paths, or tree traversal.
- Persist maximum reading progress so layout changes cannot move it backwards.
- Do not use dwell time, analytics, or hidden heuristics to qualify reading.
- Do not fetch arbitrary original-page HTML as if it were feed-provided content.
- Do not market local notifications as immediate or guaranteed.

## Follow-up decisions

- Tag naming, case sensitivity, colour/icon semantics, and deletion behaviour.
- Exact notification scope for v1 and whether it belongs after the first launch.
- Exact automatic-read override behaviour after a user manually marks an entry
  unread.
