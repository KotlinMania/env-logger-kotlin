// port-lint: source src/writer/target.rs
package io.github.kotlinmania.envlogger.writer

import io.github.kotlinmania.envlogger.PipeSink

/**
 * Log target, either `stdout`, `stderr` or a custom pipe.
 *
 * Upstream marks the enum `#[non_exhaustive]`; downstream `when` branches
 * over [Target] should always carry an `else` arm so future variants
 * compile cleanly. The upstream `#[derive(Default)]` is replicated by
 * [Target.default], which returns [Stderr].
 */
public sealed class Target {
    /** Logs will be sent to standard output. */
    public data object Stdout : Target()

    /** Logs will be sent to standard error. */
    public data object Stderr : Target()

    /** Logs will be sent to a custom pipe. */
    public class Pipe(public val sink: PipeSink) : Target()

    /**
     * Mirrors the upstream `std::fmt::Debug` implementation. The Kotlin
     * port collapses Rust's `Debug` and `Display` shapes onto Kotlin's
     * single [toString], so the lowercase tag is observable through any
     * formatter that asks for a string representation.
     */
    override fun toString(): String = when (this) {
        is Stdout -> "stdout"
        is Stderr -> "stderr"
        is Pipe -> "pipe"
    }

    public companion object {
        /** Returns the default [Target], which is [Stderr]. */
        public fun default(): Target = Stderr
    }
}
