pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins { kotlin("multiplatform") version "2.3.21" }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "env-logger-kotlin"

// log-kotlin:0.1.0 is not yet on Maven Central. When the sibling source
// checkout is present alongside this repo in the kotlinmania workspace, wire
// it in as a Gradle composite build so the Maven coordinate
// `io.github.kotlinmania:log-kotlin:0.1.0` resolves to that included build's
// project instead of failing dependency resolution. When the sibling
// directory is absent (e.g. on a stand-alone CI checkout), this block is a
// no-op and resolution falls through to Maven Central — which is the path
// that will succeed once log-kotlin publishes its 0.1.0 release.
val logKotlinSibling = rootDir.parentFile?.resolve("log-kotlin")
if (logKotlinSibling != null &&
    logKotlinSibling.resolve("settings.gradle.kts").exists() &&
    logKotlinSibling.resolve("build.gradle.kts").exists()
) {
    includeBuild(logKotlinSibling)
}
