// port-lint: source env_logger/src/writer/target.rs
package io.github.kotlinmania.envlogger.writer

import io.github.kotlinmania.envlogger.PipeSink

/**
 * Log target, either `stdout`, `stderr` or a custom pipe.
 *
 * Downstream `when` branches over [Target] should carry an `else` arm so future variants
 * compile cleanly. The default target is [Target.default], which returns [Stderr].
 */
public sealed class Target {
    /** Logs will be sent to standard output. */
    public object Stdout : Target()

    /** Logs will be sent to standard error. */
    public object Stderr : Target()

    /** Logs will be sent to a custom pipe. */
    public class Pipe(
        public val sink: PipeSink,
    ) : Target()

    /**
     * String representation of target.
     */
    override fun toString(): String =
        when (this) {
            is Stdout -> "stdout"
            is Stderr -> "stderr"
            is Pipe -> "pipe"
        }

    /**
     * Formats target for display and debug representations.
     */
    public fun fmt(): String = toString()

    public companion object {
        /** Returns the default [Target], which is [Stderr]. */
        public fun default(): Target = Stderr
    }
}
