---
name: climate-tuner
description: Tune the Planet project's ClimateSimulationGlobals/climate_config.json against the bundled Earth Hersfeldt reference. Use when the user asks to calibrate, optimize, iterate on, score, resume, or apply Earth climate parameter tuning, including requests such as "tune the climate", "improve the Hersfeldt score", or "run another climate batch".
---

# Climate Tuner

Use the project's native Godot runner; do not attempt to call the Kotlin tuner directly with `java` or Gradle.

## Workflow

1. Locate `planet/skills/tune_climate.ps1` if the runner, paths, or options may have changed.
2. Confirm the user intent:
   - Use a new batch unless the user asks to continue, then add `--resume`.
   - Add `--apply` only when the user explicitly authorizes changing `climate_config.json`.
3. Read the prior `report.json` before selecting a new hypothesis. Prioritize, in order:
   - the largest reference-to-simulated confusion;
   - the weakest latitude or elevation band;
   - the averaged Hersfeldt classifier inputs for that confusion; and
   - the baseline deltas from previous candidates (corrected versus regressed tiles and classes).
4. Select a focused parameter set. With a budget of 9 and no interactions, use at most four parameters because the baseline plus both directions needs `1 + 2N` evaluations.
   - Temperature: `baseTemperature,baseTemperatureInsolationScalar,oceanBaseTemp,oceanInsolationScale`
   - Moisture: `moistureToMm,startingMoistureMultiplier,moisturePropagationMultiplier,landPrecipitationScalar`
   - Circulation/precipitation: choose up to four relevant entries from `climate_tuning.json`.
5. Add `--interactions A+B` only for a concrete paired hypothesis. Each pair reserves up to four evaluations, so raise the budget or reduce the number of coordinate parameters accordingly.
6. From the `planet` directory, run `./skills/tune_climate.ps1` with `--parameters`, `--max-evaluations`, and any authorized `--resume`/`--apply` flags. Use an explicit output/report directory for experiments that should not overwrite the default run.
7. Read the generated `report.json` and inspect the baseline/best files under its `artifacts` directory. Report baseline and best loss, exact-match percentage, changed values, corrected/regressed tiles, leading remaining confusion, evaluation count, and whether `climate_config.json` changed.

## Safety and interpretation

- Each evaluation reloads `save/earth.json` and simulates all months. Do not claim a run is quick; use the requested budget and wait for completion.
- Lower loss is better. Prefer the report's `bestLoss`; use match percentage as a companion metric.
- White or transparent pixels in the reference are an unscored ocean/background mask. Never interpret `referenceMisses` as climate errors or optimize them through climate parameters; use `referenceCoveragePercent` only as an import/reference alignment diagnostic.
- A tile's reference target is the majority painted class in its fixed geometric footprint. Use confusion, geographic bands, classifier inputs, and candidate deltas to form the next hypothesis.
- A run without `--apply` must leave `climate_config.json` unchanged. When `--apply` is used, the runner writes a backup before its first replacement.
- If no improvement is found, retain the existing configuration and state that outcome plainly.
- For follow-up tuning, inspect the prior report and use `--resume` only when continuing from its best candidate is appropriate.
