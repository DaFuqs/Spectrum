package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.api.energy.color.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class ColoredLogBlock extends RotatedPillarBlock implements RevelationAware, ColoredTree {

	public static final MapCodec<ColoredLogBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			propertiesCodec(),
			InkColor.CODEC.fieldOf("color").forGetter(ColoredLogBlock::getColor)
	).apply(instance, ColoredLogBlock::new));
	
	private static final Map<InkColor, ColoredLogBlock> LOGS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredLogBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		LOGS.put(color, this);
		RevelationAware.register(this);
	}

	@Override
	public MapCodec<? extends ColoredLogBlock> codec() {
		return CODEC;
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return ColoredTree.getTreeCloakAdvancementIdentifier(ColoredTree.TreePart.LOG, this.color);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (Direction.Axis axis : RotatedPillarBlock.AXIS.getPossibleValues()) {
			map.put(this.defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis), Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis));
		}
		return map;
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.OAK_LOG.asItem());
	}
	
	@Override
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredLogBlock byColor(InkColor color) {
		return LOGS.get(color);
	}
	
	public static Collection<ColoredLogBlock> all() {
		return LOGS.values();
	}
	
}
