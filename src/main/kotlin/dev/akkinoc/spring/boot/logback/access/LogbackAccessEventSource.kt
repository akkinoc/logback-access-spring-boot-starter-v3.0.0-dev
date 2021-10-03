package dev.akkinoc.spring.boot.logback.access

import ch.qos.logback.access.spi.ServerAdapter
import java.io.Serializable
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * The Logback-access event source.
 *
 * @see LogbackAccessEvent
 */
abstract class LogbackAccessEventSource {

    /**
     * The value of [LogbackAccessEvent.getRequest].
     */
    abstract val request: HttpServletRequest?

    /**
     * The value of [LogbackAccessEvent.getResponse].
     */
    abstract val response: HttpServletResponse?

    /**
     * The value of [LogbackAccessEvent.getServerAdapter].
     */
    abstract val serverAdapter: ServerAdapter?

    /**
     * The value of [LogbackAccessEvent.getTimeStamp].
     */
    abstract val timeStamp: Long

    /**
     * The value of [LogbackAccessEvent.getElapsedTime] and [LogbackAccessEvent.getElapsedSeconds].
     */
    abstract val elapsedTime: Long

    /**
     * The value of [LogbackAccessEvent.getThreadName].
     */
    abstract val threadName: String

    /**
     * The value of [LogbackAccessEvent.getServerName].
     */
    abstract val serverName: String

    /**
     * The value of [LogbackAccessEvent.getLocalPort].
     */
    abstract val localPort: Int

    /**
     * The value of [LogbackAccessEvent.getRemoteAddr].
     */
    abstract val remoteAddr: String

    /**
     * The value of [LogbackAccessEvent.getRemoteHost].
     */
    abstract val remoteHost: String

    /**
     * The value of [LogbackAccessEvent.getRemoteUser].
     */
    abstract val remoteUser: String?

    /**
     * The value of [LogbackAccessEvent.getProtocol].
     */
    abstract val protocol: String

    /**
     * The value of [LogbackAccessEvent.getMethod].
     */
    abstract val method: String

    /**
     * The value of [LogbackAccessEvent.getRequestURI].
     */
    abstract val requestURI: String

    /**
     * The value of [LogbackAccessEvent.getQueryString].
     */
    abstract val queryString: String

    /**
     * The value of [LogbackAccessEvent.getRequestURL].
     */
    abstract val requestURL: String

    /**
     * The value of [LogbackAccessEvent.getRequestHeaderMap],
     * [LogbackAccessEvent.getRequestHeaderNames] and [LogbackAccessEvent.getRequestHeader].
     */
    abstract val requestHeaderMap: Map<String, String>

    /**
     * The value of [LogbackAccessEvent.getCookie].
     */
    abstract val cookieMap: Map<String, String>

    /**
     * The value of [LogbackAccessEvent.getRequestParameterMap] and [LogbackAccessEvent.getRequestParameter].
     */
    abstract val requestParameterMap: Map<String, List<String>>

    /**
     * The value of [LogbackAccessEvent.getAttribute].
     */
    abstract val attributeMap: Map<String, String>

    /**
     * The value of [LogbackAccessEvent.getSessionID].
     */
    abstract val sessionID: String?

    /**
     * The value of [LogbackAccessEvent.getRequestContent].
     */
    abstract val requestContent: String?

    /**
     * The value of [LogbackAccessEvent.getStatusCode].
     */
    abstract val statusCode: Int

    /**
     * The value of [LogbackAccessEvent.getResponseHeaderMap],
     * [LogbackAccessEvent.getResponseHeaderNameList] and [LogbackAccessEvent.getResponseHeader].
     */
    abstract val responseHeaderMap: Map<String, String>

    /**
     * The value of [LogbackAccessEvent.getContentLength].
     */
    abstract val contentLength: Long

    /**
     * The value of [LogbackAccessEvent.getResponseContent].
     */
    abstract val responseContent: String?

    /**
     * Returns a serializable Logback-access event source with fixed evaluated values.
     *
     * @return A serializable Logback-access event source with fixed evaluated values.
     */
    open fun fix(): LogbackAccessEventSource {
        return Fixed(this)
    }

    /**
     * The serializable Logback-access event source with fixed evaluated values.
     *
     * @param source The Logback-access event source.
     */
    private class Fixed(source: LogbackAccessEventSource) : LogbackAccessEventSource(), Serializable {

        @Transient
        override val request: HttpServletRequest? = source.request

        @Transient
        override val response: HttpServletResponse? = source.response

        @Transient
        override val serverAdapter: ServerAdapter? = source.serverAdapter

        override val timeStamp: Long = source.timeStamp

        override val elapsedTime: Long = source.elapsedTime

        override val threadName: String = source.threadName

        override val serverName: String = source.serverName

        override val localPort: Int = source.localPort

        override val remoteAddr: String = source.remoteAddr

        override val remoteHost: String = source.remoteHost

        override val remoteUser: String? = source.remoteUser

        override val protocol: String = source.protocol

        override val method: String = source.method

        override val requestURI: String = source.requestURI

        override val queryString: String = source.queryString

        override val requestURL: String = source.requestURL

        override val requestHeaderMap: Map<String, String> = source.requestHeaderMap

        override val cookieMap: Map<String, String> = source.cookieMap

        override val requestParameterMap: Map<String, List<String>> = source.requestParameterMap

        override val attributeMap: Map<String, String> = source.attributeMap

        override val sessionID: String? = source.sessionID

        override val requestContent: String? = source.requestContent

        override val statusCode: Int = source.statusCode

        override val responseHeaderMap: Map<String, String> = source.responseHeaderMap

        override val contentLength: Long = source.contentLength

        override val responseContent: String? = source.responseContent

        override fun fix(): LogbackAccessEventSource = this

    }

}
