package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.core.joran.action.Action
import ch.qos.logback.core.joran.event.InPlayListener
import ch.qos.logback.core.joran.event.SaxEvent
import ch.qos.logback.core.joran.spi.InterpretationContext
import org.xml.sax.Attributes

/**
 * The Joran action to support `<springProfile>` tags.
 * Allows a section to only be enabled when a specific profile is active.
 *
 * @see org.springframework.boot.logging.logback.SpringProfileAction
 */
class LogbackAccessJoranSpringProfileAction : Action(), InPlayListener {

    override fun begin(context: InterpretationContext, name: String, attributes: Attributes) {
    }

    override fun inPlay(event: SaxEvent) {
    }

    override fun end(context: InterpretationContext, name: String) {
    }

}
