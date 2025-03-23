package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneBlockMixin {
	
	@Inject(method = "canPlaceAt(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"),
			locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true
	)
	private void spectrum$allowPlantingOnSlushWithoutWaterNearby(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir, BlockState blockState) {
		if (blockState.isOf(SpectrumBlocks.SLUSH)) {
			cir.setReturnValue(true);
		}
	}
	
}
