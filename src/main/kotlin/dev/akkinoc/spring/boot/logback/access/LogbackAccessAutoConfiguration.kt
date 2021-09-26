package dev.akkinoc.spring.boot.logback.access

import dev.akkinoc.spring.boot.logback.access.jetty.LogbackAccessJettyConfiguration
import dev.akkinoc.spring.boot.logback.access.tomcat.LogbackAccessTomcatConfiguration
import dev.akkinoc.spring.boot.logback.access.undertow.LogbackAccessUndertowReactiveConfiguration
import dev.akkinoc.spring.boot.logback.access.undertow.LogbackAccessUndertowServletConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.springframework.core.io.ResourceLoader

/**
 * The auto-configuration for Logback-access.
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(ServletWebServerFactoryAutoConfiguration::class, ReactiveWebServerFactoryAutoConfiguration::class)
@ConditionalOnProperty(prefix = "logback.access", name = ["enabled"], havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication
@EnableConfigurationProperties(LogbackAccessProperties::class)
@Import(
        LogbackAccessTomcatConfiguration::class,
        LogbackAccessJettyConfiguration::class,
        LogbackAccessUndertowServletConfiguration::class,
        LogbackAccessUndertowReactiveConfiguration::class,
)
class LogbackAccessAutoConfiguration {

    /**
     * Provides the Logback-access context.
     *
     * @param logbackAccessProperties The configuration properties for Logback-access.
     * @param resourceLoader The resource loader.
     * @param environment The environment.
     * @return The Logback-access context.
     */
    @Bean
    @ConditionalOnMissingBean
    fun logbackAccessContext(
            logbackAccessProperties: LogbackAccessProperties,
            resourceLoader: ResourceLoader,
            environment: Environment,
    ): LogbackAccessContext {
        val logbackAccessContext = LogbackAccessContext(logbackAccessProperties, resourceLoader, environment)
        log.debug("Providing the {}: {}", LogbackAccessContext::class.simpleName, logbackAccessContext)
        return logbackAccessContext
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessAutoConfiguration::class.java)

    }

}
