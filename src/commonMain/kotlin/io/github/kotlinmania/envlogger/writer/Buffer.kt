// port-lint: source env_logger/src/writer/buffer.rs
package io.github.kotlinmania.envlogger.writer

import io.github.kotlinmania.envlogger.PipeSink

internal class BufferWriter private constructor(
    private val target: WritableTarget,
    private val writeStyle: WriteStyle,
) {
    internal fun writeStyle(): WriteStyle = writeStyle

    internal fun buffer(): Buffer = Buffer(ArrayList())

    internal fun print(buf: Buffer) {
        val bytes = buf.asBytes()
        when (target) {
            WritableTarget.WriteStdout -> {
                writeAllToCommonStdout(bytes)
            }
            WritableTarget.PrintStdout -> {
                val text = bytes.decodeToString()
                kotlin.io.print(text)
            }
            WritableTarget.WriteStderr -> {
                writeAllToCommonStdout(bytes)
            }
            WritableTarget.PrintStderr -> {
                val text = bytes.decodeToString()
                kotlin.io.print(text)
            }
            is WritableTarget.Pipe -> {
                target.sink.writeAll(bytes)
                target.sink.flush()
            }
        }
    }

    internal companion object {
        internal fun stderr(isTest: Boolean, writeStyle: WriteStyle): BufferWriter =
            BufferWriter(
                target = if (isTest) WritableTarget.PrintStderr else WritableTarget.WriteStderr,
                writeStyle = writeStyle,
            )

        internal fun stdout(isTest: Boolean, writeStyle: WriteStyle): BufferWriter =
            BufferWriter(
                target = if (isTest) WritableTarget.PrintStdout else WritableTarget.WriteStdout,
                writeStyle = writeStyle,
            )

        internal fun pipe(pipe: PipeSink, writeStyle: WriteStyle): BufferWriter =
            BufferWriter(
                target = WritableTarget.Pipe(pipe),
                writeStyle = writeStyle,
            )
    }
}

private fun writeAllToCommonStdout(buf: ByteArray) {
    kotlin.io.print(buf.decodeToString())
}

internal class Buffer internal constructor(
    private val storage: MutableList<Byte>,
) {
    internal fun clear() {
        storage.clear()
    }

    internal fun write(buf: ByteArray): Int {
        for (b in buf) {
            storage.add(b)
        }
        return buf.size
    }

    internal fun flush() {
        // Buffer-level flush is a no-op upstream; the buffered bytes are
        // surfaced via [asBytes] and consumed by [BufferWriter.print].
    }

    internal fun asBytes(): ByteArray {
        val out = ByteArray(storage.size)
        for (i in storage.indices) {
            out[i] = storage[i]
        }
        return out
    }

    /**
     * Formats buffer as string representation.
     */
    public fun fmt(): String = toString()

    override fun toString(): String = asBytes().decodeToString()
}

internal fun adapt(buf: ByteArray, writeStyle: WriteStyle): ByteArray = buf

/**
 * Log target, either `stdout`, `stderr` or a custom pipe.
 *
 * Same as [Target], except the pipe is wrapped in a sink for interior
 * mutability. The Kotlin port leaves locking to the caller-
 * supplied [PipeSink] implementation.
 */
internal sealed class WritableTarget {
    /** Logs will be written to standard output. */
    internal object WriteStdout : WritableTarget()

    /** Logs will be printed to standard output. */
    internal object PrintStdout : WritableTarget()

    /** Logs will be written to standard error. */
    internal object WriteStderr : WritableTarget()

    /** Logs will be printed to standard error. */
    internal object PrintStderr : WritableTarget()

    /** Logs will be sent to a custom pipe. */
    internal class Pipe(
        internal val sink: PipeSink,
    ) : WritableTarget()

    /**
     * Mirrors the upstream `std::fmt::Debug` implementation; the two
     * `Stdout` variants and the two `Stderr` variants share their string
     * names because they share their output destination.
     */
    override fun toString(): String =
        when (this) {
            WriteStdout -> "stdout"
            PrintStdout -> "stdout"
            WriteStderr -> "stderr"
            PrintStderr -> "stderr"
            is Pipe -> "pipe"
        }
}
