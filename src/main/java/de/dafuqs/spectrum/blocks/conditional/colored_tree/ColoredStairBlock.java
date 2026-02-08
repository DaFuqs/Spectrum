package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.flammable.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class ColoredStairBlock extends FlammableStairBlock {
	
	public static final MapCodec<ColoredStairBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BlockState.CODEC.fieldOf("base_state").forGetter(b -> b.baseState),
			propertiesCodec(),
			InkColor.CODEC.fieldOf("color").forGetter(ColoredStairBlock::getColor)
	).apply(instance, ColoredStairBlock::new));
	
	private static final Map<InkColor, ColoredStairBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredStairBlock(BlockState baseBlockState, Properties settings, InkColor color) {
		super(baseBlockState, settings);
		this.color = color;
		BLOCKS.put(color, this);
	}
	
	@Override
	public MapCodec<? extends ColoredStairBlock> codec() {
		return CODEC;
	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredStairBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
	
}
