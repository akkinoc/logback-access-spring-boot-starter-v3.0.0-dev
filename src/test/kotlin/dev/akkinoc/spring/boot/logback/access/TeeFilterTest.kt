package dev.akkinoc.spring.boot.logback.access

import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCapture
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCaptureExtension
import dev.akkinoc.spring.boot.logback.access.test.type.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowServletWebTest
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where the tee filter is enabled.
 *
 * @property supportsRequestContents Whether to support request contents.
 * @property supportsResponseContents Whether to support response contents.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(
        properties = [
            "logback.access.config=classpath:logback-access.capture.xml",
            "logback.access.tee-filter.enabled=true",
        ]
)
sealed class TeeFilterTest(
        private val supportsRequestContents: Boolean,
        private val supportsResponseContents: Boolean,
) {

    @Test
    fun `Appends a Logback-access event`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = Assertions.assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.requestContent.shouldBeEmpty()
        if (supportsResponseContents) event.responseContent.shouldBe("mock-text")
        else event.responseContent.shouldBeEmpty()
    }

    @Test
    fun `Appends a Logback-access event with a request content`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.post("/mock-controller/text")
                .header("content-type", "text/plain")
                .body("posted-text")
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = Assertions.assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        if (supportsRequestContents) event.requestContent.shouldBe("posted-text")
        else event.requestContent.shouldBeEmpty()
        if (supportsResponseContents) event.responseContent.shouldBe("mock-text")
        else event.responseContent.shouldBeEmpty()
    }

    @Test
    fun `Appends a Logback-access event with a request content in form data`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.post("/mock-controller/form-data")
                .header("content-type", "application/x-www-form-urlencoded")
                .body("a=value+%40a&b=value1+%40b&b=value2+%40b&c=")
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = Assertions.assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        if (supportsRequestContents) event.requestContent.shouldBe("a=value+%40a&b=value1+%40b&b=value2+%40b&c=")
        else event.requestContent.shouldBeEmpty()
        if (supportsResponseContents) event.responseContent.shouldBe("mock-text")
        else event.responseContent.shouldBeEmpty()
    }

}

/**
 * Tests the [TeeFilterTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTeeFilterTest : TeeFilterTest(
        supportsRequestContents = true,
        supportsResponseContents = true,
)

/**
 * Tests the [TeeFilterTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebTeeFilterTest : TeeFilterTest(
        supportsRequestContents = false,
        supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTeeFilterTest : TeeFilterTest(
        supportsRequestContents = true,
        supportsResponseContents = true,
)

/**
 * Tests the [TeeFilterTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebTeeFilterTest : TeeFilterTest(
        supportsRequestContents = false,
        supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebTeeFilterTest : TeeFilterTest(
        supportsRequestContents = true,
        supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebTeeFilterTest : TeeFilterTest(
        supportsRequestContents = false,
        supportsResponseContents = false,
)
