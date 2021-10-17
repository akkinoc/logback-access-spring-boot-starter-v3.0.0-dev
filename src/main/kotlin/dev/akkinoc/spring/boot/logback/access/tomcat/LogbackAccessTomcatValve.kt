package dev.akkinoc.spring.boot.logback.access.tomcat

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEvent
import org.apache.catalina.AccessLog
import org.apache.catalina.Valve
import org.apache.catalina.connector.Request
import org.apache.catalina.connector.Response
import org.apache.catalina.valves.RemoteIpValve
import org.apache.catalina.valves.ValveBase
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

/**
 * The Tomcat [Valve] to emit Logback-access events.
 *
 * @property logbackAccessContext The Logback-access context.
 * @see org.apache.catalina.valves.AccessLogValve
 * @see ch.qos.logback.access.tomcat.LogbackValve
 */
class LogbackAccessTomcatValve(
        private val logbackAccessContext: LogbackAccessContext,
) : ValveBase(true), AccessLog {

    /**
     * Whether to enable request attributes to work with [RemoteIpValve].
     */
    private var requestAttributesEnabled: Boolean = false

    override fun getRequestAttributesEnabled(): Boolean {
        return requestAttributesEnabled
    }

    override fun setRequestAttributesEnabled(value: Boolean) {
        requestAttributesEnabled = value
    }

    override fun initInternal() {
        super.initInternal()
        val props = logbackAccessContext.properties.tomcat
        val requestAttributesEnabled = props.requestAttributesEnabled
                ?: container.pipeline.valves.any { it is RemoteIpValve }
        setRequestAttributesEnabled(requestAttributesEnabled)
    }

    override fun invoke(request: Request, response: Response) {
        log.debug(
                "Handling the {}/{}: {} => {} @{}",
                Request::class.simpleName,
                Response::class.simpleName,
                request,
                response,
                logbackAccessContext,
        )
        getNext().invoke(request, response)
    }

    override fun log(request: Request, response: Response, time: Long) {
        log.debug(
                "Logging the {}/{}: {} => {} ({}ms) @{}",
                Request::class.simpleName,
                Response::class.simpleName,
                request,
                response,
                time,
                logbackAccessContext,
        )
        val source = LogbackAccessTomcatEventSource(request, response, getRequestAttributesEnabled())
        val event = LogbackAccessEvent(source)
        logbackAccessContext.emit(event)
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessTomcatValve::class.java)

    }

}
