package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.tags.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(SwimNodeEvaluator.class)
public class SwimNodeEvaluatorMixin {
	
	// fixes weirdness with entities like fish not being able to swim in liquid crystal
	// TODO: test and remove in MC 26.1 since Neo patched that in https://github.com/neoforged/NeoForge/commit/ad038e822a142901aeb33b0eedde0a892588b662
	@ModifyArg(method = "getPathTypeOfMob(Lnet/minecraft/world/level/pathfinder/PathfindingContext;IIILnet/minecraft/world/entity/Mob;)Lnet/minecraft/world/level/pathfinder/PathType;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"), index = 0)
	TagKey<Fluid> spectrum$getPathTypeOfMob(TagKey<Fluid> tag) {
		return SpectrumFluidTags.WATER_PATH_NODES;
	}
	
}
