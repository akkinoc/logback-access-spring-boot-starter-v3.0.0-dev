package dev.akkinoc.spring.boot.logback.access

import org.apache.catalina.valves.RemoteIpValve
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
 * @property tomcat
 *  The properties for the Tomcat web server.
 * @property teeFilter
 *  The properties for the tee filter.
 */
@ConfigurationProperties("logback.access")
@ConstructorBinding
data class LogbackAccessProperties
@JvmOverloads
constructor(
        val enabled: Boolean = true,
        val config: String? = null,
        val tomcat: Tomcat = Tomcat(),
        val teeFilter: TeeFilter = TeeFilter(),
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

    /**
     * The properties for the Tomcat web server.
     *
     * @property requestAttributesEnabled
     *  Whether to enable request attributes to work with [RemoteIpValve].
     *  Defaults to the presence of [RemoteIpValve] enabled by the property "server.forward-headers-strategy=native".
     */
    data class Tomcat
    @JvmOverloads
    constructor(
            val requestAttributesEnabled: Boolean? = null,
    )

    /**
     * The properties for the tee filter.
     *
     * @property enabled
     *  Whether to enable the tee filter.
     * @property includes
     *  The host names to activate.
     * @property excludes
     *  The host names to deactivate.
     */
    data class TeeFilter
    @JvmOverloads
    constructor(
            val enabled: Boolean = false,
            val includes: String? = null,
            val excludes: String? = null,
    )

}
