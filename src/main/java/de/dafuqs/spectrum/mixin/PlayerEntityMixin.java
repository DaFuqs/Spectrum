package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}
	
	@Shadow
	protected abstract boolean canPlayerFitWithinBlocksAndEntitiesWhen(Pose pose);
	
	@Inject(method = "updateSwimming()V", at = @At("HEAD"), cancellable = true)
    public void updateSwimming(CallbackInfo ci) {
        if (SpectrumCurioItem.hasEquipped(this, SpectrumItems.RING_OF_DENSER_STEPS.get())) {
            this.setSwimming(false);
            ci.cancel();
        }
    }

    @WrapOperation(method = "updatePlayerPose", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setPose" + "(Lnet/minecraft/world/entity/Pose;)V"))
    public void forceSwimmingState(Player instance, Pose entityPose, Operation<Void> original) {
        var component = MiscPlayerDataAttachmentType.get(instance);
        if ((component.shouldLieDown() || instance.hasEffect(SpectrumMobEffects.FATAL_SLUMBER)) && canPlayerFitWithinBlocksAndEntitiesWhen(Pose.SWIMMING)) {
            instance.setPose(Pose.SWIMMING);
            return;
        }
        original.call(instance, entityPose);
    }
}