package dev.akkinoc.spring.boot.logback.access.jetty

import ch.qos.logback.access.jetty.JettyServerAdapter
import ch.qos.logback.access.spi.AccessEvent
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response

/**
 * The Logback-access event for the Jetty web server.
 *
 * @param request The request.
 * @param response The response.
 */
class LogbackAccessJettyEvent(request: Request, response: Response) :
        AccessEvent(request, response, ServerAdapter(request, response)) {

    override fun toString(): String = "${this::class.simpleName}($requestURL $statusCode)"

    /**
     * The server adapter.
     *
     * @param request The request.
     * @param response The response.
     */
    class ServerAdapter(request: Request, response: Response) : JettyServerAdapter(request, response)

}
