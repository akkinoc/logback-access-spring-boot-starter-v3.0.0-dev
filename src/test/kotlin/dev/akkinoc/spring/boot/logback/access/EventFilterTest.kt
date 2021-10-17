package dev.akkinoc.spring.boot.logback.access

import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEventsContinually
import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCapture
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCaptureExtension
import dev.akkinoc.spring.boot.logback.access.test.type.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowServletWebTest
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where the event filter is used in the configuration file.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(properties = ["logback.access.config=classpath:logback-access-test.filter.capture.xml"])
sealed class EventFilterTest {

    @Test
    fun `Appends a Logback-access event because it was accepted by the filter`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text")
                .header("mock-event-filter-reply", "accept")
                .build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        assertLogbackAccessEventsEventually { capture.shouldBeSingleton() }
    }

    @Test
    fun `Appends a Logback-access event because it was passed by the filter`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text")
                .header("mock-event-filter-reply", "neutral")
                .build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        assertLogbackAccessEventsEventually { capture.shouldBeSingleton() }
    }

    @Test
    fun `Does not append a Logback-access event because it was denied by the filter`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text")
                .header("mock-event-filter-reply", "deny")
                .build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        assertLogbackAccessEventsContinually { capture.shouldBeEmpty() }
    }

}

/**
 * Tests the [EventFilterTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebEventFilterTest : EventFilterTest()

/**
 * Tests the [EventFilterTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebEventFilterTest : EventFilterTest()

/**
 * Tests the [EventFilterTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebEventFilterTest : EventFilterTest()

/**
 * Tests the [EventFilterTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebEventFilterTest : EventFilterTest()

/**
 * Tests the [EventFilterTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebEventFilterTest : EventFilterTest()

/**
 * Tests the [EventFilterTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebEventFilterTest : EventFilterTest()
