# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 7/8 (87.5%)
- **Function parity:** 72/147 matched (target 110) — 49.0%
- **Class/type parity:** 16/26 matched (target 30) — 61.5%
- **Combined symbol parity:** 88/173 matched (target 140) — 50.9%
- **Average inline-code cosine:** 0.00 (function body across 4 matched files)
- **Average documentation cosine:** 0.80 (doc text across 4 matched files)
- **Cheat-zeroed Files:** 7
- **Critical Issues:** 7 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. writer.target

- **Target:** `writer.Target [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2010210.0
- **Functions:** 0/1 matched (target 7)
- **Missing functions:** `fmt`
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/writer/target.rs` vs expected `writer/target.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/writer/target.rs` vs expected `writer/target.rs`
- **Proposed provenance header:** `// port-lint: source writer/target.rs` (current: `// port-lint: source src/writer/target.rs`)
- **Proposed provenance header:** `// port-lint: source writer/target.rs` (current: `// port-lint: source src/writer/target.rs`)
- **Lint issues:** 2

### 2. writer.buffer

- **Target:** `writer.Buffer [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1021510.0
- **Functions:** 10/12 matched (target 13)
- **Missing functions:** `adapt`, `fmt`
- **Types:** 3/3 matched (target 8)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/writer/buffer.rs` vs expected `writer/buffer.rs`
- **Proposed provenance header:** `// port-lint: source writer/buffer.rs` (current: `// port-lint: source src/writer/buffer.rs`)
- **Lint issues:** 1

### 3. fmt.mod

- **Target:** `fmt.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 75510.0
- **Functions:** 41/45 matched (target 52)
- **Missing functions:** `new`, `fmt`, `write_record`, `formatter`
- **Types:** 7/10 matched (target 8)
- **Missing types:** `RecordFormat`, `FormatFn`, `IndentWrapper`
- **Tests:** 13/15 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/fmt/mod.rs` vs expected `fmt/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/fmt/mod.rs` vs expected `fmt/mod.rs`
- **Proposed provenance header:** `// port-lint: source fmt/mod.rs` (current: `// port-lint: source src/fmt/mod.rs`)
- **Proposed provenance header:** `// port-lint: source fmt/mod.rs` (current: `// port-lint: source src/fmt/mod.rs`)
- **Lint issues:** 2

### 4. fmt.humantime

- **Target:** `fmt.Humantime [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 20910.0
- **Functions:** 6/7 matched (target 8)
- **Missing functions:** `fmt`
- **Types:** 1/2 matched
- **Missing types:** `TimestampValue`
- **Tests:** 1/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/fmt/humantime.rs` vs expected `fmt/humantime.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/fmt/humantime.rs` vs expected `fmt/humantime.rs`
- **Proposed provenance header:** `// port-lint: source fmt/humantime.rs` (current: `// port-lint: source src/fmt/humantime.rs`)
- **Proposed provenance header:** `// port-lint: source fmt/humantime.rs` (current: `// port-lint: source src/fmt/humantime.rs`)
- **Lint issues:** 2

### 5. writer.mod

- **Target:** `writer.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 11510.0
- **Functions:** 11/12 matched (target 26)
- **Missing functions:** `from`
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_
- **Tests:** 2/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/writer/mod.rs` vs expected `writer/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/writer/mod.rs` vs expected `writer/mod.rs`
- **Proposed provenance header:** `// port-lint: source writer/mod.rs` (current: `// port-lint: source src/writer/mod.rs`)
- **Proposed provenance header:** `// port-lint: source writer/mod.rs` (current: `// port-lint: source src/writer/mod.rs`)
- **Lint issues:** 2

### 6. fmt.kv

- **Target:** `fmt.Kv [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10610.0
- **Functions:** 4/4 matched
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 1)
- **Missing types:** `KvFormatFn`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/fmt/kv.rs` vs expected `fmt/kv.rs`
- **Proposed provenance header:** `// port-lint: source fmt/kv.rs` (current: `// port-lint: source src/fmt/kv.rs`)
- **Lint issues:** 1

### 7. lib

- **Target:** `envlogger.Lib [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 0)
- **Missing types:** `ReadmeDoctests`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source src/lib.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

