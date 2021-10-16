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
import org.springframework.http.RequestEntity

/**
 * Tests the case where the configuration file is auto-detected.
 */
@ExtendWith(EventsCaptureExtension::class)
sealed class ConfigurationFileAutoDetectionTest {

    @Test
    fun `Appends a Logback-access event`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        assertLogbackAccessEventsEventually { capture.shouldBeSingleton() }
    }

}

/**
 * Tests the case where the configuration file ("classpath:logback-access-test.xml") is auto-detected.
 */
sealed class TestConfigurationFileAutoDetectionTest : ConfigurationFileAutoDetectionTest()

/**
 * Tests the [TestConfigurationFileAutoDetectionTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTestConfigurationFileAutoDetectionTest : TestConfigurationFileAutoDetectionTest()

/**
 * Tests the [TestConfigurationFileAutoDetectionTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebTestConfigurationFileAutoDetectionTest : TestConfigurationFileAutoDetectionTest()

/**
 * Tests the [TestConfigurationFileAutoDetectionTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTestConfigurationFileAutoDetectionTest : TestConfigurationFileAutoDetectionTest()

/**
 * Tests the [TestConfigurationFileAutoDetectionTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebTestConfigurationFileAutoDetectionTest : TestConfigurationFileAutoDetectionTest()

/**
 * Tests the [TestConfigurationFileAutoDetectionTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebTestConfigurationFileAutoDetectionTest : TestConfigurationFileAutoDetectionTest()

/**
 * Tests the [TestConfigurationFileAutoDetectionTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebTestConfigurationFileAutoDetectionTest : TestConfigurationFileAutoDetectionTest()

/**
 * Tests the case where the configuration file ("classpath:logback-access.xml") is auto-detected.
 */
sealed class MainConfigurationFileAutoDetectionTest : ConfigurationFileAutoDetectionTest()

/**
 * Tests the [MainConfigurationFileAutoDetectionTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebMainConfigurationFileAutoDetectionTest : MainConfigurationFileAutoDetectionTest()

/**
 * Tests the [MainConfigurationFileAutoDetectionTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebMainConfigurationFileAutoDetectionTest : MainConfigurationFileAutoDetectionTest()

/**
 * Tests the [MainConfigurationFileAutoDetectionTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebMainConfigurationFileAutoDetectionTest : MainConfigurationFileAutoDetectionTest()

/**
 * Tests the [MainConfigurationFileAutoDetectionTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebMainConfigurationFileAutoDetectionTest : MainConfigurationFileAutoDetectionTest()

/**
 * Tests the [MainConfigurationFileAutoDetectionTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebMainConfigurationFileAutoDetectionTest : MainConfigurationFileAutoDetectionTest()

/**
 * Tests the [MainConfigurationFileAutoDetectionTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebMainConfigurationFileAutoDetectionTest : MainConfigurationFileAutoDetectionTest()

/**
 * Tests the case where the configuration file ("classpath:logback-access-test-spring.xml") is auto-detected.
 */
sealed class TestSpringConfigurationFileAutoDetectionTest : ConfigurationFileAutoDetectionTest()

/**
 * Tests the [TestSpringConfigurationFileAutoDetectionTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTestSpringConfigurationFileAutoDetectionTest : TestSpringConfigurationFileAutoDetectionTest()

/**
 * Tests the [TestSpringConfigurationFileAutoDetectionTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebTestSpringConfigurationFileAutoDetectionTest : TestSpringConfigurationFileAutoDetectionTest()

/**
 * Tests the [TestSpringConfigurationFileAutoDetectionTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTestSpringConfigurationFileAutoDetectionTest : TestSpringConfigurationFileAutoDetectionTest()

/**
 * Tests the [TestSpringConfigurationFileAutoDetectionTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebTestSpringConfigurationFileAutoDetectionTest : TestSpringConfigurationFileAutoDetectionTest()

/**
 * Tests the [TestSpringConfigurationFileAutoDetectionTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebTestSpringConfigurationFileAutoDetectionTest : TestSpringConfigurationFileAutoDetectionTest()

/**
 * Tests the [TestSpringConfigurationFileAutoDetectionTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebTestSpringConfigurationFileAutoDetectionTest : TestSpringConfigurationFileAutoDetectionTest()
