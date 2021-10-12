package dev.akkinoc.spring.boot.logback.access.test.mock

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.MediaType
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.util.concurrent.CompletableFuture
import javax.servlet.http.HttpSession
import kotlin.text.Charsets.UTF_8

/**
 * The mock controller for the servlet web server.
 */
@RestController
@RequestMapping("/mock-controller")
class MockServletController {

    /**
     * Gets a text.
     *
     * @return A text.
     */
    @GetMapping("/text")
    fun getText(): String {
        val response = "mock-text"
        log.debug("Getting a text: {}", response)
        return response
    }

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
     * Gets a text with response headers.
     *
     * @return A [ResponseEntity] to return a text with response headers.
     */
    @GetMapping("/text-with-response-headers")
    fun getTextWithResponseHeaders(): ResponseEntity<String> {
        val response = ResponseEntity.ok()
                .header("a", "value @a")
                .header("b", "value1 @b", "value2 @b")
                .header("c", "")
                .body("mock-text")
        log.debug("Getting a text with response headers: {}", response)
        return response
    }

    /**
     * Gets an empty text.
     *
     * @return An empty text.
     */
    @GetMapping("/empty-text")
    fun getEmptyText(): String {
        val response = ""
        log.debug("Getting an empty text: {}", response)
        return response
    }

    /**
     * Gets a text asynchronously.
     *
     * @return A [CompletableFuture] to return a text asynchronously.
     */
    @GetMapping("/text-asynchronously")
    fun getTextAsynchronously(): CompletableFuture<String> {
        val response = CompletableFuture.supplyAsync { "mock-text" }
        log.debug("Getting a text asynchronously: {}", response)
        return response
    }

    /**
     * Gets a text with chunked transfer encoding.
     *
     * @return A [ResponseEntity] to return a text with chunked transfer encoding.
     */
    @GetMapping("/text-with-chunked-transfer-encoding")
    fun getTextWithChunkedTransferEncoding(): ResponseEntity<StreamingResponseBody> {
        val response = ResponseEntity.ok()
                .contentType(MediaType(TEXT_PLAIN, UTF_8))
                .body(StreamingResponseBody { it.write("mock-text".toByteArray(UTF_8)) })
        log.debug("Getting a text with chunked transfer encoding: {}", response)
        return response
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(MockServletController::class.java)

    }

}
