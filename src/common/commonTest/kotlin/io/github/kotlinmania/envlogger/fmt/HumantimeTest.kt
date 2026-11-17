// port-lint: tests fmt/humantime.rs
package io.github.kotlinmania.envlogger.fmt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class HumantimeTest {
    @Test
    fun testDisplayTimestamp() {
        var ts =
            Timestamp(
                time = Instant.fromEpochSeconds(0L),
                precision = TimestampPrecision.Nanos,
            )

        assertEquals("1970-01-01T00:00:00.000000000Z", ts.toString())

        ts = Timestamp(time = ts.time, precision = TimestampPrecision.Micros)
        assertEquals("1970-01-01T00:00:00.000000Z", ts.toString())

        ts = Timestamp(time = ts.time, precision = TimestampPrecision.Millis)
        assertEquals("1970-01-01T00:00:00.000Z", ts.toString())

        ts = Timestamp(time = ts.time, precision = TimestampPrecision.Seconds)
        assertEquals("1970-01-01T00:00:00Z", ts.toString())
    }
}
