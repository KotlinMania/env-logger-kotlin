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
