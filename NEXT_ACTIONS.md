# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/8 (100.0%)
- **Function parity:** 134/136 matched (target 213) — 98.5%
- **Class/type parity:** 25/26 matched (target 45) — 96.2%
- **Combined symbol parity:** 159/162 matched (target 258) — 98.1%
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
- **Functions:** 12/12 matched (target 15)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 8)
- **Missing types:** _none_

### 3. fmt.mod

- **Target:** `fmt.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 35510.0
- **Functions:** 43/45 matched (target 60)
- **Missing functions:** `write_record`, `formatter`
- **Types:** 9/10 matched
- **Missing types:** `RecordFormat`
- **Tests:** 13/15 matched

### 4. logger

- **Target:** `envlogger.Logger`
- **Similarity:** 0.84
- **Dependents:** 0
- **Priority Score:** 5901.6
- **Functions:** 55/55 matched (target 88)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 8)
- **Missing types:** _none_
- **Tests:** 5/5 matched

### 5. writer.mod

- **Target:** `writer.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 1510.0
- **Functions:** 12/12 matched (target 27)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 6. fmt.humantime

- **Target:** `fmt.Humantime`
- **Similarity:** 0.57
- **Dependents:** 0
- **Priority Score:** 904.3
- **Functions:** 7/7 matched (target 11)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 1/1 matched

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

