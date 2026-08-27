// port-lint: source env_logger/src/logger.rs
package io.github.kotlinmania.envlogger

import io.github.kotlinmania.envlogger.fmt.Formatter
import io.github.kotlinmania.envlogger.fmt.RecordFormat
import io.github.kotlinmania.envlogger.fmt.TimestampPrecision
import io.github.kotlinmania.envlogger.writer.Target
import io.github.kotlinmania.envlogger.writer.WriteStyle
import io.github.kotlinmania.envlogger.writer.Writer
import io.github.kotlinmania.log.LevelFilter
import io.github.kotlinmania.log.Log
import io.github.kotlinmania.log.Metadata
import io.github.kotlinmania.log.Record
import io.github.kotlinmania.log.setLogger
import io.github.kotlinmania.log.setMaxLevel

/**
 * The default name for the environment variable to read filters from.
 */
public const val DEFAULT_FILTER_ENV: String = "RUST_LOG"

/**
 * The default name for the environment variable to read style preferences from.
 */
public const val DEFAULT_WRITE_STYLE_ENV: String = "RUST_LOG_STYLE"

private val SYSTEM_ENV: MutableMap<String, String> = mutableMapOf()

internal fun getEnvVar(name: String): String? = SYSTEM_ENV[name]

internal fun setEnvVar(name: String, value: String) {
    SYSTEM_ENV[name] = value
}

internal fun removeEnvVar(name: String) {
    SYSTEM_ENV.remove(name)
}

/**
 * Filter directive specifying a target module and maximum level filter.
 */
internal data class Directive(
    val name: String?,
    val level: LevelFilter,
)

/**
 * Filter implementation parsing directives and matching log records.
 */
internal class Filter internal constructor(
    private val directives: List<Directive>,
    private val regex: Regex?,
) {
    internal fun maxLevel(): LevelFilter {
        if (directives.isEmpty()) return LevelFilter.Error
        var max = LevelFilter.Off
        for (d in directives) {
            if (d.level.ordinal > max.ordinal) {
                max = d.level
            }
        }
        return max
    }

    internal fun matches(record: Record): Boolean {
        if (record.level().toLevelFilter().ordinal > maxLevel().ordinal) {
            return false
        }
        val target = record.target()
        var matchedLevel: LevelFilter? = null
        var bestMatchLen = -1

        for (d in directives) {
            val dName = d.name
            if (dName == null) {
                if (bestMatchLen < 0) {
                    matchedLevel = d.level
                }
            } else if (target.startsWith(dName) && (target.length == dName.length || target.getOrNull(dName.length) == ':' || target.getOrNull(dName.length) == '.')) {
                if (dName.length > bestMatchLen) {
                    bestMatchLen = dName.length
                    matchedLevel = d.level
                }
            }
        }

        val effectiveLevel = matchedLevel ?: LevelFilter.Error
        if (record.level().toLevelFilter().ordinal > effectiveLevel.ordinal) {
            return false
        }

        val rx = regex
        if (rx != null) {
            val formatted = record.args().toString()
            if (!rx.containsMatchIn(formatted)) {
                return false
            }
        }

        return true
    }

    internal fun enabled(metadata: Metadata): Boolean {
        if (metadata.level().toLevelFilter().ordinal > maxLevel().ordinal) {
            return false
        }
        val target = metadata.target()
        var matchedLevel: LevelFilter? = null
        var bestMatchLen = -1

        for (d in directives) {
            val dName = d.name
            if (dName == null) {
                if (bestMatchLen < 0) {
                    matchedLevel = d.level
                }
            } else if (target.startsWith(dName) && (target.length == dName.length || target.getOrNull(dName.length) == ':' || target.getOrNull(dName.length) == '.')) {
                if (dName.length > bestMatchLen) {
                    bestMatchLen = dName.length
                    matchedLevel = d.level
                }
            }
        }

        val effectiveLevel = matchedLevel ?: LevelFilter.Error
        return metadata.level().toLevelFilter().ordinal <= effectiveLevel.ordinal
    }
}

/**
 * Builder for constructing a [Filter].
 */
internal class FilterBuilder {
    private val directives: MutableList<Directive> = mutableListOf()
    private var regex: Regex? = null

    internal fun filterModule(module: String, level: LevelFilter): FilterBuilder {
        directives.removeAll { it.name == module }
        directives.add(Directive(module, level))
        return this
    }

    internal fun filterLevel(level: LevelFilter): FilterBuilder {
        directives.removeAll { it.name == null }
        directives.add(Directive(null, level))
        return this
    }

    internal fun filter(module: String?, level: LevelFilter): FilterBuilder {
        directives.removeAll { it.name == module }
        directives.add(Directive(module, level))
        return this
    }

    internal fun parse(filters: String): FilterBuilder {
        val parts = filters.split(',')
        for (rawPart in parts) {
            val part = rawPart.trim()
            if (part.isEmpty()) continue

            var spec = part
            if (spec.contains('/')) {
                val slashIdx = spec.indexOf('/')
                val pattern = spec.substring(slashIdx + 1).trimEnd('/')
                if (pattern.isNotEmpty()) {
                    regex = Regex(pattern)
                }
                spec = spec.substring(0, slashIdx).trim()
            }
            if (spec.isEmpty()) continue

            if (spec.contains('=')) {
                val eqIdx = spec.indexOf('=')
                val target = spec.substring(0, eqIdx).trim()
                val levelStr = spec.substring(eqIdx + 1).trim()
                val parsedLevel = parseLevelFilter(levelStr) ?: LevelFilter.Trace
                filter(target.ifEmpty { null }, parsedLevel)
            } else {
                val parsed = parseLevelFilter(spec)
                if (parsed != null) {
                    filterLevel(parsed)
                } else {
                    filter(spec, LevelFilter.Trace)
                }
            }
        }
        return this
    }

    private fun parseLevelFilter(str: String): LevelFilter? {
        val lower = str.lowercase()
        return when (lower) {
            "off", "0" -> LevelFilter.Off
            "error", "1" -> LevelFilter.Error
            "warn", "2" -> LevelFilter.Warn
            "info", "3" -> LevelFilter.Info
            "debug", "4" -> LevelFilter.Debug
            "trace", "5" -> LevelFilter.Trace
            else -> null
        }
    }

    internal fun build(): Filter = Filter(directives.toList(), regex)
}

/**
 * `Builder` acts as builder for initializing a `Logger`.
 *
 * It can be used to customize the log format, change the environment variable used
 * to provide the logging directives and also set the default log level filter.
 */
public class Builder internal constructor() {
    internal val filter: FilterBuilder = FilterBuilder()
    internal val writer: io.github.kotlinmania.envlogger.writer.Builder =
        io.github.kotlinmania.envlogger.writer
            .Builder()
    internal val format: io.github.kotlinmania.envlogger.fmt.Builder =
        io.github.kotlinmania.envlogger.fmt
            .Builder()
    internal var built: Boolean = false

    public companion object {
        /**
         * Initializes the log builder with defaults.
         */
        public fun default(): Builder = Builder()

        /**
         * Initializes the log builder with defaults.
         */
        public fun new(): Builder = Builder()

        /**
         * Initializes the log builder from the environment.
         */
        public fun fromEnv(env: Env): Builder {
            val builder = Builder.new()
            builder.parseEnv(env)
            return builder
        }

        /**
         * Initializes the log builder from the environment variable named [env].
         */
        public fun fromEnv(env: String): Builder = fromEnv(Env.new().filter(env))

        /**
         * Initializes the log builder from the environment using default variable names.
         */
        public fun fromDefaultEnv(): Builder = fromEnv(Env.default())
    }

    /**
     * Applies the configuration from the environment.
     */
    public fun parseEnv(env: Env): Builder {
        val filterStr = env.getFilter()
        if (filterStr != null) {
            parseFilters(filterStr)
        }

        val writeStyleStr = env.getWriteStyle()
        if (writeStyleStr != null) {
            parseWriteStyle(writeStyleStr)
        }

        return this
    }

    /**
     * Applies the configuration from the environment variable named [env].
     */
    public fun parseEnv(env: String): Builder = parseEnv(Env.new().filter(env))

    /**
     * Applies the configuration from the environment using default variable names.
     */
    public fun parseDefaultEnv(): Builder = parseEnv(Env.default())

    /**
     * Sets the format function for formatting the log output.
     */
    public fun format(format: (Formatter, Record) -> Unit): Builder {
        this.format.customFormat = RecordFormat { formatter, record -> format(formatter, record) }
        return this
    }

    /**
     * Use the default format.
     */
    public fun defaultFormat(): Builder {
        this.format.customFormat = null
        this.format.defaultFormat =
            io.github.kotlinmania.envlogger.fmt
                .ConfigurableFormat()
        return this
    }

    /**
     * Whether or not to write the level in the default format.
     */
    public fun formatLevel(write: Boolean): Builder {
        this.format.defaultFormat.level(write)
        return this
    }

    /**
     * Whether or not to write the source file path in the default format.
     */
    public fun formatFile(write: Boolean): Builder {
        this.format.defaultFormat.file(write)
        return this
    }

    /**
     * Whether or not to write the source line number path in the default format.
     */
    public fun formatLineNumber(write: Boolean): Builder {
        this.format.defaultFormat.lineNumber(write)
        return this
    }

    /**
     * Whether or not to write the source path and line number.
     */
    public fun formatSourcePath(write: Boolean): Builder {
        formatFile(write)
        formatLineNumber(write)
        return this
    }

    /**
     * Whether or not to write the module path in the default format.
     */
    public fun formatModulePath(write: Boolean): Builder {
        this.format.defaultFormat.modulePath(write)
        return this
    }

    /**
     * Whether or not to write the target in the default format.
     */
    public fun formatTarget(write: Boolean): Builder {
        this.format.defaultFormat.target(write)
        return this
    }

    /**
     * Configures the amount of spaces to use to indent multiline log records.
     */
    public fun formatIndent(indent: Int?): Builder {
        this.format.defaultFormat.indent(indent)
        return this
    }

    /**
     * Configures if timestamp should be included and in what precision.
     */
    public fun formatTimestamp(timestamp: TimestampPrecision?): Builder {
        this.format.defaultFormat.timestamp(timestamp)
        return this
    }

    /**
     * Configures the timestamp to use second precision.
     */
    public fun formatTimestampSecs(): Builder = formatTimestamp(TimestampPrecision.Seconds)

    /**
     * Configures the timestamp to use millisecond precision.
     */
    public fun formatTimestampMillis(): Builder = formatTimestamp(TimestampPrecision.Millis)

    /**
     * Configures the timestamp to use microsecond precision.
     */
    public fun formatTimestampMicros(): Builder = formatTimestamp(TimestampPrecision.Micros)

    /**
     * Configures the timestamp to use nanosecond precision.
     */
    public fun formatTimestampNanos(): Builder = formatTimestamp(TimestampPrecision.Nanos)

    /**
     * Configures the end of line suffix.
     */
    public fun formatSuffix(suffix: String): Builder {
        this.format.defaultFormat.suffix(suffix)
        return this
    }

    /**
     * Set the format for structured key/value pairs in the log record.
     */
    public fun formatKeyValues(format: (Formatter, io.github.kotlinmania.log.kv.Source) -> Unit): Builder {
        this.format.defaultFormat.keyValues { formatter, source -> format(formatter, source) }
        return this
    }

    /**
     * Adds a directive to the filter for a specific module.
     */
    public fun filterModule(module: String, level: LevelFilter): Builder {
        this.filter.filterModule(module, level)
        return this
    }

    /**
     * Adds a directive to the filter for all modules.
     */
    public fun filterLevel(level: LevelFilter): Builder {
        this.filter.filterLevel(level)
        return this
    }

    /**
     * Adds filters to the logger.
     */
    public fun filter(module: String?, level: LevelFilter): Builder {
        this.filter.filter(module, level)
        return this
    }

    /**
     * Parses the directives string in the same form as the environment variable.
     */
    public fun parseFilters(filters: String): Builder {
        this.filter.parse(filters)
        return this
    }

    /**
     * Sets the target for the log output.
     */
    public fun target(target: Target): Builder {
        this.writer.target(target)
        return this
    }

    /**
     * Sets whether or not styles will be written.
     */
    public fun writeStyle(writeStyle: WriteStyle): Builder {
        this.writer.writeStyle(writeStyle)
        return this
    }

    /**
     * Parses whether or not to write styles in the same form as the environment variable.
     */
    public fun parseWriteStyle(writeStyle: String): Builder {
        this.writer.parseWriteStyle(writeStyle)
        return this
    }

    /**
     * Sets whether or not the logger will be used in unit tests.
     */
    public fun isTest(isTest: Boolean): Builder {
        this.writer.isTest(isTest)
        return this
    }

    /**
     * Initializes the global logger with the built env logger.
     */
    public fun tryInit(): Result<Unit> {
        val logger = build()
        val maxLevel = logger.filter()
        val r = setLogger(logger)
        if (r.isSuccess) {
            setMaxLevel(maxLevel)
        }
        return r
    }

    /**
     * Initializes the global logger with the built env logger.
     */
    public fun init() {
        tryInit().getOrThrow()
    }

    /**
     * Build an env logger.
     */
    public fun build(): Logger {
        check(!built) { "attempt to re-use consumed builder" }
        built = true

        return Logger(
            writer = this.writer.build(),
            filter = this.filter.build(),
            format = this.format.build(),
        )
    }

    /**
     * Formats builder representation.
     */
    public fun fmt(): String = toString()

    override fun toString(): String =
        if (built) {
            "Logger(built=true)"
        } else {
            "Logger(filter=$filter, writer=$writer)"
        }
}

/**
 * The env logger.
 *
 * This class implements the [Log] interface from the log module,
 * which allows it to act as a logger.
 */
public class Logger internal constructor(
    private val writer: Writer,
    private val filter: Filter,
    private val format: RecordFormat,
) : Log {
    public companion object {
        /**
         * Creates the logger from the environment.
         */
        public fun fromEnv(env: Env): Logger = Builder.fromEnv(env).build()

        /**
         * Creates the logger from the environment variable named [env].
         */
        public fun fromEnv(env: String): Logger = Builder.fromEnv(env).build()

        /**
         * Creates the logger from the environment using default variable names.
         */
        public fun fromDefaultEnv(): Logger = Builder.fromDefaultEnv().build()
    }

    /**
     * Returns the maximum [LevelFilter] that this env logger instance is configured to output.
     */
    public fun filter(): LevelFilter = filter.maxLevel()

    /**
     * Checks if this record matches the configured filter.
     */
    public fun matches(record: Record): Boolean = filter.matches(record)

    override fun enabled(metadata: Metadata): Boolean = filter.enabled(metadata)

    override fun log(record: Record) {
        if (matches(record)) {
            val formatter = Formatter(writer)
            try {
                format.format(formatter, record)
                formatter.print(writer)
            } finally {
                formatter.clear()
            }
        }
    }

    override fun flush() {}

    /**
     * Formats logger representation.
     */
    public fun fmt(): String = toString()

    override fun toString(): String = "Logger(filter=$filter)"
}

/**
 * Set of environment variables to configure from.
 */
public class Env internal constructor(
    internal var filterVar: Var,
    internal var writeStyleVar: Var,
) {
    public companion object {
        /**
         * Get a default set of environment variables.
         */
        public fun default(): Env =
            Env(
                filterVar = Var.new(DEFAULT_FILTER_ENV),
                writeStyleVar = Var.new(DEFAULT_WRITE_STYLE_ENV),
            )

        /**
         * Get a default set of environment variables.
         */
        public fun new(): Env = default()

        /**
         * Create an [Env] configured with a custom filter environment variable name.
         */
        public fun from(filterEnv: String): Env = default().filter(filterEnv)
    }

    /**
     * Specify an environment variable to read the filter from.
     */
    public fun filter(filterEnv: String): Env {
        this.filterVar = Var.new(filterEnv)
        return this
    }

    /**
     * Specify an environment variable to read the filter from, with a fallback default.
     */
    public fun filterOr(filterEnv: String, default: String): Env {
        this.filterVar = Var.newWithDefault(filterEnv, default)
        return this
    }

    /**
     * Use the default environment variable to read the filter from, with a fallback default.
     */
    public fun defaultFilterOr(default: String): Env {
        this.filterVar = Var.newWithDefault(DEFAULT_FILTER_ENV, default)
        return this
    }

    internal fun getFilter(): String? = filterVar.get()

    /**
     * Specify an environment variable to read the style from.
     */
    public fun writeStyle(writeStyleEnv: String): Env {
        this.writeStyleVar = Var.new(writeStyleEnv)
        return this
    }

    /**
     * Specify an environment variable to read the style from, with a fallback default.
     */
    public fun writeStyleOr(writeStyleEnv: String, default: String): Env {
        this.writeStyleVar = Var.newWithDefault(writeStyleEnv, default)
        return this
    }

    /**
     * Use the default environment variable to read the style from, with a fallback default.
     */
    public fun defaultWriteStyleOr(default: String): Env {
        this.writeStyleVar = Var.newWithDefault(DEFAULT_WRITE_STYLE_ENV, default)
        return this
    }

    internal fun getWriteStyle(): String? = writeStyleVar.get()

    override fun toString(): String = "Env(filter=$filterVar, writeStyle=$writeStyleVar)"
}

/**
 * Variable holder for an environment variable and fallback default.
 */
internal data class Var(
    val name: String,
    val default: String?,
) {
    internal fun get(): String? = getEnvVar(name) ?: default

    internal companion object {
        internal fun new(name: String): Var = Var(name = name, default = null)

        internal fun newWithDefault(name: String, default: String): Var = Var(name = name, default = default)
    }
}

/**
 * Attempts to initialize the global logger with an env logger.
 */
public fun tryInit(): Result<Unit> = tryInitFromEnv(Env.default())

/**
 * Initializes the global logger with an env logger.
 */
public fun initLogger() {
    tryInit().getOrThrow()
}

/**
 * Attempts to initialize the global logger with an env logger from the given environment variables.
 */
public fun tryInitFromEnv(env: Env): Result<Unit> {
    val builder = Builder.fromEnv(env)
    return builder.tryInit()
}

/**
 * Attempts to initialize the global logger with an env logger from the given environment variable name.
 */
public fun tryInitFromEnv(env: String): Result<Unit> = tryInitFromEnv(Env.new().filter(env))

/**
 * Initializes the global logger with an env logger from the given environment variables.
 */
public fun initFromEnv(env: Env) {
    tryInitFromEnv(env).getOrThrow()
}

/**
 * Initializes the global logger with an env logger from the given environment variable name.
 */
public fun initFromEnv(env: String) {
    tryInitFromEnv(env).getOrThrow()
}

/**
 * Create a new builder with the default environment variables.
 */
public fun builder(): Builder = Builder.fromDefaultEnv()

/**
 * Create a builder from the given environment variables.
 */
public fun fromEnv(env: Env): Builder = Builder.fromEnv(env)

/**
 * Create a builder from the given environment variable name.
 */
public fun fromEnv(env: String): Builder = Builder.fromEnv(env)
