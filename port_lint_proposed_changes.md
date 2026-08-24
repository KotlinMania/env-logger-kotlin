# port-lint Proposed Changes

**Generated:** 2026-08-24
**Source:** tmp/env_logger
**Target:** src/commonMain/kotlin

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/kotlinmania/envlogger/writer/Target.kt` | `// port-lint: source writer/target.rs` | `// port-lint: source writer/target.rs` | `writer/target.rs` | `port-lint provenance header matched only after fallback normalization: 'writer/target.rs' vs expected 'writer/target.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/envlogger/writer/TargetTest.kt` | `// port-lint: tests writer/target.rs` | `// port-lint: tests writer/target.rs` | `writer/target.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:writer/target.rs' vs expected 'writer/target.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/envlogger/writer/Buffer.kt` | `// port-lint: source writer/buffer.rs` | `// port-lint: source writer/buffer.rs` | `writer/buffer.rs` | `port-lint provenance header matched only after fallback normalization: 'writer/buffer.rs' vs expected 'writer/buffer.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/envlogger/fmt/Mod.kt` | `// port-lint: source fmt/mod.rs` | `// port-lint: source fmt/mod.rs` | `fmt/mod.rs` | `port-lint provenance header matched only after fallback normalization: 'fmt/mod.rs' vs expected 'fmt/mod.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/envlogger/fmt/ModTest.kt` | `// port-lint: tests fmt/mod.rs` | `// port-lint: tests fmt/mod.rs` | `fmt/mod.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:fmt/mod.rs' vs expected 'fmt/mod.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/envlogger/Logger.kt` | `// port-lint: source logger.rs` | `// port-lint: source logger.rs` | `logger.rs` | `port-lint provenance header matched only after fallback normalization: 'logger.rs' vs expected 'logger.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/envlogger/LoggerTest.kt` | `// port-lint: source logger.rs` | `// port-lint: source logger.rs` | `logger.rs` | `port-lint provenance header matched only after fallback normalization: 'logger.rs' vs expected 'logger.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/envlogger/writer/Mod.kt` | `// port-lint: source writer/mod.rs` | `// port-lint: source writer/mod.rs` | `writer/mod.rs` | `port-lint provenance header matched only after fallback normalization: 'writer/mod.rs' vs expected 'writer/mod.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/envlogger/writer/ModTest.kt` | `// port-lint: tests writer/mod.rs` | `// port-lint: tests writer/mod.rs` | `writer/mod.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:writer/mod.rs' vs expected 'writer/mod.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/envlogger/fmt/Humantime.kt` | `// port-lint: source fmt/humantime.rs` | `// port-lint: source fmt/humantime.rs` | `fmt/humantime.rs` | `port-lint provenance header matched only after fallback normalization: 'fmt/humantime.rs' vs expected 'fmt/humantime.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/envlogger/fmt/HumantimeTest.kt` | `// port-lint: tests fmt/humantime.rs` | `// port-lint: tests fmt/humantime.rs` | `fmt/humantime.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:fmt/humantime.rs' vs expected 'fmt/humantime.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/envlogger/fmt/Kv.kt` | `// port-lint: source fmt/kv.rs` | `// port-lint: source fmt/kv.rs` | `fmt/kv.rs` | `port-lint provenance header matched only after fallback normalization: 'fmt/kv.rs' vs expected 'fmt/kv.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/envlogger/Lib.kt` | `// port-lint: source lib.rs` | `// port-lint: source lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'lib.rs' vs expected 'lib.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/envlogger/PipeSink.kt` | `// port-lint: source lib.rs` | `// port-lint: source lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'lib.rs' vs expected 'lib.rs'` |
