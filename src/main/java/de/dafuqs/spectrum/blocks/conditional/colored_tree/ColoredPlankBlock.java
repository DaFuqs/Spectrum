package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.flammable.*;
import it.unimi.dsi.fastutil.objects.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class ColoredPlankBlock extends FlammablePlankBlock {
	
	public static final MapCodec<ColoredPlankBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			propertiesCodec(),
			InkColor.CODEC.fieldOf("color").forGetter(ColoredPlankBlock::getColor)
	).apply(instance, ColoredPlankBlock::new));
	
	private static final Map<InkColor, ColoredPlankBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredPlankBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		BLOCKS.put(color, this);
	}
	
	public static Iterable<? extends ColoredPlankBlock> all() {
		return BLOCKS.values();
	}
	
	@Override
	public MapCodec<? extends ColoredPlankBlock> codec() {
		return CODEC;
	}
	
	public InkColor getColor() {
		return this.color;
	}

	public static @Nullable ColoredPlankBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
	
}
