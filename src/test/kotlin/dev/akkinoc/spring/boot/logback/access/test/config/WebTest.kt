package dev.akkinoc.spring.boot.logback.access.test.config

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
@Import(TomcatServletWebTestConfiguration::class)
annotation class TomcatServletWebTest

/**
 * Indicates a Spring Boot based test using the Tomcat reactive web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(properties = ["spring.main.web-application-type=reactive"], webEnvironment = RANDOM_PORT)
@Import(TomcatReactiveWebTestConfiguration::class)
annotation class TomcatReactiveWebTest

/**
 * Indicates a Spring Boot based test using the Jetty servlet web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(properties = ["spring.main.web-application-type=servlet"], webEnvironment = RANDOM_PORT)
@Import(JettyServletWebTestConfiguration::class)
annotation class JettyServletWebTest

/**
 * Indicates a Spring Boot based test using the Jetty reactive web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(properties = ["spring.main.web-application-type=reactive"], webEnvironment = RANDOM_PORT)
@Import(JettyReactiveWebTestConfiguration::class)
annotation class JettyReactiveWebTest

/**
 * Indicates a Spring Boot based test using the Undertow servlet web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(properties = ["spring.main.web-application-type=servlet"], webEnvironment = RANDOM_PORT)
@Import(UndertowServletWebTestConfiguration::class)
annotation class UndertowServletWebTest

/**
 * Indicates a Spring Boot based test using the Undertow reactive web server.
 */
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@SpringBootTest(properties = ["spring.main.web-application-type=reactive"], webEnvironment = RANDOM_PORT)
@Import(UndertowReactiveWebTestConfiguration::class)
annotation class UndertowReactiveWebTest
