package dev.akkinoc.spring.boot.logback.access.undertow

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import io.undertow.server.ExchangeCompletionListener.NextListener
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange

/**
 * The Undertow [HttpHandler] to emit Logback-access events.
 *
 * @property logbackAccessContext The Logback-access context.
 * @property next The next [HttpHandler].
 * @see io.undertow.server.handlers.accesslog.AccessLogHandler
 */
class LogbackAccessUndertowHttpHandler(
        private val logbackAccessContext: LogbackAccessContext,
        private val next: HttpHandler,
) : HttpHandler {

    override fun handleRequest(exchange: HttpServerExchange) {
        exchange.addExchangeCompleteListener(::emit)
        next.handleRequest(exchange)
    }

    /**
     * Emits the Logback-access event.
     *
     * @param exchange The request/response exchange.
     * @param next The next listener.
     */
    private fun emit(exchange: HttpServerExchange, next: NextListener) {
        val source = LogbackAccessUndertowEventSource(exchange)
        logbackAccessContext.emit(source)
        next.proceed()
    }

}
