# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/15 (53.3%)
- **Function parity:** 134/150 matched (target 213) — 89.3%
- **Class/type parity:** 25/26 matched (target 45) — 96.2%
- **Combined symbol parity:** 159/176 matched (target 258) — 90.3%
- **Average inline-code cosine:** 0.65 (function body across 6 matched files)
- **Average documentation cosine:** 0.85 (doc text across 6 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 5 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. writer.target

- **Target:** `writer.Target [PROVENANCE-FALLBACK]`
- **Similarity:** 0.15
- **Dependents:** 2
- **Priority Score:** 2000208.5
- **Functions:** 1/1 matched (target 8)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `writer/target.rs` vs expected `writer/target.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:writer/target.rs` vs expected `writer/target.rs`
- **Proposed provenance header:** `// port-lint: source writer/target.rs` (current: `// port-lint: source writer/target.rs`)
- **Proposed provenance header:** `// port-lint: tests writer/target.rs` (current: `// port-lint: tests writer/target.rs`)
- **Lint issues:** 2

### 2. writer.buffer

- **Target:** `writer.Buffer [PROVENANCE-FALLBACK]`
- **Similarity:** 0.57
- **Dependents:** 1
- **Priority Score:** 1001504.3
- **Functions:** 12/12 matched (target 15)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 8)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `writer/buffer.rs` vs expected `writer/buffer.rs`
- **Proposed provenance header:** `// port-lint: source writer/buffer.rs` (current: `// port-lint: source writer/buffer.rs`)
- **Lint issues:** 1

### 3. fmt.mod

- **Target:** `fmt.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 35510.0
- **Functions:** 43/45 matched (target 60)
- **Missing functions:** `write_record`, `formatter`
- **Types:** 9/10 matched
- **Missing types:** `RecordFormat`
- **Tests:** 13/15 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/mod.rs` vs expected `fmt/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:fmt/mod.rs` vs expected `fmt/mod.rs`
- **Proposed provenance header:** `// port-lint: source fmt/mod.rs` (current: `// port-lint: source fmt/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests fmt/mod.rs` (current: `// port-lint: tests fmt/mod.rs`)
- **Lint issues:** 2

### 4. logger

- **Target:** `envlogger.Logger [PROVENANCE-FALLBACK]`
- **Similarity:** 0.84
- **Dependents:** 0
- **Priority Score:** 5901.6
- **Functions:** 55/55 matched (target 88)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 8)
- **Missing types:** _none_
- **Tests:** 5/5 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `logger.rs` vs expected `logger.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `logger.rs` vs expected `logger.rs`
- **Proposed provenance header:** `// port-lint: source logger.rs` (current: `// port-lint: source logger.rs`)
- **Proposed provenance header:** `// port-lint: source logger.rs` (current: `// port-lint: source logger.rs`)
- **Lint issues:** 2

### 5. writer.mod

- **Target:** `writer.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 1510.0
- **Functions:** 12/12 matched (target 27)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_
- **Tests:** 2/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `writer/mod.rs` vs expected `writer/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:writer/mod.rs` vs expected `writer/mod.rs`
- **Proposed provenance header:** `// port-lint: source writer/mod.rs` (current: `// port-lint: source writer/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests writer/mod.rs` (current: `// port-lint: tests writer/mod.rs`)
- **Lint issues:** 2

### 6. fmt.humantime

- **Target:** `fmt.Humantime [PROVENANCE-FALLBACK]`
- **Similarity:** 0.57
- **Dependents:** 0
- **Priority Score:** 904.3
- **Functions:** 7/7 matched (target 11)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 1/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/humantime.rs` vs expected `fmt/humantime.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:fmt/humantime.rs` vs expected `fmt/humantime.rs`
- **Proposed provenance header:** `// port-lint: source fmt/humantime.rs` (current: `// port-lint: source fmt/humantime.rs`)
- **Proposed provenance header:** `// port-lint: tests fmt/humantime.rs` (current: `// port-lint: tests fmt/humantime.rs`)
- **Lint issues:** 2

### 7. fmt.kv

- **Target:** `fmt.Kv [PROVENANCE-FALLBACK]`
- **Similarity:** 0.79
- **Dependents:** 0
- **Priority Score:** 602.1
- **Functions:** 4/4 matched
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fmt/kv.rs` vs expected `fmt/kv.rs`
- **Proposed provenance header:** `// port-lint: source fmt/kv.rs` (current: `// port-lint: source fmt/kv.rs`)
- **Lint issues:** 1

### 8. lib

- **Target:** `envlogger.Lib [PROVENANCE-FALLBACK]`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 100.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Lint issues:** 2

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

