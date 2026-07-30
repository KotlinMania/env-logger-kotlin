// port-lint: source src/fmt/mod.rs (#[cfg(test)] tests module)
package io.github.kotlinmania.envlogger.fmt

import io.github.kotlinmania.envlogger.writer.WriteStyle
import io.github.kotlinmania.log.Arguments
import io.github.kotlinmania.log.Level
import io.github.kotlinmania.log.Record
import io.github.kotlinmania.log.kv.Key
import io.github.kotlinmania.log.kv.Source
import io.github.kotlinmania.log.kv.toValue
import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.kotlinmania.envlogger.writer.Builder as WriterBuilder

private fun writeRecord(record: Record, fmt: ConfigurableFormatWriter): String {
    val buf = fmt.buf.buf
    fmt.write(record)
    return buf.asBytes().decodeToString()
}

private fun writeTarget(target: String, fmt: ConfigurableFormatWriter): String =
    writeRecord(
        Record
            .builder()
            .args(Arguments("log\nmessage", emptyList()))
            .level(Level.Info)
            .file("test.rs")
            .line(144)
            .modulePath("test::path")
            .target(target)
            .build(),
        fmt,
    )

private fun write(fmt: ConfigurableFormatWriter): String = writeTarget("", fmt)

private fun formatter(): Formatter {
    val writer = WriterBuilder.new().writeStyle(WriteStyle.Never).build()
    return Formatter(writer)
}

private fun kvSource(vararg pairs: Pair<String, UInt>): Source =
    Source { visitor ->
        for ((k, v) in pairs) {
            visitor.visitPair(Key.fromStr(k), v.toValue()).getOrElse { return@Source Result.failure(it) }
        }
        Result.success(Unit)
    }

class ModTest {
    @Test
    fun formatWithHeader() {
        val f = formatter()
        val written =
            write(
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = true,
                            target = false,
                            level = true,
                            sourceFile = false,
                            sourceLineNumber = false,
                            kvFormat = ::hiddenKvFormat,
                            indent = null,
                            suffix = "\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals("[INFO  test::path] log\nmessage\n", written)
    }

    @Test
    fun formatNoHeader() {
        val f = formatter()
        val written =
            write(
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = false,
                            target = false,
                            level = false,
                            sourceFile = false,
                            sourceLineNumber = false,
                            kvFormat = ::hiddenKvFormat,
                            indent = null,
                            suffix = "\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals("log\nmessage\n", written)
    }

    @Test
    fun formatIndentSpaces() {
        val f = formatter()
        val written =
            write(
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = true,
                            target = false,
                            level = true,
                            sourceFile = false,
                            sourceLineNumber = false,
                            kvFormat = ::hiddenKvFormat,
                            indent = 4,
                            suffix = "\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals("[INFO  test::path] log\n    message\n", written)
    }

    @Test
    fun formatIndentZeroSpaces() {
        val f = formatter()
        val written =
            write(
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = true,
                            target = false,
                            level = true,
                            sourceFile = false,
                            sourceLineNumber = false,
                            kvFormat = ::hiddenKvFormat,
                            indent = 0,
                            suffix = "\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals("[INFO  test::path] log\nmessage\n", written)
    }

    @Test
    fun formatIndentSpacesNoHeader() {
        val f = formatter()
        val written =
            write(
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = false,
                            target = false,
                            level = false,
                            sourceFile = false,
                            sourceLineNumber = false,
                            kvFormat = ::hiddenKvFormat,
                            indent = 4,
                            suffix = "\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals("log\n    message\n", written)
    }

    @Test
    fun formatSuffix() {
        val f = formatter()
        val written =
            write(
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = false,
                            target = false,
                            level = false,
                            sourceFile = false,
                            sourceLineNumber = false,
                            kvFormat = ::hiddenKvFormat,
                            indent = null,
                            suffix = "\n\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals("log\nmessage\n\n", written)
    }

    @Test
    fun formatSuffixWithIndent() {
        val f = formatter()
        val written =
            write(
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = false,
                            target = false,
                            level = false,
                            sourceFile = false,
                            sourceLineNumber = false,
                            kvFormat = ::hiddenKvFormat,
                            indent = 4,
                            suffix = "\n\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals("log\n\n    message\n\n", written)
    }

    @Test
    fun formatTarget() {
        val f = formatter()
        val written =
            writeTarget(
                "target",
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = true,
                            target = true,
                            level = true,
                            sourceFile = false,
                            sourceLineNumber = false,
                            kvFormat = ::hiddenKvFormat,
                            indent = null,
                            suffix = "\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals("[INFO  test::path target] log\nmessage\n", written)
    }

    @Test
    fun formatEmptyTarget() {
        val f = formatter()
        val written =
            write(
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = true,
                            target = true,
                            level = true,
                            sourceFile = false,
                            sourceLineNumber = false,
                            kvFormat = ::hiddenKvFormat,
                            indent = null,
                            suffix = "\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals("[INFO  test::path] log\nmessage\n", written)
    }

    @Test
    fun formatNoTarget() {
        val f = formatter()
        val written =
            writeTarget(
                "target",
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = true,
                            target = false,
                            level = true,
                            sourceFile = false,
                            sourceLineNumber = false,
                            kvFormat = ::hiddenKvFormat,
                            indent = null,
                            suffix = "\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals("[INFO  test::path] log\nmessage\n", written)
    }

    @Test
    fun formatWithSourceFileAndLineNumber() {
        val f = formatter()
        val written =
            write(
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = false,
                            target = false,
                            level = true,
                            sourceFile = true,
                            sourceLineNumber = true,
                            kvFormat = ::hiddenKvFormat,
                            indent = null,
                            suffix = "\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals("[INFO  test.rs:144] log\nmessage\n", written)
    }

    @Test
    fun formatKvDefault() {
        val kvs = kvSource("a" to 1u, "b" to 2u)
        val f = formatter()
        val record =
            Record
                .builder()
                .args(Arguments("log message", emptyList()))
                .level(Level.Info)
                .modulePath("test::path")
                .keyValues(kvs)
                .build()

        val written =
            writeRecord(
                record,
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = false,
                            target = false,
                            level = true,
                            sourceFile = false,
                            sourceLineNumber = false,
                            kvFormat = ::defaultKvFormat,
                            indent = null,
                            suffix = "\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals("[INFO ] log message a=1 b=2\n", written)
    }

    @Test
    fun formatKvDefaultFull() {
        val kvs = kvSource("a" to 1u, "b" to 2u)
        val f = formatter()
        val record =
            Record
                .builder()
                .args(Arguments("log\nmessage", emptyList()))
                .level(Level.Info)
                .modulePath("test::path")
                .target("target")
                .file("test.rs")
                .line(42)
                .keyValues(kvs)
                .build()

        val written =
            writeRecord(
                record,
                ConfigurableFormatWriter(
                    format =
                        ConfigurableFormat(
                            timestamp = null,
                            modulePath = true,
                            target = true,
                            level = true,
                            sourceFile = true,
                            sourceLineNumber = true,
                            kvFormat = ::defaultKvFormat,
                            indent = null,
                            suffix = "\n",
                        ),
                    writtenHeaderValue = false,
                    buf = f,
                ),
            )
        assertEquals(
            "[INFO  test::path test.rs:42 target] log\nmessage a=1 b=2\n",
            written,
        )
    }
}
