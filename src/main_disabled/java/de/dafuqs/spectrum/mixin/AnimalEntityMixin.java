package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin({Animal.class})
public class AnimalEntityMixin {
	
	// Enabled animals to spawn and pathfind
	// it does, however, not remove the ambient light requirement for animal spawns
	@Inject(method = "getWalkTargetValue", at = @At("HEAD"), cancellable = true)
	public void getPathfindingFavor(BlockPos pos, LevelReader world, CallbackInfoReturnable<Float> cir) {
		if (world.getBlockState(pos.below()).is(SpectrumBlockTags.ANIMALS_SPAWNABLE_ON_ADDITIONS)) {
			cir.setReturnValue(10.0F);
		}
	}
	
	
}
