package dev.akkinoc.spring.boot.logback.access.test.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.test.context.FilteredClassLoader
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.MergedContextConfiguration
import java.net.URL
import java.net.URLClassLoader

/**
 * The test context customizer to configure the class loader.
 *
 * @property hiddenClasses The classes to hide.
 * @property additionalClassPath The class path to add.
 */
data class TestContextClassLoaderCustomizer(
        private val hiddenClasses: Set<Class<*>> = emptySet(),
        private val additionalClassPath: URL? = null,
) : ContextCustomizer {

    override fun customizeContext(context: ConfigurableApplicationContext, config: MergedContextConfiguration) {
        hideClasses(context)
        addClassPath(context)
        log.debug("Customized the {}: {}", ConfigurableApplicationContext::class.simpleName, context)
    }

    /**
     * Hides the specified classes from the class loader.
     *
     * @param context The test context.
     */
    private fun hideClasses(context: ConfigurableApplicationContext) {
        if (hiddenClasses.isEmpty()) return
        context.classLoader = FilteredClassLoader(*hiddenClasses.toTypedArray())
    }

    /**
     * Adds the specified class path to the class loader.
     *
     * @param context The test context.
     */
    private fun addClassPath(context: ConfigurableApplicationContext) {
        if (additionalClassPath == null) return
        val urls = arrayOf(additionalClassPath)
        val parent = context.classLoader
        checkNotNull(parent) { "Failed to get the current class loader: $context" }
        context.classLoader = URLClassLoader(urls, parent)
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(TestContextClassLoaderCustomizer::class.java)

    }

}
