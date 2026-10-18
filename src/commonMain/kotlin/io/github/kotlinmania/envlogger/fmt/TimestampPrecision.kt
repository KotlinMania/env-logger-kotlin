package io.github.kotlinmania.envlogger.fmt

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
