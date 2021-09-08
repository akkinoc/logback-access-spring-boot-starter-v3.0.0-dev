package dev.akkinoc.spring.boot.logback.access.tomcat

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer

/**
 * The Logback-access installer for the Tomcat web server.
 */
class TomcatLogbackAccessInstaller : WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory> {

    override fun customize(factory: ConfigurableTomcatWebServerFactory) {
        // TODO
        log.debug("Installed Logback-access into the WebServerFactory: $factory")
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(TomcatLogbackAccessInstaller::class.java)

    }

}
