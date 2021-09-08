package dev.akkinoc.spring.boot.logback.access.tomcat

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import org.apache.catalina.AccessLog
import org.apache.catalina.Valve
import org.apache.catalina.connector.Request
import org.apache.catalina.connector.Response
import org.apache.catalina.valves.ValveBase

/**
 * The Tomcat [Valve] to emit Logback-access events.
 *
 * This class was implemented with reference to:
 *
 * * [org.apache.catalina.valves.AccessLogValve]
 * * [ch.qos.logback.access.tomcat.LogbackValve]
 *
 * @property logbackAccessContext The Logback-access context.
 */
class LogbackAccessTomcatValve(private val logbackAccessContext: LogbackAccessContext) : ValveBase(true), AccessLog {

    /**
     * Whether to enable request attributes.
     */
    private var requestAttributesEnabled: Boolean = false

    override fun getRequestAttributesEnabled(): Boolean {
        return requestAttributesEnabled
    }

    override fun setRequestAttributesEnabled(value: Boolean) {
        requestAttributesEnabled = value
    }

    override fun invoke(request: Request, response: Response) {
        getNext().invoke(request, response)
    }

    override fun log(request: Request, response: Response, time: Long) {
        val event = LogbackAccessTomcatEvent(request, response)
        logbackAccessContext.emit(event)
    }

}
