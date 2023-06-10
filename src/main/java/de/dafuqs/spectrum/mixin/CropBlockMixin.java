package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.farming.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin {
	
	@ModifyReturnValue(method = "canPlantOnTop(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z", at = @At("RETURN"))
	public boolean canPlantOnTopOfCustomFarmland(boolean original, @NotNull BlockState floor, BlockView world, BlockPos pos) {
		if (!original) {
			Block originalBlock = floor.getBlock();
			if (originalBlock instanceof ExtraTickFarmlandBlock || originalBlock instanceof ImmutableFarmlandBlock) {
				return true;
			}
		}
		return original;
	}
	
	@ModifyReturnValue(method = "canGrow", at = @At("RETURN"))
	public boolean cannotGrowOnShaleClay(boolean original, World world, Random random, BlockPos pos, BlockState state) {
		if (original && world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			return false;
		}
		return original;
	}
	
	@Inject(at = @At("HEAD"), method = "grow", cancellable = true)
	public void preventGrowthOnShaleClay(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			ci.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "isFertilizable(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)Z", cancellable = true)
	public void isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient, CallbackInfoReturnable<Boolean> cir) {
		if (world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			cir.cancel();
		}
	}
	
	@ModifyExpressionValue(method = "getAvailableMoisture", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/world/BlockView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	private static BlockState catchState(BlockState original) {
		Block originalBlock = original.getBlock();
		if (originalBlock instanceof ExtraTickFarmlandBlock || originalBlock instanceof ImmutableFarmlandBlock) {
			return Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, original.get(FarmlandBlock.MOISTURE));
		}
		return original;
	}
	
}
