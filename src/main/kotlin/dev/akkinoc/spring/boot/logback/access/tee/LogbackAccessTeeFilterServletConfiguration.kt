package dev.akkinoc.spring.boot.logback.access.tee

import ch.qos.logback.access.AccessConstants.TEE_FILTER_EXCLUDES_PARAM
import ch.qos.logback.access.AccessConstants.TEE_FILTER_INCLUDES_PARAM
import ch.qos.logback.access.servlet.TeeFilter
import dev.akkinoc.spring.boot.logback.access.LogbackAccessProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The configuration to register the tee filter for the servlet web server.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
        prefix = "logback.access.tee-filter",
        name = ["enabled"],
        havingValue = "true",
        matchIfMissing = false,
)
@ConditionalOnWebApplication(type = SERVLET)
class LogbackAccessTeeFilterServletConfiguration {

    /**
     * Provides the tee filter for the servlet web server.
     *
     * @param logbackAccessProperties The configuration properties for Logback-access.
     * @return The tee filter for the servlet web server.
     */
    @Bean
    @ConditionalOnMissingFilterBean
    fun logbackAccessTeeFilter(
            logbackAccessProperties: LogbackAccessProperties,
    ): FilterRegistrationBean<TeeFilter> {
        val props = logbackAccessProperties.teeFilter
        val logbackAccessTeeFilter = FilterRegistrationBean(TeeFilter())
        props.includes?.also { logbackAccessTeeFilter.addInitParameter(TEE_FILTER_INCLUDES_PARAM, it) }
        props.excludes?.also { logbackAccessTeeFilter.addInitParameter(TEE_FILTER_EXCLUDES_PARAM, it) }
        log.debug("Providing the {}: {}", TeeFilter::class.simpleName, logbackAccessTeeFilter)
        return logbackAccessTeeFilter
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessTeeFilterServletConfiguration::class.java)

    }

}
