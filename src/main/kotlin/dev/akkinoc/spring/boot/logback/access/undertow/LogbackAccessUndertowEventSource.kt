package dev.akkinoc.spring.boot.logback.access.undertow

import ch.qos.logback.access.spi.ServerAdapter
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEventSource
import io.undertow.attribute.BytesSentAttribute
import io.undertow.attribute.ResponseCodeAttribute
import io.undertow.server.HttpServerExchange
import io.undertow.servlet.attribute.ServletSessionIdAttribute
import io.undertow.servlet.handlers.ServletRequestContext
import java.lang.String.CASE_INSENSITIVE_ORDER
import java.lang.System.currentTimeMillis
import java.lang.System.nanoTime
import java.lang.Thread.currentThread
import java.util.Collections.unmodifiableMap
import java.util.concurrent.TimeUnit.NANOSECONDS
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
        exchange.protocol.toString()
    }

    override val method: String by lazy {
        exchange.requestMethod.toString()
    }

    override val requestURI: String by lazy {
        exchange.requestURI
    }

    override val queryString: String by lazy {
        val query = exchange.queryString.ifEmpty { return@lazy "" }
        "?$query"
    }

    override val requestURL: String by lazy {
        "$method $requestURI$queryString $protocol"
    }

    override val requestHeaderMap: Map<String, String> by lazy {
        val headers = sortedMapOf<String, String>(CASE_INSENSITIVE_ORDER)
        exchange.requestHeaders.associateTo(headers) { it.headerName.toString() to it.first }
        unmodifiableMap(headers)
    }

    override val cookieMap: Map<String, String> by lazy {
        val cookies = exchange.requestCookies().associate { it.name to it.value }
        unmodifiableMap(cookies)
    }

    override val requestParameterMap: Map<String, List<String>> by lazy {
        val parameters = exchange.queryParameters.mapValues { it.value.toList() }
        unmodifiableMap(parameters)
    }

    override val attributeMap: Map<String, String> by lazy {
        emptyMap()
    }

    override val sessionID: String? by lazy {
        ServletSessionIdAttribute.INSTANCE.readAttribute(exchange)
    }

    override val requestContent: String? by lazy {
        null
    }

    override val statusCode: Int by lazy {
        ResponseCodeAttribute.INSTANCE.readAttribute(exchange).toInt()
    }

    override val responseHeaderMap: Map<String, String> by lazy {
        val headers = sortedMapOf<String, String>(CASE_INSENSITIVE_ORDER)
        exchange.responseHeaders.associateTo(headers) { it.headerName.toString() to it.first }
        unmodifiableMap(headers)
    }

    override val contentLength: Long by lazy {
        BytesSentAttribute(false).readAttribute(exchange).toLong()
    }

    override val responseContent: String? by lazy {
        null
    }

}
