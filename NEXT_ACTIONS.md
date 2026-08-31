# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/8 (100.0%)
- **Function parity:** 79/79 matched (target 137) — 100.0%
- **Class/type parity:** 13/13 matched (target 33) — 100.0%
- **Combined symbol parity:** 92/92 matched (target 170) — 100.0%
- **Average inline-code cosine:** 0.65 (function body across 6 matched files)
- **Average documentation cosine:** 0.85 (doc text across 6 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 5 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. writer.target

- **Target:** `writer.Target`
- **Similarity:** 0.15
- **Dependents:** 2
- **Priority Score:** 2000208.5
- **Functions:** 1/1 matched (target 8)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_

### 2. writer.buffer

- **Target:** `writer.Buffer`
- **Similarity:** 0.57
- **Dependents:** 1
- **Priority Score:** 1001504.3
- **Functions:** 12/12 matched (target 20)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 10)
- **Missing types:** _none_

### 3. logger

- **Target:** `envlogger.Logger`
- **Similarity:** 0.83
- **Dependents:** 0
- **Priority Score:** 5901.7
- **Functions:** 55/55 matched (target 91)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 8)
- **Missing types:** _none_
- **Tests:** 5/5 matched

### 4. fmt.humantime

- **Target:** `fmt.Humantime`
- **Similarity:** 0.57
- **Dependents:** 0
- **Priority Score:** 904.3
- **Functions:** 7/7 matched (target 11)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 5. fmt.kv

- **Target:** `fmt.Kv`
- **Similarity:** 0.79
- **Dependents:** 0
- **Priority Score:** 602.1
- **Functions:** 4/4 matched (target 7)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

### 6. lib

- **Target:** `envlogger.Lib`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 100.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 3)
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
| `fmt.mod` | `fmt.Mod` | `fmt/mod` |
| `writer.mod` | `writer.Mod` | `writer/mod` |

