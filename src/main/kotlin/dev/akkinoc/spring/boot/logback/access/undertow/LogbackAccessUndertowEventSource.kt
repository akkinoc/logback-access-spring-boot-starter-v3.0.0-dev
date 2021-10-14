package dev.akkinoc.spring.boot.logback.access.undertow

import ch.qos.logback.access.AccessConstants.LB_INPUT_BUFFER
import ch.qos.logback.access.AccessConstants.LB_OUTPUT_BUFFER
import ch.qos.logback.access.spi.ServerAdapter
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEventSource
import dev.akkinoc.spring.boot.logback.access.util.LogbackAccessEventSourceSupport.formatRequestContent
import dev.akkinoc.spring.boot.logback.access.util.LogbackAccessEventSourceSupport.formatResponseContent
import io.undertow.server.HttpServerExchange
import io.undertow.servlet.handlers.ServletRequestContext
import java.lang.String.CASE_INSENSITIVE_ORDER
import java.lang.System.currentTimeMillis
import java.lang.System.nanoTime
import java.lang.Thread.currentThread
import java.util.Collections.unmodifiableList
import java.util.Collections.unmodifiableMap
import java.util.concurrent.TimeUnit.NANOSECONDS
import javax.servlet.RequestDispatcher.FORWARD_QUERY_STRING
import javax.servlet.RequestDispatcher.FORWARD_REQUEST_URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * The Logback-access event source for the Undertow web server.
 *
 * @property exchange The request/response exchange.
 * @see ch.qos.logback.access.spi.AccessEvent
 * @see ch.qos.logback.access.PatternLayout
 * @see io.undertow.servlet.spec.HttpServletRequestImpl
 * @see io.undertow.server.handlers.accesslog.AccessLogHandler
 * @see io.undertow.attribute.ExchangeAttribute
 */
class LogbackAccessUndertowEventSource(
        private val exchange: HttpServerExchange,
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

    override val serverName: String by lazy {
        exchange.hostName
    }

    override val localPort: Int by lazy {
        exchange.destinationAddress.port
    }

    override val remoteAddr: String by lazy {
        val sourceAddr = exchange.sourceAddress
        val addr = sourceAddr.address ?: return@lazy sourceAddr.hostString
        addr.hostAddress
    }

    override val remoteHost: String by lazy {
        exchange.sourceAddress.hostString
    }

    override val remoteUser: String? by lazy {
        val securityContext = exchange.securityContext ?: return@lazy null
        val account = securityContext.authenticatedAccount ?: return@lazy null
        account.principal.name
    }

    override val protocol: String by lazy {
        "${exchange.protocol}"
    }

    override val method: String by lazy {
        "${exchange.requestMethod}"
    }

    override val requestURI: String by lazy {
        request?.getAttribute(FORWARD_REQUEST_URI) as String? ?: exchange.requestURI
    }

    override val queryString: String by lazy {
        val query = request?.getAttribute(FORWARD_QUERY_STRING) as String? ?: exchange.queryString
        if (query.isEmpty()) "" else "?$query"
    }

    override val requestURL: String by lazy {
        "$method $requestURI$queryString $protocol"
    }

    override val requestHeaderMap: Map<String, String> by lazy {
        val headers = sortedMapOf<String, String>(CASE_INSENSITIVE_ORDER)
        exchange.requestHeaders.associateTo(headers) { "${it.headerName}" to it.first }
        unmodifiableMap(headers)
    }

    override val cookieMap: Map<String, String> by lazy {
        val cookies = linkedMapOf<String, String>()
        exchange.requestCookies().associateTo(cookies) { it.name to it.value }
        unmodifiableMap(cookies)
    }

    override val requestParameterMap: Map<String, List<String>> by lazy {
        val params = linkedMapOf<String, List<String>>()
        if (request != null) request.parameterMap.mapValuesTo(params) { unmodifiableList(it.value.asList()) }
        else exchange.queryParameters.mapValuesTo(params) { unmodifiableList(it.value.toList()) }
        unmodifiableMap(params)
    }

    override val attributeMap: Map<String, String> by lazy {
        val attrs = linkedMapOf<String, String>()
        if (request != null) {
            request.attributeNames.asSequence()
                    .filter { it !in setOf(LB_INPUT_BUFFER, LB_OUTPUT_BUFFER) }
                    .associateWithTo(attrs) { "${request.getAttribute(it)}" }
        }
        unmodifiableMap(attrs)
    }

    override val sessionID: String? by lazy {
        request?.getSession(false)?.id
    }

    override val requestContent: String? by lazy {
        formatRequestContent(
                method = method,
                contentType = request?.contentType,
                params = requestParameterMap,
                bytes = request?.getAttribute(LB_INPUT_BUFFER) as ByteArray?,
        )
    }

    override val statusCode: Int by lazy {
        exchange.statusCode
    }

    override val responseHeaderMap: Map<String, String> by lazy {
        val headers = sortedMapOf<String, String>(CASE_INSENSITIVE_ORDER)
        exchange.responseHeaders.associateTo(headers) { "${it.headerName}" to it.first }
        unmodifiableMap(headers)
    }

    override val contentLength: Long by lazy {
        exchange.responseBytesSent
    }

    override val responseContent: String? by lazy {
        formatResponseContent(
                contentType = response?.contentType,
                bytes = request?.getAttribute(LB_OUTPUT_BUFFER) as ByteArray?,
        )
    }

}
