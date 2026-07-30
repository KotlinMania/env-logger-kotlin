// port-lint: source src/writer/mod.rs
package io.github.kotlinmania.envlogger.writer

import io.github.kotlinmania.envlogger.PipeSink
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class ModTest {
    @Test
    fun parseWriteStyleValid() {
        val inputs =
            listOf(
                "auto" to WriteStyle.Auto,
                "always" to WriteStyle.Always,
                "never" to WriteStyle.Never,
            )

        for ((input, expected) in inputs) {
            assertEquals(expected, parseWriteStyleSpec(input))
        }
    }

    @Test
    fun parseWriteStyleInvalid() {
        val inputs = listOf("", "true", "false", "NEVER!!")

        for (input in inputs) {
            assertEquals(WriteStyle.Auto, parseWriteStyleSpec(input))
        }
    }

    @Test
    fun writeStyleDefaultIsAuto() {
        assertSame(WriteStyle.Auto, WriteStyle.default())
    }

    @Test
    fun builderDefaultsAreStderrAndAuto() {
        val builder = Builder.new()
        val writer = builder.build()

        // Auto collapses to Never in commonMain (matches upstream when the
        // `auto-color` feature is absent), proving the auto-resolution branch.
        assertSame(WriteStyle.Never, writer.writeStyle())
    }

    @Test
    fun builderPreservesExplicitNever() {
        val writer =
            Builder
                .new()
                .writeStyle(WriteStyle.Never)
                .build()

        assertSame(WriteStyle.Never, writer.writeStyle())
    }

    @Test
    fun builderPreservesExplicitAlways() {
        val writer =
            Builder
                .new()
                .writeStyle(WriteStyle.Always)
                .build()

        assertSame(WriteStyle.Always, writer.writeStyle())
    }

    @Test
    fun parseWriteStyleViaBuilder() {
        val writer =
            Builder
                .new()
                .parseWriteStyle("always")
                .build()

        assertSame(WriteStyle.Always, writer.writeStyle())
    }

    @Test
    fun rebuildingFailsWithUpstreamMessage() {
        val builder = Builder.new()
        builder.build()

        val error = assertFailsWith<IllegalStateException> { builder.build() }
        assertEquals("attempt to re-use consumed builder", error.message)
    }

    @Test
    fun pipeTargetRoutesBytesToCallerSink() {
        val sink = RecordingSink()
        val writer =
            Builder
                .new()
                .target(Target.Pipe(sink))
                .writeStyle(WriteStyle.Never)
                .build()

        val buffer = writer.buffer()
        val payload = "hello".encodeToByteArray()
        val written = buffer.write(payload)
        writer.print(buffer)

        assertEquals(payload.size, written)
        assertContentEquals(payload, sink.collected())
        assertEquals(1, sink.flushCount)
    }

    @Test
    fun bufferClearAndAsBytesRoundTrip() {
        val writer =
            Builder
                .new()
                .target(Target.Pipe(RecordingSink()))
                .writeStyle(WriteStyle.Never)
                .build()

        val buffer = writer.buffer()
        buffer.write("alpha".encodeToByteArray())
        assertContentEquals("alpha".encodeToByteArray(), buffer.asBytes())

        buffer.clear()
        assertContentEquals(ByteArray(0), buffer.asBytes())
    }

    @Test
    fun bufferToStringMatchesUtf8Decoding() {
        val writer =
            Builder
                .new()
                .target(Target.Pipe(RecordingSink()))
                .writeStyle(WriteStyle.Never)
                .build()

        val buffer = writer.buffer()
        buffer.write("café".encodeToByteArray())

        assertEquals("café", buffer.toString())
    }

    private class RecordingSink : PipeSink {
        private val sink = ArrayList<Byte>()
        var flushCount: Int = 0
            private set

        override fun writeAll(buf: ByteArray) {
            for (b in buf) {
                sink.add(b)
            }
        }

        override fun flush() {
            flushCount += 1
        }

        fun collected(): ByteArray {
            val out = ByteArray(sink.size)
            for (i in sink.indices) {
                out[i] = sink[i]
            }
            return out
        }
    }
}
