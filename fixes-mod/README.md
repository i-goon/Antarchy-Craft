# Antarchy Craft Fixes

A small NeoForge mod the pack ships to fix bugs in other mods with mixins. It contains only our own code, so nothing from the mods it fixes is redistributed.

| Fix | Mod | What it does |
|---|---|---|
| King's Tree freeze | Antarchy 2.1.1 | Spawns the King on the server thread instead of during worldgen. The King's collision check only runs once every chunk under his hitbox is loaded. Before this, generating a King's Tree deadlocked the server and the watchdog killed it. |
| Chunky crash | Distant Horizons 3.3.3 | DH hooks into Chunky on the first chunk load, before Chunky has started, so loading a singleplayer world crashed with "Chunky is not loaded." The hook now waits and retries on the next chunk load. Skipped when DH isn't installed. |
| Busy-base LOD rebuilds | Distant Horizons 3.3.3 | DH rebuilds a chunk's LOD every time Minecraft saves the chunk, and 1.21 saves changed chunks about every 10 seconds. At a busy base that kept DH's threads at about half of all CPU while standing still. DH now takes at most one save per chunk per minute. Saves that arrive sooner are held, and the newest one is passed on once the minute is up or the level unloads, so LODs stay within about a minute of current. Benchmark at a farm-heavy base: 77 to 90 FPS, DH background CPU down about 70%. |

The mod depends on Antarchy `[2.1.1,2.1.2)`, so a newer Antarchy refuses to load with it and the fix gets checked again. Distant Horizons is optional but pinned to `[3.3.3,3.4)` for the same reason.

## Build

Put the official `antarchy-2.1.1+1.21.1-neoforge.jar` and `DistantHorizons-3.3.3-1.21.1-fabric-neoforge.jar` in `libs/` (compile only, never bundled), then run:

```
./gradlew build
```

with JDK 21. The jar lands in `build/libs/` and is published as an asset on the `custom-mods` release.
