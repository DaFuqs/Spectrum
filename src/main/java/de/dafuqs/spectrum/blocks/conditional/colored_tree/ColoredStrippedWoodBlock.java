package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.google.common.collect.*;
import de.dafuqs.revelationary.api.revelations.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.util.*;

public class ColoredStrippedWoodBlock extends PillarBlock implements RevelationAware, ColoredTree {
	
	private static final Map<DyeColor, ColoredStrippedWoodBlock> WOOD = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredStrippedWoodBlock(Settings settings, DyeColor color) {
		super(settings);
		this.color = color;
		WOOD.put(color, this);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return ColoredTree.getTreeCloakAdvancementIdentifier(TreePart.STRIPPED_WOOD, this.color);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Map.of(this.getDefaultState(), Blocks.STRIPPED_OAK_WOOD.getDefaultState());
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.STRIPPED_OAK_WOOD.asItem());
	}
	
	@Override
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredStrippedWoodBlock byColor(DyeColor color) {
		return WOOD.get(color);
	}
	
}
