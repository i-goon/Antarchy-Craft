# Antarchy Craft Fixes

A small NeoForge mod the pack ships to fix bugs in other mods with mixins. It contains only our own code, so nothing from the mods it fixes is redistributed.

| Fix | Mod | What it does |
|---|---|---|
| King's Tree freeze | Antarchy 2.1.1 | Spawns the King on the server thread instead of during worldgen. The King's collision check only runs once every chunk under his hitbox is loaded. Before this, generating a King's Tree deadlocked the server and the watchdog killed it. |

| Chunky crash | Distant Horizons 3.3.3 | DH hooks into Chunky on the first chunk load, before Chunky has started, so loading a singleplayer world crashed with "Chunky is not loaded." The hook now waits and retries on the next chunk load. Skipped when DH isn't installed. |

The mod depends on Antarchy `[2.1.1,2.1.2)`, so a newer Antarchy refuses to load with it and the fix gets checked again. Distant Horizons is optional but pinned to `[3.3.3,3.4)` for the same reason.

## Build

Put the official `antarchy-2.1.1+1.21.1-neoforge.jar` and `DistantHorizons-3.3.3-1.21.1-fabric-neoforge.jar` in `libs/` (compile only, never bundled), then run:

```
./gradlew build
```

with JDK 21. The jar lands in `build/libs/` and is published as an asset on the `custom-mods` release.
