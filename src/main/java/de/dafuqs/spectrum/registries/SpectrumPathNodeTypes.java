package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.registry.*;
import net.minecraft.entity.ai.pathing.*;

public class SpectrumPathNodeTypes {

	public static void register() {
		LandPathNodeTypesRegistry.register(SpectrumBlocks.PRIMORDIAL_FIRE, PathNodeType.DAMAGE_FIRE, PathNodeType.DANGER_FIRE);
		LandPathNodeTypesRegistry.register(SpectrumBlocks.MIDNIGHT_SOLUTION, PathNodeType.DAMAGE_OTHER, PathNodeType.DANGER_OTHER);
		LandPathNodeTypesRegistry.register(SpectrumBlocks.DRAGONROT, PathNodeType.DAMAGE_OTHER, PathNodeType.DANGER_OTHER);

		LandPathNodeTypesRegistry.register(SpectrumBlocks.PYRITE_RIPPER, PathNodeType.DAMAGE_OTHER, PathNodeType.DANGER_OTHER);
		LandPathNodeTypesRegistry.register(SpectrumBlocks.BRISTLE_SPROUTS, PathNodeType.DAMAGE_OTHER, PathNodeType.DANGER_OTHER);
		LandPathNodeTypesRegistry.register(SpectrumBlocks.SAWBLADE_HOLLY_BUSH, PathNodeType.DAMAGE_OTHER, PathNodeType.DANGER_OTHER);

		LandPathNodeTypesRegistry.register(SpectrumBlocks.MUD, PathNodeType.WATER, PathNodeType.WATER_BORDER);
		LandPathNodeTypesRegistry.register(SpectrumBlocks.LIQUID_CRYSTAL, PathNodeType.WATER, PathNodeType.WATER_BORDER);
	}

}
