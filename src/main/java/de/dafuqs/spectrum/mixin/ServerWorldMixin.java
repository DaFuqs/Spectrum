package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

	@Shadow public abstract void setTimeOfDay(long timeOfDay);

	@Inject(at = @At("TAIL"), method = "spawnEntity")
	private void spectrum$emitSpawnEntityEvent(Entity entity, final CallbackInfoReturnable<Boolean> info) {
		entity.emitGameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
	}

	@Inject(method = "onBlockChanged(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;)V", at = @At("HEAD"))
	private void spectrum$emitBlockChangedEvent(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci) {
		((ServerWorld) (Object) this).emitGameEvent(SpectrumGameEvents.BLOCK_CHANGED, pos, GameEvent.Emitter.of(newBlock));
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V"))
	private void spectrum$sleepThroughDay(ServerWorld instance, long timeOfDay, Operation<Void> original, @Local long l) {
		var time = TimeHelper.getTimeOfDay(l);
		if (time.isDay()) {
			setTimeOfDay(13000);
			return;
		}
		original.call(instance, timeOfDay);
	}

	@Inject(method = "method_18773", at = @At(value = "HEAD"))
	private static void spectrum$removeSomnolence(ServerPlayerEntity player, CallbackInfo ci) {
		player.removeStatusEffect(SpectrumStatusEffects.SOMNOLENCE);
	}
}