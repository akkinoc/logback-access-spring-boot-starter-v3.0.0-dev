package dev.akkinoc.spring.boot.logback.access

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * The configuration properties for Logback-access.
 *
 * @property enabled
 *  Whether to enable auto-configuration.
 * @property config
 *  The location of the configuration file.
 *  Auto-detected by default:
 *      1. "classpath:logback-access-test.xml"
 *      2. "classpath:logback-access.xml"
 *      3. "classpath:logback-access-test-spring.xml"
 *      4. "classpath:logback-access-spring.xml"
 *      5. "classpath:dev/akkinoc/spring/boot/logback/access/logback-access-spring.xml"
 */
@ConfigurationProperties("logback.access")
@ConstructorBinding
data class LogbackAccessProperties(
        val enabled: Boolean = true,
        val config: String? = null,
) {

    companion object {

        /**
         * The default locations of the configuration file.
         */
        @JvmStatic
        val DEFAULT_CONFIGS: List<String> = listOf(
                "classpath:logback-access-test.xml",
                "classpath:logback-access.xml",
                "classpath:logback-access-test-spring.xml",
                "classpath:logback-access-spring.xml",
        )

        /**
         * The fallback location of the configuration file.
         */
        @JvmStatic
        val FALLBACK_CONFIG: String = "classpath:dev/akkinoc/spring/boot/logback/access/logback-access-spring.xml"

    }

}
