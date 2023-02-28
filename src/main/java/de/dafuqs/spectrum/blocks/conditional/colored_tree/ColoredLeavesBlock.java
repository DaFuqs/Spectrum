package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.google.common.collect.Maps;
import de.dafuqs.revelationary.api.revelations.RevelationAware;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.client.SpectrumColorProviders;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Hashtable;
import java.util.Map;

public class ColoredLeavesBlock extends LeavesBlock implements RevelationAware {
	
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
		return SpectrumCommon.locate("milestones/reveal_colored_trees");
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		for (int distance = 1; distance < 8; distance++) {
			hashtable.put(this.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, false), Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, false));
			hashtable.put(this.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, true), Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, true));
		}
		return hashtable;
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
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredLeavesBlock byColor(DyeColor color) {
		return LEAVES.get(color);
	}
	
}
