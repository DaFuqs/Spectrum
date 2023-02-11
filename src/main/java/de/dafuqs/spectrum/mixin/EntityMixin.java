package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.cca.LastKillComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
	
	@Inject(method = "onKilledOther", at = @At("HEAD"))
	private void spectrum$rememberKillOther(ServerWorld world, LivingEntity other, CallbackInfo ci) {
		if((Object) this instanceof LivingEntity livingEntity && !livingEntity.getWorld().isClient) {
			LastKillComponent.rememberKillTick(livingEntity, livingEntity.getWorld().getTime());
		}
	}
	
}
