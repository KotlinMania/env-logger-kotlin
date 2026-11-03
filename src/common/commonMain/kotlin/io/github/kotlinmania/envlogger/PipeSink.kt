// port-lint: source lib.rs
package io.github.kotlinmania.envlogger

/**
 * Byte sink for custom output pipes.
 *
 * Callers that own a stream (a file handle, a network socket, an in-memory buffer)
 * implement this to receive formatted log bytes.
 */
public interface PipeSink {
    /** Writes every byte of [buf] to the sink. */
    public fun writeAll(buf: ByteArray)

    /** Flushes any buffered output. */
    public fun flush()
}
