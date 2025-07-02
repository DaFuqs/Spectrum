package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.blocks.farming.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin {
	
	@ModifyExpressionValue(method = "getGrowthSpeed", at = @At(value = "INVOKE", ordinal = 0, target = "net/minecraft/world/level/BlockGetter.getBlockState (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
	private static BlockState spectrum$getAvailableMoisture(BlockState original) {
		Block originalBlock = original.getBlock();
		if (originalBlock instanceof SpectrumFarmlandBlock) {
			return Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, original.getValue(FarmBlock.MOISTURE));
		}
		return original;
	}
	
	@Inject(method = "growCrops", at = @At("HEAD"), cancellable = true)
	private void spectrum$cancelGrowthAttempts(Level world, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (world.getBlockState(pos.below()).is(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			ci.cancel();
		}
	}
	
	@Inject(method = "growCrops", at = @At("HEAD"), cancellable = true)
	public void spectrum$hasRandomTicks(Level world, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (world.getBlockState(pos.below()).is(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			ci.cancel();
		}
	}
	
}
