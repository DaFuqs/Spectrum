package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.farming.*;
import net.minecraft.block.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

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
	
}
