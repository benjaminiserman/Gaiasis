---
name: sync-configs-onto-globals
description: Manually align JSON tuning configuration values with their corresponding Kotlin global objects. Use when a project has config files such as `tectonics_config.json`, `ecology_config.json`, or `climate_config.json` and the declared global defaults must match the current JSON values without adding runtime loading behavior.
---

# Sync Configs onto Globals

Synchronize defaults in source code from configuration files; do not change the configuration files unless the user explicitly requests that direction.

1. Locate each JSON configuration file and its corresponding Kotlin global object.
2. Compare every JSON property with the `var` declaration of the same name. Treat equivalent Kotlin numeric syntax (for example, `0.20` and `0.2`, digit separators, or a mathematically identical constant expression) as equal.
3. Report JSON properties without a matching global before editing. Do not invent a new property unless the user asks.
4. Update only declarations whose runtime values differ, using `apply_patch`. Preserve comments and unrelated user changes.
5. Do not add config reloads, startup synchronization, serialization changes, or runtime overrides: this skill updates declared defaults manually.
6. Repeat the full comparison after editing. Do not report synchronization as complete until the second comparison finds no mismatches or missing globals. Merely reading this file, spot-checking known values, or running tests does not constitute running the sync workflow.
7. Run the project’s appropriate compilation or test command and report the result. Completion requires both a clean post-edit comparison and a successful test command.
8. If Gradle fails because stale Java or Gradle processes have locked files under `build`, report that as a separate verification failure. You may stop Gradle daemons with the project wrapper’s `--stop` option and retry the tests, but do not claim success unless the retry passes.

## Completion checklist

Before reporting success, verify all of the following:

- The complete set of mapped JSON properties was compared with the corresponding globals.
- No JSON properties are missing from the globals.
- No runtime-value mismatches remain after editing.
- The project compilation or test command exited successfully.

For Planet, use this mapping:

| JSON | Global object |
| --- | --- |
| `tectonics_config.json` | `TectonicGlobals` |
| `ecology_config.json` | `EcologyGlobals` |
| `climate_config.json` | `ClimateSimulationGlobals` |
