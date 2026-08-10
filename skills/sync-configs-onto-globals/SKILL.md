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
6. Run the project’s appropriate compilation or test command and report the result.

For Planet, use this mapping:

| JSON | Global object |
| --- | --- |
| `tectonics_config.json` | `TectonicGlobals` |
| `ecology_config.json` | `EcologyGlobals` |
| `climate_config.json` | `ClimateSimulationGlobals` |
