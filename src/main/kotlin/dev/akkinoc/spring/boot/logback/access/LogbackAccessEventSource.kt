package dev.akkinoc.spring.boot.logback.access

import java.io.Serializable

/**
 * The Logback-access event source.
 */
interface LogbackAccessEventSource {

    /**
     * The value of [LogbackAccessEvent.getTimeStamp].
     */
    val timeStamp: Long

    /**
     * The value of [LogbackAccessEvent.getThreadName].
     */
    val threadName: String

    /**
     * The value of [LogbackAccessEvent.getServerName].
     */
    val serverName: String

    /**
     * The value of [LogbackAccessEvent.getLocalPort].
     */
    val localPort: Int

    /**
     * The value of [LogbackAccessEvent.getRemoteAddr].
     */
    val remoteAddr: String

    /**
     * The value of [LogbackAccessEvent.getRemoteHost].
     */
    val remoteHost: String

    /**
     * The value of [LogbackAccessEvent.getRemoteUser].
     */
    val remoteUser: String?

    /**
     * The value of [LogbackAccessEvent.getProtocol].
     */
    val protocol: String

    /**
     * The value of [LogbackAccessEvent.getMethod].
     */
    val method: String

    /**
     * The value of [LogbackAccessEvent.getRequestURI].
     */
    val requestURI: String

    /**
     * The value of [LogbackAccessEvent.getQueryString].
     */
    val queryString: String

    /**
     * The value of [LogbackAccessEvent.getRequestURL].
     */
    val requestURL: String

    /**
     * The value of [LogbackAccessEvent.getRequestHeaderMap].
     */
    val requestHeaderMap: Map<String, String>

    /**
     * The value of [LogbackAccessEvent.getCookie].
     */
    val cookieMap: Map<String, String>

    /**
     * The value of [LogbackAccessEvent.getElapsedTime].
     */
    val elapsedTime: Long?

    /**
     * Returns a serializable Logback-access event source with fixed evaluated values.
     *
     * @return A serializable Logback-access event source with fixed evaluated values.
     */
    fun fix(): LogbackAccessEventSource = Fixed(this)

    /**
     * The serializable Logback-access event source with fixed evaluated values.
     */
    private data class Fixed(
            override val timeStamp: Long,
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
            override val requestURL: String,
            override val requestHeaderMap: Map<String, String>,
            override val cookieMap: Map<String, String>,
            override val elapsedTime: Long?,
    ) : LogbackAccessEventSource, Serializable {

        constructor(source: LogbackAccessEventSource) : this(
                timeStamp = source.timeStamp,
                threadName = source.threadName,
                serverName = source.serverName,
                localPort = source.localPort,
                remoteAddr = source.remoteAddr,
                remoteHost = source.remoteHost,
                remoteUser = source.remoteUser,
                protocol = source.protocol,
                method = source.method,
                requestURI = source.requestURI,
                queryString = source.queryString,
                requestURL = source.requestURL,
                requestHeaderMap = source.requestHeaderMap,
                cookieMap = source.cookieMap,
                elapsedTime = source.elapsedTime,
        )

        override fun fix(): LogbackAccessEventSource = this

    }

}
