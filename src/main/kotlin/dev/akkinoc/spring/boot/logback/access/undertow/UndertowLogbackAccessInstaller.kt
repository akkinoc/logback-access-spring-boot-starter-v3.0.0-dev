package dev.akkinoc.spring.boot.logback.access.undertow

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer

/**
 * The Logback-access installer for the Undertow web server.
 */
class UndertowLogbackAccessInstaller : WebServerFactoryCustomizer<ConfigurableUndertowWebServerFactory> {

    override fun customize(factory: ConfigurableUndertowWebServerFactory) {
        // TODO
        log.debug("Installed Logback-access into the WebServerFactory: $factory")
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(UndertowLogbackAccessInstaller::class.java)

    }

}
