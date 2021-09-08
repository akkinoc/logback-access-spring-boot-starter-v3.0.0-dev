package dev.akkinoc.spring.boot.logback.access.jetty

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.RequestLog
import org.eclipse.jetty.server.Response

/**
 * The Jetty [RequestLog] to emit Logback-access events.
 *
 * This class was implemented with reference to:
 *
 * * [org.eclipse.jetty.server.CustomRequestLog]
 * * [ch.qos.logback.access.jetty.RequestLogImpl]
 *
 * @property logbackAccessContext The Logback-access context.
 */
class LogbackAccessJettyRequestLog(private val logbackAccessContext: LogbackAccessContext) : RequestLog {

    override fun log(request: Request, response: Response) {
        val event = LogbackAccessJettyEvent(request, response)
        logbackAccessContext.emit(event)
    }

}
