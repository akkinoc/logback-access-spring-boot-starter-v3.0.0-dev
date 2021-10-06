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
     * @return A [CompletableFuture] to return a mock text asynchronously.
     */
    @GetMapping("/text-asynchronously")
    fun getTextAsynchronously(): CompletableFuture<String> {
        return CompletableFuture.supplyAsync {
            getText()
        }
    }

    /**
     * Gets a mock text with chunked transfer encoding.
     *
     * @return A [Flux] to return a mock text with chunked transfer encoding.
     */
    @GetMapping("/text-with-chunked-transfer-encoding")
    fun getTextWithChunkedTransferEncoding(): Flux<String> {
        return Flux.just("mock-text")
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

}
