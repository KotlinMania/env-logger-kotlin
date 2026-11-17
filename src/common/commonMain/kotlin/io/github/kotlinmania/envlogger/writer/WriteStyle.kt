package io.github.kotlinmania.envlogger.writer

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
        public fun from(choice: String): WriteStyle = parseWriteStyle(choice)
    }
}

internal fun parseWriteStyle(spec: String): WriteStyle =
    when (spec) {
        "auto" -> WriteStyle.Auto
        "always" -> WriteStyle.Always
        "never" -> WriteStyle.Never
        else -> WriteStyle.default()
    }
