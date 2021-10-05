package dev.akkinoc.spring.boot.logback.access

import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEvents
import dev.akkinoc.spring.boot.logback.access.test.type.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowServletWebTest
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus.OK

/**
 * Tests the case where the configuration is the default.
 */
sealed class BasicTest {

    @Test
    fun `Provides the configuration properties for Logback-access`(
            @Autowired logbackAccessProperties: LogbackAccessProperties?,
    ) {
        logbackAccessProperties.shouldBe(LogbackAccessProperties())
    }

    @Test
    fun `Provides the Logback-access context`(
            @Autowired logbackAccessContext: LogbackAccessContext?,
            @Autowired logbackAccessProperties: LogbackAccessProperties?,
    ) {
        logbackAccessContext.shouldNotBeNull()
        logbackAccessContext.properties.shouldBe(logbackAccessProperties)
    }

    @Test
    @ExtendWith(OutputCaptureExtension::class)
    fun `Appends a Logback-access event`(
            @Autowired rest: TestRestTemplate,
            capture: CapturedOutput,
    ) {
        val response = rest.getForEntity<String>("/mock-controller/text")
        response.statusCode.shouldBe(OK)
        assertLogbackAccessEvents {
            val regex = Regex("""^127\.0\.0\.1 - - \[.+] "GET /mock-controller/text HTTP/1\.1" 200 9$""")
            capture.out.lines().shouldHaveSingleElement { it matches regex }
        }
    }

}

/**
 * Tests the [BasicTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebBasicTest : BasicTest()

/**
 * Tests the [BasicTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebBasicTest : BasicTest()

/**
 * Tests the [BasicTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebBasicTest : BasicTest()

/**
 * Tests the [BasicTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebBasicTest : BasicTest()

/**
 * Tests the [BasicTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebBasicTest : BasicTest()

/**
 * Tests the [BasicTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebBasicTest : BasicTest()
