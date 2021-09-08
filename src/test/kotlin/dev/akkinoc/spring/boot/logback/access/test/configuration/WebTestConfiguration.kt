package dev.akkinoc.spring.boot.logback.access.test.configuration

import dev.akkinoc.spring.boot.logback.access.test.mock.MockController
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

/**
 * The test configuration for testing using the web server.
 */
@TestConfiguration(proxyBeanMethods = false)
class WebTestConfiguration {

    /**
     * Provides the mock controller.
     *
     * @return The mock controller.
     */
    @Bean
    fun mockController(): MockController {
        val mockController = MockController()
        log.debug("Providing the {}: {}", MockController::class.simpleName, mockController)
        return mockController
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(WebTestConfiguration::class.java)

    }

}
