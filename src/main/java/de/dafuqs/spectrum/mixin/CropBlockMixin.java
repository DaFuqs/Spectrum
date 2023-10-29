package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.farming.*;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin {
	
	@ModifyExpressionValue(method = "getAvailableMoisture", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/world/BlockView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	private static BlockState catchState(BlockState original) {
		Block originalBlock = original.getBlock();
		if (originalBlock instanceof ExtraTickFarmlandBlock || originalBlock instanceof ImmutableFarmlandBlock) {
			return Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, original.get(FarmlandBlock.MOISTURE));
		}
		return original;
	}

	@Inject(method = "canGrow", at = @At("HEAD"), cancellable = true)
	private void markUnableToGrow(World world, Random random, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}

	@Inject(method = "applyGrowth", at = @At("HEAD"), cancellable = true)
	private void cancelGrowthAttempts(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			ci.cancel();
		}
	}
}
