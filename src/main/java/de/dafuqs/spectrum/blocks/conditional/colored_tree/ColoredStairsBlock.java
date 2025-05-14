package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.energy.color.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class ColoredStairsBlock extends StairBlock {

	public static final MapCodec<ColoredStairsBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BlockState.CODEC.fieldOf("base_state").forGetter(b -> b.baseState),
			propertiesCodec(),
			InkColor.CODEC.fieldOf("color").forGetter(ColoredStairsBlock::getColor)
	).apply(instance, ColoredStairsBlock::new));
	
	private static final Map<InkColor, ColoredStairsBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredStairsBlock(BlockState baseBlockState, Properties settings, InkColor color) {
		super(baseBlockState, settings);
		this.color = color;
		BLOCKS.put(color, this);
	}

	@Override
	public MapCodec<? extends ColoredStairsBlock> codec() {
		return CODEC;
	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredStairsBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
	
}
