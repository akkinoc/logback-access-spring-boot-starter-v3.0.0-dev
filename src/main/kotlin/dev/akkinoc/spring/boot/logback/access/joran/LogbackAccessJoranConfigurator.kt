package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.access.joran.JoranConfigurator
import org.springframework.core.env.Environment

/**
 * The [JoranConfigurator] that supports additional rules.
 *
 * @property environment The environment.
 * @see org.springframework.boot.logging.logback.SpringBootJoranConfigurator
 */
class LogbackAccessJoranConfigurator(private val environment: Environment) : JoranConfigurator()
