// port-lint: source writer/mod.rs
package io.github.kotlinmania.envlogger.writer

import io.github.kotlinmania.envlogger.PipeSink

// Upstream Rust module declarations and re-exports preserved as a ledger:
//   mod buffer;                          -> Buffer.kt (sibling, internal-visible)
//   mod target;                          -> Target.kt (sibling)
//   pub(crate) use buffer::Buffer;       -> internal members of Buffer.kt are
//                                           reachable via the writer package.
//   pub use target::Target;              -> Target is public on Target.kt;
//                                           the writer package itself is the
//                                           public surface, so no re-export
//                                           typealias is added (per repo
//                                           CLAUDE.md "Re-exports from
//                                           upstream mod.rs files").

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
    }
}

// Upstream gates two `From` conversions on the `color` feature, mapping
// `anstream::ColorChoice` to and from [WriteStyle]. The Kotlin port does not
// depend on `anstream`, so those conversions are omitted (equivalent to
// `cfg(not(feature = "color"))`).

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

    /** Whether or not to capture logs for `cargo test`. */
    internal fun isTest(isTest: Boolean): Builder {
        this.isTest = isTest
        return this
    }

    /** Build a terminal writer. */
    internal fun build(): Writer {
        check(!built) { "attempt to re-use consumed builder" }
        built = true

        // Upstream resolves `WriteStyle::Auto` against the runtime stdout/
        // stderr stream via `anstream::AutoStream::choice` when the
        // `auto-color` feature is enabled. The Kotlin port runs without
        // `anstream`, so `Auto` collapses to `Never` for the stdout/stderr
        // targets and is preserved verbatim for pipe targets, matching the
        // shape of `cfg(not(feature = "auto-color"))` upstream.
        var colorChoice = writeStyle
        if (colorChoice == WriteStyle.Auto) {
            colorChoice = WriteStyle.Never
        }

        // Upstream uses `mem::take(&mut self.target)` to move the target out
        // of the builder. Kotlin lacks move semantics; the equivalent is
        // reading the current target and resetting the builder's field to
        // the default, which preserves the assertion that the consumed
        // builder cannot be reused.
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
