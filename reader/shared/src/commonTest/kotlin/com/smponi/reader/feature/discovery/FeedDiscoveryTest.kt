package com.smponi.reader.feature.discovery

import com.smponi.reader.feature.onboarding.OnboardingOutcome
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class FeedDiscoveryTest {
    @Test
    fun `normal website begins feed discovery immediately`() {
        val discovery = beginFeedDiscovery(
            OnboardingOutcome.FollowWebsite("example.com"),
        )

        assertEquals(
            FeedDiscoveryState.Discovering("https://example.com"),
            discovery.state,
        )
        discovery.close()
    }

    @Test
    fun `discovered feeds become an actionable result`() = runTest {
        val discovery = beginFeedDiscovery(
            outcome = OnboardingOutcome.FollowWebsite("example.com"),
            source = FeedDiscoverySource {
                FeedDiscoveryResult.Found(
                    listOf(FeedCandidate(title = "Example feed", url = "https://example.com/feed.xml")),
                )
            },
        )

        discovery.discover()

        assertEquals(
            FeedDiscoveryState.Found(
                website = "https://example.com",
                candidates = listOf(
                    FeedCandidate(title = "Example feed", url = "https://example.com/feed.xml"),
                ),
            ),
            discovery.state,
        )
    }

    @Test
    fun `normal website exposes its advertised feed`() = runTest {
        val client = HttpClient(
            MockEngine {
                respond(
                    content = """
                        <html>
                          <head>
                            <link rel="alternate" type="application/rss+xml"
                                  title="Example updates" href="/feed.xml">
                          </head>
                        </html>
                    """.trimIndent(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "text/html; charset=UTF-8"),
                )
            },
        )
        val discovery = beginFeedDiscovery(
            outcome = OnboardingOutcome.FollowWebsite("example.com"),
            source = KtorFeedDiscoverySource(client),
        )

        discovery.discover()

        assertEquals(
            FeedDiscoveryState.Found(
                website = "https://example.com",
                candidates = listOf(
                    FeedCandidate(
                        title = "Example updates",
                        url = "https://example.com/feed.xml",
                    ),
                ),
            ),
            discovery.state,
        )
        discovery.close()
    }

    @Test
    fun `website failure offers an honest retry state`() = runTest {
        val client = HttpClient(
            MockEngine {
                respond(
                    content = "Temporarily unavailable",
                    status = HttpStatusCode.ServiceUnavailable,
                )
            },
        )
        val discovery = beginFeedDiscovery(
            outcome = OnboardingOutcome.FollowWebsite("example.com"),
            source = KtorFeedDiscoverySource(client),
        )

        discovery.discover()

        assertEquals(
            FeedDiscoveryState.Failed("https://example.com"),
            discovery.state,
        )
        discovery.close()
    }

    @Test
    fun `website without advertised feeds offers a useful empty result`() = runTest {
        val client = HttpClient(
            MockEngine {
                respond(
                    content = "<html><head><title>No feed here</title></head></html>",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "text/html"),
                )
            },
        )
        val discovery = beginFeedDiscovery(
            outcome = OnboardingOutcome.FollowWebsite("example.com"),
            source = KtorFeedDiscoverySource(client),
        )

        discovery.discover()

        assertEquals(
            FeedDiscoveryState.NoFeeds("https://example.com"),
            discovery.state,
        )
        discovery.close()
    }
}
