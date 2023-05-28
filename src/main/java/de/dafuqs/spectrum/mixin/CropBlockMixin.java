package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.farming.ImmutableFarmlandBlock;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin {
	
	@Inject(at = @At("RETURN"), method = "canPlantOnTop(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true)
	public void canPlantOnTopOfCustomFarmland(@NotNull BlockState floor, BlockView world, BlockPos pos, @NotNull CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			if (floor.getBlock() instanceof ExtraTickFarmlandBlock || floor.getBlock() instanceof ImmutableFarmlandBlock) {
				cir.setReturnValue(true);
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "canGrow", cancellable = true)
	public void cannotGrowOnShaleClay(World world, Random random, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY));
	}

	@Inject(at = @At("HEAD"), method = "grow", cancellable = true)
	public void preventGrowthOnShaleClay(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (world.getBlockState(pos.down()).isOf(SpectrumBlocks.TILLED_SHALE_CLAY))
			ci.cancel();
	}

	// TODO: Come back to this later, this is going to be difficult
	@ModifyVariable(method = "getAvailableMoisture", name = "g", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BlockView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	private static float modifyMoisture(float value) {

		return value;
	}
	
	// TODO: Redirect bad
	/*@Redirect(method= "getAvailableMoisture(Lnet/minecraft/block/Block;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F", at=@At(value="INVOKE", target="Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
	private static boolean getAvailableMoistureForCustomFarmland(@NotNull BlockState blockState, Block block) {
		return blockState.isOf(block) || blockState.getBlock() instanceof ExtraTickFarmlandBlock;
	}*/
	
}
