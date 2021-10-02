package dev.akkinoc.spring.boot.logback.access

import ch.qos.logback.access.spi.IAccessEvent
import ch.qos.logback.access.spi.IAccessEvent.NA
import ch.qos.logback.access.spi.IAccessEvent.SENTINEL
import ch.qos.logback.access.spi.ServerAdapter
import java.io.Serializable
import java.util.Collections.enumeration
import java.util.Enumeration
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * The Logback-access event.
 *
 * @property source The Logback-access event source.
 * @see ch.qos.logback.access.spi.AccessEvent
 */
class LogbackAccessEvent(private var source: LogbackAccessEventSource) : IAccessEvent, Serializable {

    override fun getRequest(): HttpServletRequest? = source.request

    override fun getResponse(): HttpServletResponse? = source.response

    override fun getServerAdapter(): ServerAdapter? = source.serverAdapter

    override fun getTimeStamp(): Long = source.timeStamp

    override fun getElapsedTime(): Long = source.elapsedTime ?: SENTINEL.toLong()

    override fun getElapsedSeconds(): Long = source.elapsedTime?.let { MILLISECONDS.toSeconds(it) } ?: SENTINEL.toLong()

    override fun getThreadName(): String = source.threadName

    override fun setThreadName(value: String) = throw UnsupportedOperationException("Cannot change: $this")

    override fun getServerName(): String = source.serverName

    override fun getLocalPort(): Int = source.localPort

    override fun getRemoteAddr(): String = source.remoteAddr

    override fun getRemoteHost(): String = source.remoteHost

    override fun getRemoteUser(): String = source.remoteUser ?: NA

    override fun getProtocol(): String = source.protocol

    override fun getMethod(): String = source.method

    override fun getRequestURI(): String = source.requestURI

    override fun getQueryString(): String = source.queryString

    override fun getRequestURL(): String = source.requestURL

    override fun getRequestHeaderMap(): Map<String, String> = source.requestHeaderMap

    override fun getRequestHeaderNames(): Enumeration<String> = enumeration(source.requestHeaderMap.keys)

    override fun getRequestHeader(key: String): String = source.requestHeaderMap[key] ?: NA

    override fun getCookie(key: String): String = source.cookieMap[key] ?: NA

    override fun getRequestParameterMap(): Map<String, Array<String>> = source.requestParameterMap

    override fun getRequestParameter(key: String): Array<String> = source.requestParameterMap[key] ?: arrayOf(NA)

    override fun getAttribute(key: String): String = source.attributeMap[key] ?: NA

    override fun getSessionID(): String = source.sessionID ?: NA

    override fun getRequestContent(): String = source.requestContent ?: ""

    override fun getStatusCode(): Int = source.statusCode

    override fun getResponseHeaderMap(): Map<String, String> = source.responseHeaderMap

    override fun getResponseHeaderNameList(): List<String> = source.responseHeaderMap.keys.toList()

    override fun getResponseHeader(key: String): String = source.responseHeaderMap[key] ?: NA

    override fun getContentLength(): Long = source.contentLength

    override fun getResponseContent(): String = source.responseContent ?: ""

    override fun prepareForDeferredProcessing() {
        source = source.fix()
    }

    override fun toString(): String = "${this::class.simpleName}($requestURL $statusCode)"

}
