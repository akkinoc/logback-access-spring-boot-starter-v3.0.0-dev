package dev.akkinoc.spring.boot.logback.access

import dev.akkinoc.spring.boot.logback.access.jetty.JettyLogbackAccessInstaller
import dev.akkinoc.spring.boot.logback.access.test.config.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.config.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.config.NonWebTest
import dev.akkinoc.spring.boot.logback.access.test.config.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.config.TomcatServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.config.UndertowReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.config.UndertowServletWebTest
import dev.akkinoc.spring.boot.logback.access.tomcat.TomcatLogbackAccessInstaller
import dev.akkinoc.spring.boot.logback.access.undertow.UndertowLogbackAccessInstaller
import io.kotest.matchers.nulls.shouldBeNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where auto-configuration is inactive.
 */
sealed class InactiveTest {

    @Test
    fun `Does not provide the configuration properties`(
            @Autowired logbackAccessProperties: LogbackAccessProperties?,
    ) {
        logbackAccessProperties.shouldBeNull()
    }

    @Test
    fun `Does not provide the Logback-access installer for the Tomcat web server`(
            @Autowired tomcatLogbackAccessInstaller: TomcatLogbackAccessInstaller?,
    ) {
        tomcatLogbackAccessInstaller.shouldBeNull()
    }

    @Test
    fun `Does not provide the Logback-access installer for the Jetty web server`(
            @Autowired jettyLogbackAccessInstaller: JettyLogbackAccessInstaller?,
    ) {
        jettyLogbackAccessInstaller.shouldBeNull()
    }

    @Test
    fun `Does not provide the Logback-access installer for the Undertow web server`(
            @Autowired undertowLogbackAccessInstaller: UndertowLogbackAccessInstaller?,
    ) {
        undertowLogbackAccessInstaller.shouldBeNull()
    }

}

/**
 * Tests the case where the web server is not used.
 */
@NonWebTest
class NonWebInactiveTest : InactiveTest()

/**
 * Tests the case where auto-configuration is disabled.
 */
@TestPropertySource(properties = ["logback.access.enabled=false"])
sealed class DisabledTest : InactiveTest()

/**
 * Tests the [DisabledTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebDisabledTest : DisabledTest()

/**
 * Tests the [DisabledTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebDisabledTest : DisabledTest()

/**
 * Tests the [DisabledTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebDisabledTest : DisabledTest()

/**
 * Tests the [DisabledTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebDisabledTest : DisabledTest()

/**
 * Tests the [DisabledTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebDisabledTest : DisabledTest()

/**
 * Tests the [DisabledTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebDisabledTest : DisabledTest()
