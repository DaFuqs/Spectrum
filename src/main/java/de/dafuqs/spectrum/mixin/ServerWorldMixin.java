package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.blocklikeentities.api.BlockLikeEntity;
import de.dafuqs.spectrum.blocks.blocklikeentities.api.BlockLikeSet;
import de.dafuqs.spectrum.events.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.EntityList;
import net.minecraft.world.event.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

	@Shadow
	@Final
	EntityList entityList;
	@Shadow
	private int idleTimeout;

	@Inject(at = @At("TAIL"), method = "spawnEntity")
	private void spectrum$emitSpawnEntityEvent(Entity entity, final CallbackInfoReturnable<Boolean> info) {
		entity.emitGameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
	}

	@Inject(method = "onBlockChanged(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;)V", at = @At("HEAD"))
	private void spectrum$emitBlockChangedEvent(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci) {
		((ServerWorld) (Object) this).emitGameEvent(SpectrumGameEvents.BLOCK_CHANGED, pos, GameEvent.Emitter.of(newBlock));
	}

	/**
	 * Handles additional ticks for BlockLikeEntities, which happens right after normal ticking
	 */
	@Inject(method = "tick", at = @At(value = "RETURN"))
	void postEntityTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		if (this.idleTimeout < 300) {
			entityList.forEach(entityObj -> {
				if (entityObj instanceof BlockLikeEntity entity) {
					entity.spectrum$postTick();
				} else if (entityObj == null) {
					SpectrumCommon.LOGGER.error("Started checking null entities in ServerWorldMixin::postEntityTick");
				}
			});
			BlockLikeSet.getAllSets().forEachRemaining(BlockLikeSet::postTick);
		}
	}
}