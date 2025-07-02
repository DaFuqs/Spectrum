package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.registry.*;
import net.minecraft.world.level.pathfinder.*;

public class SpectrumPathNodeTypes {

	public static void register() {
		LandPathNodeTypesRegistry.register(SpectrumBlocks.PRIMORDIAL_FIRE, PathType.DAMAGE_FIRE, PathType.DANGER_FIRE);
		LandPathNodeTypesRegistry.register(SpectrumBlocks.MIDNIGHT_SOLUTION, PathType.DAMAGE_OTHER, PathType.DANGER_OTHER);
		LandPathNodeTypesRegistry.register(SpectrumBlocks.DRAGONROT, PathType.DAMAGE_OTHER, PathType.DANGER_OTHER);
		
		LandPathNodeTypesRegistry.register(SpectrumBlocks.PYRITE_RIPPER, PathType.DAMAGE_OTHER, PathType.DANGER_OTHER);
		LandPathNodeTypesRegistry.register(SpectrumBlocks.BRISTLE_SPROUTS, PathType.DAMAGE_OTHER, PathType.DANGER_OTHER);
		LandPathNodeTypesRegistry.register(SpectrumBlocks.SAWBLADE_HOLLY_BUSH, PathType.DAMAGE_OTHER, PathType.DANGER_OTHER);
		
		LandPathNodeTypesRegistry.register(SpectrumBlocks.SLUDGE, PathType.WATER, PathType.WATER_BORDER);
		LandPathNodeTypesRegistry.register(SpectrumBlocks.LIQUID_CRYSTAL, PathType.WATER, PathType.WATER_BORDER);
	}

}
