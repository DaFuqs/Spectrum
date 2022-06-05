package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.entity.entity.ShootingStarEntity;
import de.dafuqs.spectrum.events.SpectrumGameEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	
	@Inject(at = @At(value = "RETURN"), method = "tick")
	void postEntityTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		ShootingStarEntity.doShootingStarSpawns((ServerWorld) (Object) this);
	}
	
	@Inject(at = @At("TAIL"), method = "spawnEntity")
	private void spawnEntity(Entity entity, final CallbackInfoReturnable<Boolean> info) {
		entity.emitGameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
	}
	
}