// port-lint: tests writer/target.rs
package io.github.kotlinmania.envlogger.writer

import io.github.kotlinmania.envlogger.PipeSink
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class TargetTest {
    @Test
    fun defaultTargetIsStderr() {
        assertSame(Target.Stderr, Target.default())
    }

    @Test
    fun debugNamesMatchUpstream() {
        assertEquals("stdout", Target.Stdout.toString())
        assertEquals("stderr", Target.Stderr.toString())
        assertEquals("pipe", Target.Pipe(NoopSink).toString())
    }

    @Test
    fun pipeCarriesCustomSink() {
        val target = Target.Pipe(NoopSink)

        assertSame(NoopSink, target.sink)
    }

    private object NoopSink : PipeSink {
        override fun writeAll(buf: ByteArray) = Unit

        override fun flush() = Unit
    }
}
