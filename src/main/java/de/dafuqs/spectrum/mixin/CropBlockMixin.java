package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.blocks.farming.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin {

	@ModifyExpressionValue(method = "getAvailableMoisture", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/world/BlockView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	private static BlockState spectrum$getAvailableMoisture(BlockState original) {
		Block originalBlock = original.getBlock();
		if (originalBlock instanceof SpectrumFarmlandBlock) {
			return Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, original.get(FarmlandBlock.MOISTURE));
		}
		return original;
	}
	
	@Inject(method = "applyGrowth", at = @At("HEAD"), cancellable = true)
	private void spectrum$cancelGrowthAttempts(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			ci.cancel();
		}
	}
	
	@Inject(method = "applyGrowth", at = @At("HEAD"), cancellable = true)
	public void spectrum$hasRandomTicks(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			ci.cancel();
		}
	}
	
}
