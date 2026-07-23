# ADR-0002: localizable presentation and Navigation 3

- **Status:** Accepted
- **Decision date:** 2026-07-23
- **Decider:** Product owner
- **Supersedes:** None
- **Related:** `PRD-005`, `PRD-011`, `PRD-014`

## Context

kmpRSS will be developed largely by independent AI agents. Two foundation rules
must therefore be explicit before feature UI expands:

1. Copy embedded in Kotlin or Swift is expensive to discover later and is easily
   omitted from localization, accessibility, and platform review.
2. Ad-hoc string routes or platform-specific route graphs would create
   untestable navigation state, collisions, and divergent Android/iOS behaviour.

Navigation 3 is designed around app-owned back-stack state whose entries are
keys. Its runtime supports Kotlin Multiplatform targets, while its UI layer must
not be assumed to provide the iOS experience. This matches kmpRSS's rule to share
stable meaning while rendering platform-appropriate interaction.

## Decision

### 1. All app-owned presentation strings are localizable

Before any release, every kmpRSS-owned string that a person can see or an
assistive technology can announce must come from a localizable resource. The
scope includes:

- visible labels, headings, body copy, buttons, menus and placeholders;
- accessibility names, descriptions, hints, states and custom actions;
- errors, validation, loading/empty states and recovery instructions;
- formatted values, plurals and quantity-dependent copy;
- notifications, shortcuts, widgets and platform-host chrome.

Shared Compose UI uses Compose Multiplatform resources in
`commonMain/composeResources/values/strings.xml`, generated `Res.string`
accessors, and `stringResource`/`getString`. Locale variants use qualified
resource directories. Genuinely platform-only copy uses Android or Apple
localizable resources.

Remote feed content, URLs, internal identifiers, analytics-free diagnostic logs,
and test fixtures are data rather than app copy and are outside this rule. The
number of languages shipped is a separate product decision; externalizing every
app-owned string is mandatory even if the first release has one locale.

### 2. Navigation 3 is the authoritative navigation system

Navigation state is modeled with Navigation 3 on Android and iOS. Shared runtime
state is authoritative; a platform renderer must not maintain a parallel route
graph.

All navigation keys:

- implement a sealed app contract, `AppNavKey : NavKey`;
- are concrete `@Serializable` data objects or data classes;
- have intentional equality that uniquely identifies one logical entry;
- contain only stable, serializable identity needed to restore the destination;
- never contain localized/display strings, mutable domain objects, callbacks,
  service instances, composables, or platform objects.

A singleton destination is a `data object`. An entity destination carries a
stable identifier such as `feedId` or `entryId`. If two simultaneous instances
for the same entity must retain independent entry state, the key also carries a
stable `instanceId`. Random instance IDs are not added when entity identity is
sufficient.

Raw string routes and `Any`-typed back stacks are prohibited. Features emit
typed navigation callbacks/events. A central, independently testable navigator
owns stack mutation, and an exhaustive entry provider resolves every key.

Android renders the stack with Navigation 3 `NavDisplay` and Android back/scene
integration. iOS renders the same Navigation 3 runtime stack through an
Apple-appropriate display adapter because the AndroidX Navigation 3 UI artifact
must not be assumed to be KMP UI. Non-Android restoration uses the explicit
serialization configuration required by the selected stable Navigation 3
version.

## Consequences

### Positive

- Copy can be translated, reviewed, pseudo-localized, length-tested, and audited
  without searching implementation code.
- Accessibility copy follows the same localization discipline as visible text.
- Navigation state is serializable, deterministic, testable, and shared without
  forcing the same platform chrome.
- Type and field identity prevent collisions hidden by string route conventions.
- Feature UI remains testable because it emits events instead of owning a router.

### Costs and accepted constraints

- Every feature slice must add resource entries before its UI is considered done.
- Resource keys and navigation key types become compatibility surfaces that
  require deliberate naming and evolution.
- Multi-platform navigation needs an iOS display adapter instead of assuming the
  Android Navigation 3 UI artifact works everywhere.
- Release preparation includes explicit localization and navigation audits in
  addition to compilation and lint.

## Verification

- Resource audit: no app-owned user-facing or assistive literals remain in
  production UI, notification, shortcut, widget, or host code.
- Resource completeness: base and declared locale files have compatible keys,
  placeholders, and plural definitions.
- UI review: long/pseudo-localized text, largest font, and applicable RTL layout
  do not clip or hide actions.
- Navigation unit tests: every key round-trips through serialization; equality
  matches documented identity; independent entries have distinct keys.
- Navigation integration tests: stack restoration and every key-to-entry mapping
  work on Android and iOS adapters; unknown keys fail during development.

## References

- [Navigation 3 overview](https://developer.android.com/guide/navigation/navigation-3)
- [Save and manage Navigation 3 state](https://developer.android.com/guide/navigation/navigation-3/save-state)
- [Navigation 3 release notes and KMP support](https://developer.android.com/jetpack/androidx/releases/navigation3)
- [Compose Multiplatform resources](https://kotlinlang.org/docs/multiplatform/compose-multiplatform-resources-usage.html)
- [Localizing Compose Multiplatform strings](https://kotlinlang.org/docs/multiplatform/compose-localize-strings.html)
