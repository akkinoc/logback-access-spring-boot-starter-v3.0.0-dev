package dev.akkinoc.spring.boot.logback.access.test.type

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment

/**
 * Indicates a Spring Boot based test that does not use a web server.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
annotation class NonWebTest
