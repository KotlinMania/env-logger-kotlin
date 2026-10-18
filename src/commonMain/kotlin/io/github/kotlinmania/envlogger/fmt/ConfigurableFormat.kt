package io.github.kotlinmania.envlogger.fmt

import ai.solace.tui.anstyle.AnsiColor
import ai.solace.tui.anstyle.Style
import io.github.kotlinmania.envlogger.writer.WriteStyle
import io.github.kotlinmania.log.Record

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
