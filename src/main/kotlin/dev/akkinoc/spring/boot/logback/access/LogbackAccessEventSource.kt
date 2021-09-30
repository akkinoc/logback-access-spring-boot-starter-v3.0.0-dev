package dev.akkinoc.spring.boot.logback.access

import java.io.Serializable

/**
 * The Logback-access event source.
 */
interface LogbackAccessEventSource {

    /**
     * The value of [LogbackAccessEvent.getTimeStamp].
     */
    val timestamp: Long

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
     * The value of [LogbackAccessEvent.getElapsedTime].
     */
    val elapsedTime: Long?

    /**
     * The Logback-access event source fixed with evaluated values that can be serialized.
     */
    class Fixed(
            override val timestamp: Long,
            override val threadName: String,
            override val serverName: String,
            override val localPort: Int,
            override val remoteAddr: String,
            override val remoteHost: String,
            override val remoteUser: String?,
            override val elapsedTime: Long?,
    ) : LogbackAccessEventSource, Serializable {

        constructor(source: LogbackAccessEventSource) : this(
                timestamp = source.timestamp,
                threadName = source.threadName,
                serverName = source.serverName,
                localPort = source.localPort,
                remoteAddr = source.remoteAddr,
                remoteHost = source.remoteHost,
                remoteUser = source.remoteUser,
                elapsedTime = source.elapsedTime,
        )

    }

}
