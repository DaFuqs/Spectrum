package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.energy.color.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class ColoredSlabBlock extends SlabBlock {

	public static final MapCodec<ColoredSlabBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			propertiesCodec(),
			InkColor.CODEC.fieldOf("color").forGetter(ColoredSlabBlock::getColor)
	).apply(instance, ColoredSlabBlock::new));
	
	private static final Map<InkColor, ColoredSlabBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredSlabBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		BLOCKS.put(color, this);
	}

	@Override
	public MapCodec<? extends ColoredSlabBlock> codec() {
		return CODEC;
	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredSlabBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
	
}
