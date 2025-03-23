package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(PistonBlock.class)
public class PistonBlockMixin {

	@WrapOperation(method = "isMovable", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getHardness(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F"))
	private static float spectrum$enableUnbreakableMovement(BlockState instance, BlockView blockView, BlockPos pos, Operation<Float> original) {
		if (instance.isIn(SpectrumBlockTags.UNBREAKABLE_MOVABLE)) {
			return 0F;
		}
		return original.call(instance, blockView, pos);
	}
}
