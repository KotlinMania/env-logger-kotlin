# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 6/8 (75.0%)
- **Function parity:** 91/93 matched (target 140) — 97.8%
- **Class/type parity:** 12/12 matched (target 30) — 100.0%
- **Combined symbol parity:** 103/105 matched (target 170) — 98.1%
- **Average inline-code cosine:** 0.35 (function body across 6 matched files)
- **Average documentation cosine:** 0.71 (doc text across 6 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 5 files with <0.60 function similarity
- **Needs Review:** 0 files with 0.60-0.84 function similarity
- **Excellent:** 1 files with >=0.85 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. writer.target

- **Target:** `writer.Target`
- **Similarity:** 0.04
- **Dependents:** 2
- **Priority Score:** 2000209.6
- **Functions:** 1/1 matched (target 8)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_

### 2. writer.buffer

- **Target:** `writer.Buffer`
- **Similarity:** 0.18
- **Dependents:** 1
- **Priority Score:** 1001608.2
- **Functions:** 13/13 matched (target 23)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 10)
- **Missing types:** _none_

### 3. logger

- **Target:** `envlogger.Logger`
- **Similarity:** 0.38
- **Dependents:** 0
- **Priority Score:** 17006.2
- **Functions:** 65/66 matched (target 91)
- **Missing functions:** `init`
- **Types:** 4/4 matched (target 8)
- **Missing types:** _none_
- **Tests:** 5/5 matched

### 4. fmt.humantime

- **Target:** `fmt.Humantime`
- **Similarity:** 0.16
- **Dependents:** 0
- **Priority Score:** 11108.4
- **Functions:** 8/9 matched (target 11)
- **Missing functions:** `fmt`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 5. fmt.kv

- **Target:** `fmt.Kv`
- **Similarity:** 0.35
- **Dependents:** 0
- **Priority Score:** 606.5
- **Functions:** 4/4 matched (target 7)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Matched

| Source | Target | Path |
|--------|--------|------|
| `lib` | `envlogger.PipeSink` | `lib` |

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `fmt.mod` | `fmt.Mod` | 0 | `fmt/mod.rs` | `fmt/Mod.kt` |
| `writer.mod` | `writer.Mod` | 0 | `writer/mod.rs` | `writer/Mod.kt` |

