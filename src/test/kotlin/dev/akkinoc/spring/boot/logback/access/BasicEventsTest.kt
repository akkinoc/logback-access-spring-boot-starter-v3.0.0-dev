package dev.akkinoc.spring.boot.logback.access

import ch.qos.logback.access.spi.IAccessEvent.NA
import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEvents
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCapture
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCaptureExtension
import dev.akkinoc.spring.boot.logback.access.test.type.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowServletWebTest
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.longs.shouldBeBetween
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import java.lang.System.currentTimeMillis
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * Tests the appended Logback-access events in the case where the configuration is the default.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(properties = ["logback.access.config=classpath:logback-access.capture.xml"])
sealed class BasicEventsTest {

    @Test
    fun `Appends a Logback-access event`(
            @Autowired rest: TestRestTemplate,
            @LocalServerPort port: Int,
            capture: EventsCapture,
    ) {
        val started = currentTimeMillis()
        val response = rest.getForEntity<String>("/mock-controller/text")
        response.statusCode.shouldBe(HttpStatus.OK)
        response.body.shouldBe("MOCK-TEXT")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        val finished = currentTimeMillis()
        event.request.shouldBeNull()
        event.response.shouldBeNull()
        event.serverAdapter.shouldBeNull()
        event.timeStamp.shouldBeBetween(started, finished)
        event.elapsedTime.shouldBeBetween(0L, finished - started)
        event.elapsedSeconds.shouldBeBetween(0L, MILLISECONDS.toSeconds(finished - started))
        event.threadName.shouldNotBeEmpty()
        shouldThrowUnit<UnsupportedOperationException> { event.threadName = "CHANGED" }
        event.serverName.shouldBe("localhost")
        event.localPort.shouldBe(port)
        event.remoteAddr.shouldBe("127.0.0.1")
        event.remoteHost.shouldBe("127.0.0.1")
        event.remoteUser.shouldBe(NA)
        event.protocol.shouldBe("HTTP/1.1")
        event.method.shouldBe("GET")
        event.requestURI.shouldBe("/mock-controller/text")
        event.queryString.shouldBeEmpty()
        event.requestURL.shouldBe("GET /mock-controller/text HTTP/1.1")
        event.statusCode.shouldBe(HttpStatus.OK.value())
        event.contentLength.shouldBe(9L)
    }

}

/**
 * Tests the [BasicEventsTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebBasicEventsTest : BasicEventsTest()

/**
 * Tests the [BasicEventsTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebBasicEventsTest : BasicEventsTest()

/**
 * Tests the [BasicEventsTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebBasicEventsTest : BasicEventsTest()

/**
 * Tests the [BasicEventsTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebBasicEventsTest : BasicEventsTest()

/**
 * Tests the [BasicEventsTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebBasicEventsTest : BasicEventsTest()

/**
 * Tests the [BasicEventsTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebBasicEventsTest : BasicEventsTest()
