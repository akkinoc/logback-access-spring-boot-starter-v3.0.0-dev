package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.access.joran.JoranConfigurator
import org.springframework.core.env.Environment

/**
 * The [JoranConfigurator] that supports additional rules.
 *
 * This class was implemented with reference to:
 *
 * * [org.springframework.boot.logging.logback.SpringBootJoranConfigurator]
 *
 * @property environment The environment.
 */
class LogbackAccessJoranConfigurator(private val environment: Environment) : JoranConfigurator()
