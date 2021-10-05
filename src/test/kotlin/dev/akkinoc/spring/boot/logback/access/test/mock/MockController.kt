package dev.akkinoc.spring.boot.logback.access.test.mock

import org.springframework.http.ResponseEntity
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

    /**
     * Gets a mock text with response headers.
     *
     * @return A mock text with response headers.
     */
    @GetMapping("/text-with-response-headers")
    fun getTextWithResponseHeaders(): ResponseEntity<String> {
        return ResponseEntity.ok()
                .header("MOCK-RESPONSE-HEADER1", "MOCK-RESPONSE-HEADER1-VALUE")
                .header("MOCK-RESPONSE-HEADER2", "MOCK-RESPONSE-HEADER2-VALUE1", "MOCK-RESPONSE-HEADER2-VALUE2")
                .body("MOCK-TEXT-WITH-RESPONSE-HEADERS")
    }

}
