package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin({AnimalEntity.class})
public class AnimalEntityMixin {
	
	// Enabled animals to spawn and pathfind
	// it does, however, not remove the ambient light requirement for animal spawns
	@Inject(method = "getPathfindingFavor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/WorldView;)F", at = @At("HEAD"), cancellable = true)
	public void getPathfindingFavor(BlockPos pos, WorldView world, CallbackInfoReturnable<Float> cir) {
		if (world.getBlockState(pos.down()).isOf(SpectrumBlocks.OVERGROWN_BLACKSLAG)) {
			cir.setReturnValue(10.0F);
		}
	}
	
	
}
