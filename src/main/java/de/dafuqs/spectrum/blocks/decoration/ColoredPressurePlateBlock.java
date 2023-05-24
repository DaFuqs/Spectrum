package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.util.DyeColor;

import java.util.Map;

public class ColoredPressurePlateBlock extends PressurePlateBlock {
	
	private static final Map<DyeColor, ColoredPressurePlateBlock> BLOCKS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule type, Settings settings, DyeColor color) {
		// TODO - BlockSetType/WoodType
		super(type, settings, BlockSetType.SPRUCE);
		this.color = color;
		BLOCKS.put(color, this);
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredPressurePlateBlock byColor(DyeColor color) {
		return BLOCKS.get(color);
	}
	
}
