package dev.akkinoc.spring.boot.logback.access.joran

import ch.qos.logback.core.joran.action.Action
import ch.qos.logback.core.joran.event.InPlayListener
import ch.qos.logback.core.joran.event.SaxEvent
import ch.qos.logback.core.joran.spi.InterpretationContext
import ch.qos.logback.core.util.OptionHelper.substVars
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.util.StringUtils.commaDelimitedListToStringArray
import org.springframework.util.StringUtils.trimArrayElements
import org.xml.sax.Attributes

/**
 * The Joran action to support `<springProfile>` tags.
 * Allows a section to only be enabled when a specific profile is active.
 *
 * @property environment The environment.
 * @see org.springframework.boot.logging.logback.SpringProfileAction
 */
class LogbackAccessJoranSpringProfileAction(private val environment: Environment) : Action(), InPlayListener {

    /**
     * The depth in parsing.
     */
    private var depth: Int = 0

    /**
     * Whether to accept the specified profile.
     */
    private var accepts: Boolean = false

    /**
     * The SAX events in parsing.
     */
    private var events: MutableList<SaxEvent> = mutableListOf()

    override fun begin(ic: InterpretationContext, elem: String, attrs: Attributes) {
        if (++depth != 1) return
        val name = attrs.getValue(NAME_ATTRIBUTE)
        val names = trimArrayElements(commaDelimitedListToStringArray(name))
        names.indices.forEach { names[it] = substVars(names[it], ic, context) }
        accepts = names.isNotEmpty() && environment.acceptsProfiles(Profiles.of(*names))
        ic.addInPlayListener(this)
    }

    override fun end(ic: InterpretationContext, elem: String) {
        if (--depth != 0) return
        ic.removeInPlayListener(this)
        if (accepts) {
            val events = events.subList(1, events.lastIndex)
            ic.joranInterpreter.eventPlayer.addEventsDynamically(events, 1)
        }
        events.clear()
    }

    override fun inPlay(event: SaxEvent) {
        events += event
    }

}
