package net.river.peacefulhostiles.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity; // Import HostileEntity
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity; // To potentially check if the target is a player
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.river.peacefulhostiles.PeacefulHostiles; // For logging
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class) // Target the MobEntity class
public abstract class MobEntityMixin extends LivingEntity {

    // Mixins need to 'extend' the class they target if they need access to its fields/methods
    // or if the target class is abstract. We also need a matching constructor.
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    // Inject code at the beginning of the setTarget method
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void peacefulhostiles_preventTargetingInPeaceful(LivingEntity target, CallbackInfo ci) {
        // Make sure we are on the server side and the target is not null
        if (this.getWorld().isClient() || target == null) {
            return; // Don't interfere on the client side or if target is null
        }

        // Check if 'this' mob is actually a hostile mob
        // We need to cast 'this' because MobEntity isn't necessarily HostileEntity
        if ((Object)this instanceof HostileEntity) {
            // Check if the world difficulty is Peaceful
            if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
                // Optional: Only cancel if the target is a player? For now, let's cancel for any target.
                // if (target instanceof PlayerEntity) {
                PeacefulHostiles.LOGGER.debug("Preventing {} from targeting {} in Peaceful difficulty.",
                        this.getType().getName().getString(), target.getType().getName().getString());
                // Cancel the original setTarget method
                ci.cancel();
                // }
            }
        }
    }
}