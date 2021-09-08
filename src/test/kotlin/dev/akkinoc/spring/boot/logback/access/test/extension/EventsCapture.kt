package dev.akkinoc.spring.boot.logback.access.test.extension

import ch.qos.logback.access.spi.IAccessEvent
import java.util.concurrent.CopyOnWriteArrayList

/**
 * The Logback-access events capture.
 */
class EventsCapture : MutableList<IAccessEvent> by CopyOnWriteArrayList()
