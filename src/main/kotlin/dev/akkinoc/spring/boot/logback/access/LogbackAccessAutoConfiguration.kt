package dev.akkinoc.spring.boot.logback.access

import dev.akkinoc.spring.boot.logback.access.jetty.JettyLogbackAccessInstaller
import dev.akkinoc.spring.boot.logback.access.tomcat.TomcatLogbackAccessInstaller
import dev.akkinoc.spring.boot.logback.access.undertow.UndertowLogbackAccessInstaller
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.embedded.jetty.ConfigurableJettyWebServerFactory
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory
import org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The auto-configuration for Logback-access.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "logback.access", name = ["enabled"], havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(LogbackAccessProperties::class)
class LogbackAccessAutoConfiguration {

    /**
     * For the Tomcat web server.
     */
    @Configuration
    @ConditionalOnBean(ConfigurableTomcatWebServerFactory::class)
    class ForTomcat {

        /**
         * Provides the Logback-access installer.
         *
         * @return The Logback-access installer.
         */
        @Bean
        @ConditionalOnMissingBean
        fun tomcatLogbackAccessInstaller(): TomcatLogbackAccessInstaller {
            val tomcatLogbackAccessInstaller = TomcatLogbackAccessInstaller()
            log.debug("Providing the TomcatLogbackAccessInstaller: $tomcatLogbackAccessInstaller")
            return tomcatLogbackAccessInstaller
        }

    }

    /**
     * For the Jetty web server.
     */
    @Configuration
    @ConditionalOnBean(ConfigurableJettyWebServerFactory::class)
    class ForJetty {

        /**
         * Provides the Logback-access installer.
         *
         * @return The Logback-access installer.
         */
        @Bean
        @ConditionalOnMissingBean
        fun jettyLogbackAccessInstaller(): JettyLogbackAccessInstaller {
            val jettyLogbackAccessInstaller = JettyLogbackAccessInstaller()
            log.debug("Providing the JettyLogbackAccessInstaller: $jettyLogbackAccessInstaller")
            return jettyLogbackAccessInstaller
        }

    }

    /**
     * For the Undertow web server.
     */
    @Configuration
    @ConditionalOnBean(ConfigurableUndertowWebServerFactory::class)
    class ForUndertow {

        /**
         * Provides the Logback-access installer.
         *
         * @return The Logback-access installer.
         */
        @Bean
        @ConditionalOnMissingBean
        fun undertowLogbackAccessInstaller(): UndertowLogbackAccessInstaller {
            val undertowLogbackAccessInstaller = UndertowLogbackAccessInstaller()
            log.debug("Providing the UndertowLogbackAccessInstaller: $undertowLogbackAccessInstaller")
            return undertowLogbackAccessInstaller
        }

    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessAutoConfiguration::class.java)

    }

}
