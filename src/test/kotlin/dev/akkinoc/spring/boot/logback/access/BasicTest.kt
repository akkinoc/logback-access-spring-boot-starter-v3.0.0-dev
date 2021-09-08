package dev.akkinoc.spring.boot.logback.access

import dev.akkinoc.spring.boot.logback.access.jetty.JettyLogbackAccessInstaller
import dev.akkinoc.spring.boot.logback.access.test.config.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.config.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.config.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.config.TomcatServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.config.UndertowReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.config.UndertowServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.config.WebTestConfiguration
import dev.akkinoc.spring.boot.logback.access.tomcat.TomcatLogbackAccessInstaller
import dev.akkinoc.spring.boot.logback.access.undertow.UndertowLogbackAccessInstaller
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Tests the case where the configuration is the default.
 */
sealed class BasicTest {

    @Test
    fun `Provides the configuration properties`(
            @Autowired logbackAccessProperties: LogbackAccessProperties?,
    ) {
        logbackAccessProperties.shouldNotBeNull()
        logbackAccessProperties.enabled.shouldBe(true)
    }

    @Test
    fun `Provides the Logback-access installer for the Tomcat web server, if that web server is used`(
            @Autowired tomcatLogbackAccessInstaller: TomcatLogbackAccessInstaller?,
            @Autowired webTestConfiguration: WebTestConfiguration,
    ) {
        if (webTestConfiguration.usesTomcat()) tomcatLogbackAccessInstaller.shouldNotBeNull()
        else tomcatLogbackAccessInstaller.shouldBeNull()
    }

    @Test
    fun `Provides the Logback-access installer for the Jetty web server, if that web server is used`(
            @Autowired jettyLogbackAccessInstaller: JettyLogbackAccessInstaller?,
            @Autowired webTestConfiguration: WebTestConfiguration,
    ) {
        if (webTestConfiguration.usesJetty()) jettyLogbackAccessInstaller.shouldNotBeNull()
        else jettyLogbackAccessInstaller.shouldBeNull()
    }

    @Test
    fun `Provides the Logback-access installer for the Undertow web server, if that web server is used`(
            @Autowired undertowLogbackAccessInstaller: UndertowLogbackAccessInstaller?,
            @Autowired webTestConfiguration: WebTestConfiguration,
    ) {
        if (webTestConfiguration.usesUndertow()) undertowLogbackAccessInstaller.shouldNotBeNull()
        else undertowLogbackAccessInstaller.shouldBeNull()
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
