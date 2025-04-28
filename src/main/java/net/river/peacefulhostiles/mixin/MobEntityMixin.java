package net.river.peacefulhostiles.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.river.peacefulhostiles.PeacefulHostiles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.math.BlockPos; // Needed if you are extending LivingEntity which depends on it indirectly sometimes. Add if needed by compiler.

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void peacefulhostiles_preventTargetingInPeaceful(LivingEntity target, CallbackInfo ci) {
        if (this.getWorld().isClient() || target == null) {
            return;
        }

        // Check if 'this' mob is actually a hostile mob
        if ((Object)this instanceof HostileEntity) {
            // Check if the world difficulty is Peaceful
            if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
                // Log the action using the requested format (more or less)
                PeacefulHostiles.LOGGER.info("Turning {} peaceful (preventing target selection of {}).",
                        this.getType().getName().getString(), target.getType().getName().getString());

                // Cancel the original setTarget method
                ci.cancel();
            }
        }
    }
}