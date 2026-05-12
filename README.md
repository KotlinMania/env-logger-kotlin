# env-logger-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Fenv--logger--kotlin-blue.svg)](https://github.com/KotlinMania/env-logger-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/env-logger-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/env-logger-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/env-logger-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/env-logger-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`rust-cli/env_logger`](https://github.com/rust-cli/env_logger).

**Original Project:** This port is based on [`rust-cli/env_logger`](https://github.com/rust-cli/env_logger). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `rust-cli/env_logger`

> The text below is reproduced and lightly edited from [`https://github.com/rust-cli/env_logger`](https://github.com/rust-cli/env_logger). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs so they continue to resolve from this repository.

## env_logger

[![crates.io](https://img.shields.io/crates/v/env_logger.svg)](https://crates.io/crates/env_logger)
[![Documentation](https://docs.rs/env_logger/badge.svg)](https://docs.rs/env_logger)

Implements a logger that can be configured via environment variables.

## Usage

### In libraries

`env_logger` makes sense when used in executables (binary projects). Libraries should use the [`log`](https://docs.rs/log) crate instead.

### In executables

It must be added along with `log` to the project dependencies:

```console
$ cargo add log env_logger
```

`env_logger` must be initialized as early as possible in the project. After it's initialized, you can use the `log` macros to do actual logging.

```rust
use log::info;

fn main() {
    env_logger::init();

    info!("starting up");

    // ...
}
```

Then when running the executable, specify a value for the **`RUST_LOG`**
environment variable that corresponds with the log messages you want to show.

```bash
$ RUST_LOG=info ./main
[2018-11-03T06:09:06Z INFO  default] starting up
```

The letter case is not significant for the logging level names; e.g., `debug`,
`DEBUG`, and `dEbuG` all represent the same logging level. Therefore, the
previous example could also have been written this way, specifying the log
level as `INFO` rather than as `info`:

```bash
$ RUST_LOG=INFO ./main
[2018-11-03T06:09:06Z INFO  default] starting up
```

So which form should you use? For consistency, our convention is to use lower
case names. Where our docs do use other forms, they do so in the context of
specific examples, so you won't be surprised if you see similar usage in the
wild.

The log levels that may be specified correspond to the [`log::Level`][level-enum]
enum from the `log` crate. They are:

   * `error`
   * `warn`
   * `info`
   * `debug`
   * `trace`

[level-enum]:  https://docs.rs/log/latest/log/enum.Level.html  "log::Level (docs.rs)"

There is also a pseudo logging level, `off`, which may be specified to disable
all logging for a given module or for the entire application. As with the
logging levels, the letter case is not significant.

`env_logger` can be configured in other ways besides an environment variable. See [the examples](https://github.com/rust-cli/env_logger/tree/main/examples) for more approaches.

### In tests

Tests can use the `env_logger` crate to see log messages generated during that test:

```console
$ cargo add log
$ cargo add --dev env_logger
```

```rust
use log::info;

fn add_one(num: i32) -> i32 {
    info!("add_one called with {}", num);
    num + 1
}

#[cfg(test)]
mod tests {
    use super::*;

    fn init() {
        let _ = env_logger::builder().is_test(true).try_init();
    }

    #[test]
    fn it_adds_one() {
        init();

        info!("can log from the test too");
        assert_eq!(3, add_one(2));
    }

    #[test]
    fn it_handles_negative_numbers() {
        init();

        info!("logging from another test");
        assert_eq!(-7, add_one(-8));
    }
}
```

Assuming the module under test is called `my_lib`, running the tests with the
`RUST_LOG` filtering to info messages from this module looks like:

```bash
$ RUST_LOG=my_lib=info cargo test
     Running target/debug/my_lib-...

running 2 tests
[INFO my_lib::tests] logging from another test
[INFO my_lib] add_one called with -8
test tests::it_handles_negative_numbers ... ok
[INFO my_lib::tests] can log from the test too
[INFO my_lib] add_one called with 2
test tests::it_adds_one ... ok

test result: ok. 2 passed; 0 failed; 0 ignored; 0 measured
```

Note that `env_logger::try_init()` needs to be called in each test in which you
want to enable logging. Additionally, the default behavior of tests to
run in parallel means that logging output may be interleaved with test output.
Either run tests in a single thread by specifying `RUST_TEST_THREADS=1` or by
running one test by specifying its name as an argument to the test binaries as
directed by the `cargo test` help docs:

```bash
$ RUST_LOG=my_lib=info cargo test it_adds_one
     Running target/debug/my_lib-...

running 1 test
[INFO my_lib::tests] can log from the test too
[INFO my_lib] add_one called with 2
test tests::it_adds_one ... ok

test result: ok. 1 passed; 0 failed; 0 ignored; 0 measured
```

## Configuring log target

By default, `env_logger` logs to stderr. If you want to log to stdout instead,
you can use the `Builder` to change the log target:

```rust
use std::env;
use env_logger::{Builder, Target};

let mut builder = Builder::from_default_env();
builder.target(Target::Stdout);

builder.init();
```

## Stability of the default format

The default format won't optimise for long-term stability, and explicitly makes no guarantees about the stability of its output across major, minor or patch version bumps during `0.x`.

If you want to capture or interpret the output of `env_logger` programmatically then you should use a custom format.

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:env-logger-kotlin:0.1.0")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same MIT license as the upstream [`rust-cli/env_logger`](https://github.com/rust-cli/env_logger). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the env_logger authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`rust-cli/env_logger`](https://github.com/rust-cli/env_logger) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
