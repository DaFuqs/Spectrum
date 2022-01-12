package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.entity.entity.GravityBlockEntity;
import de.dafuqs.spectrum.entity.entity.ShootingStarEntity;
import de.dafuqs.spectrum.events.SpectrumGameEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class SpectrumServerWorldMixin {

	@Shadow
	private int idleTimeout;

	@Inject(at = @At(value = "RETURN"), method = "tick")
	void postEntityTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
		if (this.idleTimeout < 300) {
			for (GravityBlockEntity entry : ((ServerWorld)(Object) this).getEntitiesByType(SpectrumEntityTypes.GRAVITY_BLOCK, Entity::isAlive)) {
				entry.onPostTick();
			}
		}

		ShootingStarEntity.doShootingStarSpawns((ServerWorld)(Object) this);
	}

	@Inject(at = @At("TAIL"), method = "spawnEntity")
	private void spawnEntity(Entity entity, final CallbackInfoReturnable<Boolean> info) {
		entity.emitGameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
	}

}