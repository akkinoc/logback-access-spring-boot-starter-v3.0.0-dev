package dev.akkinoc.spring.boot.logback.access.test.mock

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.util.concurrent.CompletableFuture

/**
 * The mock controller.
 */
@RestController
@RequestMapping("/mock-controller")
class MockController {

    /**
     * Gets a text.
     *
     * @return A text.
     */
    @GetMapping("/text")
    fun getText(): String {
        return "mock-text"
    }

    /**
     * Gets a text with response headers.
     *
     * @return A [ResponseEntity] to return a text with response headers.
     */
    @GetMapping("/text-with-response-headers")
    fun getTextWithResponseHeaders(): ResponseEntity<String> {
        return ResponseEntity.ok()
                .header("a", "value @a")
                .header("b", "value1 @b", "value2 @b")
                .header("c", "")
                .body("mock-text")
    }

    /**
     * Gets an empty text.
     *
     * @return An empty text.
     */
    @GetMapping("/empty-text")
    fun getEmptyText(): String {
        return ""
    }

    /**
     * Gets a text asynchronously.
     *
     * @return A [CompletableFuture] to return a text asynchronously.
     */
    @GetMapping("/text-asynchronously")
    fun getTextAsynchronously(): CompletableFuture<String> {
        return CompletableFuture.supplyAsync { "mock-text" }
    }

    /**
     * Gets a text with chunked transfer encoding.
     *
     * @return A [Flux] to return a text with chunked transfer encoding.
     */
    @GetMapping("/text-with-chunked-transfer-encoding")
    fun getTextWithChunkedTransferEncoding(): Flux<String> {
        return Flux.just("mock-text")
    }

}
