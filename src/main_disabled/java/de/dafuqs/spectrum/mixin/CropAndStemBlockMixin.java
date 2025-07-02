package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin({StemBlock.class, CropBlock.class})
public abstract class CropAndStemBlockMixin {
	
	@Inject(method = "isBonemealSuccess", at = @At("HEAD"), cancellable = true)
	private void spectrum$markUnableToGrow(Level world, RandomSource random, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (world.getBlockState(pos.below()).is(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			cir.setReturnValue(false);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "performBonemeal", cancellable = true)
	public void spectrum$preventGrowthOnShaleClay(ServerLevel world, RandomSource random, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (world.getBlockState(pos.below()).is(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			ci.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "isValidBonemealTarget", cancellable = true)
	public void spectrum$isFertilizable(LevelReader world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (world.getBlockState(pos.below()).is(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			cir.setReturnValue(false);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
	public void spectrum$isFertilizable(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if (world.getBlockState(pos.below()).is(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			ci.cancel();
		}
	}

}
