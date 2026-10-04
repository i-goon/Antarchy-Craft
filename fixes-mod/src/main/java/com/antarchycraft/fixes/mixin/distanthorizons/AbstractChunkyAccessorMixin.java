package com.antarchycraft.fixes.mixin.distanthorizons;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.seibel.distanthorizons.core.wrapperInterfaces.modAccessor.AbstractChunkyAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Distant Horizons 3.3.3 hooks into Chunky on the first chunk load, but while a singleplayer world
 * is loading its spawn chunks Chunky has not started yet, so {@code ChunkyProvider.get()} throws
 * "Chunky is not loaded." and the world crashes. DH also marks the hook as done before trying it,
 * so it never retries.
 */
@Mixin(value = AbstractChunkyAccessor.class, remap = false)
public abstract class AbstractChunkyAccessorMixin {
    @Shadow
    private boolean listenerBound;

    /** If Chunky isn't up yet, leave the hook unbound so DH tries again on the next chunk load. */
    @WrapOperation(method = "tryRunFirstTimeSetup", at = @At(value = "INVOKE",
            target = "Lcom/seibel/distanthorizons/core/wrapperInterfaces/modAccessor/AbstractChunkyAccessor;bindOnGenerationProgressEvent()V"))
    private void antarchycraftfixes$retryUntilChunkyLoads(AbstractChunkyAccessor accessor, Operation<Void> original) {
        try {
            original.call(accessor);
        } catch (IllegalStateException chunkyNotLoaded) {
            this.listenerBound = false;
        }
    }
}
