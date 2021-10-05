package dev.akkinoc.spring.boot.logback.access

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
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.longs.shouldBeBetween
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus.OK
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
        response.statusCode.shouldBe(OK)
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
        event.remoteUser.shouldBe("-")
        event.protocol.shouldBe("HTTP/1.1")
        event.method.shouldBe("GET")
        event.requestURI.shouldBe("/mock-controller/text")
        event.queryString.shouldBeEmpty()
        event.requestURL.shouldBe("GET /mock-controller/text HTTP/1.1")
        event.requestHeaderMap.shouldNotBeNull()
        event.requestHeaderNames.shouldNotBeNull()
        event.getRequestHeader("MOCK-UNKNOWN-REQUEST-HEADER").shouldBe("-")
        event.getCookie("MOCK-UNKNOWN-COOKIE").shouldBe("-")
        event.requestParameterMap.shouldNotBeNull()
        event.getRequestParameter("MOCK-UNKNOWN-REQUEST-PARAMETER").shouldContainExactly("-")
        event.getAttribute("MOCK-UNKNOWN-ATTRIBUTE").shouldBe("-")
        event.sessionID.shouldBe("-")
        event.requestContent.shouldBeEmpty()
        event.statusCode.shouldBe(200)
        event.responseHeaderMap.shouldNotBeNull()
        event.responseHeaderNameList.shouldNotBeNull()
        event.getResponseHeader("MOCK-UNKNOWN-RESPONSE-HEADER").shouldBe("-")
        event.contentLength.shouldBe(9L)
        event.responseContent.shouldBeEmpty()
    }

    @Test
    fun `Appends a Logback-access event with response headers`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val response = rest.getForEntity<String>("/mock-controller/text-with-response-headers")
        response.statusCode.shouldBe(OK)
        response.body.shouldBe("MOCK-TEXT-WITH-RESPONSE-HEADERS")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.responseHeaderMap.shouldContain("MOCK-RESPONSE-HEADER1", "MOCK-RESPONSE-HEADER1-VALUE")
        event.responseHeaderMap.shouldContain("MOCK-RESPONSE-HEADER2", "MOCK-RESPONSE-HEADER2-VALUE1")
        event.responseHeaderNameList.shouldContainAll("MOCK-RESPONSE-HEADER1", "MOCK-RESPONSE-HEADER2")
        event.getResponseHeader("MOCK-RESPONSE-HEADER1").shouldBe("MOCK-RESPONSE-HEADER1-VALUE")
        event.getResponseHeader("mock-response-header1").shouldBe("MOCK-RESPONSE-HEADER1-VALUE")
        event.getResponseHeader("MOCK-RESPONSE-HEADER2").shouldBe("MOCK-RESPONSE-HEADER2-VALUE1")
        event.getResponseHeader("mock-response-header2").shouldBe("MOCK-RESPONSE-HEADER2-VALUE1")
        event.getResponseHeader("MOCK-UNKNOWN-RESPONSE-HEADER").shouldBe("-")
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
