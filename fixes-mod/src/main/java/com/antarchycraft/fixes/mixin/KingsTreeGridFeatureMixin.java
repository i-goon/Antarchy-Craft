package com.antarchycraft.fixes.mixin;

import com.craisinlord.antarchy.content.worldgen.elythia.KingsTreeGridFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Antarchy 2.1.1 spawns the King from {@code KingsTreeGridFeature.place}, which runs on a worldgen
 * thread, and checks his spawn spot against the live level. That collision check loads chunks under
 * the King's large hitbox, so the worldgen thread waits on the server thread while the server thread
 * waits for the tree chunk to finish generating, and the server watchdog kills the server.
 */
@Mixin(KingsTreeGridFeature.class)
public abstract class KingsTreeGridFeatureMixin {
    @Shadow
    private static void spawnTreeKing(WorldGenLevel level, int centerX, int centerZ, int baseY) {
        throw new AssertionError();
    }

    /** Spawn the King on the server thread once the tree chunk is done, instead of during worldgen. */
    @Redirect(method = "place", at = @At(value = "INVOKE",
            target = "Lcom/craisinlord/antarchy/content/worldgen/elythia/KingsTreeGridFeature;spawnTreeKing(Lnet/minecraft/world/level/WorldGenLevel;III)V"))
    private void antarchycraftfixes$spawnKingOnServerThread(WorldGenLevel level, int centerX, int centerZ, int baseY) {
        level.getLevel().getServer().execute(() -> spawnTreeKing(level, centerX, centerZ, baseY));
    }

    /** Only test spawn spots whose whole hitbox is loaded, so the check never generates chunks. */
    @Redirect(method = "findSafeKingSpawn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z"))
    private static boolean antarchycraftfixes$noCollisionInLoadedChunks(ServerLevel level, Entity king, AABB box) {
        return level.hasChunksAt(BlockPos.containing(box.minX, box.minY, box.minZ),
                BlockPos.containing(box.maxX, box.maxY, box.maxZ))
                && level.noCollision(king, box);
    }
}
