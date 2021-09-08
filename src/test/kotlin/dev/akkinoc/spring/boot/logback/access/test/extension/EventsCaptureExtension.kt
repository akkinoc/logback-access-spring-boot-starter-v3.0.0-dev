package dev.akkinoc.spring.boot.logback.access.test.extension

import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCaptureAppender.Companion.captures
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

/**
 * The test extension to capture Logback-access events that will be appended.
 * To use it, include [EventsCaptureAppender] in the Logback-access configuration.
 * The captured Logback-access events can be obtained from a [EventsCapture] test parameter.
 */
class EventsCaptureExtension :
        BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback, ParameterResolver {

    override fun beforeAll(context: ExtensionContext) {
        val id = context.uniqueId
        captures[id] = context.getAccessEventsCapture()
        log.debug("Started the {}: {} @{}", EventsCapture::class.simpleName, id, context)
    }

    override fun afterAll(context: ExtensionContext) {
        val id = context.uniqueId
        log.debug("Ending the {}: {} @{}", EventsCapture::class.simpleName, id, context)
        captures -= id
    }

    override fun beforeEach(context: ExtensionContext) {
        val id = context.uniqueId
        captures[id] = context.getAccessEventsCapture()
        log.debug("Started the {}: {} @{}", EventsCapture::class.simpleName, id, context)
    }

    override fun afterEach(context: ExtensionContext) {
        val id = context.uniqueId
        log.debug("Ending the {}: {} @{}", EventsCapture::class.simpleName, id, context)
        captures -= id
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == EventsCapture::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return extensionContext.getAccessEventsCapture()
    }

    /**
     * Retrieves the Logback-access events capture that is present in the extension context.
     * If it is not present, creates, saves, and returns it.
     *
     * @receiver The extension context.
     * @return The Logback-access events capture.
     */
    private fun ExtensionContext.getAccessEventsCapture(): EventsCapture {
        val namespace = Namespace.create(this::class)
        val store = getStore(namespace)
        return store.getOrComputeIfAbsent(EventsCapture::class.java)
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(EventsCaptureExtension::class.java)

    }

}
