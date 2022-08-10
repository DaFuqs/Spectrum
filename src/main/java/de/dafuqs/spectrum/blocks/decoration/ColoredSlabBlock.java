package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.Maps;
import net.minecraft.block.SlabBlock;
import net.minecraft.util.DyeColor;

import java.util.Map;

public class ColoredSlabBlock extends SlabBlock {
	
	private static final Map<DyeColor, ColoredSlabBlock> BLOCKS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredSlabBlock(Settings settings, DyeColor color) {
		super(settings);
		this.color = color;
		BLOCKS.put(color, this);
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredSlabBlock byColor(DyeColor color) {
		return BLOCKS.get(color);
	}
	
}
