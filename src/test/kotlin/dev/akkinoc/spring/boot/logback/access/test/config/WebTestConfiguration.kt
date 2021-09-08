package dev.akkinoc.spring.boot.logback.access.test.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.embedded.jetty.JettyReactiveWebServerFactory
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.embedded.tomcat.TomcatReactiveWebServerFactory
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.embedded.undertow.UndertowReactiveWebServerFactory
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.context.annotation.Bean

/**
 * The test configuration for using the web server.
 */
sealed interface WebTestConfiguration {

    /**
     * Returns whether to use the Tomcat web server.
     *
     * @return Whether to use the Tomcat web server.
     */
    fun usesTomcat(): Boolean = false

    /**
     * Returns whether to use the Jetty web server.
     *
     * @return Whether to use the Jetty web server.
     */
    fun usesJetty(): Boolean = false

    /**
     * Returns whether to use the Undertow web server.
     *
     * @return Whether to use the Undertow web server.
     */
    fun usesUndertow(): Boolean = false

    /**
     * Returns whether to use a servlet web server.
     *
     * @return Whether to use a servlet web server.
     */
    fun usesServlet(): Boolean = false

    /**
     * Returns whether to use a reactive web server.
     *
     * @return Whether to use a reactive web server.
     */
    fun usesReactive(): Boolean = false

}

/**
 * The test configuration for using the Tomcat servlet web server.
 */
@TestConfiguration(proxyBeanMethods = false)
class TomcatServletWebTestConfiguration : WebTestConfiguration {

    override fun usesTomcat(): Boolean = true

    override fun usesServlet(): Boolean = true

    /**
     * Provides the [TomcatServletWebServerFactory].
     *
     * @return The [TomcatServletWebServerFactory].
     */
    @Bean
    fun tomcatServletWebServerFactory(): TomcatServletWebServerFactory {
        val tomcatServletWebServerFactory = TomcatServletWebServerFactory()
        log.debug("Providing the TomcatServletWebServerFactory: $tomcatServletWebServerFactory")
        return tomcatServletWebServerFactory
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(TomcatServletWebTestConfiguration::class.java)

    }

}

/**
 * The test configuration for using the Tomcat reactive web server.
 */
@TestConfiguration(proxyBeanMethods = false)
class TomcatReactiveWebTestConfiguration : WebTestConfiguration {

    override fun usesTomcat(): Boolean = true

    override fun usesReactive(): Boolean = true

    /**
     * Provides the [TomcatReactiveWebServerFactory].
     *
     * @return The [TomcatReactiveWebServerFactory].
     */
    @Bean
    fun tomcatReactiveWebServerFactory(): TomcatReactiveWebServerFactory {
        val tomcatReactiveWebServerFactory = TomcatReactiveWebServerFactory()
        log.debug("Providing the TomcatReactiveWebServerFactory: $tomcatReactiveWebServerFactory")
        return tomcatReactiveWebServerFactory
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(TomcatReactiveWebTestConfiguration::class.java)

    }

}

/**
 * The test configuration for using the Jetty servlet web server.
 */
@TestConfiguration(proxyBeanMethods = false)
class JettyServletWebTestConfiguration : WebTestConfiguration {

    override fun usesJetty(): Boolean = true

    override fun usesServlet(): Boolean = true

    /**
     * Provides the [JettyServletWebServerFactory].
     *
     * @return The [JettyServletWebServerFactory].
     */
    @Bean
    fun jettyServletWebServerFactory(): JettyServletWebServerFactory {
        val jettyServletWebServerFactory = JettyServletWebServerFactory()
        log.debug("Providing the JettyServletWebServerFactory: $jettyServletWebServerFactory")
        return jettyServletWebServerFactory
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(JettyServletWebTestConfiguration::class.java)

    }

}

/**
 * The test configuration for using the Jetty reactive web server.
 */
@TestConfiguration(proxyBeanMethods = false)
class JettyReactiveWebTestConfiguration : WebTestConfiguration {

    override fun usesJetty(): Boolean = true

    override fun usesReactive(): Boolean = true

    /**
     * Provides the [JettyReactiveWebServerFactory].
     *
     * @return The [JettyReactiveWebServerFactory].
     */
    @Bean
    fun jettyReactiveWebServerFactory(): JettyReactiveWebServerFactory {
        val jettyReactiveWebServerFactory = JettyReactiveWebServerFactory()
        log.debug("Providing the JettyReactiveWebServerFactory: $jettyReactiveWebServerFactory")
        return jettyReactiveWebServerFactory
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(JettyReactiveWebTestConfiguration::class.java)

    }

}

/**
 * The test configuration for using the Undertow servlet web server.
 */
@TestConfiguration(proxyBeanMethods = false)
class UndertowServletWebTestConfiguration : WebTestConfiguration {

    override fun usesUndertow(): Boolean = true

    override fun usesServlet(): Boolean = true

    /**
     * Provides the [UndertowServletWebServerFactory].
     *
     * @return The [UndertowServletWebServerFactory].
     */
    @Bean
    fun undertowServletWebServerFactory(): UndertowServletWebServerFactory {
        val undertowServletWebServerFactory = UndertowServletWebServerFactory()
        log.debug("Providing the UndertowServletWebServerFactory: $undertowServletWebServerFactory")
        return undertowServletWebServerFactory
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(UndertowServletWebTestConfiguration::class.java)

    }

}

/**
 * The test configuration for using the Undertow reactive web server.
 */
@TestConfiguration(proxyBeanMethods = false)
class UndertowReactiveWebTestConfiguration : WebTestConfiguration {

    override fun usesUndertow(): Boolean = true

    override fun usesReactive(): Boolean = true

    /**
     * Provides the [UndertowReactiveWebServerFactory].
     *
     * @return The [UndertowReactiveWebServerFactory].
     */
    @Bean
    fun undertowReactiveWebServerFactory(): UndertowReactiveWebServerFactory {
        val undertowReactiveWebServerFactory = UndertowReactiveWebServerFactory()
        log.debug("Providing the UndertowReactiveWebServerFactory: $undertowReactiveWebServerFactory")
        return undertowReactiveWebServerFactory
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(UndertowReactiveWebTestConfiguration::class.java)

    }

}
