# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/8 (100.0%)
- **Function parity:** 152/162 matched (target 226) — 93.8%
- **Class/type parity:** 25/25 matched (target 46) — 100.0%
- **Combined symbol parity:** 177/187 matched (target 272) — 94.7%
- **Average inline-code cosine:** 0.34 (function body across 6 matched files)
- **Average documentation cosine:** 0.85 (doc text across 6 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 7 files with <0.60 function similarity
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
- **Similarity:** 0.01
- **Dependents:** 2
- **Priority Score:** 2000210.0
- **Functions:** 1/1 matched (target 8)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_

### 2. writer.buffer

- **Target:** `writer.Buffer`
- **Similarity:** 0.17
- **Dependents:** 1
- **Priority Score:** 1011608.2
- **Functions:** 12/13 matched (target 20)
- **Missing functions:** `fmt`
- **Types:** 3/3 matched (target 10)
- **Missing types:** _none_

### 3. fmt.mod

- **Target:** `fmt.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 66410.0
- **Functions:** 48/54 matched (target 62)
- **Missing functions:** `default`, `flush`, `write_record`, `write_target`, `write`, `formatter`
- **Types:** 10/10 matched (target 11)
- **Missing types:** _none_
- **Tests:** 13/17 matched

### 4. logger

- **Target:** `envlogger.Logger`
- **Similarity:** 0.38
- **Dependents:** 0
- **Priority Score:** 17006.2
- **Functions:** 65/66 matched (target 91)
- **Missing functions:** `init`
- **Types:** 4/4 matched (target 8)
- **Missing types:** _none_
- **Tests:** 5/5 matched

### 5. writer.mod

- **Target:** `writer.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 11810.0
- **Functions:** 14/15 matched (target 27)
- **Missing functions:** `from`
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 6. fmt.humantime

- **Target:** `fmt.Humantime`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 11108.5
- **Functions:** 8/9 matched (target 11)
- **Missing functions:** `fmt`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 7. fmt.kv

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
| `lib` | `envlogger.Lib` | `lib` |

