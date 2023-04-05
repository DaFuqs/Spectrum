package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.events.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
	
	@Inject(at = @At("TAIL"), method = "spawnEntity")
	private void spectrum$emitSpawnEntityEvent(Entity entity, final CallbackInfoReturnable<Boolean> info) {
		entity.emitGameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
	}
	
	@Inject(method = "onBlockChanged(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;)V", at = @At("HEAD"))
	private void spectrum$emitBlockChangedEvent(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci) {
		((ServerWorld) (Object) this).emitGameEvent(SpectrumGameEvents.BLOCK_CHANGED, pos, GameEvent.Emitter.of(newBlock));
	}
	
}