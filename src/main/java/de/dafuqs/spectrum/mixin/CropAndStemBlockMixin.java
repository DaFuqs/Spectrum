package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin({StemBlock.class, CropBlock.class})
public abstract class CropAndStemBlockMixin {
	
	@Inject(method = "canGrow", at = @At("HEAD"), cancellable = true)
	private void spectrum$markUnableToGrow(World world, Random random, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			cir.setReturnValue(false);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "grow", cancellable = true)
	public void spectrum$preventGrowthOnShaleClay(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			ci.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "isFertilizable", cancellable = true)
	public void spectrum$isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient, CallbackInfoReturnable<Boolean> cir) {
		if (world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			cir.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
	public void spectrum$isFertilizable(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		if (world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			ci.cancel();
		}
	}

}
