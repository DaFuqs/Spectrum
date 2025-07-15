package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin {
	
	@Shadow
	public abstract void setDayTime(long timeOfDay);
	
	@Inject(at = @At("TAIL"), method = "addFreshEntity")
	private void spectrum$emitSpawnEntityEvent(Entity entity, final CallbackInfoReturnable<Boolean> info) {
		entity.gameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
	}
	
	@Inject(method = "onBlockStateChange", at = @At("HEAD"))
	private void spectrum$emitBlockChangedEvent(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci) {
		((ServerLevel) (Object) this).gameEvent(SpectrumGameEvents.BLOCK_CHANGED, pos, GameEvent.Context.of(newBlock));
	}
	
	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V"))
	private void spectrum$sleepThroughDay(ServerLevel instance, long timeOfDay, Operation<Void> original, @Local long l) {
		var time = TimeHelper.getTimeOfDay(l);
		if (time.isDay()) {
			setDayTime((l - l % 24000L) - 11000L);
			return;
		}
		original.call(instance, timeOfDay);
	}
	
	@Inject(method = "method_18773", at = @At(value = "HEAD"))
	private static void spectrum$applyWakeupEffects(ServerPlayer player, CallbackInfo ci) {
		MiscPlayerDataComponent.get(player).resetSleepingState(false);
		player.removeEffect(SpectrumStatusEffects.SOMNOLENCE);
	}
}