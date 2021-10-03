package dev.akkinoc.spring.boot.logback.access.test.mock

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * The mock controller.
 */
@RestController
@RequestMapping("/mock-controller")
class MockController {

    /**
     * Gets a mock text.
     *
     * @return A mock text.
     */
    @GetMapping("/text")
    fun getText(): String {
        return "MOCK-TEXT"
    }

}
