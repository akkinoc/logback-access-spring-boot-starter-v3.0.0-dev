package dev.akkinoc.spring.boot.logback.access

import ch.qos.logback.access.spi.AccessContext
import ch.qos.logback.access.spi.IAccessEvent
import ch.qos.logback.core.spi.FilterReply.DENY
import dev.akkinoc.spring.boot.logback.access.LogbackAccessProperties.Companion.DEFAULT_CONFIGS
import dev.akkinoc.spring.boot.logback.access.LogbackAccessProperties.Companion.FALLBACK_CONFIG
import dev.akkinoc.spring.boot.logback.access.joran.LogbackAccessJoranConfigurator
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.core.env.Environment
import org.springframework.core.io.ResourceLoader

/**
 * The Logback-access context.
 *
 * @property logbackAccessProperties The configuration properties for Logback-access.
 * @param resourceLoader The resource loader.
 * @param environment The environment.
 */
class LogbackAccessContext(
        private val logbackAccessProperties: LogbackAccessProperties,
        resourceLoader: ResourceLoader,
        environment: Environment,
) : AutoCloseable {

    /**
     * The raw Logback-access context.
     */
    private val raw: AccessContext = AccessContext()

    init {
        val (name, resource) = logbackAccessProperties.config?.let { it to resourceLoader.getResource(it) }
                ?: DEFAULT_CONFIGS.asSequence()
                        .map { it to resourceLoader.getResource(it) }
                        .firstOrNull { (_, resource) -> resource.exists() }
                ?: FALLBACK_CONFIG.let { it to resourceLoader.getResource(it) }
        val configurator = LogbackAccessJoranConfigurator(environment)
        configurator.context = raw
        configurator.doConfigure(resource.url)
        raw.name = name
        raw.start()
        log.debug("Initialized the {}: {}", LogbackAccessContext::class.simpleName, this)
    }

    /**
     * Emits the Logback-access event.
     *
     * @param event The Logback-access event.
     */
    fun emit(event: IAccessEvent) {
        val filterReply = raw.getFilterChainDecision(event)
        log.debug("Emitting the {}: {} {} @{}", IAccessEvent::class.simpleName, filterReply, event, this)
        if (filterReply != DENY) raw.callAppenders(event)
    }

    override fun close() {
        log.debug("Closing the {}: {}", LogbackAccessContext::class.simpleName, this)
        raw.stop()
        raw.reset()
        raw.detachAndStopAllAppenders()
        raw.copyOfAttachedFiltersList.forEach { it.stop() }
        raw.clearAllFilters()
    }

    override fun toString(): String = "${this::class.simpleName}(${raw.name})"

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessContext::class.java)

    }

}
