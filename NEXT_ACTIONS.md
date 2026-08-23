# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/8 (100.0%)
- **Function parity:** 128/136 matched (target 201) — 94.1%
- **Class/type parity:** 24/26 matched (target 44) — 92.3%
- **Combined symbol parity:** 152/162 matched (target 245) — 93.8%
- **Average inline-code cosine:** 0.61 (function body across 6 matched files)
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

- **Target:** `writer.Target`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2010210.0
- **Functions:** 0/1 matched (target 7)
- **Missing functions:** `fmt`
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_

### 2. writer.buffer

- **Target:** `writer.Buffer`
- **Similarity:** 0.51
- **Dependents:** 1
- **Priority Score:** 1021504.9
- **Functions:** 10/12 matched (target 13)
- **Missing functions:** `adapt`, `fmt`
- **Types:** 3/3 matched (target 8)
- **Missing types:** _none_

### 3. fmt.mod

- **Target:** `fmt.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 55510.0
- **Functions:** 42/45 matched (target 55)
- **Missing functions:** `fmt`, `write_record`, `formatter`
- **Types:** 8/10 matched (target 9)
- **Missing types:** `RecordFormat`, `FormatFn`
- **Tests:** 13/15 matched

### 4. logger

- **Target:** `envlogger.Logger`
- **Similarity:** 0.84
- **Dependents:** 0
- **Priority Score:** 15901.6
- **Functions:** 54/55 matched (target 86)
- **Missing functions:** `fmt`
- **Types:** 4/4 matched (target 8)
- **Missing types:** _none_
- **Tests:** 5/5 matched

### 5. fmt.humantime

- **Target:** `fmt.Humantime`
- **Similarity:** 0.52
- **Dependents:** 0
- **Priority Score:** 10904.8
- **Functions:** 6/7 matched (target 9)
- **Missing functions:** `fmt`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 6. writer.mod

- **Target:** `writer.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 1510.0
- **Functions:** 12/12 matched (target 27)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 7. fmt.kv

- **Target:** `fmt.Kv`
- **Similarity:** 0.79
- **Dependents:** 0
- **Priority Score:** 602.1
- **Functions:** 4/4 matched
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

### 8. lib

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

