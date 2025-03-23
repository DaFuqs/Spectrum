package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.*;

public interface ColoredTree {
	
	enum TreePart {
		SAPLING,
		LOG,
		LEAVES,
		STRIPPED_LOG,
		WOOD,
		STRIPPED_WOOD
	}
	
	static Identifier getTreeCloakAdvancementIdentifier(TreePart treePart, DyeColor color) {
		switch (color) {
			case WHITE, LIGHT_GRAY, GRAY -> {
				return SpectrumAdvancements.REVEAL_COLORED_TREES_WHITE;
			}
			case BLACK, BROWN -> {
				return SpectrumAdvancements.REVEAL_COLORED_TREES_BLACK;
			}
			default -> {
				return treePart == TreePart.SAPLING ? SpectrumAdvancements.REVEAL_COLORED_SAPLINGS_CMY : SpectrumAdvancements.REVEAL_COLORED_TREES_CMY;
			}
		}
	}
	
	DyeColor getColor();
	
}
