# Changelog

All notable changes to Antarchy Craft. Format is Keep a Changelog; this file is read by `publish.yml` for CurseForge + Modrinth.

## [1.0.31] - 2026-08-26
### Added
- Twilight Forest quests — follow the boss progression from the Naga to the Snow Queen.
- Draconic Evolution weapons now show correctly with shaders on.

### Changed
- Changelogs now show real update notes on CurseForge and Modrinth.

### QOL
- Quark rotation lock no longer shares the shader toggle key (K) — no more cube indicator by the crosshair.

## [1.0.30] - 2026-08-23
### Added
- Bosses chapter: kill quests for Ender Dragon (`minecraft:ender_dragon`) and Chaos Guardian (`draconicevolution:draconic_guardian`) with ATM9 descriptions.

## [1.0.29] - 2026-08-23
### Fixed
- Quest item clicks now open JEI recipes — added `ftb-xmod-compat-neoforge-21.1.11` and switched FTB Quests tasks to `item: "mod:id"` string form.

## [1.0.28] - 2026-08-23
### Added
- Big Bertha chapter (30+ quests, 3 branches): Ultimate Sword → Blade / Hilt / Handle → Big Bertha, with full dependency lines and `filename: "big_bertha"` to prevent hex-duplicate chapters.

## [1.0.27] - 2026-08-22
### Added
- `e4mc` 6.2.1 (serverless world sharing, MIT, client-side) — `/e4mc` tunnels your singleplayer to friends.
- `CustomSkinLoader` 15.0.1 Universal (client-side) — restores skins for offline/cracked display.
- Synced `Our Antarchy Craft` instance (was missing 18 mods).

## [1.0.26] - 2026-08-16
### Fixed
- CurseForge moderation rejection: added `update.curseforge` metadata to 9 CF-hosted mods (Jade, Jade Addons, Valhelsia Furniture, Ore Hammer, TrashSlot, TerraBlender, Crafting Tweaks, Antarchy, TFMG resourcepack) so `manifest.json` references them instead of embedding jars.

## [1.0.25] - 2026-08-16
### Added
- Ore Hammer 2.2.0 — vein-hammer for AllTheOres.
### Fixed
- `pack.toml` corruption from failed version bump.

## [1.0.24] - 2026-08-15
### Added
- Jade + Jade Addons (block/tool HUD).
### Changed
- Unbound Draconic Evolution `tool_config` / `tool_modules` from `C` (conflicted with Just Zoom).

## [1.0.23] - 2026-08-15
### Fixed
- Packwiz layout: game files now at pack root (was `overrides/overrides` double-nesting in exports). Removed duplicate `antarchy` entry. Export now 579 files clean.

## [1.0.22] - 2026-08-15
### Added
- Complementary Reimagined r5.8.1 shaderpack (shipped as `overrides/shaderpacks/*.zip`, gitignored exception) with custom `iris.properties` defaults.

## [1.0.21] - 2026-08-15
### Added
- Valhelsia Furniture (silences Valhelsia Structures notice).

## [1.0.20] - 2026-08-15
### Fixed
- Default Options: `config/defaultoptions/keybindings.txt` (was wrong folder + filename; now applies on fresh installs).

## [1.0.19] - 2026-08-15
### Fixed
- CurseForge export: `side=server` mods were skipped from `manifest.json` (13 mods missing: YUNG's suite, ServerCore, etc.).

## [1.0.18] - 2026-08-15
### Changed
- Antarchy now uses Modrinth metadata (was custom GitHub Releases URL; it was always on Modrinth).

## [1.0.17] - 2026-08-15
### Added
- CurseForge readiness: `update.curseforge` for 149 mods, custom jars hosted on GitHub Releases (`custom-mods`).

## [1.0.16] - 2026-08-15
### Added
- Dual publish: CurseForge (1653892) alongside Modrinth (EHj9agqR).
