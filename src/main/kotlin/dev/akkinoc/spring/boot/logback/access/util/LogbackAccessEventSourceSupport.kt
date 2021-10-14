package dev.akkinoc.spring.boot.logback.access.util

import ch.qos.logback.access.AccessConstants.IMAGE_CONTENT_TYPE
import ch.qos.logback.access.AccessConstants.X_WWW_FORM_URLECODED
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEventSource
import java.nio.charset.Charset.defaultCharset

/**
 * The utility methods to support [LogbackAccessEventSource].
 */
object LogbackAccessEventSourceSupport {

    /**
     * Formats the request content.
     *
     * @param method The method.
     * @param contentType The content type.
     * @param params The parameters.
     * @param bytes The body bytes.
     * @return A formatted request content.
     */
    fun formatRequestContent(
            method: String?,
            contentType: String?,
            params: Map<String, List<String>>,
            bytes: ByteArray?,
    ): String? {
        if (bytes != null) return String(bytes, defaultCharset())
        if (method.equals("POST", ignoreCase = true) && contentType.orEmpty().startsWith(X_WWW_FORM_URLECODED)) {
            return params.asSequence()
                    .flatMap { (key, values) -> values.asSequence().map { key to it } }
                    .joinToString("&") { (key, value) -> "$key=$value" }
        }
        return null
    }

    /**
     * Formats the response content.
     *
     * @param contentType The content type.
     * @param bytes The body bytes.
     * @return A formatted request content.
     */
    fun formatResponseContent(
            contentType: String?,
            bytes: ByteArray?,
    ): String? {
        if (contentType.orEmpty().startsWith(IMAGE_CONTENT_TYPE)) return "[IMAGE CONTENTS SUPPRESSED]"
        if (bytes != null) return String(bytes, defaultCharset())
        return null
    }

}
