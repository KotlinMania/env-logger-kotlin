// port-lint: source fmt/kv.rs
package io.github.kotlinmania.envlogger.fmt

import ai.solace.tui.anstyle.Effects
import ai.solace.tui.anstyle.Style
import io.github.kotlinmania.envlogger.writer.WriteStyle
import io.github.kotlinmania.log.kv.Key
import io.github.kotlinmania.log.kv.Source
import io.github.kotlinmania.log.kv.Value
import io.github.kotlinmania.log.kv.VisitSource

/**
 * Format function for serializing key/value pairs.
 *
 * This function determines how key/value pairs for structured logs are
 * serialized within the default format.
 */
public typealias KvFormatFn = (Formatter, Source) -> Unit

/**
 * Null Key Value Format.
 *
 * This function is intended to be passed to
 * [ConfigurableFormat.keyValues].
 *
 * This key value format simply ignores any key/value fields and doesn't
 * include them in the output.
 */
public fun hiddenKvFormat(formatter: Formatter, fields: Source) {
    // Null format ignores formatter and fields.
}

/**
 * Default Key Value Format.
 *
 * This function is intended to be passed to
 * [ConfigurableFormat.keyValues].
 *
 * This is the default key/value format. Which uses an "=" as the separator
 * between the key and value and a " " between each pair.
 *
 * For example: `ip=127.0.0.1 port=123456 path=/example`.
 */
public fun defaultKvFormat(formatter: Formatter, fields: Source) {
    fields.visit(DefaultVisitSource(formatter)).getOrThrow()
}

private class DefaultVisitSource(
    val formatter: Formatter,
) : VisitSource {
    override fun visitPair(key: Key, value: Value): Result<Unit> {
        formatter.writeStr(" ${styleKey(key)}=$value")
        return Result.success(Unit)
    }

    fun styleKey(text: Key): StyledValue<Key> =
        StyledValue(
            style =
                if (formatter.writeStyleField == WriteStyle.Never) {
                    Style()
                } else {
                    Style().effects(Effects.ITALIC)
                },
            value = text,
        )
}
