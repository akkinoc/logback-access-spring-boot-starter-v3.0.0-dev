package dev.akkinoc.spring.boot.logback.access.test.mock

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.supplyAsync

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
        return "mock-text"
    }

    /**
     * Gets a mock text with response headers.
     *
     * @return A [ResponseEntity] to return a mock text with response headers.
     */
    @GetMapping("/text-with-response-headers")
    fun getTextWithResponseHeaders(): ResponseEntity<String> {
        return ResponseEntity.ok()
                .header("mock-response-header", "mock-response-header-value")
                .header("mock-empty-response-header", "")
                .header(
                        "mock-multi-response-header",
                        "mock-multi-response-header-value1",
                        "mock-multi-response-header-value2",
                )
                .body("mock-text")
    }

    /**
     * Gets a mock text asynchronously.
     *
     * @return A [CompletableFuture] to return a mock text.
     */
    @GetMapping("/text-asynchronously")
    fun getTextAsynchronously(): CompletableFuture<String> {
        return supplyAsync {
            getText()
        }
    }

}
