package dev.akkinoc.spring.boot.logback.access.undertow

import ch.qos.logback.access.AccessConstants
import ch.qos.logback.access.servlet.Util.isFormUrlEncoded
import ch.qos.logback.access.servlet.Util.isImageResponse
import ch.qos.logback.access.spi.ServerAdapter
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEventSource
import dev.akkinoc.spring.boot.logback.access.security.LogbackAccessSecurityServletFilter
import dev.akkinoc.spring.boot.logback.access.value.LogbackAccessLocalPortStrategy
import io.undertow.server.HttpServerExchange
import io.undertow.servlet.handlers.ServletRequestContext
import java.lang.System.currentTimeMillis
import java.lang.System.nanoTime
import java.lang.Thread.currentThread
import java.net.URLEncoder.encode
import java.util.Collections.unmodifiableList
import java.util.Collections.unmodifiableMap
import java.util.concurrent.TimeUnit.NANOSECONDS
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * The Logback-access event source for the Undertow web server.
 *
 * @property exchange The request/response exchange.
 * @property localPortStrategy The strategy to change the behavior of [localPort].
 * @see ch.qos.logback.access.spi.AccessEvent
 * @see ch.qos.logback.access.PatternLayout
 * @see io.undertow.servlet.spec.HttpServletRequestImpl
 * @see io.undertow.server.handlers.accesslog.AccessLogHandler
 * @see io.undertow.attribute.ExchangeAttribute
 */
class LogbackAccessUndertowEventSource(
        private val exchange: HttpServerExchange,
        private val localPortStrategy: LogbackAccessLocalPortStrategy,
) : LogbackAccessEventSource() {

    override val request: HttpServletRequest? = run {
        val context = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY) ?: return@run null
        context.servletRequest as HttpServletRequest
    }

    override val response: HttpServletResponse? = run {
        val context = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY) ?: return@run null
        context.servletResponse as HttpServletResponse
    }

    override val serverAdapter: ServerAdapter? = null

    override val timeStamp: Long = currentTimeMillis()

    override val elapsedTime: Long? = run {
        val started = exchange.requestStartTime.takeIf { it != -1L } ?: return@run null
        val nanos = nanoTime() - started
        NANOSECONDS.toMillis(nanos)
    }

    override val threadName: String = currentThread().name

    override val serverName: String by lazy(LazyThreadSafetyMode.NONE) {
        exchange.hostName
    }

    override val localPort: Int by lazy(LazyThreadSafetyMode.NONE) {
        when (localPortStrategy) {
            LogbackAccessLocalPortStrategy.LOCAL -> exchange.destinationAddress.port
            LogbackAccessLocalPortStrategy.SERVER -> exchange.hostPort
        }
    }

    override val remoteAddr: String by lazy(LazyThreadSafetyMode.NONE) {
        val sourceAddr = exchange.sourceAddress
        val addr = sourceAddr.address ?: return@lazy sourceAddr.hostString
        addr.hostAddress
    }

    override val remoteHost: String by lazy(LazyThreadSafetyMode.NONE) {
        exchange.sourceAddress.hostString
    }

    override val remoteUser: String? by lazy(LazyThreadSafetyMode.NONE) {
        request?.getAttribute<String>(LogbackAccessSecurityServletFilter.REMOTE_USER_ATTRIBUTE)?.also { return@lazy it }
        val securityContext = exchange.securityContext ?: return@lazy null
        val account = securityContext.authenticatedAccount ?: return@lazy null
        account.principal.name
    }

    override val protocol: String by lazy(LazyThreadSafetyMode.NONE) {
        "${exchange.protocol}"
    }

    override val method: String by lazy(LazyThreadSafetyMode.NONE) {
        "${exchange.requestMethod}"
    }

    override val requestURI: String by lazy(LazyThreadSafetyMode.NONE) {
        request?.getAttribute<String>(RequestDispatcher.FORWARD_REQUEST_URI) ?: exchange.requestURI
    }

    override val queryString: String by lazy(LazyThreadSafetyMode.NONE) {
        val query = request?.getAttribute<String>(RequestDispatcher.FORWARD_QUERY_STRING) ?: exchange.queryString
        if (query.isEmpty()) "" else "?$query"
    }

    override val requestURL: String by lazy(LazyThreadSafetyMode.NONE) {
        "$method $requestURI$queryString $protocol"
    }

    override val requestHeaderMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val headers = sortedMapOf<String, String>(String.CASE_INSENSITIVE_ORDER)
        exchange.requestHeaders.associateTo(headers) { "${it.headerName}" to it.first }
        unmodifiableMap(headers)
    }

    override val cookieMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val cookies = linkedMapOf<String, String>()
        exchange.requestCookies().associateTo(cookies) { it.name to it.value }
        unmodifiableMap(cookies)
    }

    override val requestParameterMap: Map<String, List<String>> by lazy(LazyThreadSafetyMode.NONE) {
        val params = linkedMapOf<String, List<String>>()
        if (request != null) request.parameterMap.mapValuesTo(params) { unmodifiableList(it.value.asList()) }
        else exchange.queryParameters.mapValuesTo(params) { unmodifiableList(it.value.toList()) }
        unmodifiableMap(params)
    }

    override val attributeMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val attrs = linkedMapOf<String, String>()
        if (request != null) {
            request.attributeNames.asSequence()
                    .filter { it != AccessConstants.LB_INPUT_BUFFER }
                    .filter { it != AccessConstants.LB_OUTPUT_BUFFER }
                    .associateWithTo(attrs) { "${request.getAttribute<Any>(it)}" }
        }
        unmodifiableMap(attrs)
    }

    override val sessionID: String? by lazy(LazyThreadSafetyMode.NONE) {
        request?.getSession(false)?.id
    }

    override val requestContent: String? by lazy(LazyThreadSafetyMode.NONE) {
        val bytes = request?.getAttribute<ByteArray>(AccessConstants.LB_INPUT_BUFFER)
        if (bytes == null && request != null && isFormUrlEncoded(request)) {
            return@lazy requestParameterMap.asSequence()
                    .flatMap { (key, values) -> values.asSequence().map { key to it } }
                    .map { (key, value) -> encode(key, Charsets.UTF_8) to encode(value, Charsets.UTF_8) }
                    .joinToString("&") { (key, value) -> "$key=$value" }
        }
        bytes?.let { String(it, Charsets.UTF_8) }
    }

    override val statusCode: Int by lazy(LazyThreadSafetyMode.NONE) {
        exchange.statusCode
    }

    override val responseHeaderMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val headers = sortedMapOf<String, String>(String.CASE_INSENSITIVE_ORDER)
        exchange.responseHeaders.associateTo(headers) { "${it.headerName}" to it.first }
        unmodifiableMap(headers)
    }

    override val contentLength: Long by lazy(LazyThreadSafetyMode.NONE) {
        exchange.responseBytesSent
    }

    override val responseContent: String? by lazy(LazyThreadSafetyMode.NONE) {
        if (response != null && isImageResponse(response)) return@lazy "[IMAGE CONTENTS SUPPRESSED]"
        val bytes = request?.getAttribute<ByteArray>(AccessConstants.LB_OUTPUT_BUFFER)
        bytes?.let { String(it, Charsets.UTF_8) }
    }

    /**
     * Gets the request attribute.
     *
     * @receiver The request.
     * @param <T> The request attribute type.
     * @param name The request attribute name.
     * @return The request attribute value.
     */
    private inline fun <reified T> HttpServletRequest.getAttribute(name: String): T? {
        return getAttribute(name) as T?
    }

}
