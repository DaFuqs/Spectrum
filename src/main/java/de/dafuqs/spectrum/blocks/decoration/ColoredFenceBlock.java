package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.Maps;
import net.minecraft.block.FenceBlock;
import net.minecraft.util.DyeColor;

import java.util.Map;

public class ColoredFenceBlock extends FenceBlock {
	
	private static final Map<DyeColor, ColoredFenceBlock> BLOCKS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredFenceBlock(Settings settings, DyeColor color) {
		super(settings);
		this.color = color;
		BLOCKS.put(color, this);
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredFenceBlock byColor(DyeColor color) {
		return BLOCKS.get(color);
	}
	
}
