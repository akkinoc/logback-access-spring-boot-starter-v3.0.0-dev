package dev.akkinoc.spring.boot.logback.access

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration

/**
 * The test context configuration.
 */
@SpringBootConfiguration(proxyBeanMethods = false)
@EnableAutoConfiguration
class TestContextConfiguration
