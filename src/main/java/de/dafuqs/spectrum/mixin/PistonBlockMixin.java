package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.piston.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(PistonBaseBlock.class)
public class PistonBlockMixin {
	
	@WrapOperation(method = "isPushable", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroySpeed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
	private static float spectrum$enableUnbreakableMovement(BlockState instance, BlockGetter blockView, BlockPos pos, Operation<Float> original) {
		if (instance.is(SpectrumBlockTags.UNBREAKABLE_MOVABLE)) {
			return 0F;
		}
		return original.call(instance, blockView, pos);
	}
}
