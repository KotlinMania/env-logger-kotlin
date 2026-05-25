package io.github.kotlinmania.envlogger

/**
 * Byte sink the upstream Rust crate plugs in via
 * `Box<dyn std::io::Write + Send + 'static>`. Kotlin Multiplatform's common
 * surface has no analog of `std::io::Write`, so the port carries this small
 * interface in env-logger-kotlin itself. Callers that own a stream (a file
 * handle, a network socket, an in-memory buffer) implement this to receive
 * formatted log bytes.
 *
 * [writeAll] mirrors upstream `io::Write::write_all`: every call must consume
 * the full slice or throw. [flush] mirrors upstream `io::Write::flush`.
 */
public interface PipeSink {
    /** Writes every byte of [buf] to the sink. */
    public fun writeAll(buf: ByteArray)

    /** Flushes any buffered output. */
    public fun flush()
}
