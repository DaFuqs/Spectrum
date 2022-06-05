package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.events.SpectrumGameEvents;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BuddingAmethystBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(BuddingAmethystBlock.class)
public class BuddingAmethystBlockMixin {
	
	@Inject(method = "randomTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z", shift = At.Shift.AFTER),
			locals = LocalCapture.CAPTURE_FAILHARD)
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci, Direction direction, BlockPos offsetPos, BlockState originalOffsetState, Block blockToGrow, BlockState blockStateToGrow) {
		if (blockStateToGrow.isIn(SpectrumBlockTags.CRYSTAL_APOTHECARY_HARVESTABLE)) {
			world.emitGameEvent(SpectrumGameEvents.CRYSTAL_APOTHECARY_HARVESTABLE_GROWN, offsetPos);
		}
	}
	
}
