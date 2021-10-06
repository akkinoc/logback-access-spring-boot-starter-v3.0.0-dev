package dev.akkinoc.spring.boot.logback.access.tomcat

import ch.qos.logback.access.spi.ServerAdapter
import ch.qos.logback.access.tomcat.TomcatServerAdapter
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEventSource
import org.apache.catalina.connector.Request
import org.apache.catalina.connector.Response
import java.lang.String.CASE_INSENSITIVE_ORDER
import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.util.Collections.unmodifiableMap

/**
 * The Logback-access event source for the Tomcat web server.
 *
 * @see ch.qos.logback.access.spi.AccessEvent
 * @see ch.qos.logback.access.tomcat.TomcatServerAdapter
 * @see ch.qos.logback.access.PatternLayout
 * @see org.apache.catalina.valves.AccessLogValve
 * @see org.apache.catalina.valves.AbstractAccessLogValve
 * @see org.apache.catalina.valves.AbstractAccessLogValve.AccessLogElement
 */
class LogbackAccessTomcatEventSource(
        override val request: Request,
        override val response: Response,
) : LogbackAccessEventSource() {

    override val serverAdapter: ServerAdapter = TomcatServerAdapter(request, response)

    override val timeStamp: Long = currentTimeMillis()

    override val elapsedTime: Long = timeStamp - request.coyoteRequest.startTime

    override val threadName: String = currentThread().name

    override val serverName: String by lazy {
        request.serverName
    }

    override val localPort: Int by lazy {
        request.localPort
    }

    override val remoteAddr: String by lazy {
        request.remoteAddr
    }

    override val remoteHost: String by lazy {
        request.remoteHost
    }

    override val remoteUser: String? by lazy {
        request.remoteUser
    }

    override val protocol: String by lazy {
        request.protocol
    }

    override val method: String by lazy {
        request.method
    }

    override val requestURI: String by lazy {
        request.requestURI
    }

    override val queryString: String by lazy {
        val query = request.queryString ?: return@lazy ""
        "?$query"
    }

    override val requestURL: String by lazy {
        "$method $requestURI$queryString $protocol"
    }

    override val requestHeaderMap: Map<String, String> by lazy {
        val headers = sortedMapOf<String, String>(CASE_INSENSITIVE_ORDER)
        request.headerNames.asSequence().associateWithTo(headers) { request.getHeader(it) }
        unmodifiableMap(headers)
    }

    override val cookieMap: Map<String, String> by lazy {
        // TODO
        emptyMap()
    }

    override val requestParameterMap: Map<String, List<String>> by lazy {
        // TODO
        emptyMap()
    }

    override val attributeMap: Map<String, String> by lazy {
        // TODO
        emptyMap()
    }

    override val sessionID: String? by lazy {
        // TODO
        null
    }

    override val requestContent: String? by lazy {
        // TODO
        null
    }

    override val statusCode: Int by lazy {
        response.status
    }

    override val responseHeaderMap: Map<String, String> by lazy {
        val headers = sortedMapOf<String, String>(CASE_INSENSITIVE_ORDER)
        response.headerNames.associateWithTo(headers) { response.getHeader(it) }
        unmodifiableMap(headers)
    }

    override val contentLength: Long by lazy {
        response.getBytesWritten(false)
    }

    override val responseContent: String? by lazy {
        // TODO
        null
    }

}
