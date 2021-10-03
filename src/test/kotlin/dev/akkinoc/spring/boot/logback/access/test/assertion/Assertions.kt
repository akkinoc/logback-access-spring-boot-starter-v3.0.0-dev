package dev.akkinoc.spring.boot.logback.access.test.assertion

import io.kotest.assertions.timing.EventuallyConfig
import io.kotest.assertions.timing.eventually
import io.kotest.assertions.until.fixed
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * The utility methods to support test assertions.
 */
object Assertions {

    /**
     * Asserts that the assertion block will pass within a short time.
     * It is used to assert Logback-access events that may be appended late.
     *
     * @param T The return value type of the assertion function.
     * @param assert The assertion function that is called repeatedly.
     * @return The return value of the assertion function.
     */
    fun <T> assertLogbackAccessEvents(assert: () -> T): T {
        return runBlocking {
            val config = EventuallyConfig(duration = seconds(1), interval = milliseconds(100).fixed())
            eventually(config) { assert() }
        }
    }

}
