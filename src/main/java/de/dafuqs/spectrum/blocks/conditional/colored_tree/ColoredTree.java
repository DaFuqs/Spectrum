package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.spectrum.*;
import net.minecraft.util.*;

public interface ColoredTree {
	
	Identifier SAPLING_CMY_ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("milestones/reveal_colored_saplings_cmy");
	Identifier TREES_CMY_IDENTIFIER = SpectrumCommon.locate("milestones/reveal_colored_trees_cmy");
	Identifier TREES_B_IDENTIFIER = SpectrumCommon.locate("milestones/reveal_colored_trees_k");
	Identifier TREES_W_IDENTIFIER = SpectrumCommon.locate("milestones/reveal_colored_trees_w");
	
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
				return TREES_W_IDENTIFIER;
			}
			case BLACK, BROWN -> {
				return TREES_B_IDENTIFIER;
			}
			default -> {
				return treePart == TreePart.SAPLING ? SAPLING_CMY_ADVANCEMENT_IDENTIFIER : TREES_CMY_IDENTIFIER;
			}
		}
	}
	
	DyeColor getColor();
	
}
