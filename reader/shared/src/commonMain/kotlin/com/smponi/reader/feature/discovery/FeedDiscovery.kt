package com.smponi.reader.feature.discovery

import com.smponi.reader.feature.onboarding.OnboardingOutcome
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException

/** Observable feed-discovery progress for one website handed off by onboarding. */
sealed interface FeedDiscoveryState {
    val website: String

    /** The website is ready for its feed links to be discovered. */
    data class Discovering(override val website: String) : FeedDiscoveryState

    /** Feed links declared by the website are ready for the next subscription slice. */
    data class Found(override val website: String, val candidates: List<FeedCandidate>) : FeedDiscoveryState

    /** The website loaded successfully but did not advertise a supported feed. */
    data class NoFeeds(override val website: String) : FeedDiscoveryState

    /** Discovery could not complete; callers can retry without losing the website. */
    data class Failed(override val website: String) : FeedDiscoveryState
}

/** One feed advertised by the requested website. */
data class FeedCandidate(val title: String, val url: String)

internal sealed interface FeedDiscoveryResult {
    data class Found(val candidates: List<FeedCandidate>) : FeedDiscoveryResult
}

internal fun interface FeedDiscoverySource {
    suspend fun discover(website: String): FeedDiscoveryResult

    fun close() = Unit
}

/**
 * Public feature session shared by callers and tests.
 *
 * Network discovery and platform rendering remain implementation details behind this state interface.
 */
class FeedDiscovery internal constructor(initialState: FeedDiscoveryState, private val source: FeedDiscoverySource) {
    var state: FeedDiscoveryState = initialState
        internal set

    /** Resolves the currently advertised website and publishes the observable result. */
    suspend fun discover() {
        val website = state.website
        state = FeedDiscoveryState.Discovering(website)
        state = try {
            when (val result = source.discover(website)) {
                is FeedDiscoveryResult.Found ->
                    if (result.candidates.isEmpty()) {
                        FeedDiscoveryState.NoFeeds(website)
                    } else {
                        FeedDiscoveryState.Found(
                            website = website,
                            candidates = result.candidates,
                        )
                    }
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (_: Exception) {
            FeedDiscoveryState.Failed(website)
        }
    }

    /** Releases the platform HTTP client owned by this discovery session. */
    fun close() = source.close()
}

/** Starts discovery immediately from the meaningful onboarding handoff. */
fun beginFeedDiscovery(outcome: OnboardingOutcome.FollowWebsite): FeedDiscovery {
    val client = HttpClient()
    return beginFeedDiscovery(
        outcome = outcome,
        source = KtorFeedDiscoverySource(client),
    )
}

internal fun beginFeedDiscovery(outcome: OnboardingOutcome.FollowWebsite, source: FeedDiscoverySource): FeedDiscovery {
    val website = outcome.website.withWebScheme()
    return FeedDiscovery(
        initialState = FeedDiscoveryState.Discovering(website),
        source = source,
    )
}

private fun String.withWebScheme(): String = when {
    startsWith("https://", ignoreCase = true) -> this
    startsWith("http://", ignoreCase = true) -> this
    else -> "https://$this"
}

/**
 * Small Ktor adapter that owns the real HTTP exchange and HTML feed-link interpretation.
 *
 * Ktor supplies redirect, TLS, cancellation and platform-engine behaviour. Feed parsing and subscription remain out of
 * scope.
 */
internal class KtorFeedDiscoverySource(private val client: HttpClient) : FeedDiscoverySource {
    override suspend fun discover(website: String): FeedDiscoveryResult {
        val response = client.get(website)
        check(response.status.isSuccess()) {
            "Website request failed with HTTP ${response.status.value}"
        }

        val resolvedWebsite = response.call.request.url.toString()
        val contentType = response.headers[HttpHeaders.ContentType].orEmpty().lowercase()
        val candidates = if (contentType.isFeedContentType()) {
            listOf(FeedCandidate(title = resolvedWebsite, url = resolvedWebsite))
        } else {
            advertisedFeeds(response.bodyAsText(), resolvedWebsite)
        }
        return FeedDiscoveryResult.Found(candidates)
    }

    override fun close() = client.close()
}

private fun String.isFeedContentType(): Boolean =
    startsWith("application/rss+xml") || startsWith("application/atom+xml")

private fun advertisedFeeds(document: String, website: String): List<FeedCandidate> = LinkTag.findAll(document)
    .mapNotNull { tag ->
        val attributes = Attribute.findAll(tag.value)
            .associate { match ->
                val value = match.groupValues.drop(2).firstOrNull(String::isNotEmpty).orEmpty()
                match.groupValues[1].lowercase() to value
            }
        val relation = attributes["rel"].orEmpty().lowercase().split(Whitespace)
        val contentType = attributes["type"].orEmpty().lowercase().substringBefore(';').trim()
        val href = attributes["href"].orEmpty()
        if ("alternate" !in relation || !contentType.isFeedContentType() || href.isBlank()) {
            null
        } else {
            val resolvedUrl = resolveWebUrl(website, href)
            FeedCandidate(
                title = attributes["title"].orEmpty().ifBlank { resolvedUrl },
                url = resolvedUrl,
            )
        }
    }
    .distinctBy(FeedCandidate::url)
    .toList()

private fun resolveWebUrl(base: String, reference: String): String = when {
    reference.startsWith("https://", ignoreCase = true) ||
        reference.startsWith("http://", ignoreCase = true) -> reference

    reference.startsWith("//") -> "${base.substringBefore(':')}:$reference"
    reference.startsWith("/") -> "${base.webOrigin()}$reference"
    else -> "${base.webDirectory()}$reference"
}

private fun String.webOrigin(): String {
    val authorityStart = indexOf("://").takeIf { it >= 0 }?.plus(3) ?: return this
    val pathStart = indexOf('/', startIndex = authorityStart)
    return if (pathStart < 0) this else substring(0, pathStart)
}

private fun String.webDirectory(): String {
    val cleanBase = substringBefore('#').substringBefore('?')
    val origin = cleanBase.webOrigin()
    if (cleanBase.length == origin.length) return "$origin/"
    return cleanBase.substringBeforeLast('/') + "/"
}

private val LinkTag = Regex("""<link\b[^>]*>""", setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL))
private val Attribute = Regex("""([:\w-]+)\s*=\s*(?:"([^"]*)"|'([^']*)'|([^\s"'=<>]+))""")
private val Whitespace = Regex("""\s+""")
