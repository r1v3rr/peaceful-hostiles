package net.river.peacefulhostiles.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PhantomEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Target the PhantomEntity class
@Mixin(PhantomEntity.class)
public abstract class PhantomEntityMixin extends MobEntity {

    // Constructor needed because we're extending MobEntity indirectly via the mixin
    protected PhantomEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Prevents Phantoms from being automatically removed in Peaceful difficulty.
     *
     * @param cir CallbackInfoReturnable to modify the return value.
     */
    @Inject(method = "isDisallowedInPeaceful()Z", at = @At("HEAD"), cancellable = true)
    private void peacefulhostiles_allowInPeaceful(CallbackInfoReturnable<Boolean> cir) {
        // Set the return value to false, indicating the mob IS allowed in peaceful
        cir.setReturnValue(false);
        // No need to explicitly cancel, setReturnValue at HEAD does this implicitly
    }
}