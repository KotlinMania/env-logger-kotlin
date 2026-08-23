// port-lint: source logger.rs
package io.github.kotlinmania.envlogger

import io.github.kotlinmania.log.LevelFilter
import kotlin.test.Test
import kotlin.test.assertEquals

class LoggerTest {
    @Test
    fun envGetFilterReadsFromVarIfSet() {
        setEnvVar("env_get_filter_reads_from_var_if_set", "from var")

        val env = Env.new().filterOr("env_get_filter_reads_from_var_if_set", "from default")

        assertEquals("from var", env.getFilter())
    }

    @Test
    fun envGetFilterReadsFromDefaultIfVarNotSet() {
        removeEnvVar("env_get_filter_reads_from_default_if_var_not_set")

        val env =
            Env.new().filterOr(
                "env_get_filter_reads_from_default_if_var_not_set",
                "from default",
            )

        assertEquals("from default", env.getFilter())
    }

    @Test
    fun envGetWriteStyleReadsFromVarIfSet() {
        setEnvVar("env_get_write_style_reads_from_var_if_set", "from var")

        val env =
            Env.new().writeStyleOr("env_get_write_style_reads_from_var_if_set", "from default")

        assertEquals("from var", env.getWriteStyle())
    }

    @Test
    fun envGetWriteStyleReadsFromDefaultIfVarNotSet() {
        removeEnvVar("env_get_write_style_reads_from_default_if_var_not_set")

        val env =
            Env.new().writeStyleOr(
                "env_get_write_style_reads_from_default_if_var_not_set",
                "from default",
            )

        assertEquals("from default", env.getWriteStyle())
    }

    @Test
    fun builderParseEnvOverridesExistingFilters() {
        setEnvVar(
            "builder_parse_default_env_overrides_existing_filters",
            "debug",
        )
        val env = Env.new().filter("builder_parse_default_env_overrides_existing_filters")

        val builder = Builder.new()
        builder.filterLevel(LevelFilter.Trace)
        // Overrides global level to debug
        builder.parseEnv(env)

        assertEquals(LevelFilter.Debug, builder.filter.build().maxLevel())
    }
}
