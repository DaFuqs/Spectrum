package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.fluid.*;
import net.minecraft.registry.tag.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

/**
 * WaterPathNodeMakers are hardcoded to check for the #minecraft:water fluid tag
 * To make it pick up other fluids for pathfinding we modify that check to use a
 * custom pathfinding fluid tag which includes water, but can be extended.
 */
@Mixin(WaterPathNodeMaker.class)
public class WaterPathNodeMarkerMixin {
	
	@ModifyArg(method = "getNodeType", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	private TagKey<Fluid> spectrum$tagBasedWaterNavigation(TagKey<Fluid> tag) {
		return SpectrumFluidTags.USES_WATER_PATHFINDING;
	}
	
}
