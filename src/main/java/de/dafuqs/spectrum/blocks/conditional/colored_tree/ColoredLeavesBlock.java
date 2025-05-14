package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.registries.client.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class ColoredLeavesBlock extends LeavesBlock implements RevelationAware, ColoredTree {

	public static final MapCodec<ColoredLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			propertiesCodec(),
			InkColor.CODEC.fieldOf("color").forGetter(ColoredLeavesBlock::getColor)
	).apply(instance, ColoredLeavesBlock::new));
	
	private static final Map<InkColor, ColoredLeavesBlock> LEAVES = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredLeavesBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		LEAVES.put(color, this);
		RevelationAware.register(this);
	}

	@Override
	public MapCodec<? extends ColoredLeavesBlock> codec() {
		return CODEC;
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return ColoredTree.getTreeCloakAdvancementIdentifier(TreePart.LEAVES, this.color);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (int distance = 1; distance < 8; distance++) {
			map.put(this.defaultBlockState().setValue(LeavesBlock.DISTANCE, distance).setValue(LeavesBlock.PERSISTENT, false).setValue(WATERLOGGED, false), Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, distance).setValue(LeavesBlock.PERSISTENT, false).setValue(WATERLOGGED, false));
			map.put(this.defaultBlockState().setValue(LeavesBlock.DISTANCE, distance).setValue(LeavesBlock.PERSISTENT, false).setValue(WATERLOGGED, true), Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, distance).setValue(LeavesBlock.PERSISTENT, false).setValue(WATERLOGGED, true));
			
			map.put(this.defaultBlockState().setValue(LeavesBlock.DISTANCE, distance).setValue(LeavesBlock.PERSISTENT, true).setValue(WATERLOGGED, false), Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, distance).setValue(LeavesBlock.PERSISTENT, true).setValue(WATERLOGGED, false));
			map.put(this.defaultBlockState().setValue(LeavesBlock.DISTANCE, distance).setValue(LeavesBlock.PERSISTENT, true).setValue(WATERLOGGED, true), Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, distance).setValue(LeavesBlock.PERSISTENT, true).setValue(WATERLOGGED, true));
		}
		return map;
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.OAK_LEAVES.asItem());
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
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredLeavesBlock byColor(InkColor color) {
		return LEAVES.get(color);
	}
	
	public static Collection<ColoredLeavesBlock> all() {
		return LEAVES.values();
	}
	
}
