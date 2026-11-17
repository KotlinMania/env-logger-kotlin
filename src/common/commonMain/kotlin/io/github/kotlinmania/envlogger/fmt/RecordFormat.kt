package io.github.kotlinmania.envlogger.fmt

import io.github.kotlinmania.log.Record

/**
 * Format function for serializing a [Record] into a [Formatter].
 */
public interface RecordFormat {
    public fun format(formatter: Formatter, record: Record)

    public companion object {
        public inline operator fun invoke(crossinline block: (Formatter, Record) -> Unit): RecordFormat =
            object : RecordFormat {
                override fun format(formatter: Formatter, record: Record) {
                    block(formatter, record)
                }
            }
    }
}

/**
 * Format function alias.
 */
public typealias FormatFn = RecordFormat

/**
 * Adapts a [ConfigurableFormat] to the [RecordFormat] functional interface.
 */
internal fun ConfigurableFormat.asRecordFormat(): RecordFormat =
    object : RecordFormat {
        override fun format(formatter: Formatter, record: Record) {
            this@asRecordFormat.format(formatter, record)
        }
    }

/**
 * Builder for a record format function.
 */
internal class Builder {
    var defaultFormat: ConfigurableFormat = ConfigurableFormat()
    var customFormat: FormatFn? = null
    private var built: Boolean = false

    /**
     * Convert the format into a callable function.
     *
     * If the `customFormat` is not null, then any `defaultFormat` switches are
     * ignored. If the `customFormat` is null, then a default format is
     * returned. Any `defaultFormat` switches set to `false` won't be written by
     * the format.
     */
    fun build(): FormatFn {
        check(!built) { "attempt to re-use consumed builder" }

        val builtFormat = defaultFormat
        val builtCustom = customFormat
        defaultFormat = ConfigurableFormat()
        customFormat = null
        built = true

        return builtCustom ?: builtFormat.asRecordFormat()
    }
}
