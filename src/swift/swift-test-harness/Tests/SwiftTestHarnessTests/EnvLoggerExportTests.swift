import Testing
import EnvLogger

// Smoke test for the Kotlin → Swift Export → SPM → swift test pipeline.
//
// The file's mere existence and successful compilation prove three layers
// of the pipeline:
//
//   1. `embedSwiftExportForXcode` produced `EnvLogger.swiftmodule/`
//      and the supporting KotlinRuntimeSupport / ExportedKotlinPackages /
//      KotlinRuntime swiftmodule bundles. If any of them were missing,
//      `import EnvLogger` above would fail at compile time.
//
//   2. The static archive `libEnvLogger.a` (produced by the
//      `linkSwiftExportBinaryDebugStaticMacosArm64` and
//      `mergeMacosDebugSwiftExportLibraries` tasks) supplied every
//      `__root____*` and `KotlinError`-related symbol the Swift modules
//      reference. If the archive were missing or empty, this test
//      executable would fail to link with "undefined symbols for
//      architecture arm64".
//
//   3. The Kotlin `swiftExport { moduleName = "EnvLogger" }` and
//      `flattenPackage = "io.github.kotlinmania.envlogger"` configuration in
//      build.gradle.kts produced a module name that's both syntactically
//      valid as a Swift identifier and reachable from this Package.swift
//      via the `EnvLoggerLibrary` product.
//
// Add more meaningful per-API tests below as the Swift Export surface
// grows. For now the import + a single passing assertion is the
// canary that the pipeline is green for this repo.
@Suite
struct EnvLoggerExportTests {
    @Test
    func envAndConstantsExposeDefaults() {
        #expect(DEFAULT_FILTER_ENV == "RUST_LOG")
        #expect(DEFAULT_WRITE_STYLE_ENV == "RUST_LOG_STYLE")

        let env = Env.Companion.shared.default()
            .filterOr(filterEnv: "CUSTOM_LOG", default: "info")
            .writeStyleOr(writeStyleEnv: "CUSTOM_STYLE", default: "never")
        #expect(!env.toString().isEmpty)
    }

    @Test
    func builderConfiguresAndBuildsLogger() {
        let built = builder()
            .defaultFormat()
            .parseFilters(filters: "warn,env_logger_swift=debug")
            .parseWriteStyle(writeStyle: "never")
            .writeStyle(writeStyle: writer.WriteStyle.Always)
            .formatTimestamp(timestamp: fmt.TimestampPrecision.Millis)
            .formatFile(write: true)
            .formatIndent(indent: 2)
            .isTest(isTest: true)
            .target(target: writer.Target.Stderr.shared)
        #expect(!built.fmt().isEmpty)

        let logger = built.build()
        #expect(!logger.fmt().isEmpty)
        logger.flush()
    }

    @Test
    func writerEnumsAndTargetsRoundTrip() {
        #expect(writer.WriteStyle.allCases.count == 3)
        #expect(writer.WriteStyle(rawValue: writer.WriteStyle.Auto.rawValue) == .Auto)
        #expect(fmt.TimestampPrecision.allCases.count == 4)
        #expect(fmt.TimestampPrecision.Companion.shared.default() == .Seconds)

        switch writer.Target.Stderr.shared.sealedType() {
        case .stderr:
            break
        case .pipe, .stdout:
            Issue.record("Expected Stderr target")
        }
    }
}
