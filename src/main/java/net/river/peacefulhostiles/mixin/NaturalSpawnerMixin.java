package net.river.peacefulhostiles.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import net.river.peacefulhostiles.PeacefulHostiles; // Import our main mod class for the logger
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.river.peacefulhostiles.mixin.NaturalSpawner.class) // Tell Mixin this targets the NaturalSpawner class
public abstract class NaturalSpawnerMixin {

    // We are redirecting a specific call *within* the spawn method.
    // The target is the call to world.getDifficulty()
    @Redirect(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/StructureDensityManager;Lnet/minecraft/world/SpawnDensityCapper;ZZZ)V", // The signature of the spawn method we're targeting
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getDifficulty()Lnet/minecraft/world/Difficulty;") // Find where getDifficulty() is called on the ServerWorld
    )
    private static Difficulty peacefulhostiles_pretendNotPeacefulForSpawning(ServerWorld world) {
        Difficulty actualDifficulty = world.getDifficulty(); // Get the actual difficulty

        // If the game is *really* in Peaceful...
        if (actualDifficulty == Difficulty.PEACEFUL) {
            PeacefulHostiles.LOGGER.debug("Redirecting difficulty check in NaturalSpawner to allow hostile spawns.");
            // ...pretend it's Easy difficulty *just for the spawning logic*.
            // This tricks the spawner into thinking it's okay to spawn monsters based on density caps etc.
            return Difficulty.EASY; // Or NORMAL/HARD, Easy makes sense conceptually
        }

        // Otherwise (if it's Easy, Normal, or Hard), return the actual difficulty.
        return actualDifficulty;
    }
}
