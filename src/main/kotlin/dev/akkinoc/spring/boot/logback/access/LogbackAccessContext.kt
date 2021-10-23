package dev.akkinoc.spring.boot.logback.access

import ch.qos.logback.access.spi.AccessContext
import ch.qos.logback.core.spi.FilterReply.DENY
import ch.qos.logback.core.status.Status
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
 * @property properties The configuration properties for Logback-access.
 * @param resourceLoader The resource loader.
 * @param environment The environment.
 */
class LogbackAccessContext(
        val properties: LogbackAccessProperties,
        resourceLoader: ResourceLoader,
        environment: Environment,
) : AutoCloseable {

    /**
     * The raw Logback-access context.
     */
    private val raw: AccessContext = AccessContext()

    init {
        val (name, resource) = properties.config?.let { it to resourceLoader.getResource(it) }
                ?: DEFAULT_CONFIGS.asSequence()
                        .map { it to resourceLoader.getResource(it) }
                        .firstOrNull { (_, resource) -> resource.exists() }
                ?: FALLBACK_CONFIG.let { it to resourceLoader.getResource(it) }
        raw.name = name
        raw.statusManager.add(::log)
        val configurator = LogbackAccessJoranConfigurator(environment)
        configurator.context = raw
        configurator.doConfigure(resource.url)
        raw.start()
        log.debug("Initialized the {}: {}", LogbackAccessContext::class.simpleName, this)
    }

    /**
     * Logs the Logback-access status.
     *
     * @param status The Logback-access status.
     */
    private fun log(status: Status) {
        log.debug("Added the {}: {} @{}", Status::class.simpleName, status, this, status.throwable)
    }

    /**
     * Emits the Logback-access event.
     *
     * @param event The Logback-access event.
     */
    fun emit(event: LogbackAccessEvent) {
        val filterReply = raw.getFilterChainDecision(event)
        log.debug("Emitting the {}: {} {} @{}", LogbackAccessEvent::class.simpleName, filterReply, event, this)
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
