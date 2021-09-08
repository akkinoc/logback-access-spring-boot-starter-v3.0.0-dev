package dev.akkinoc.spring.boot.logback.access.jetty

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.web.embedded.jetty.ConfigurableJettyWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer

/**
 * The Logback-access installer for the Jetty web server.
 */
class JettyLogbackAccessInstaller : WebServerFactoryCustomizer<ConfigurableJettyWebServerFactory> {

    override fun customize(factory: ConfigurableJettyWebServerFactory) {
        // TODO
        log.debug("Installed Logback-access into the WebServerFactory: $factory")
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(JettyLogbackAccessInstaller::class.java)

    }

}
