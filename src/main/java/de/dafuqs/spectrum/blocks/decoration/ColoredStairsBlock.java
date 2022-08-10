package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.DyeColor;

import java.util.Map;

public class ColoredStairsBlock extends StairsBlock {
	
	private static final Map<DyeColor, ColoredStairsBlock> BLOCKS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredStairsBlock(BlockState baseBlockState, Settings settings, DyeColor color) {
		super(baseBlockState, settings);
		this.color = color;
		BLOCKS.put(color, this);
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredStairsBlock byColor(DyeColor color) {
		return BLOCKS.get(color);
	}
	
}
