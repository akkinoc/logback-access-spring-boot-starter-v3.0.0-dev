package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.core.joran.action.Action
import ch.qos.logback.core.joran.spi.InterpretationContext
import org.springframework.core.env.Environment
import org.xml.sax.Attributes

/**
 * The Joran action to support `<springProperty>` tags.
 * Allows properties to be sourced from the environment.
 *
 * @property environment The environment.
 * @see org.springframework.boot.logging.logback.SpringPropertyAction
 */
class LogbackAccessJoranSpringPropertyAction(private val environment: Environment) : Action() {

    override fun begin(context: InterpretationContext, name: String, attributes: Attributes) {
    }

    override fun end(context: InterpretationContext, name: String) {
    }

}
