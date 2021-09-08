package dev.akkinoc.spring.boot.logback.access.undertow

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import io.undertow.servlet.api.DeploymentInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer

/**
 * The [WebServerFactoryCustomizer] for the Undertow servlet web server.
 *
 * This class was implemented with reference to:
 *
 * * [org.springframework.boot.autoconfigure.web.embedded.UndertowWebServerFactoryCustomizer]
 * * [org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory]
 * * [org.springframework.boot.web.embedded.undertow.UndertowWebServerFactoryDelegate]
 * * [org.springframework.boot.web.embedded.undertow.AccessLogHttpHandlerFactory]
 *
 * @property logbackAccessContext The Logback-access context.
 */
class LogbackAccessUndertowServletWebServerFactoryCustomizer(
        private val logbackAccessContext: LogbackAccessContext,
) : WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    override fun customize(factory: UndertowServletWebServerFactory) {
        factory.addDeploymentInfoCustomizers(::customize)
        log.debug(
                "Customized the {}: {} @{}",
                UndertowServletWebServerFactory::class.simpleName,
                factory,
                logbackAccessContext,
        )
    }

    /**
     * Customizes the [DeploymentInfo].
     *
     * @param info The [DeploymentInfo].
     */
    private fun customize(info: DeploymentInfo) {
        info.addInitialHandlerChainWrapper {
            LogbackAccessUndertowHttpHandler(logbackAccessContext, it)
        }
        log.debug(
                "Customized the {}: {} @{}",
                DeploymentInfo::class.simpleName,
                info,
                logbackAccessContext,
        )
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessUndertowServletWebServerFactoryCustomizer::class.java)

    }

}
