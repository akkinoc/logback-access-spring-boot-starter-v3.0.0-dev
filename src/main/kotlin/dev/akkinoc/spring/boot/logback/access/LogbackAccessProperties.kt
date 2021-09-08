package dev.akkinoc.spring.boot.logback.access

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * The configuration properties for Logback-access.
 *
 * @property enabled
 *  Whether to enable auto-configuration.
 */
@ConfigurationProperties("logback.access")
@ConstructorBinding
data class LogbackAccessProperties(
        val enabled: Boolean = true,
)
