package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.core.joran.action.Action
import ch.qos.logback.core.joran.spi.InterpretationContext
import org.xml.sax.Attributes

/**
 * The Joran action to support `<springProperty>` tags.
 * Allows properties to be sourced from the environment.
 *
 * @see org.springframework.boot.logging.logback.SpringPropertyAction
 */
class LogbackAccessJoranSpringPropertyAction : Action() {

    override fun begin(context: InterpretationContext, name: String, attributes: Attributes) {
    }

    override fun end(context: InterpretationContext, name: String) {
    }

}
