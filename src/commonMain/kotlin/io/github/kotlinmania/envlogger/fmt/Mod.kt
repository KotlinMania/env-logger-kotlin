// port-lint: source fmt/mod.rs
package io.github.kotlinmania.envlogger.fmt

import ai.solace.tui.anstyle.AnsiColor
import ai.solace.tui.anstyle.Effects
import ai.solace.tui.anstyle.Style
import io.github.kotlinmania.envlogger.writer.Buffer
import io.github.kotlinmania.envlogger.writer.WriteStyle
import io.github.kotlinmania.envlogger.writer.Writer
import io.github.kotlinmania.log.Level
import io.github.kotlinmania.log.Record

/**
 * Formatting for log records.
 *
 * This module contains a [Formatter] that can be used to format log records
 * into without needing temporary allocations. Usually you won't need to worry
 * about the contents of this module and can use the `Formatter` like an
 * ordinary byte sink.
 *
 * # Formatting log records
 *
 * The format used to print log records can be customised using the
 * `Builder.format` method.
 *
 * Terminal styling is done through ANSI escape codes and will be adapted to
 * the capabilities of the target stream.s
 *
 * For example, you could use one of:
 * - [anstyle](https://docs.rs/anstyle) is a minimal, runtime string styling API
 *   and is re-exported as `style`
 * - [owo-colors](https://docs.rs/owo-colors) is a feature rich runtime string
 *   styling API
 * - [color-print](https://docs.rs/color-print) for feature-rich compile-time
 *   styling API
 *
 * See also [Formatter.defaultLevelStyle].
 *
 * ```
 * val builder = io.github.kotlinmania.envlogger.Builder.new()
 *
 * builder.format { buf, record ->
 *     buf.writeLine("${record.level()}: ${record.args()}")
 * }
 * ```
 *
 * # Key Value arguments
 *
 * If the `kv` feature is enabled, then the default format will include key
 * values from the log by default, but this can be disabled by calling
 * `Builder.formatKeyValues` with [hiddenKvFormat] as the format function.
 *
 * The way these keys and values are formatted can also be customized with a
 * separate format function that is called by the default format with
 * `Builder.formatKeyValues`.
 *
 * See <https://docs.rs/log/latest/log/#structured-logging>.
 */

/**
 * Formatting precision of timestamps.
 *
 * Seconds give precision of full seconds, milliseconds give thousands of a
 * second (3 decimal digits), microseconds are millionth of a second (6 decimal
 * digits) and nanoseconds are billionth of a second (9 decimal digits).
 */
public enum class TimestampPrecision {
    /** Full second precision (0 decimal digits) */
    Seconds,

    /** Millisecond precision (3 decimal digits) */
    Millis,

    /** Microsecond precision (6 decimal digits) */
    Micros,

    /** Nanosecond precision (9 decimal digits) */
    Nanos,
    ;

    public companion object {
        /** The default timestamp precision is seconds. */
        public fun default(): TimestampPrecision = Seconds
    }
}

/**
 * A formatter to write logs into.
 *
 * `Formatter` provides a byte-sink interface for writing log records. It also
 * supports terminal styling using ANSI escape codes.
 *
 * # Examples
 *
 * Format a log record with the default header:
 *
 * ```
 * val builder = io.github.kotlinmania.envlogger.Builder.new()
 *
 * builder.format { buf, record ->
 *     buf.writeLine("${record.level()}: ${record.args()}")
 * }
 * ```
 */
public class Formatter internal constructor(
    internal val buf: Buffer,
    internal val writeStyleField: WriteStyle,
) {
    internal constructor(writer: Writer) : this(writer.buffer(), writer.writeStyle())

    internal fun writeStyle(): WriteStyle = writeStyleField

    internal fun print(writer: Writer) {
        writer.print(buf)
    }

    internal fun clear() {
        buf.clear()
    }

    public companion object {
        internal fun new(writer: Writer): Formatter = Formatter(writer)
    }

    internal fun writeRecord(record: Record) {
        writeStr(record.args().toString())
    }

    /**
     * Get the default [Style] for the given level.
     *
     * The style can be used to print other values besides the level.
     *
     * See [Style] for how to adapt it to the styling crate of your choice.
     */
    public fun defaultLevelStyle(level: Level): Style =
        if (writeStyleField == WriteStyle.Never) {
            Style()
        } else {
            when (level) {
                Level.Trace -> AnsiColor.Cyan.onDefault()
                Level.Debug -> AnsiColor.Blue.onDefault()
                Level.Info -> AnsiColor.Green.onDefault()
                Level.Warn -> AnsiColor.Yellow.onDefault()
                Level.Error -> AnsiColor.Red.onDefault().effects(Effects.BOLD)
            }
        }

    /** Writes the given byte slice into this formatter's buffer. */
    public fun write(buf: ByteArray): Int = this.buf.write(buf)

    /** Flushes this formatter's buffer. */
    public fun flush() {
        buf.flush()
    }

    /** Convenience: writes every byte of [buf] to this formatter's buffer. */
    public fun writeAll(buf: ByteArray) {
        this.buf.write(buf)
    }

    /** Convenience: writes [s] as UTF-8 bytes to this formatter's buffer. */
    public fun writeStr(s: String) {
        this.buf.write(s.encodeToByteArray())
    }

    /** Convenience: writes [s] followed by a newline to this formatter's buffer. */
    public fun writeLine(s: String) {
        writeStr(s)
        writeStr("\n")
    }

    /**
     * Formats formatter representation.
     */
    public fun fmt(): String = toString()

    override fun toString(): String = "Formatter(buf=$buf, writeStyle=$writeStyleField)"
}

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
private fun ConfigurableFormat.asRecordFormat(): RecordFormat =
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

/**
 * A value that can be printed using the given styles.
 */
internal data class StyledValue<T : Any>(
    val style: Style,
    val value: T,
) {
    /**
     * Renders the value wrapped in the style's ANSI escape sequences. Equivalent
     * to upstream `impl<T: Display> Display for StyledValue<T>`.
     */
    /**
     * Formats styled value representation.
     */
    internal fun fmt(): String = toString()

    override fun toString(): String =
        buildString {
            append(style.render())
            append(value)
            append(style.renderReset())
        }
}

/** Subtle styling for header punctuation (open/close brackets, etc.). */
internal typealias SubtleStyle = StyledValue<String>

/** A [custom format][io.github.kotlinmania.envlogger.Builder.format] with settings for which fields to show. */
public class ConfigurableFormat internal constructor(
    // This format needs to work with any combination of crate features.
    internal var timestamp: TimestampPrecision? = TimestampPrecision.default(),
    internal var modulePath: Boolean = false,
    internal var target: Boolean = true,
    internal var level: Boolean = true,
    internal var sourceFile: Boolean = false,
    internal var sourceLineNumber: Boolean = false,
    internal var indent: Int? = 4,
    internal var suffix: String = "\n",
    internal var kvFormat: KvFormatFn? = null,
) {
    /** Format the [Record] as configured for outputting. */
    public fun format(formatter: Formatter, record: Record) {
        val fmt =
            ConfigurableFormatWriter(
                format = this,
                buf = formatter,
                writtenHeaderValue = false,
            )
        fmt.write(record)
    }

    /** Whether or not to write the level in the default format. */
    public fun level(write: Boolean): ConfigurableFormat {
        level = write
        return this
    }

    /** Whether or not to write the source file path in the default format. */
    public fun file(write: Boolean): ConfigurableFormat {
        sourceFile = write
        return this
    }

    /**
     * Whether or not to write the source line number path in the default format.
     *
     * Only has effect if `formatFile` is also enabled.
     */
    public fun lineNumber(write: Boolean): ConfigurableFormat {
        sourceLineNumber = write
        return this
    }

    /** Whether or not to write the module path in the default format. */
    public fun modulePath(write: Boolean): ConfigurableFormat {
        modulePath = write
        return this
    }

    /** Whether or not to write the target in the default format. */
    public fun target(write: Boolean): ConfigurableFormat {
        target = write
        return this
    }

    /**
     * Configures the amount of spaces to use to indent multiline log records.
     * A value of `null` disables any kind of indentation.
     */
    public fun indent(indent: Int?): ConfigurableFormat {
        this.indent = indent
        return this
    }

    /** Configures if timestamp should be included and in what precision. */
    public fun timestamp(timestamp: TimestampPrecision?): ConfigurableFormat {
        this.timestamp = timestamp
        return this
    }

    /** Configures the end of line suffix. */
    public fun suffix(suffix: String): ConfigurableFormat {
        this.suffix = suffix
        return this
    }

    /**
     * Set the format for structured key/value pairs in the log record.
     *
     * With the default format, this function is called for each record and
     * should format the structured key-value pairs as returned by
     * [Record.keyValues].
     *
     * The format function is expected to output the string directly to the
     * [Formatter] so that implementations can use the standard byte-sink
     * convenience methods, similar to the main format function.
     *
     * The default format uses a space to separate each key-value pair, with an
     * "=" between the key and value.
     */
    public fun keyValues(format: KvFormatFn): ConfigurableFormat {
        kvFormat = format
        return this
    }

    /**
     * Formats configurable format representation.
     */
    public fun fmt(): String = toString()
}

/**
 * The default format.
 */
internal class ConfigurableFormatWriter(
    val format: ConfigurableFormat,
    val buf: Formatter,
    var writtenHeaderValue: Boolean,
) {
    fun write(record: Record) {
        writeTimestamp()
        writeLevel(record)
        writeModulePath(record)
        writeSourceLocation(record)
        writeTarget(record)
        finishHeader()

        writeArgs(record)
        writeKv(record)
        buf.writeStr(format.suffix)
    }

    fun subtleStyle(text: String): SubtleStyle =
        StyledValue(
            style =
                if (buf.writeStyleField == WriteStyle.Never) {
                    Style()
                } else {
                    AnsiColor.BrightBlack.onDefault()
                },
            value = text,
        )

    fun writeHeaderValue(value: Any) {
        if (!writtenHeaderValue) {
            writtenHeaderValue = true
            val openBrace = subtleStyle("[")
            buf.writeStr("$openBrace$value")
        } else {
            buf.writeStr(" $value")
        }
    }

    fun writeLevel(record: Record) {
        if (!format.level) {
            return
        }

        val level = record.level()
        val styled: Any =
            StyledValue(
                style = buf.defaultLevelStyle(level),
                value = level.toString().padEnd(5),
            )
        writeHeaderValue(styled)
    }

    fun writeTimestamp() {
        val precision = format.timestamp ?: return
        val ts =
            when (precision) {
                TimestampPrecision.Seconds -> buf.timestampSeconds()
                TimestampPrecision.Millis -> buf.timestampMillis()
                TimestampPrecision.Micros -> buf.timestampMicros()
                TimestampPrecision.Nanos -> buf.timestampNanos()
            }
        writeHeaderValue(ts)
    }

    fun writeModulePath(record: Record) {
        if (!format.modulePath) {
            return
        }

        val modulePath = record.modulePath() ?: return
        writeHeaderValue(modulePath)
    }

    fun writeSourceLocation(record: Record) {
        if (!format.sourceFile) {
            return
        }

        val filePath = record.file() ?: return
        val line = if (format.sourceLineNumber) record.line() else null
        if (line != null) {
            writeHeaderValue("$filePath:$line")
        } else {
            writeHeaderValue(filePath)
        }
    }

    fun writeTarget(record: Record) {
        if (!format.target) {
            return
        }

        val target = record.target()
        if (target.isEmpty()) return
        writeHeaderValue(target)
    }

    fun finishHeader() {
        if (writtenHeaderValue) {
            val closeBrace = subtleStyle("]")
            buf.writeStr("$closeBrace ")
        }
    }

    fun writeArgs(record: Record) {
        val indentCount = format.indent
        if (indentCount == null) {
            // Fast path for no indentation.
            buf.writeStr(record.args().toString())
            return
        }

        val wrapper = IndentWrapper(this, indentCount)
        wrapper.write(record.args().toString().encodeToByteArray())
    }

    fun writeKv(record: Record) {
        val kvFormat: KvFormatFn = this.format.kvFormat ?: ::defaultKvFormat
        kvFormat(buf, record.keyValues())
    }
}

internal class IndentWrapper(
    internal val fmt: ConfigurableFormatWriter,
    internal val indentCount: Int,
) {
    fun write(buf: ByteArray): Int {
        val rendered = buf
        var first = true
        var start = 0
        var i = 0
        while (i <= rendered.size) {
            if (i == rendered.size || rendered[i] == '\n'.code.toByte()) {
                if (!first) {
                    fmt.buf.writeStr(fmt.format.suffix)
                    fmt.buf.writeStr(" ".repeat(indentCount))
                }
                fmt.buf.writeAll(rendered.copyOfRange(start, i))
                first = false
                start = i + 1
            }
            i++
        }
        return buf.size
    }
}
