package dev.akkinoc.spring.boot.logback.access.jetty

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.RequestLog
import org.eclipse.jetty.server.Response

/**
 * The Jetty [RequestLog] to emit Logback-access events.
 *
 * @property logbackAccessContext The Logback-access context.
 * @see org.eclipse.jetty.server.CustomRequestLog
 * @see ch.qos.logback.access.jetty.RequestLogImpl
 */
class LogbackAccessJettyRequestLog(private val logbackAccessContext: LogbackAccessContext) : RequestLog {

    override fun log(request: Request, response: Response) {
        val source = LogbackAccessJettyEventSource(request, response)
        logbackAccessContext.emit(source)
    }

}
