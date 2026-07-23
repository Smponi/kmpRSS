# kmpRSS core product

- **Status:** Accepted — product decisions recorded in [ADR-0001](../adr/0001-v1-product-foundation.md)
- **Last updated:** 2026-07-23
- **Scope:** Product definition, not implementation architecture

## Product promise

> kmpRSS turns the feeds a person deliberately chose into the calmest, most
> comfortable reading experience on their phone — offline, accessible, private,
> and unmistakably at home on Android and iOS.

An award-worthy RSS reader is not distinguished by the longest feature list. Its
quality is felt in three repeated moments: adding a source without friction,
deciding what deserves attention, and reading without fighting the interface.

## Product principles

1. **Reading is the product.** Typography, layout, images, code, links, selection,
   orientation changes, and restoration of reading position are core behaviour.
2. **Calm over engagement.** No infinite-growth mechanics, ads, recommendations,
   guilt, or streak punishment. The user controls the sources and pace.
3. **Native in behaviour, shared in capability.** Android follows Material 3
   Expressive; iOS follows contemporary Apple interaction and visual language.
   Domain rules can be shared while navigation and components may differ.
4. **Local-first and offline-capable.** Previously downloaded entries, images
   where practical, reading state, and organisation remain useful without a
   network connection.
5. **Accessible by design.** Dynamic text, screen readers, keyboard/switch access,
   contrast, reduced motion, touch targets, and meaningful semantics are release
   criteria, not polish tasks.
6. **Honest system behaviour.** Background work is best effort. The product never
   promises an exact refresh or notification time that an OS cannot guarantee.
7. **User-owned data.** Subscriptions can be imported and exported without an
   account. Destructive actions are clear and recoverable where reasonable.
8. **Earn every onboarding step.** A newcomer reaches real app value quickly.
   There is no mandatory marketing carousel, no upfront tour of future features,
   and no optional preference or permission gate before the app can be used.
9. **Localizable by construction.** Before release, every user-visible and
   assistive string owned by kmpRSS comes from a localizable resource. Copy is
   never trapped in a composable, navigation key, notification builder, or native
   host. The set of languages shipped is a separate product decision.

## The three core loops

### 1. Subscribe

Paste or share a website/feed URL → discover candidate feeds → preview identity
and recent entries → choose zero or more tags and a notification preference →
subscribe. The primary wording is “follow a website”; RSS terminology is secondary.

### 2. Triage

Open Inbox/tag → scan a stable, readable list → open, save, mark, or dismiss
with low-cost gestures and accessible equivalents → switch tag without losing
position or context.

### 3. Read

Open an entry → render clean feed content immediately → preserve author intent
while enforcing safe, readable typography → optionally open the original page →
restore position on return → update read state and optional reading-day progress.

## Prioritised scope

### P0 — indispensable launch experience

| ID | Capability | Acceptance intent |
|---|---|---|
| PRD-001 | Feed discovery and subscription | A newcomer can follow a normal website URL without understanding RSS. Direct feed URLs, shared URLs, and OPML each have a clear path and actionable errors. Multiple discovered feeds are presented rather than guessed. |
| PRD-002 | Robust feed ingestion | Supported RSS and Atom variants are normalised, sanitised, deduplicated, and tolerant of common malformed data. One broken feed cannot block others. Exact format matrix remains an engineering decision. |
| PRD-003 | Excellent reader | Feed-provided content supports headings, paragraphs, lists, quotes, links, images, code, and accessible semantics. Users can adjust text size and at least one density/spacing dimension. Reading position survives normal navigation. |
| PRD-004 | Original page | Every entry with a valid canonical URL can open in the platform-appropriate browser experience and return without losing reader state. |
| PRD-005 | Inbox, tag, and feed navigation | Users can move between all unread, tags, and individual feeds quickly. A subscription may appear under multiple tags. The UI preserves scroll/filter context where expected on each OS. |
| PRD-006 | Independent read and saved states | Mark read/unread and save/unsave from list and reader. An entry is automatically read when its reading progress first reaches 80%. Bulk “mark all read” is scoped, confirmable, and undoable. Saving never implicitly changes read state. |
| PRD-007 | Subscription management | Rename local display title, add/remove tags, inspect feed URL/health, change notification rule, refresh, and unsubscribe. Unsubscribe clearly explains local-data consequences and offers a short undo window. |
| PRD-008 | Offline-first use | Downloaded text and state changes work offline. Pending changes reconcile locally without duplication. The UI distinguishes cached content from actions requiring a network. |
| PRD-009 | Refresh | Refresh occurs on launch when stale, manually on request, and in the background on an OS-controlled best-effort basis. Failures use backoff and expose a useful last-success/error state without noisy alerts. |
| PRD-010 | OPML portability | Import maps folders to tags where representable. Export produces a valid subscription file and preserves multi-tag membership as far as OPML allows. Import is idempotent and reports duplicates/errors. |
| PRD-011 | Accessibility and native platform UX | Critical journeys pass screen-reader, large-text, contrast, reduced-motion, orientation, and platform navigation checks. Android and iOS may use different components and structures. |
| PRD-012 | Privacy and safety | Remote HTML is sanitised; dangerous schemes/content are blocked; tracking exposure is minimised; no account or analytics is required for core reading. Data can be deleted and exported. |
| PRD-013 | Short, motivating onboarding | Onboarding explains the immediate value, then moves directly into following a first website or using the app. Every mandatory step is necessary for that next action; optional preferences, education, and permissions are skippable or requested later in context. |
| PRD-014 | Localizable release surface | Before release, every kmpRSS-owned string visible or announced to a user is loaded from a localizable resource. This includes visible copy, accessibility labels/hints, errors, empty states, formatted quantities, notifications, shortcuts, widgets, and platform-host UI. Hard-coded production UI strings block release. Remote feed content, URLs, internal identifiers, logs, and test fixtures are not app copy. |

### P1 — important differentiation after the core is excellent

| ID | Capability | Product position |
|---|---|---|
| PRD-101 | Share extension | Share a website/feed to subscribe; optionally share an article to save it. Tag selection belongs in a lightweight confirmation flow, not a full app replica. |
| PRD-102 | Notification rules | Opt in per feed (later per tag), choose immediate-best-effort or digest, and set quiet hours. Permission is requested in context only. v1 uses only locally discovered entries; a timely server-backed service is deferred to v2 or later. |
| PRD-103 | Reading rhythm | Optional reading-day heatmap/streak with a gentle intensity scale. A day qualifies when one entry reaches 80% reading progress. Missing a day never removes content, nags, shames, or blocks a feature. It can be disabled and is accessible without colour alone. |
| PRD-104 | Local search and filters | Search cached titles/authors/content; filter unread, saved, date, feed, and tag. Results communicate the limits of offline indexing. |
| PRD-105 | Full-content extraction | For truncated feeds, optionally derive a clean article from the original page with clear fallback and attribution. This is separate from baseline feed-content rendering. |
| PRD-106 | Thoughtful media handling | Image sizing/captions/alt text, data-saving policy, placeholders, and zoom behave well; media never destroys reading flow. Audio/video can open externally until deliberately supported. |

### P2 — valuable, but not required to prove the product

- Account-based cross-device sync for subscriptions, state, preferences, and
  reading position.
- Widgets, shortcuts, and platform surfaces after the primary loops are stable.
- Rule-based smart tags.
- Feed catalogue/editorial discovery, only if it stays separate from the calm,
  user-chosen inbox.
- Highlights, notes, annotations, and export to knowledge tools.
- Self-hosted sync or compatibility with established RSS services.
- Server-backed near-real-time notifications; reconsider for v2 or later.

## Explicit non-goals for the first release

- A social network, comments, follower counts, or public reading rankings.
- Algorithmic engagement feed, ads, sponsored recommendations, or unread-count
  pressure as a success metric.
- Guaranteed exact polling intervals or instant local-only notifications.
- A desktop/web client, podcast client, or complete web browser.
- Pixel-identical Android and iOS UI.
- AI summarisation before the trust, privacy, cost, and quality model is explicit.

## Quality bar and product measures

These are targets to validate and refine, not yet release commitments.

| Dimension | Proposed measure |
|---|---|
| Add | A valid direct feed can be subscribed to in at most three decisions after pasting/sharing; errors always offer a next action. |
| Read | Cached entry text becomes interactive without a network dependency; no visible layout jump for normal text-only content. |
| Progress | Read state and reading-day credit use one deterministic 80% content-coverage rule; no time tracking is required. |
| Resume | Returning from the original page or app interruption restores the meaningful list/reader position. |
| Accessibility | The subscribe, triage, read, organise, and unsubscribe journeys complete with TalkBack and VoiceOver and at the largest supported text setting. |
| Reliability | One malformed feed or entry is isolated and cannot abort the full refresh. Imports and repeated refreshes do not duplicate subscriptions/entries. |
| Calm | Notifications default off; reading rhythm is optional; no feature relies only on colour; bulk actions are reversible. |
| Ownership | OPML export and local-data deletion require no account or paid tier. |
| Onboarding | A newcomer can reach the first meaningful app action without completing a marketing carousel, feature tour, permission sequence, or optional setup. |
| Localization | A release audit finds no kmpRSS-owned user-facing or assistive string literal in production UI code; base resources are complete and every declared locale passes key, placeholder, plural, long-text, and RTL checks where applicable. |

## Refresh and notification constraint

Android and iOS both defer recurring background work according to system and
device conditions. Therefore “every 6 hours” may be a scheduling preference, not
a promise. The honest initial model is:

- always refresh explicitly when the user requests it;
- refresh on foreground entry when data is stale;
- request energy-aware background refresh and display its last successful time;
- provide notifications only for entries actually discovered by a completed
  refresh;
- require a server-side feed watcher plus push delivery if the future product
  promises timely notifications while the app is dormant.

References: [Apple background strategies](https://developer.apple.com/documentation/BackgroundTasks/choosing-background-strategies-for-your-app), [Android WorkManager periodic work](https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started/define-work).

## Reading progress rule

`readingProgress` is the greatest fraction of the laid-out article content that
the viewport has reached. It is based on content coverage, not dwell time:

```text
readingProgress = clamp(furthestViewportEnd / articleContentHeight, 0, 1)
```

When progress reaches `0.8` for the first time, the entry becomes read and the
current local date earns reading-day credit. If at least 80% of a short entry is
already visible on open, it qualifies immediately. This is an accepted trade-off,
not an edge case to “fix” with a hidden timer. Users can still explicitly change
the read state. Font size and orientation changes must not reduce stored maximum
progress.

## Accepted product decisions

| ID | Decision | Consequence |
|---|---|---|
| Q-1 | Newcomers are the primary v1 audience. | Lead with “follow a website” and hide protocol vocabulary from the primary journey; retain OPML for experts. |
| Q-2 | v1 is local-only and has no account. | No cloud identity or cross-device sync assumptions. OPML is the migration path but does not carry read/saved state. |
| Q-3 | A subscription can have multiple tags. | Model a many-to-many relationship. “Untagged” is a computed view, not a real tag. |
| Q-4 | Saved is core v1 behaviour. | Saved and read are independent domain states and both are available from list and reader. |
| Q-5 | An entry is automatically read at 80% reading progress. | Use content coverage; do not infer read state from opening or elapsed time alone. |
| Q-6 | The same 80% event earns reading-day credit. | Short entries may qualify immediately. No additional dwell-time or length rule. |
| Q-7 | Full-content extraction is not part of v1. | Render feed-provided content well and provide “open original”; reconsider extraction later. |
| Q-8 | Server-backed timely notifications are deferred to v2 or later. | Initial notifications can only be best effort for entries found by local refresh. Do not introduce a backend in v1. |
| Q-9 | Tags are flat and cannot be nested. | Keep navigation and selection simple. A subscription can still have many tags; the domain has no parent tag or hierarchy path. |

Source of record: [ADR-0001](../adr/0001-v1-product-foundation.md).

Localization and navigation implementation constraints are recorded in
[ADR-0002](../adr/0002-localization-and-navigation.md).
