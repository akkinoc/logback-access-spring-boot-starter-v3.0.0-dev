package dev.akkinoc.spring.boot.logback.access.test.mock

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpSession

/**
 * The mock controller for testing using the servlet web server.
 */
@RestController
@RequestMapping("/mock-controller")
class MockServletController {

    /**
     * Gets a text with a session.
     *
     * @param session The session.
     * @return A text.
     */
    @GetMapping("/text-with-session")
    fun getTextWithSession(session: HttpSession): String {
        val response = "mock-text"
        log.debug("Getting a text with a session: {}; {}", response, session)
        return response
    }

    /**
     * Gets a text with a forward response.
     *
     * @return A [ModelAndView] to return a text with a forward response.
     */
    @GetMapping("/text-with-forward")
    fun getTextWithForward(): ModelAndView {
        val response = ModelAndView("forward:text?forwarded=true")
        log.debug("Getting a text with a forward response: {}", response)
        return response
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(MockServletController::class.java)

    }

}
