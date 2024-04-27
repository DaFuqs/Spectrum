package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.google.common.collect.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.util.*;

public class ColoredLeavesBlock extends LeavesBlock implements RevelationAware, ColoredTree {
	
	private static final Map<DyeColor, ColoredLeavesBlock> LEAVES = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredLeavesBlock(Settings settings, DyeColor color) {
		super(settings);
		this.color = color;
		LEAVES.put(color, this);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return ColoredTree.getTreeCloakAdvancementIdentifier(TreePart.LEAVES, this.color);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (int distance = 1; distance < 8; distance++) {
			map.put(this.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, false).with(WATERLOGGED, false), Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, false).with(WATERLOGGED, false));
			map.put(this.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, false).with(WATERLOGGED, true), Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, false).with(WATERLOGGED, true));
			
			map.put(this.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, true).with(WATERLOGGED, false), Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, true).with(WATERLOGGED, false));
			map.put(this.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, true).with(WATERLOGGED, true), Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, true).with(WATERLOGGED, true));
		}
		return map;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.OAK_LEAVES.asItem());
	}
	
	@Override
	public void onUncloak() {
		if (SpectrumColorProviders.coloredLeavesBlockColorProvider != null && SpectrumColorProviders.coloredLeavesItemColorProvider != null) {
			SpectrumColorProviders.coloredLeavesBlockColorProvider.setShouldApply(false);
			SpectrumColorProviders.coloredLeavesItemColorProvider.setShouldApply(false);
		}
	}
	
	@Override
	public void onCloak() {
		if (SpectrumColorProviders.coloredLeavesBlockColorProvider != null && SpectrumColorProviders.coloredLeavesItemColorProvider != null) {
			SpectrumColorProviders.coloredLeavesBlockColorProvider.setShouldApply(true);
			SpectrumColorProviders.coloredLeavesItemColorProvider.setShouldApply(true);
		}
	}
	
	@Override
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredLeavesBlock byColor(DyeColor color) {
		return LEAVES.get(color);
	}
	
	public static Collection<ColoredLeavesBlock> all() {
		return LEAVES.values();
	}
	
}
