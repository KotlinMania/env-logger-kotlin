# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/15 (53.3%)
- **Function parity:** 134/150 matched (target 226) — 89.3%
- **Class/type parity:** 26/26 matched (target 49) — 100.0%
- **Combined symbol parity:** 160/176 matched (target 275) — 90.9%
- **Average inline-code cosine:** 0.58 (function body across 5 matched files)
- **Average documentation cosine:** 0.83 (doc text across 5 matched files)
- **Cheat-zeroed Files:** 3
- **Critical Issues:** 6 files with <0.60 function similarity

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

### 3. fmt.mod

- **Target:** `fmt.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 25510.0
- **Functions:** 43/45 matched (target 62)
- **Missing functions:** `write_record`, `formatter`
- **Types:** 10/10 matched (target 11)
- **Missing types:** _none_
- **Tests:** 13/15 matched

### 4. env_logger.logger

- **Target:** `envlogger.Logger`
- **Similarity:** 0.83
- **Dependents:** 0
- **Priority Score:** 5901.7
- **Functions:** 55/55 matched (target 91)
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
- **Functions:** 4/4 matched (target 7)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

### 8. env_logger.lib

- **Target:** `envlogger.Lib [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
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

