package dev.akkinoc.spring.boot.logback.access.undertow

import ch.qos.logback.access.spi.IAccessEvent
import io.undertow.attribute.RemoteHostAttribute
import io.undertow.attribute.RemoteUserAttribute
import io.undertow.server.HttpServerExchange
import io.undertow.servlet.handlers.ServletRequestContext
import java.lang.System.currentTimeMillis
import java.lang.System.nanoTime
import java.util.Enumeration
import java.util.concurrent.TimeUnit.NANOSECONDS
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import ch.qos.logback.access.spi.ServerAdapter as IServerAdapter

/**
 * The Logback-access event for the Undertow web server.
 *
 * This class was implemented with reference to:
 *
 * * [ch.qos.logback.access.spi.AccessEvent]
 * * [io.undertow.servlet.spec.HttpServletRequestImpl]
 * * [io.undertow.server.handlers.accesslog.AccessLogHandler]
 * * [io.undertow.attribute.ExchangeAttribute] subclasses
 *
 * @property exchange The request/response exchange.
 */
class LogbackAccessUndertowEvent(private val exchange: HttpServerExchange) : IAccessEvent {

    /**
     * The time in nanoseconds when the instance was created.
     */
    private val nanoTimestamp: Long = nanoTime()

    /**
     * @see getTimeStamp
     */
    private val timestamp: Long = currentTimeMillis()

    /**
     * @see getElapsedTime
     */
    private val elapsedTime: Long = exchange.requestStartTime
            .takeIf { it != -1L } // just in case the request start time is not recorded
            ?.let { nanoTimestamp - it }
            ?.takeIf { it >= 0 } // just in case the request start time is set to the future
            ?.let { it / 1_000_000 }
            ?: -1

    /**
     * @see getServerName
     */
    private val lazyServerName: String by lazy {
        exchange.hostName
    }

    /**
     * @see getLocalPort
     */
    private val lazyLocalPort: Int by lazy {
        exchange.destinationAddress.port
    }

    /**
     * @see getRemoteAddr
     */
    private val lazyRemoteAddr: String by lazy {
        exchange.sourceAddress.let { it.address?.hostAddress ?: it.hostString }
    }

    /**
     * @see getRemoteHost
     */
    private val lazyRemoteHost: String by lazy { RemoteHostAttribute.INSTANCE.readAttribute(exchange) }

    /**
     * @see getRemoteUser
     */
    private val lazyRemoteUser: String? by lazy { RemoteUserAttribute.INSTANCE.readAttribute(exchange) }

    override fun getRequest(): HttpServletRequest? {
        val context = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY) ?: return null
        return context.servletRequest as? HttpServletRequest
    }

    override fun getResponse(): HttpServletResponse? {
        val context = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY) ?: return null
        return context.servletResponse as? HttpServletResponse
    }

    override fun getTimeStamp(): Long = timestamp

    override fun getElapsedTime(): Long = elapsedTime

    override fun getElapsedSeconds(): Long = if (elapsedTime < 0) elapsedTime else elapsedTime / 1_000

    override fun getServerName(): String = lazyServerName

    override fun getLocalPort(): Int = lazyLocalPort

    override fun getRemoteAddr(): String = lazyRemoteAddr

    override fun getRemoteHost(): String = lazyRemoteHost

    override fun getRemoteUser(): String? = lazyRemoteUser

    override fun getRequestURI(): String {
        TODO("Not yet implemented")
    }

    override fun getRequestURL(): String {
        TODO("Not yet implemented")
    }

    override fun getProtocol(): String {
        TODO("Not yet implemented")
    }

    override fun getMethod(): String {
        TODO("Not yet implemented")
    }

    override fun getSessionID(): String {
        TODO("Not yet implemented")
    }

    override fun setThreadName(threadName: String?) {
        TODO("Not yet implemented")
    }

    override fun getThreadName(): String {
        TODO("Not yet implemented")
    }

    override fun getQueryString(): String {
        TODO("Not yet implemented")
    }

    override fun getRequestHeader(key: String?): String {
        TODO("Not yet implemented")
    }

    override fun getRequestHeaderNames(): Enumeration<String> {
        TODO("Not yet implemented")
    }

    override fun getRequestHeaderMap(): MutableMap<String, String> {
        TODO("Not yet implemented")
    }

    override fun getRequestParameterMap(): MutableMap<String, Array<String>> {
        TODO("Not yet implemented")
    }

    override fun getAttribute(key: String?): String {
        TODO("Not yet implemented")
    }

    override fun getRequestParameter(key: String?): Array<String> {
        TODO("Not yet implemented")
    }

    override fun getCookie(key: String?): String {
        TODO("Not yet implemented")
    }

    override fun getContentLength(): Long {
        TODO("Not yet implemented")
    }

    override fun getStatusCode(): Int {
        TODO("Not yet implemented")
    }

    override fun getRequestContent(): String {
        TODO("Not yet implemented")
    }

    override fun getResponseContent(): String {
        TODO("Not yet implemented")
    }

    override fun getServerAdapter(): ch.qos.logback.access.spi.ServerAdapter {
        TODO("Not yet implemented")
    }

    override fun getResponseHeader(key: String?): String {
        TODO("Not yet implemented")
    }

    override fun getResponseHeaderMap(): MutableMap<String, String> {
        TODO("Not yet implemented")
    }

    override fun getResponseHeaderNameList(): MutableList<String> {
        TODO("Not yet implemented")
    }

    override fun prepareForDeferredProcessing() {
        // TODO
        getRequestHeaderMap();
        getRequestParameterMap();
        getResponseHeaderMap();
        getLocalPort();
        getMethod();
        getProtocol();
        getRemoteAddr();
        getRemoteHost();
        getRemoteUser();
        getRequestURI();
        getRequestURL();
        getServerName();
        getTimeStamp();
        getElapsedTime();

        getStatusCode();
        getContentLength();
        getRequestContent();
        getResponseContent();

//        copyAttributeMap();
    }

    override fun toString(): String = "${this::class.simpleName}($requestURL $statusCode)"

    /**
     * The server adapter.
     *
     * @property exchange The request/response exchange.
     */
    class ServerAdapter(private val exchange: HttpServerExchange) : IServerAdapter {

        /**
         * The request timestamp.
         */
        private val requestTimestamp: Long = run {
            val currentTimeMillis = currentTimeMillis()
            val nanoTime = nanoTime()
            val requestStartTime = exchange.requestStartTime
            if (requestStartTime == -1L) return@run -1 // just in case the request start time was not recorded
            val nanos = nanoTime - requestStartTime
            currentTimeMillis - NANOSECONDS.toMillis(nanos)
        }

        override fun getRequestTimestamp(): Long = requestTimestamp

        override fun getStatusCode(): Int {
            return exchange.statusCode
        }

        override fun getContentLength(): Long {
            return exchange.responseBytesSent
        }

        override fun buildResponseHeaderMap(): Map<String, String?> {
            return exchange.responseHeaders.associate { "${it.headerName}" to it.firstOrNull() }
        }

    }

}
