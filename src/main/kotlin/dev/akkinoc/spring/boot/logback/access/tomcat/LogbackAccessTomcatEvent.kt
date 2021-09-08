package dev.akkinoc.spring.boot.logback.access.tomcat

import ch.qos.logback.access.spi.AccessEvent
import ch.qos.logback.access.tomcat.TomcatServerAdapter
import org.apache.catalina.connector.Request
import org.apache.catalina.connector.Response

/**
 * The Logback-access event for the Tomcat web server.
 *
 * @param request The request.
 * @param response The response.
 */
class LogbackAccessTomcatEvent(request: Request, response: Response) :
        AccessEvent(request, response, ServerAdapter(request, response)) {

    override fun toString(): String = "${this::class.simpleName}($requestURL $statusCode)"

    /**
     * The server adapter.
     *
     * @param request The request.
     * @param response The response.
     */
    class ServerAdapter(request: Request, response: Response) : TomcatServerAdapter(request, response)

}
