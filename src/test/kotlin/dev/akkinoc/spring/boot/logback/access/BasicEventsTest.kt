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
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.longs.shouldBeBetween
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.maps.shouldBeEmpty
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
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.RequestEntity
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
        val url = "/mock-controller/text"
        val request = RequestEntity.get(url).build()
        val started = currentTimeMillis()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
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
        event.getRequestHeader("mock-unknown-request-header").shouldBe("-")
        event.getCookie("mock-unknown-cookie").shouldBe("-")
        event.requestParameterMap.shouldBeEmpty()
        event.getRequestParameter("mock-unknown-request-parameter").shouldContainExactly("-")
        event.getAttribute("mock-unknown-attribute").shouldBe("-")
        event.sessionID.shouldBe("-")
        event.requestContent.shouldBeEmpty()
        event.statusCode.shouldBe(200)
        event.responseHeaderMap.shouldNotBeNull()
        event.responseHeaderNameList.shouldNotBeNull()
        event.getResponseHeader("mock-unknown-response-header").shouldBe("-")
        event.contentLength.shouldBe(9L)
        event.responseContent.shouldBeEmpty()
    }

    @Test
    fun `Appends a Logback-access event with a query string`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val url = "/mock-controller/text?mock-query1&mock-query2"
        val request = RequestEntity.get(url).build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.requestURI.shouldBe("/mock-controller/text")
        event.queryString.shouldBe("?mock-query1&mock-query2")
        event.requestURL.shouldBe("GET /mock-controller/text?mock-query1&mock-query2 HTTP/1.1")
    }

    @Test
    fun `Appends a Logback-access event with request headers`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val url = "/mock-controller/text"
        val request = RequestEntity.get(url)
                .header("mock-request-header", "mock-request-header-value")
                .header("mock-empty-request-header", "")
                .header("mock-multi-request-header", "mock-multi-request-header-value1")
                .header("mock-multi-request-header", "mock-multi-request-header-value2")
                .build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.requestHeaderMap.also {
            it.shouldContain("mock-request-header" to "mock-request-header-value")
            it.shouldContain("mock-empty-request-header" to "")
            it.shouldContain("mock-multi-request-header" to "mock-multi-request-header-value1")
        }
        event.requestHeaderNames.toList().also {
            it.shouldContain("mock-request-header")
            it.shouldNotContain("Mock-Request-Header")
            it.shouldNotContain("MOCK-REQUEST-HEADER")
            it.shouldContain("mock-empty-request-header")
            it.shouldNotContain("Mock-Empty-Request-Header")
            it.shouldNotContain("MOCK-EMPTY-REQUEST-HEADER")
            it.shouldContain("mock-multi-request-header")
            it.shouldNotContain("Mock-Multi-Request-Header")
            it.shouldNotContain("MOCK-MULTI-REQUEST-HEADER")
        }
        event.getRequestHeader("mock-request-header").shouldBe("mock-request-header-value")
        event.getRequestHeader("Mock-Request-Header").shouldBe("mock-request-header-value")
        event.getRequestHeader("MOCK-REQUEST-HEADER").shouldBe("mock-request-header-value")
        event.getRequestHeader("mock-empty-request-header").shouldBeEmpty()
        event.getRequestHeader("Mock-Empty-Request-Header").shouldBeEmpty()
        event.getRequestHeader("MOCK-EMPTY-REQUEST-HEADER").shouldBeEmpty()
        event.getRequestHeader("mock-multi-request-header").shouldBe("mock-multi-request-header-value1")
        event.getRequestHeader("Mock-Multi-Request-Header").shouldBe("mock-multi-request-header-value1")
        event.getRequestHeader("MOCK-MULTI-REQUEST-HEADER").shouldBe("mock-multi-request-header-value1")
    }

    @Test
    fun `Appends a Logback-access event with response headers`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val url = "/mock-controller/text-with-response-headers"
        val request = RequestEntity.get(url).build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.responseHeaderMap.also {
            it.shouldContain("mock-response-header" to "mock-response-header-value")
            it.shouldContain("mock-empty-response-header" to "")
            it.shouldContain("mock-multi-response-header" to "mock-multi-response-header-value1")
        }
        event.responseHeaderNameList.also {
            it.shouldContain("mock-response-header")
            it.shouldNotContain("Mock-Response-Header")
            it.shouldNotContain("MOCK-RESPONSE-HEADER")
            it.shouldContain("mock-empty-response-header")
            it.shouldNotContain("Mock-Empty-Response-Header")
            it.shouldNotContain("MOCK-EMPTY-RESPONSE-HEADER")
            it.shouldContain("mock-multi-response-header")
            it.shouldNotContain("Mock-Multi-Response-Header")
            it.shouldNotContain("MOCK-MULTI-RESPONSE-HEADER")
        }
        event.getResponseHeader("mock-response-header").shouldBe("mock-response-header-value")
        event.getResponseHeader("Mock-Response-Header").shouldBe("mock-response-header-value")
        event.getResponseHeader("MOCK-RESPONSE-HEADER").shouldBe("mock-response-header-value")
        event.getResponseHeader("mock-empty-response-header").shouldBeEmpty()
        event.getResponseHeader("Mock-Empty-Response-Header").shouldBeEmpty()
        event.getResponseHeader("MOCK-EMPTY-RESPONSE-HEADER").shouldBeEmpty()
        event.getResponseHeader("mock-multi-response-header").shouldBe("mock-multi-response-header-value1")
        event.getResponseHeader("Mock-Multi-Response-Header").shouldBe("mock-multi-response-header-value1")
        event.getResponseHeader("MOCK-MULTI-RESPONSE-HEADER").shouldBe("mock-multi-response-header-value1")
    }

    @Test
    fun `Appends a Logback-access event with an empty response body`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val url = "/mock-controller/empty-text"
        val request = RequestEntity.get(url).build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.hasBody().shouldBeFalse()
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.contentLength.shouldBeZero()
    }

    @Test
    fun `Appends a Logback-access event with an asynchronous request`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val url = "/mock-controller/text-asynchronously"
        val request = RequestEntity.get(url).build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.threadName.shouldNotBeEmpty()
    }

    @Test
    fun `Appends a Logback-access event with chunked transfer encoding`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val url = "/mock-controller/text-with-chunked-transfer-encoding"
        val request = RequestEntity.get(url).build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.headers.getFirst("transfer-encoding").shouldBe("chunked")
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.contentLength.shouldBeGreaterThanOrEqual(9L)
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
