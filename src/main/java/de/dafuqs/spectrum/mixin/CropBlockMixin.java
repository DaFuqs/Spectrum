package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.ExtraTickFarmlandBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class CropBlockMixin {
	
	@Inject(at = @At("RETURN"), method = "canPlantOnTop(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true)
	public void canPlantOnTopOfCustomFarmland(@NotNull BlockState floor, BlockView world, BlockPos pos, @NotNull CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			if (floor.getBlock() instanceof ExtraTickFarmlandBlock) {
				cir.setReturnValue(true);
			}
		}
	}
	
	// TODO: Redirect bad
	/*@Redirect(method= "getAvailableMoisture(Lnet/minecraft/block/Block;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F", at=@At(value="INVOKE", target="Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
	private static boolean getAvailableMoistureForCustomFarmland(@NotNull BlockState blockState, Block block) {
		return blockState.isOf(block) || blockState.getBlock() instanceof ExtraTickFarmlandBlock;
	}*/
	
}
