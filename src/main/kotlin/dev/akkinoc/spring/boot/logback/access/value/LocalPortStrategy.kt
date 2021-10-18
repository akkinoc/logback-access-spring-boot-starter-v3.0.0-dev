package dev.akkinoc.spring.boot.logback.access.value

import ch.qos.logback.access.spi.IAccessEvent
import javax.servlet.ServletRequest

/**
 * The strategy to change the behavior of [IAccessEvent.getLocalPort].
 */
enum class LocalPortStrategy {

    /**
     * Returns the port number of the interface on which the request was received.
     * If a servlet web server is used, this is the equivalent of [ServletRequest.getLocalPort].
     */
    LOCAL,

    /**
     * Returns the port number to which the request was sent.
     * If a servlet web server is used, this is the equivalent of [ServletRequest.getServerPort].
     * It helps to identify the destination port number used by the client when forward headers are enabled.
     */
    SERVER,

}
