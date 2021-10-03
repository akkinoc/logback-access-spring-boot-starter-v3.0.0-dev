package dev.akkinoc.spring.boot.logback.access.jetty

import ch.qos.logback.access.jetty.JettyServerAdapter
import ch.qos.logback.access.spi.AccessEvent
import ch.qos.logback.access.spi.ServerAdapter
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEventSource
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread

/**
 * The Logback-access event source for the Jetty web server.
 *
 * @see ch.qos.logback.access.spi.AccessEvent
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

    override val serverName: String by lazy {
        request.serverName
    }

    override val localPort: Int by lazy {
        request.localPort
    }

    /**
     * TODO: 後で使わなくする
     */
    private val delegate: AccessEvent = AccessEvent(request, response, serverAdapter)

    override val remoteAddr: String by lazy {
        delegate.remoteAddr
    }

    override val remoteHost: String by lazy {
        delegate.remoteHost
    }

    override val remoteUser: String? by lazy {
        delegate.remoteUser
    }

    override val protocol: String by lazy {
        delegate.protocol
    }

    override val method: String by lazy {
        delegate.method
    }

    override val requestURI: String by lazy {
        delegate.requestURI
    }

    override val queryString: String by lazy {
        delegate.queryString
    }

    override val requestURL: String by lazy {
        delegate.requestURL
    }

    override val requestHeaderMap: Map<String, String> by lazy {
        delegate.requestHeaderMap
    }

    override val cookieMap: Map<String, String> by lazy {
        emptyMap()
    }

    override val requestParameterMap: Map<String, List<String>> by lazy {
        emptyMap()
    }

    override val attributeMap: Map<String, String> by lazy {
        emptyMap()
    }

    override val sessionID: String? by lazy {
        null
    }

    override val requestContent: String? by lazy {
        null
    }

    override val statusCode: Int by lazy {
        delegate.statusCode
    }

    override val responseHeaderMap: Map<String, String> by lazy {
        emptyMap()
    }

    override val contentLength: Long by lazy {
        delegate.contentLength
    }

    override val responseContent: String? by lazy {
        null
    }

}
