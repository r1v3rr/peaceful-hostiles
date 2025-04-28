package net.river.peacefulhostiles.mixin;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.SpawnDensityCapper;
import net.minecraft.world.biome.SpawnSettings;
import net.river.peacefulhostiles.PeacefulHostiles; // For logging
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.class) // Target the SpawnHelper class
public abstract class SpawnHelperMixin {

    // Inject at the beginning of the canSpawn method and make it cancellable
    @Inject(
            method = "canSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/world/StructureDensityManager;Lnet/minecraft/world/SpawnDensityCapper;Lnet/minecraft/world/biome/SpawnSettings$SpawnEntry;Lnet/minecraft/util/math/BlockPos$Mutable;D)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void peacefulhostiles_allowMonstersInPeaceful(ServerWorld world, SpawnGroup group, StructureDensityManager structureDensityManager, SpawnDensityCapper densityCapper, SpawnSettings.SpawnEntry spawnEntry, BlockPos.Mutable pos, double squaredDistance, CallbackInfoReturnable<Boolean> cir) {
        // Check if the world is Peaceful AND the mob group is NOT peaceful (i.e., hostile, ambient, etc.)
        if (world.getDifficulty() == Difficulty.PEACEFUL && !group.isPeaceful()) {
            PeacefulHostiles.LOGGER.debug("Allowing potential spawn of {} in Peaceful difficulty via SpawnHelperMixin.", group.getName());
            // Force the check to return true for non-peaceful groups in Peaceful difficulty
            // We let the *rest* of the spawning logic (light level, density caps, etc.) proceed normally.
            // This doesn't guarantee a spawn, just bypasses the difficulty check here.
            cir.setReturnValue(true);
            cir.cancel(); // Prevent the original method's logic from running for this case
        }
        // If not Peaceful or if it's a peaceful group, let the original method run normally.
    }
}