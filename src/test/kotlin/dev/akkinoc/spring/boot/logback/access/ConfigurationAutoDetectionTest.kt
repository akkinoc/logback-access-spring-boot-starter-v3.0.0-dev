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
sealed class ConfigurationAutoDetectionTest {

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
sealed class TestConfigurationAutoDetectionTest : ConfigurationAutoDetectionTest()

/**
 * Tests the [TestConfigurationAutoDetectionTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTestConfigurationAutoDetectionTest : TestConfigurationAutoDetectionTest()

/**
 * Tests the [TestConfigurationAutoDetectionTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebTestConfigurationAutoDetectionTest : TestConfigurationAutoDetectionTest()

/**
 * Tests the [TestConfigurationAutoDetectionTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTestConfigurationAutoDetectionTest : TestConfigurationAutoDetectionTest()

/**
 * Tests the [TestConfigurationAutoDetectionTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebTestConfigurationAutoDetectionTest : TestConfigurationAutoDetectionTest()

/**
 * Tests the [TestConfigurationAutoDetectionTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebTestConfigurationAutoDetectionTest : TestConfigurationAutoDetectionTest()

/**
 * Tests the [TestConfigurationAutoDetectionTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebTestConfigurationAutoDetectionTest : TestConfigurationAutoDetectionTest()

/**
 * Tests the case where the configuration file ("classpath:logback-access.xml") is auto-detected.
 */
sealed class MainConfigurationAutoDetectionTest : ConfigurationAutoDetectionTest()

/**
 * Tests the [MainConfigurationAutoDetectionTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebMainConfigurationAutoDetectionTest : MainConfigurationAutoDetectionTest()

/**
 * Tests the [MainConfigurationAutoDetectionTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebMainConfigurationAutoDetectionTest : MainConfigurationAutoDetectionTest()

/**
 * Tests the [MainConfigurationAutoDetectionTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebMainConfigurationAutoDetectionTest : MainConfigurationAutoDetectionTest()

/**
 * Tests the [MainConfigurationAutoDetectionTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebMainConfigurationAutoDetectionTest : MainConfigurationAutoDetectionTest()

/**
 * Tests the [MainConfigurationAutoDetectionTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebMainConfigurationAutoDetectionTest : MainConfigurationAutoDetectionTest()

/**
 * Tests the [MainConfigurationAutoDetectionTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebMainConfigurationAutoDetectionTest : MainConfigurationAutoDetectionTest()

/**
 * Tests the case where the configuration file ("classpath:logback-access-test-spring.xml") is auto-detected.
 */
sealed class TestSpringConfigurationAutoDetectionTest : ConfigurationAutoDetectionTest()

/**
 * Tests the [TestSpringConfigurationAutoDetectionTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTestSpringConfigurationAutoDetectionTest : TestSpringConfigurationAutoDetectionTest()

/**
 * Tests the [TestSpringConfigurationAutoDetectionTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebTestSpringConfigurationAutoDetectionTest : TestSpringConfigurationAutoDetectionTest()

/**
 * Tests the [TestSpringConfigurationAutoDetectionTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTestSpringConfigurationAutoDetectionTest : TestSpringConfigurationAutoDetectionTest()

/**
 * Tests the [TestSpringConfigurationAutoDetectionTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebTestSpringConfigurationAutoDetectionTest : TestSpringConfigurationAutoDetectionTest()

/**
 * Tests the [TestSpringConfigurationAutoDetectionTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebTestSpringConfigurationAutoDetectionTest : TestSpringConfigurationAutoDetectionTest()

/**
 * Tests the [TestSpringConfigurationAutoDetectionTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebTestSpringConfigurationAutoDetectionTest : TestSpringConfigurationAutoDetectionTest()

/**
 * Tests the case where the configuration file ("classpath:logback-access-spring.xml") is auto-detected.
 */
sealed class MainSpringConfigurationAutoDetectionTest : ConfigurationAutoDetectionTest()

/**
 * Tests the [MainSpringConfigurationAutoDetectionTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebMainSpringConfigurationAutoDetectionTest : MainSpringConfigurationAutoDetectionTest()

/**
 * Tests the [MainSpringConfigurationAutoDetectionTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebMainSpringConfigurationAutoDetectionTest : MainSpringConfigurationAutoDetectionTest()

/**
 * Tests the [MainSpringConfigurationAutoDetectionTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebMainSpringConfigurationAutoDetectionTest : MainSpringConfigurationAutoDetectionTest()

/**
 * Tests the [MainSpringConfigurationAutoDetectionTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebMainSpringConfigurationAutoDetectionTest : MainSpringConfigurationAutoDetectionTest()

/**
 * Tests the [MainSpringConfigurationAutoDetectionTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebMainSpringConfigurationAutoDetectionTest : MainSpringConfigurationAutoDetectionTest()

/**
 * Tests the [MainSpringConfigurationAutoDetectionTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebMainSpringConfigurationAutoDetectionTest : MainSpringConfigurationAutoDetectionTest()
