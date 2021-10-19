package dev.akkinoc.spring.boot.logback.access

import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where forward headers are unsupported.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(
        properties = [
            "server.forward-headers-strategy=none",
            "logback.access.config=classpath:logback-access-test.capture.xml",
        ],
)
sealed class ForwardHeadersNonSupportTest {

    @Test
    fun `Appends a Logback-access event`(
            @Autowired rest: TestRestTemplate,
            @LocalServerPort port: Int,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text")
                .header("x-forwarded-host", "forwarded-host")
                .header("x-forwarded-port", "12345")
                .header("x-forwarded-for", "1.2.3.4")
                .header("x-forwarded-proto", "https")
                .build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        event.serverName.shouldBe("localhost")
        event.localPort.shouldBe(port)
        event.remoteAddr.shouldBe("127.0.0.1")
        event.remoteHost.shouldBe("127.0.0.1")
        event.protocol.shouldBe("HTTP/1.1")
    }

}

/**
 * Tests the [ForwardHeadersNonSupportTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebForwardHeadersNonSupportTest : ForwardHeadersNonSupportTest()

/**
 * Tests the [ForwardHeadersNonSupportTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebForwardHeadersNonSupportTest : ForwardHeadersNonSupportTest()

/**
 * Tests the [ForwardHeadersNonSupportTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebForwardHeadersNonSupportTest : ForwardHeadersNonSupportTest()

/**
 * Tests the [ForwardHeadersNonSupportTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebForwardHeadersNonSupportTest : ForwardHeadersNonSupportTest()

/**
 * Tests the [ForwardHeadersNonSupportTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebForwardHeadersNonSupportTest : ForwardHeadersNonSupportTest()

/**
 * Tests the [ForwardHeadersNonSupportTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebForwardHeadersNonSupportTest : ForwardHeadersNonSupportTest()
