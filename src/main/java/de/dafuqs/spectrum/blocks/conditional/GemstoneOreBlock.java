package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.enums.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import net.minecraft.util.math.intprovider.*;

public class GemstoneOreBlock extends CloakedOreBlock {
	
	private final GemstoneColor gemstoneColor;
	
	public GemstoneOreBlock(Settings settings, UniformIntProvider experienceDropped, GemstoneColor gemstoneColor, Identifier cloakAdvancementIdentifier, BlockState cloakBlockState) {
		super(settings, experienceDropped, cloakAdvancementIdentifier, cloakBlockState);
		this.gemstoneColor = gemstoneColor;
	}
	
	public GemstoneColor getGemstoneColor() {
		return gemstoneColor;
	}
	
}
