// port-lint: tests env_logger/src/writer/buffer.rs
package io.github.kotlinmania.envlogger.writer

import io.github.kotlinmania.envlogger.PipeSink
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class BufferTest {
    private class TestPipeSink : PipeSink {
        val written = mutableListOf<Byte>()
        var flushed = false

        override fun writeAll(buf: ByteArray) {
            for (b in buf) {
                written.add(b)
            }
        }

        override fun flush() {
            flushed = true
        }
    }

    @Test
    fun testBufferWriteAndClear() {
        val buffer = Buffer(mutableListOf())
        val data = "Hello, World!".encodeToByteArray()
        val written = buffer.write(data)
        assertEquals(data.size, written)
        assertContentEquals(data, buffer.asBytes())
        assertEquals("Hello, World!", buffer.toString())
        assertEquals("Hello, World!", buffer.fmt())

        buffer.clear()
        assertEquals(0, buffer.asBytes().size)
    }

    @Test
    fun testBufferWriterPipe() {
        val sink = TestPipeSink()
        val writer = BufferWriter.pipe(sink, WriteStyle.Always)
        assertEquals(WriteStyle.Always, writer.writeStyle())

        val buffer = writer.buffer()
        buffer.write("Log message".encodeToByteArray())
        writer.print(buffer)

        assertEquals("Log message", sink.written.toByteArray().decodeToString())
        assertEquals(true, sink.flushed)
    }

    @Test
    fun testWritableTargetToString() {
        assertEquals("stdout", WritableTarget.WriteStdout.toString())
        assertEquals("stdout", WritableTarget.PrintStdout.toString())
        assertEquals("stderr", WritableTarget.WriteStderr.toString())
        assertEquals("stderr", WritableTarget.PrintStderr.toString())
        assertEquals("pipe", WritableTarget.Pipe(TestPipeSink()).toString())
    }
}
