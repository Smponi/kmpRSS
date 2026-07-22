# kmpRSS domain glossary

- **Status:** Accepted
- **Last updated:** 2026-07-22

Use these terms consistently in product copy, source code, tests, database names,
and documentation. UI labels may be localised or simplified.

| Term | Meaning | Important distinction |
|---|---|---|
| Feed | A remote RSS, Atom, or other explicitly supported syndication document. | A feed is remote data, not the user's relationship to it. |
| Subscription | The user's decision to follow one feed, including personal settings. | Deleting a subscription does not redefine the remote feed. |
| Entry | One item obtained from a feed and normalised into the kmpRSS model. | UI may call it an “article” even when it is audio, video, or a short post. |
| Article | Reader-facing label for an entry that has readable content. | Do not assume every entry contains complete article text. |
| Original page | The entry's canonical web destination, opened outside reader mode. | It may differ from the feed URL and may require authentication. |
| Tag | A user-managed, flat label used to organise subscriptions. | A subscription can have zero or many tags. Tags are not nested and are the domain primitive, not folders. |
| Untagged | A computed view containing subscriptions without any tag. | It is not a stored tag and must not be exported as one. |
| Inbox | A projection of entries the user has not yet read. | It is not a separate copy or ownership state. |
| Saved | A durable user marker for returning to an entry. | Saved and unread are independent states. |
| Read state | Whether the user considers an entry read or unread. | It changes explicitly or automatically when at least 80% of the article content has been seen. |
| Reading progress | The greatest proportion of laid-out article content reached by the viewport, from 0 to 1. | It is content coverage, not time spent and not analytics. Content already visible on open counts as seen. |
| Reading day | A local-calendar day on which at least one entry reached 80% reading progress. | Its boundary follows the device timezone. Short entries may qualify immediately. |
| Reading streak | Consecutive reading days. | It is optional encouragement, never a restriction or ranking. |
| Refresh | One attempt to fetch subscribed feeds and reconcile new/changed entries. | A requested interval is not a guaranteed execution time. |
| Manual refresh | A refresh explicitly initiated by the user. | It should give immediate, accessible feedback. |
| Background refresh | Best-effort refresh scheduled under OS battery and network policies. | It cannot promise an exact time on every platform. |
| Notification rule | Opt-in settings that determine which new entries may notify the user. | A rule does not guarantee immediate delivery without a server. |
| Feed discovery | Finding a valid feed from a pasted/shared website or feed URL. | Discovery may yield zero, one, or several candidates. |
| OPML | Portable import/export format for subscription collections. | OPML portability does not include read/saved state by default. |
| Reader preferences | User choices such as text size, spacing, width, theme, or typeface. | Preferences are accessibility/personalisation, not article mutations. |
| Full-content extraction | Deriving readable content from an original web page when the feed is truncated. | Distinct from rendering content already supplied by the feed. |

## Naming rules for implementation

- Prefer `Feed`, `Subscription`, and `Entry` in the domain layer.
- Reserve `Article` for reader presentation or article-specific capabilities.
- Use `Tag` for organisation in the domain. Do not introduce a separate
  `Category` or `Folder` entity unless a later accepted decision requires it.
- Keep tags flat. Do not add parent identifiers, paths, or hierarchy traversal.
- Do not call background refresh “sync”; sync means reconciliation across devices
  or authorities and is a separate future capability.
- Model `read` and `saved` independently; neither implies the other.
- Derive automatic read state and reading-day progress from the same 80% reading
  event. Do not add a dwell-time condition without a new product decision.
