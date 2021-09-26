package dev.akkinoc.spring.boot.logback.access.test.type

import dev.akkinoc.spring.boot.logback.access.test.configuration.WebTestConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.context.annotation.Import
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

/**
 * Indicates a Spring Boot based test using the Tomcat servlet web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(properties = ["spring.main.web-application-type=servlet"], webEnvironment = RANDOM_PORT)
@Import(WebTestConfiguration::class)
annotation class TomcatServletWebTest

/**
 * Indicates a Spring Boot based test using the Tomcat reactive web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(properties = ["spring.main.web-application-type=reactive"], webEnvironment = RANDOM_PORT)
@Import(WebTestConfiguration::class)
annotation class TomcatReactiveWebTest

/**
 * Indicates a Spring Boot based test using the Jetty servlet web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(properties = ["spring.main.web-application-type=servlet"], webEnvironment = RANDOM_PORT)
@Import(WebTestConfiguration::class)
annotation class JettyServletWebTest

/**
 * Indicates a Spring Boot based test using the Jetty reactive web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(properties = ["spring.main.web-application-type=reactive"], webEnvironment = RANDOM_PORT)
@Import(WebTestConfiguration::class)
annotation class JettyReactiveWebTest

/**
 * Indicates a Spring Boot based test using the Undertow servlet web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(properties = ["spring.main.web-application-type=servlet"], webEnvironment = RANDOM_PORT)
@Import(WebTestConfiguration::class)
annotation class UndertowServletWebTest

/**
 * Indicates a Spring Boot based test using the Undertow reactive web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(properties = ["spring.main.web-application-type=reactive"], webEnvironment = RANDOM_PORT)
@Import(WebTestConfiguration::class)
annotation class UndertowReactiveWebTest

/**
 * Indicates a Spring Boot based test using the Netty reactive web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(properties = ["spring.main.web-application-type=reactive"], webEnvironment = RANDOM_PORT)
@Import(WebTestConfiguration::class)
annotation class NettyReactiveWebTest