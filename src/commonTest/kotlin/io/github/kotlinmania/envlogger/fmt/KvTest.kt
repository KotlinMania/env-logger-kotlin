// port-lint: tests fmt/kv.rs
package io.github.kotlinmania.envlogger.fmt

import io.github.kotlinmania.log.kv.Key
import io.github.kotlinmania.log.kv.Source
import io.github.kotlinmania.log.kv.toValue
import kotlin.test.Test
import kotlin.test.assertEquals

class KvTest {
    private fun kvSource(vararg pairs: Pair<String, String>): Source =
        Source { visitor ->
            for ((k, v) in pairs) {
                visitor.visitPair(Key.fromStr(k), v.toValue()).getOrElse { return@Source Result.failure(it) }
            }
            Result.success(Unit)
        }

    @Test
    fun testHiddenKvFormatDoesNothing() {
        val f = formatter()
        val source = kvSource("key" to "value")

        hiddenKvFormat(f, source)
        assertEquals("", f.buf.asBytes().decodeToString())
    }

    @Test
    fun testDefaultKvFormatWritesPairs() {
        val f = formatter()
        val source = kvSource("ip" to "127.0.0.1", "port" to "8080")

        defaultKvFormat(f, source)
        assertEquals(" ip=127.0.0.1 port=8080", f.buf.asBytes().decodeToString())
    }
}
