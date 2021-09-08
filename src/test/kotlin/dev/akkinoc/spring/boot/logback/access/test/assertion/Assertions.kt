package dev.akkinoc.spring.boot.logback.access.test.assertion

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
     * @param assert The assertion function that is called repeatedly.
     */
    fun assertLogbackAccessEvents(assert: () -> Unit) {
        runBlocking {
            eventually(duration = seconds(1), interval = milliseconds(100).fixed()) { assert() }
        }
    }

}
