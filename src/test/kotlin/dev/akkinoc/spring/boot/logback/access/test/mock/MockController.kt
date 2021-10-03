package dev.akkinoc.spring.boot.logback.access.test.mock

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Thread.currentThread

/**
 * The mock controller.
 */
@RestController
@RequestMapping("/mock")
class MockController {

    /**
     * Gets a mock text.
     *
     * @return A mock text.
     */
    @GetMapping("/text")
    fun getText(): ResponseEntity<String> {
        return ResponseEntity.ok()
                .header("TEST-THREAD-NAME", currentThread().name)
                .body("MOCK-TEXT")
    }

}
