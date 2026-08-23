// port-lint: source writer/mod.rs
package io.github.kotlinmania.envlogger.writer

import io.github.kotlinmania.envlogger.PipeSink

/** Whether or not to print styles to the target. */
public enum class WriteStyle {
    /** Try to print styles, but don't force the issue. */
    Auto,

    /** Try very hard to print styles. */
    Always,

    /** Never print styles. */
    Never,
    ;

    public companion object {
        /** Returns the default [WriteStyle], which is [Auto]. */
        public fun default(): WriteStyle = Auto

        /** Parses a write style from a string. */
        public fun from(choice: String): WriteStyle = parseWriteStyleSpec(choice)
    }
}

/**
 * A terminal target with color awareness.
 */
internal class Writer internal constructor(
    private val inner: BufferWriter,
) {
    internal fun writeStyle(): WriteStyle = inner.writeStyle()

    internal fun buffer(): Buffer = inner.buffer()

    internal fun print(buf: Buffer) {
        inner.print(buf)
    }
}

/**
 * A builder for a terminal writer.
 *
 * The target and style choice can be configured before building.
 */
internal class Builder internal constructor() {
    private var target: Target = Target.default()
    private var writeStyle: WriteStyle = WriteStyle.default()
    private var isTest: Boolean = false
    private var built: Boolean = false

    /** Set the target to write to. */
    internal fun target(target: Target): Builder {
        this.target = target
        return this
    }

    /**
     * Parses a style choice string.
     *
     * See the "Disabling colors" section in the crate-level docs for details.
     */
    internal fun parseWriteStyle(writeStyle: String): Builder =
        writeStyle(parseWriteStyleSpec(writeStyle))

    /** Whether or not to print style characters when writing. */
    internal fun writeStyle(writeStyle: WriteStyle): Builder {
        this.writeStyle = writeStyle
        return this
    }

    /** Whether or not to capture logs for tests. */
    internal fun isTest(isTest: Boolean): Builder {
        this.isTest = isTest
        return this
    }

    /** Build a terminal writer. */
    internal fun build(): Writer {
        check(!built) { "attempt to re-use consumed builder" }
        built = true

        var colorChoice = writeStyle
        if (colorChoice == WriteStyle.Auto) {
            colorChoice = WriteStyle.Never
        }

        val currentTarget = target
        target = Target.default()

        val targetPipeSink: PipeSink? =
            when (currentTarget) {
                is Target.Pipe -> currentTarget.sink
                else -> null
            }

        val writer: BufferWriter =
            when (currentTarget) {
                Target.Stdout -> BufferWriter.stdout(isTest, colorChoice)
                Target.Stderr -> BufferWriter.stderr(isTest, colorChoice)
                is Target.Pipe -> BufferWriter.pipe(targetPipeSink!!, colorChoice)
            }

        return Writer(writer)
    }

    internal companion object {
        /** Initialize the writer builder with defaults. */
        internal fun new(): Builder = Builder()

        /** Returns a [Builder] with all fields at their defaults. */
        internal fun default(): Builder = new()
    }
}

internal fun parseWriteStyleSpec(spec: String): WriteStyle =
    when (spec) {
        "auto" -> WriteStyle.Auto
        "always" -> WriteStyle.Always
        "never" -> WriteStyle.Never
        else -> WriteStyle.default()
    }
