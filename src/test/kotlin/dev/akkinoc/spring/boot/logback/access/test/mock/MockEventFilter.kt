package dev.akkinoc.spring.boot.logback.access.test.mock

import ch.qos.logback.access.spi.IAccessEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply
import ch.qos.logback.core.spi.FilterReply.NEUTRAL

/**
 * The mock event filter.
 * The filter reply can be overridden by request header.
 */
class MockEventFilter : Filter<IAccessEvent>() {

    /**
     * The request header name for overriding filter replies.
     */
    var requestHeaderName: String = "mock-event-filter-reply"

    override fun decide(event: IAccessEvent): FilterReply {
        val value = event.getRequestHeader(requestHeaderName)
        return FilterReply.values().find { it.name.equals(value, ignoreCase = true) } ?: NEUTRAL
    }

}
