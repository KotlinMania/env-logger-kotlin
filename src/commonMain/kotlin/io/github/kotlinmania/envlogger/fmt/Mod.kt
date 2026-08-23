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

// The upstream `mod humantime` and `mod kv` declarations are translated to the
// sibling Kotlin files [Humantime.kt] and [Kv.kt] in this package.

// Upstream re-exports `pub use anstyle as style;`, `pub use self::humantime::Timestamp;`,
// `pub use self::kv::*;`, `pub use crate::writer::Target;`, and
// `pub use crate::writer::WriteStyle;`. The Kotlin port leaves these where they
// live: `ai.solace.tui.anstyle.*` for [Style], the sibling files for [Timestamp]
// and the kv helpers, and the writer package for [Target]/[WriteStyle]. The
// CLAUDE.md re-export procedure forbids minting a central `typealias`.

/**
 * Formatting precision of timestamps.
 *
 * Seconds give precision of full seconds, milliseconds give thousands of a
 * second (3 decimal digits), microseconds are millionth of a second (6 decimal
 * digits) and nanoseconds are billionth of a second (9 decimal digits).
 */
// Upstream lifts `#[allow(clippy::exhaustive_enums)]` plus `Copy, Clone, Debug`
// derives onto this enum. Kotlin enums are value types and provide a generated
// toString(), so no explicit equivalents are needed.
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

    override fun toString(): String = "Formatter(buf=$buf, writeStyle=$writeStyleField)"
}

/**
 * Format function for serializing a [Record] into a [Formatter].
 *
 * The upstream Rust trait is `RecordFormat`. It is sealed at the crate level;
 * the Kotlin port keeps it `internal` for the same reason.
 */
internal fun interface RecordFormat {
    fun format(formatter: Formatter, record: Record)
}

/**
 * Owned, type-erased record format. Upstream this is
 * `Box<dyn RecordFormat + Sync + Send>`; in the Kotlin port the boxing is
 * implicit so the alias is just [RecordFormat].
 */
internal typealias FormatFn = RecordFormat

/**
 * Adapts a [ConfigurableFormat] to the [RecordFormat] functional interface.
 *
 * Upstream Rust uses `impl RecordFormat for ConfigurableFormat`; in Kotlin
 * [ConfigurableFormat.format] already matches the [RecordFormat] shape, so the
 * adapter is implemented in the `Builder.build` path via this helper rather
 * than as a direct interface bridge.
 */
private fun ConfigurableFormat.asRecordFormat(): RecordFormat =
    RecordFormat { formatter, record -> format(formatter, record) }

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
        // Replace `self` with a fresh, marked-as-built builder, mirroring the
        // upstream `mem::replace` swap.
        defaultFormat = ConfigurableFormat()
        customFormat = null
        built = true

        return builtCustom ?: builtFormat.asRecordFormat()
    }
}

// Upstream gates `SubtleStyle` and `StyledValue<T>` on `cfg(feature = "color")`.
// The Kotlin port always carries [Style] from anstyle-kotlin, so the
// `cfg(not(feature = "color"))` branch is dropped.

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
}

// Upstream `impl RecordFormat for ConfigurableFormat` exposes the same
// `format(formatter, record)` shape. In Kotlin, [ConfigurableFormat.format]
// already matches the [RecordFormat] functional interface contract; a separate
// adapter is unnecessary because callers can pass a method reference. The
// upstream `impl Default for ConfigurableFormat` is encoded as the default
// values on the primary constructor.

/**
 * The default format.
 *
 * This format needs to work with any combination of crate features.
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

        // Wrapper that splits the rendered arguments on newlines and re-emits
        // them with the configured indent prefix between chunks.
        val rendered = record.args().toString().encodeToByteArray()
        var first = true
        var start = 0
        var i = 0
        while (i <= rendered.size) {
            if (i == rendered.size || rendered[i] == '\n'.code.toByte()) {
                if (!first) {
                    buf.writeStr(format.suffix)
                    buf.writeStr(" ".repeat(indentCount))
                }
                buf.writeAll(rendered.copyOfRange(start, i))
                first = false
                start = i + 1
            }
            i++
        }
    }

    fun writeKv(record: Record) {
        val kvFormat: KvFormatFn = this.format.kvFormat ?: KvFormatFn(::defaultKvFormat)
        kvFormat(buf, record.keyValues())
    }
}
