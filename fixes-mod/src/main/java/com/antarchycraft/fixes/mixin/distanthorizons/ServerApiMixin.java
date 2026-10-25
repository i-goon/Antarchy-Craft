package com.antarchycraft.fixes.mixin.distanthorizons;

import com.seibel.distanthorizons.core.api.internal.ServerApi;
import com.seibel.distanthorizons.core.pos.DhChunkPos;
import com.seibel.distanthorizons.core.wrapperInterfaces.chunk.IChunkWrapper;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.ILevelWrapper;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.IServerLevelWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * DH rebuilds a chunk's LOD every time Minecraft saves that chunk. Minecraft 1.21 saves changed
 * chunks in the background roughly every 10 seconds, so at a busy base (farms, crops, machines)
 * every loaded chunk keeps going back through DH's update queue, parent propagation and render
 * rebuilds. In our benchmark that kept DH's threads at ~50% of all CPU while the player stood
 * still.
 *
 * DH now takes at most one save per chunk per minute. A save that arrives sooner is held, and the
 * newest held save for each chunk is passed on once its minute is up, so a chunk that changes and
 * then goes quiet still ends up with a current LOD. Held saves are also passed on before a level
 * unloads.
 */
@Mixin(value = ServerApi.class, remap = false)
public abstract class ServerApiMixin {
    @Unique
    private static final long ANTARCHYCRAFTFIXES$MIN_INTERVAL_MS = 60_000;

    @Unique
    private static final long ANTARCHYCRAFTFIXES$FLUSH_CHECK_MS = 1_000;

    @Unique
    private static final Map<ILevelWrapper, Map<Long, Long>> ANTARCHYCRAFTFIXES$LAST_UPDATE = new ConcurrentHashMap<>();

    @Unique
    private static final Map<ILevelWrapper, Map<Long, IChunkWrapper>> ANTARCHYCRAFTFIXES$HELD = new ConcurrentHashMap<>();

    @Unique
    private static final ThreadLocal<Boolean> ANTARCHYCRAFTFIXES$REPLAYING = ThreadLocal.withInitial(() -> false);

    @Unique
    private static volatile long antarchycraftfixes$lastFlushCheck;

    @Inject(method = "serverChunkSaveEvent", at = @At("HEAD"), cancellable = true)
    private void antarchycraftfixes$throttleSaves(IChunkWrapper chunk, ILevelWrapper level, CallbackInfo ci) {
        if (ANTARCHYCRAFTFIXES$REPLAYING.get()) {
            return;
        }
        long now = System.currentTimeMillis();
        DhChunkPos pos = chunk.getChunkPos();
        long key = ((long) pos.getX() << 32) | (pos.getZ() & 0xFFFFFFFFL);
        Map<Long, Long> lastUpdate = ANTARCHYCRAFTFIXES$LAST_UPDATE.computeIfAbsent(level, l -> new ConcurrentHashMap<>());
        Map<Long, IChunkWrapper> held = ANTARCHYCRAFTFIXES$HELD.computeIfAbsent(level, l -> new ConcurrentHashMap<>());
        Long last = lastUpdate.get(key);
        if (last != null && now - last < ANTARCHYCRAFTFIXES$MIN_INTERVAL_MS) {
            held.put(key, chunk);
            ci.cancel();
        } else {
            lastUpdate.put(key, now);
            held.remove(key);
        }
        if (now - antarchycraftfixes$lastFlushCheck >= ANTARCHYCRAFTFIXES$FLUSH_CHECK_MS) {
            antarchycraftfixes$lastFlushCheck = now;
            antarchycraftfixes$passOnHeld(now, null, false);
        }
    }

    @Inject(method = "serverLevelUnloadEvent", at = @At("HEAD"))
    private void antarchycraftfixes$passOnBeforeUnload(IServerLevelWrapper level, CallbackInfo ci) {
        antarchycraftfixes$passOnHeld(System.currentTimeMillis(), level, true);
        ANTARCHYCRAFTFIXES$HELD.remove(level);
        ANTARCHYCRAFTFIXES$LAST_UPDATE.remove(level);
    }

    @Inject(method = "serverUnloadEvent", at = @At("HEAD"))
    private void antarchycraftfixes$clearOnServerUnload(CallbackInfo ci) {
        ANTARCHYCRAFTFIXES$HELD.clear();
        ANTARCHYCRAFTFIXES$LAST_UPDATE.clear();
    }

    /** Passes held saves on to DH: those whose minute is up, or all of one level's when {@code all} is set. */
    @Unique
    private void antarchycraftfixes$passOnHeld(long now, ILevelWrapper onlyLevel, boolean all) {
        List<IChunkWrapper> chunks = new ArrayList<>();
        List<ILevelWrapper> levels = new ArrayList<>();
        for (Map.Entry<ILevelWrapper, Map<Long, IChunkWrapper>> levelEntry : ANTARCHYCRAFTFIXES$HELD.entrySet()) {
            ILevelWrapper level = levelEntry.getKey();
            if (onlyLevel != null && level != onlyLevel) {
                continue;
            }
            Map<Long, Long> lastUpdate = ANTARCHYCRAFTFIXES$LAST_UPDATE.computeIfAbsent(level, l -> new ConcurrentHashMap<>());
            Iterator<Map.Entry<Long, IChunkWrapper>> it = levelEntry.getValue().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Long, IChunkWrapper> held = it.next();
                Long last = lastUpdate.get(held.getKey());
                if (all || last == null || now - last >= ANTARCHYCRAFTFIXES$MIN_INTERVAL_MS) {
                    it.remove();
                    lastUpdate.put(held.getKey(), now);
                    chunks.add(held.getValue());
                    levels.add(level);
                }
            }
        }
        if (chunks.isEmpty()) {
            return;
        }
        ANTARCHYCRAFTFIXES$REPLAYING.set(true);
        try {
            for (int i = 0; i < chunks.size(); i++) {
                ((ServerApi) (Object) this).serverChunkSaveEvent(chunks.get(i), levels.get(i));
            }
        } finally {
            ANTARCHYCRAFTFIXES$REPLAYING.set(false);
        }
    }
}
