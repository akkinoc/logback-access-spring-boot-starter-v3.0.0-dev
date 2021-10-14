package dev.akkinoc.spring.boot.logback.access.tomcat

import ch.qos.logback.access.AccessConstants.LB_INPUT_BUFFER
import ch.qos.logback.access.AccessConstants.LB_OUTPUT_BUFFER
import ch.qos.logback.access.servlet.Util.isFormUrlEncoded
import ch.qos.logback.access.servlet.Util.isImageResponse
import ch.qos.logback.access.spi.ServerAdapter
import ch.qos.logback.access.tomcat.TomcatServerAdapter
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEventSource
import org.apache.catalina.connector.Request
import org.apache.catalina.connector.Response
import java.lang.String.CASE_INSENSITIVE_ORDER
import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.util.Collections.unmodifiableList
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
        val cookies = linkedMapOf<String, String>()
        request.cookies.orEmpty().associateTo(cookies) { it.name to it.value }
        unmodifiableMap(cookies)
    }

    override val requestParameterMap: Map<String, List<String>> by lazy {
        val params = linkedMapOf<String, List<String>>()
        request.parameterMap.mapValuesTo(params) { unmodifiableList(it.value.asList()) }
        unmodifiableMap(params)
    }

    override val attributeMap: Map<String, String> by lazy {
        val attrs = linkedMapOf<String, String>()
        request.attributeNames.asSequence()
                .filter { it !in setOf(LB_INPUT_BUFFER, LB_OUTPUT_BUFFER) }
                .associateWithTo(attrs) { "${request.getAttribute(it)}" }
        unmodifiableMap(attrs)
    }

    override val sessionID: String? by lazy {
        request.getSession(false)?.id
    }

    override val requestContent: String? by lazy {
        if (isFormUrlEncoded(request)) {
            return@lazy requestParameterMap.asSequence()
                    .flatMap { (key, values) -> values.asSequence().map { key to it } }
                    .joinToString("&") { (key, value) -> "$key=$value" }
        }
        val bytes = request.getAttribute(LB_INPUT_BUFFER) as ByteArray? ?: return@lazy null
        String(bytes)
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
        if (isImageResponse(response)) return@lazy "[IMAGE CONTENTS SUPPRESSED]"
        val bytes = request.getAttribute(LB_OUTPUT_BUFFER) as ByteArray? ?: return@lazy null
        String(bytes)
    }

}
