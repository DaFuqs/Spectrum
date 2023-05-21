package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.google.common.collect.*;
import de.dafuqs.revelationary.api.revelations.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.util.*;

public class ColoredSaplingBlock extends SaplingBlock implements RevelationAware, ColoredTree {
	
	private static final Map<DyeColor, ColoredSaplingBlock> SAPLINGS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredSaplingBlock(Settings settings, DyeColor color) {
		super(new ColoredSaplingGenerator(color), settings);
		this.color = color;
		SAPLINGS.put(color, this);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return ColoredTree.getTreeCloakAdvancementIdentifier(ColoredTree.TreePart.SAPLING, this.color);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		// Colored Logs => Oak logs
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		for (int stage = 0; stage < 2; stage++) {
			hashtable.put(this.getDefaultState().with(SaplingBlock.STAGE, stage), Blocks.OAK_SAPLING.getDefaultState().with(SaplingBlock.STAGE, stage));
		}
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.OAK_SAPLING.asItem());
	}
	
	@Override
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredSaplingBlock byColor(DyeColor color) {
		return SAPLINGS.get(color);
	}
	
}
