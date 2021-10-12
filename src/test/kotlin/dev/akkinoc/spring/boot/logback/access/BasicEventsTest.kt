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
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldNotContainAnyOf
import io.kotest.matchers.longs.shouldBeBetween
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
 *
 * @property supportsRequestParametersByFormData Whether to support request parameters by form data.
 * @property supportsSessionID Whether to support session ID.
 * @property canForwardRequest Whether the web server can forward a request.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(properties = ["logback.access.config=classpath:logback-access.capture.xml"])
sealed class BasicEventsTest(
        private val supportsRequestParametersByFormData: Boolean,
        private val supportsSessionID: Boolean,
        private val canForwardRequest: Boolean,
) {

    @Test
    fun `Appends a Logback-access event`(
            @Autowired rest: TestRestTemplate,
            @LocalServerPort port: Int,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
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
        event.getRequestHeader("x").shouldBe("-")
        event.getCookie("x").shouldBe("-")
        event.requestParameterMap.shouldBeEmpty()
        event.getRequestParameter("x").shouldContainExactly("-")
        event.getAttribute("x").shouldBe("-")
        event.sessionID.shouldBe("-")
        event.requestContent.shouldBeEmpty()
        event.statusCode.shouldBe(200)
        event.responseHeaderMap.shouldNotBeNull()
        event.responseHeaderNameList.shouldNotBeNull()
        event.getResponseHeader("x").shouldBe("-")
        event.contentLength.shouldBe(9L)
        event.responseContent.shouldBeEmpty()
    }

    @Test
    fun `Appends a Logback-access event with request headers`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text")
                .header("a", "value @a")
                .header("b", "value1 @b", "value2 @b")
                .header("c", "")
                .build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.requestHeaderMap["a"].shouldBe("value @a")
        event.requestHeaderMap["b"].shouldBe("value1 @b")
        event.requestHeaderMap["c"].shouldBeEmpty()
        event.requestHeaderNames.toList().shouldContainAll("a", "b", "c")
        event.requestHeaderNames.toList().shouldNotContainAnyOf("A", "B", "C")
        event.getRequestHeader("a").shouldBe("value @a")
        event.getRequestHeader("A").shouldBe("value @a")
        event.getRequestHeader("b").shouldBe("value1 @b")
        event.getRequestHeader("B").shouldBe("value1 @b")
        event.getRequestHeader("c").shouldBeEmpty()
        event.getRequestHeader("C").shouldBeEmpty()
    }

    @Test
    fun `Appends a Logback-access event with request cookies`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text")
                .header("cookie", "a=value%20%40a; b=")
                .build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.getCookie("a").shouldBe("value%20%40a")
        event.getCookie("b").shouldBeEmpty()
    }

    @Test
    fun `Appends a Logback-access event with request parameters by query string`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text?a=value+@a&b=value1+@b&b=value2+@b&c=").build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.method.shouldBe("GET")
        event.requestURI.shouldBe("/mock-controller/text")
        event.queryString.shouldBe("?a=value+@a&b=value1+@b&b=value2+@b&c=")
        event.requestURL.shouldBe("GET /mock-controller/text?a=value+@a&b=value1+@b&b=value2+@b&c= HTTP/1.1")
        event.requestParameterMap.keys.shouldContainExactlyInAnyOrder("a", "b", "c")
        event.requestParameterMap["a"].shouldContainExactly("value @a")
        event.requestParameterMap["b"].shouldContainExactly("value1 @b", "value2 @b")
        event.requestParameterMap["c"].shouldContainExactly("")
        event.getRequestParameter("a").shouldContainExactly("value @a")
        event.getRequestParameter("b").shouldContainExactly("value1 @b", "value2 @b")
        event.getRequestParameter("c").shouldContainExactly("")
    }

    @Test
    fun `Appends a Logback-access event with request parameters by form data`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.post("/mock-controller/form-data")
                .header("content-type", "application/x-www-form-urlencoded")
                .body("a=value+@a&b=value1+@b&b=value2+@b&c=")
        val response = rest.exchange<String>(request)
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        event.method.shouldBe("POST")
        event.requestURI.shouldBe("/mock-controller/form-data")
        event.queryString.shouldBeEmpty()
        event.requestURL.shouldBe("POST /mock-controller/form-data HTTP/1.1")
        if (supportsRequestParametersByFormData) {
            event.requestParameterMap.keys.shouldContainExactlyInAnyOrder("a", "b", "c")
            event.requestParameterMap["a"].shouldContainExactly("value @a")
            event.requestParameterMap["b"].shouldContainExactly("value1 @b", "value2 @b")
            event.requestParameterMap["c"].shouldContainExactly("")
            event.getRequestParameter("a").shouldContainExactly("value @a")
            event.getRequestParameter("b").shouldContainExactly("value1 @b", "value2 @b")
            event.getRequestParameter("c").shouldContainExactly("")
        } else {
            event.requestParameterMap.shouldBeEmpty()
            event.getRequestParameter("a").shouldContainExactly("-")
            event.getRequestParameter("b").shouldContainExactly("-")
            event.getRequestParameter("c").shouldContainExactly("-")
        }
    }

    @Test
    fun `Appends a Logback-access event with a session`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text-with-session").build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        if (supportsSessionID) {
            event.sessionID.shouldNotBeEmpty().shouldNotBe("-")
        } else {
            event.sessionID.shouldBe("-")
        }
    }

    @Test
    fun `Appends a Logback-access event with response headers`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text-with-response-headers").build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.responseHeaderMap["a"].shouldBe("value @a")
        event.responseHeaderMap["b"].shouldBe("value1 @b")
        event.responseHeaderMap["c"].shouldBeEmpty()
        event.responseHeaderNameList.shouldContainAll("a", "b", "c")
        event.responseHeaderNameList.shouldNotContainAnyOf("A", "B", "C")
        event.getResponseHeader("a").shouldBe("value @a")
        event.getResponseHeader("A").shouldBe("value @a")
        event.getResponseHeader("b").shouldBe("value1 @b")
        event.getResponseHeader("B").shouldBe("value1 @b")
        event.getResponseHeader("c").shouldBeEmpty()
        event.getResponseHeader("C").shouldBeEmpty()
    }

    @Test
    fun `Appends a Logback-access event with an empty response`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/empty-text").build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.hasBody().shouldBeFalse()
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.contentLength.shouldBeZero()
    }

    @Test
    fun `Appends a Logback-access event with an asynchronous response`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text-asynchronously").build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.threadName.shouldNotBeEmpty()
        event.contentLength.shouldBe(9L)
    }

    @Test
    fun `Appends a Logback-access event with a chunked response`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text-with-chunked-transfer-encoding").build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.headers["transfer-encoding"].shouldBe(listOf("chunked"))
        response.headers["content-length"].shouldBeNull()
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.contentLength.shouldBeGreaterThanOrEqual(9L)
    }

    @Test
    fun `Appends a Logback-access event with a forward response`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        if (!canForwardRequest) return
        val request = RequestEntity.get("/mock-controller/text-with-forward?a=value+@a").build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.protocol.shouldBe("HTTP/1.1")
        event.method.shouldBe("GET")
        event.requestURI.shouldBe("/mock-controller/text-with-forward")
        event.queryString.shouldBe("?a=value+@a")
        event.requestURL.shouldBe("GET /mock-controller/text-with-forward?a=value+@a HTTP/1.1")
        event.statusCode.shouldBe(200)
    }

    @Test
    fun `Appends a Logback-access event with an error response`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/unknown?a=value+@a").build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(404)
        val event = assertLogbackAccessEvents { capture.shouldBeSingleton().single() }
        event.protocol.shouldBe("HTTP/1.1")
        event.method.shouldBe("GET")
        event.requestURI.shouldBe("/mock-controller/unknown")
        event.queryString.shouldBe("?a=value+@a")
        event.requestURL.shouldBe("GET /mock-controller/unknown?a=value+@a HTTP/1.1")
        event.statusCode.shouldBe(404)
    }

}

/**
 * Tests the [BasicEventsTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebBasicEventsTest : BasicEventsTest(
        supportsRequestParametersByFormData = true,
        supportsSessionID = true,
        canForwardRequest = true,
)

/**
 * Tests the [BasicEventsTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebBasicEventsTest : BasicEventsTest(
        supportsRequestParametersByFormData = false,
        supportsSessionID = false,
        canForwardRequest = false,
)

/**
 * Tests the [BasicEventsTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebBasicEventsTest : BasicEventsTest(
        supportsRequestParametersByFormData = true,
        supportsSessionID = true,
        canForwardRequest = true,
)

/**
 * Tests the [BasicEventsTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebBasicEventsTest : BasicEventsTest(
        supportsRequestParametersByFormData = false,
        supportsSessionID = false,
        canForwardRequest = false,
)

/**
 * Tests the [BasicEventsTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebBasicEventsTest : BasicEventsTest(
        supportsRequestParametersByFormData = true,
        supportsSessionID = true,
        canForwardRequest = true,
)

/**
 * Tests the [BasicEventsTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebBasicEventsTest : BasicEventsTest(
        supportsRequestParametersByFormData = false,
        supportsSessionID = false,
        canForwardRequest = false,
)
