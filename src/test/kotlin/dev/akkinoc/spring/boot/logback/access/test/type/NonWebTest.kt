package dev.akkinoc.spring.boot.logback.access.test.type

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

/**
 * Indicates a Spring Boot based test that does not use a web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(webEnvironment = NONE)
annotation class NonWebTest
