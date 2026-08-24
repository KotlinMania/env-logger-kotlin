=== Deep Analysis: tmp/env_logger/src (rust) -> src/commonMain/kotlin (kotlin) ===

Scanning source codebase (rust)...
Codebase: tmp/env_logger/src (rust)
  Files: 8
  Total imports: 40
  Most depended: writer.target (2 dependents)

Scanning target codebase (kotlin)...
Codebase: src/commonMain/kotlin (kotlin)
  Files: 14
  Total imports: 58
  Most depended: envlogger.PipeSink (5 dependents)

Comparing codebases...
Computing AST similarities...

=== Codebase Comparison Report ===

Source: tmp/env_logger/src (8 files)
Target: src/commonMain/kotlin (14 files)
Scoring invariant: FnSim is required function body/parameter similarity. Class/type and symbol parity are reported beside it; whole-file shape is diagnostic only.

Matched:   8 files
Unmatched: 0 source, 0 target

=== Matched Files (by porting priority) ===

Source                        Target                        FnSim     Dependents FunctionParityTypeParity  Priority
 --------------------------------------------------------------------------------------------------------------------------
writer.target                 writer.Target                 0.15      2          1/1           1/1         2000208.5 
writer.buffer                 writer.Buffer                 0.57      1          12/12         3/3         1001504.3 
fmt.mod                       fmt.Mod [STUB]                0.00      0          43/45         9/10        35510.0   
logger                        envlogger.Logger              0.84      0          55/55         4/4         5901.6    
writer.mod                    writer.Mod [STUB]             0.00      0          12/12         3/3         1510.0    
fmt.humantime                 fmt.Humantime                 0.57      0          7/7           2/2         904.3     
fmt.kv                        fmt.Kv                        0.79      0          4/4           2/2         602.1     
lib                           envlogger.Lib                 1.00      0          0/0           1/1         100.0     

=== Function and Symbol Details ===

writer.target -> writer.Target
  similarity: 0.15, priority: 2000208.5, dependents: 2
  functions: 1/1 matched (target total: 8, required body score: 0.15)
  missing functions: none
  types: 1/1 matched (target total: 6)
  missing types: none

writer.buffer -> writer.Buffer
  similarity: 0.57, priority: 1001504.3, dependents: 1
  functions: 12/12 matched (target total: 15, required body score: 0.57)
  missing functions: none
  types: 3/3 matched (target total: 8)
  missing types: none

fmt.mod -> fmt.Mod [STUB]
  similarity: 0.00, priority: 35510.0, dependents: 0
  functions: 43/45 matched (target total: 60, required body score: 0.00)
  missing functions: write_record, formatter
  types: 9/10 matched (target total: 10)
  missing types: RecordFormat
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies
  tests: 13/15 matched

logger -> envlogger.Logger
  similarity: 0.84, priority: 5901.6, dependents: 0
  functions: 55/55 matched (target total: 88, required body score: 0.84)
  missing functions: none
  types: 4/4 matched (target total: 8)
  missing types: none
  tests: 5/5 matched

writer.mod -> writer.Mod [STUB]
  similarity: 0.00, priority: 1510.0, dependents: 0
  functions: 12/12 matched (target total: 27, required body score: 0.00)
  missing functions: none
  types: 3/3 matched (target total: 5)
  missing types: none
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies
  tests: 2/2 matched

fmt.humantime -> fmt.Humantime
  similarity: 0.57, priority: 904.3, dependents: 0
  functions: 7/7 matched (target total: 11, required body score: 0.57)
  missing functions: none
  types: 2/2 matched (target total: 3)
  missing types: none
  tests: 1/1 matched

fmt.kv -> fmt.Kv
  similarity: 0.79, priority: 602.1, dependents: 0
  functions: 4/4 matched (target total: 4, required body score: 0.79)
  missing functions: none
  types: 2/2 matched (target total: 2)
  missing types: none

lib -> envlogger.Lib
  similarity: 1.00, priority: 100.0, dependents: 0
  functions: 0/0 matched (target total: 0, required body score: 1.00)
  missing functions: none
  types: 1/1 matched (target total: 3)
  missing types: none


=== Scores Forced To 0 ===

  - fmt.mod -> fmt.Mod: target contains TODO/stub/placeholder markers in function bodies
  - writer.mod -> writer.Mod: target contains TODO/stub/placeholder markers in function bodies

=== Porting Quality Summary ===

Matched by exact header:          8 / 8
Matched by provenance fallback:   0 / 8
Matched by name:                  0 / 8
Total TODOs in target: 0
Total lint errors:    0
Stub files:           2

=== Big Picture ===

- Missing files: 0
- Incomplete ports (similarity < 60%): 5
- Stub files: 2
- Files missing functions: 1 (total deficit: 2 functions)
- Type definitions missing: 1
- Files missing tests: 1 (total deficit: 2 unported `#[test]` functions)
- Documentation coverage: 783 / 1952 lines (40%)

Primary focus: replace stub files with real implementations

=== Files with Issues ===

File                          Similarity LineRatio  FunctionParityTests     TODOs Lint  Status
----------------------------------------------------------------------------------------------------
writer.Target                 0.15       0.00       1/1           -         0     0     LOW_SIM
writer.Buffer                 0.57       0.00       12/12         -         0     0     
fmt.Mod [STUB]                0.00       0.00       43/45         13/15     0     0     STUB
  missing functions: `write_record`, `formatter`
  missing types: `RecordFormat`
writer.Mod [STUB]             0.00       0.00       12/12         2/2       0     0     STUB
fmt.Humantime                 0.57       0.00       7/7           1/1       0     0     

=== Porting Recommendations ===

Incomplete ports (similarity < 60%): 5
Missing files: 0

Incomplete ports to complete:
  writer.target                  similarity=0.15 function_parity=1/1 dependents=2
  writer.buffer                  similarity=0.57 function_parity=12/12 dependents=1
  fmt.mod                        similarity=0.00 function_parity=43/45 dependents=0 [STUB]
    missing functions: `write_record`, `formatter`
    missing types: `RecordFormat`
  writer.mod                     similarity=0.00 function_parity=12/12 dependents=0 [STUB]
  fmt.humantime                  similarity=0.57 function_parity=7/7 dependents=0

=== Documentation Gaps ===

There is missing documentation that is hurting overall scoring.
Documentation coverage: 783 / 1952 lines (40%)
Files with >20% doc gap: 6

File                          Src Docs    Tgt Docs    Gap %     DocSim    DocAmt    DocEq     
----------------------------------------------------------------------------------------------
logger                        992         213         78%       0.78      0.21      0.50      
lib                           546         276         49%       0.94      0.51      0.72      
fmt.mod                       250         159         36%       0.92      0.64      0.78      
fmt.humantime                 64          48          25%       0.93      0.75      0.84      
fmt.kv                        40          26          35%       0.92      0.65      0.78      
writer.mod                    36          25          30%       0.90      0.69      0.80      

=== Generating Reports ===

✅ Generated: port_status_report.md
✅ Generated: high_priority_ports.md
✅ Generated: NEXT_ACTIONS.md
✅ Generated: port_lint_proposed_changes.md

📁 All reports generated successfully!
