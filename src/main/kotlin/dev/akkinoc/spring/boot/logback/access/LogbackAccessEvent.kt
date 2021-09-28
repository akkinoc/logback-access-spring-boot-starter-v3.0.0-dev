package dev.akkinoc.spring.boot.logback.access

import ch.qos.logback.access.spi.IAccessEvent
import ch.qos.logback.access.spi.IAccessEvent.NA
import ch.qos.logback.access.spi.IAccessEvent.SENTINEL
import ch.qos.logback.access.spi.ServerAdapter
import java.io.Serializable
import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * The Logback-access event.
 *
 * This class was implemented with reference to:
 *
 * * [ch.qos.logback.access.spi.AccessEvent]
 */
abstract class LogbackAccessEvent : IAccessEvent, Serializable {

    /**
     * The value of [getTimeStamp].
     */
    private val timestamp: Long = currentTimeMillis()

    /**
     * The value of [getThreadName].
     */
    private val threadName: String = currentThread().name

    /**
     * The value of [getServerName].
     */
    protected open val lazyServerName: String? by lazy { evaluateServerName() }

    override fun getRequest(): HttpServletRequest? = null

    override fun getResponse(): HttpServletResponse? = null

    override fun getServerAdapter(): ServerAdapter? = null

    override fun getTimeStamp(): Long = timestamp

    override fun getThreadName(): String = threadName

    override fun setThreadName(value: String) = throw UnsupportedOperationException("Cannot change: $this")

    /**
     * Evaluates the value of [getServerName].
     */
    protected open fun evaluateServerName(): String? = request?.serverName

    override fun getServerName(): String = lazyServerName ?: NA

    override fun getElapsedTime(): Long = SENTINEL.toLong()

    override fun getElapsedSeconds(): Long = elapsedTime.let { if (it >= 0L) MILLISECONDS.toSeconds(it) else it }

    override fun prepareForDeferredProcessing() {
        lazyServerName
    }

    override fun toString(): String = "${this::class.simpleName}($requestURL $statusCode)"

}
