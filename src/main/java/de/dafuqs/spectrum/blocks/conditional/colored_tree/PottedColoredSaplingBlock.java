package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.revelationary.api.revelations.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PottedColoredSaplingBlock extends FlowerPotBlock implements RevelationAware, ColoredTree {
	
	protected final DyeColor color;
	
	public PottedColoredSaplingBlock(Block content, Settings settings, DyeColor color) {
		super(content, settings);
		this.color = color;
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return ColoredTree.getTreeCloakAdvancementIdentifier(TreePart.SAPLING, this.color);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		map.put(this.getDefaultState(), Blocks.POTTED_OAK_SAPLING.getDefaultState());
		return map;
	}
	
	@Override
	public @Nullable Pair<Item, Item> getItemCloak() {
		return null; // does not exist in item form
	}
	
	@Override
	public DyeColor getColor() {
		return this.color;
	}
	
}