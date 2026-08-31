// port-lint: source env_logger/src/fmt/humantime.rs
package io.github.kotlinmania.envlogger.fmt

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Get a [Timestamp] for the current date and time in UTC.
 *
 * # Examples
 *
 * Include the current timestamp with the log record:
 *
 * ```
 * val builder = io.github.kotlinmania.envlogger.Builder.new()
 *
 * builder.format { buf, record ->
 *     val ts = buf.timestamp()
 *     buf.writeLine("$ts: ${record.level()}: ${record.args()}")
 * }
 * ```
 */
public fun Formatter.timestamp(): Timestamp =
    Timestamp(
        time = Clock.System.now(),
        precision = TimestampPrecision.Seconds,
    )

/**
 * Get a [Timestamp] for the current date and time in UTC with full second
 * precision.
 */
public fun Formatter.timestampSeconds(): Timestamp =
    Timestamp(
        time = Clock.System.now(),
        precision = TimestampPrecision.Seconds,
    )

/**
 * Get a [Timestamp] for the current date and time in UTC with millisecond
 * precision.
 */
public fun Formatter.timestampMillis(): Timestamp =
    Timestamp(
        time = Clock.System.now(),
        precision = TimestampPrecision.Millis,
    )

/**
 * Get a [Timestamp] for the current date and time in UTC with microsecond
 * precision.
 */
public fun Formatter.timestampMicros(): Timestamp =
    Timestamp(
        time = Clock.System.now(),
        precision = TimestampPrecision.Micros,
    )

/**
 * Get a [Timestamp] for the current date and time in UTC with nanosecond
 * precision.
 */
public fun Formatter.timestampNanos(): Timestamp =
    Timestamp(
        time = Clock.System.now(),
        precision = TimestampPrecision.Nanos,
    )

/**
 * An [RFC3339] formatted timestamp.
 *
 * The timestamp implements [toString] and can be written to a [Formatter].
 *
 * [RFC3339]: https://www.ietf.org/rfc/rfc3339.txt
 */
public class Timestamp internal constructor(
    internal val time: Instant,
    internal val precision: TimestampPrecision,
) {
    /**
     * Debug string representation.
     */
    public fun debugString(): String = "Timestamp($this)"

    /**
     * Render this timestamp as an RFC 3339 date-time in UTC at the configured precision.
     */
    override fun toString(): String {
        val ldt = time.toLocalDateTime(TimeZone.UTC)
        val year = ldt.year.toString().padStart(4, '0')
        val month =
            ldt.month.ordinal
                .plus(1)
                .toString()
                .padStart(2, '0')
        val day = ldt.day.toString().padStart(2, '0')
        val hour = ldt.hour.toString().padStart(2, '0')
        val minute = ldt.minute.toString().padStart(2, '0')
        val second = ldt.second.toString().padStart(2, '0')
        val nanos = ldt.nanosecond
        val fractional =
            when (precision) {
                TimestampPrecision.Seconds -> ""
                TimestampPrecision.Millis -> "." + (nanos / 1_000_000).toString().padStart(3, '0')
                TimestampPrecision.Micros -> "." + (nanos / 1_000).toString().padStart(6, '0')
                TimestampPrecision.Nanos -> "." + nanos.toString().padStart(9, '0')
            }
        return "$year-$month-${day}T$hour:$minute:$second${fractional}Z"
    }

    /**
     * Formats timestamp representation.
     */
    public fun fmt(): String = toString()
}

internal class TimestampValue(
    internal val timestamp: Timestamp,
) {
    internal fun fmt(): String = timestamp.toString()

    override fun toString(): String = timestamp.toString()
}
