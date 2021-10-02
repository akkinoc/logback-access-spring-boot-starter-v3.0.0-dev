package dev.akkinoc.spring.boot.logback.access

import java.io.Serializable

/**
 * The Logback-access event source.
 */
abstract class LogbackAccessEventSource {

    /**
     * The value of [LogbackAccessEvent.getTimeStamp].
     */
    abstract val timeStamp: Long

    /**
     * The value of [LogbackAccessEvent.getElapsedTime] and [LogbackAccessEvent.getElapsedSeconds].
     */
    abstract val elapsedTime: Long?

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
    open val requestURL: String
        get() = "$method $requestURI$queryString $protocol"

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
    abstract val requestParameterMap: Map<String, Array<String>>

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
    abstract val contentLength: Long?

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
        return Fixed(
                timeStamp = timeStamp,
                elapsedTime = elapsedTime,
                threadName = threadName,
                serverName = serverName,
                localPort = localPort,
                remoteAddr = remoteAddr,
                remoteHost = remoteHost,
                remoteUser = remoteUser,
                protocol = protocol,
                method = method,
                requestURI = requestURI,
                queryString = queryString,
                requestHeaderMap = requestHeaderMap,
                cookieMap = cookieMap,
                requestParameterMap = requestParameterMap,
                attributeMap = attributeMap,
                sessionID = sessionID,
                requestContent = requestContent,
                statusCode = statusCode,
                responseHeaderMap = responseHeaderMap,
                contentLength = contentLength,
                responseContent = responseContent,
        )
    }

    override fun toString(): String = "${this::class.simpleName}($requestURL $statusCode)"

    /**
     * The serializable Logback-access event source with fixed evaluated values.
     */
    private data class Fixed(
            override val timeStamp: Long,
            override val elapsedTime: Long?,
            override val threadName: String,
            override val serverName: String,
            override val localPort: Int,
            override val remoteAddr: String,
            override val remoteHost: String,
            override val remoteUser: String?,
            override val protocol: String,
            override val method: String,
            override val requestURI: String,
            override val queryString: String,
            override val requestHeaderMap: Map<String, String>,
            override val cookieMap: Map<String, String>,
            override val requestParameterMap: Map<String, Array<String>>,
            override val attributeMap: Map<String, String>,
            override val sessionID: String?,
            override val requestContent: String?,
            override val statusCode: Int,
            override val responseHeaderMap: Map<String, String>,
            override val contentLength: Long?,
            override val responseContent: String?,
    ) : LogbackAccessEventSource(), Serializable {

        override fun fix(): LogbackAccessEventSource = this

    }

}
