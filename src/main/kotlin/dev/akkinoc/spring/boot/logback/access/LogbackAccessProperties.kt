package dev.akkinoc.spring.boot.logback.access

import ch.qos.logback.access.spi.IAccessEvent
import org.apache.catalina.valves.RemoteIpValve
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import javax.servlet.ServletRequest

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
 * @property localPortStrategy
 *  The strategy to change the behavior of [IAccessEvent.getLocalPort].
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
        val localPortStrategy: LocalPortStrategy = LocalPortStrategy.SERVER,
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
     * The strategy to change the behavior of [IAccessEvent.getLocalPort].
     */
    enum class LocalPortStrategy {

        /**
         * Returns the port number of the interface on which the request was received.
         * If a servlet web server is used, this is the equivalent of [ServletRequest.getLocalPort].
         */
        LOCAL,

        /**
         * Returns the port number to which the request was sent.
         * If a servlet web server is used, this is the equivalent of [ServletRequest.getServerPort].
         * It helps to identify the destination port number used by the client when forward headers are enabled.
         */
        SERVER,

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
     *  By default, all hosts are activated.
     * @property excludes
     *  The host names to deactivate.
     *  By default, all hosts are activated.
     */
    data class TeeFilter
    @JvmOverloads
    constructor(
            val enabled: Boolean = false,
            val includes: String? = null,
            val excludes: String? = null,
    )

}
