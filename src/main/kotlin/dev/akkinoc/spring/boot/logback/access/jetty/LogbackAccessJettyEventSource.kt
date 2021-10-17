package dev.akkinoc.spring.boot.logback.access.jetty

import ch.qos.logback.access.AccessConstants.LB_INPUT_BUFFER
import ch.qos.logback.access.AccessConstants.LB_OUTPUT_BUFFER
import ch.qos.logback.access.jetty.JettyServerAdapter
import ch.qos.logback.access.servlet.Util.isFormUrlEncoded
import ch.qos.logback.access.servlet.Util.isImageResponse
import ch.qos.logback.access.spi.ServerAdapter
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEventSource
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import java.lang.String.CASE_INSENSITIVE_ORDER
import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.net.URLEncoder.encode
import java.util.Collections.unmodifiableList
import java.util.Collections.unmodifiableMap
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.text.Charsets.UTF_8

/**
 * The Logback-access event source for the Jetty web server.
 *
 * @see ch.qos.logback.access.spi.AccessEvent
 * @see ch.qos.logback.access.jetty.JettyServerAdapter
 * @see ch.qos.logback.access.PatternLayout
 * @see org.eclipse.jetty.server.CustomRequestLog
 */
class LogbackAccessJettyEventSource(
        override val request: Request,
        override val response: Response,
) : LogbackAccessEventSource() {

    override val serverAdapter: ServerAdapter = JettyServerAdapter(request, response)

    override val timeStamp: Long = currentTimeMillis()

    override val elapsedTime: Long = timeStamp - request.timeStamp

    override val threadName: String = currentThread().name

    override val serverName: String by lazy(NONE) {
        request.serverName
    }

    override val localPort: Int by lazy(NONE) {
        request.localPort
    }

    override val remoteAddr: String by lazy(NONE) {
        request.remoteAddr
    }

    override val remoteHost: String by lazy(NONE) {
        request.remoteHost
    }

    override val remoteUser: String? by lazy(NONE) {
        request.remoteUser
    }

    override val protocol: String by lazy(NONE) {
        request.protocol
    }

    override val method: String by lazy(NONE) {
        request.method
    }

    override val requestURI: String by lazy(NONE) {
        request.requestURI
    }

    override val queryString: String by lazy(NONE) {
        request.queryString?.let { "?$it" }.orEmpty()
    }

    override val requestURL: String by lazy(NONE) {
        "$method $requestURI$queryString $protocol"
    }

    override val requestHeaderMap: Map<String, String> by lazy(NONE) {
        val headers = sortedMapOf<String, String>(CASE_INSENSITIVE_ORDER)
        request.headerNames.asSequence().associateWithTo(headers) { request.getHeader(it) }
        unmodifiableMap(headers)
    }

    override val cookieMap: Map<String, String> by lazy(NONE) {
        val cookies = linkedMapOf<String, String>()
        request.cookies.orEmpty().associateTo(cookies) { it.name to it.value }
        unmodifiableMap(cookies)
    }

    override val requestParameterMap: Map<String, List<String>> by lazy(NONE) {
        val params = linkedMapOf<String, List<String>>()
        request.parameterMap.mapValuesTo(params) { unmodifiableList(it.value.asList()) }
        unmodifiableMap(params)
    }

    override val attributeMap: Map<String, String> by lazy(NONE) {
        val attrs = linkedMapOf<String, String>()
        request.attributeNames.asSequence()
                .filter { it !in setOf(LB_INPUT_BUFFER, LB_OUTPUT_BUFFER) }
                .associateWithTo(attrs) { "${request.getAttribute(it)}" }
        unmodifiableMap(attrs)
    }

    override val sessionID: String? by lazy(NONE) {
        request.getSession(false)?.id
    }

    override val requestContent: String? by lazy(NONE) {
        val bytes = request.getAttribute(LB_INPUT_BUFFER) as ByteArray?
        if (bytes == null && isFormUrlEncoded(request)) {
            return@lazy requestParameterMap.asSequence()
                    .flatMap { (key, values) -> values.asSequence().map { key to it } }
                    .map { (key, value) -> encode(key, UTF_8) to encode(value, UTF_8) }
                    .joinToString("&") { (key, value) -> "$key=$value" }
        }
        bytes?.let { String(it, UTF_8) }
    }

    override val statusCode: Int by lazy(NONE) {
        response.status
    }

    override val responseHeaderMap: Map<String, String> by lazy(NONE) {
        val headers = sortedMapOf<String, String>(CASE_INSENSITIVE_ORDER)
        response.headerNames.associateWithTo(headers) { response.getHeader(it) }
        unmodifiableMap(headers)
    }

    override val contentLength: Long by lazy(NONE) {
        response.contentCount
    }

    override val responseContent: String? by lazy(NONE) {
        if (isImageResponse(response)) return@lazy "[IMAGE CONTENTS SUPPRESSED]"
        val bytes = request.getAttribute(LB_OUTPUT_BUFFER) as ByteArray?
        bytes?.let { String(it, UTF_8) }
    }

}
