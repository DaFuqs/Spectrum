package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;

public interface ColoredTree {
	
	enum TreePart {
		SAPLING,
		LOG,
		LEAVES,
		STRIPPED_LOG,
		WOOD,
		STRIPPED_WOOD
	}
	
	static ResourceLocation getTreeCloakAdvancementIdentifier(TreePart treePart, InkColor color) {
		if (color == InkColors.WHITE || color == InkColors.LIGHT_GRAY || color == InkColors.GRAY) {
			return SpectrumAdvancements.REVEAL_COLORED_TREES_WHITE;
		}
		if (color == InkColors.BLACK || color == InkColors.BROWN) {
			return SpectrumAdvancements.REVEAL_COLORED_TREES_BLACK;
		}
		
		return treePart == TreePart.SAPLING ? SpectrumAdvancements.REVEAL_COLORED_SAPLINGS_CMY : SpectrumAdvancements.REVEAL_COLORED_TREES_CMY;
	}
	
	InkColor getColor();
	
}
