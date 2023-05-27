package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.Maps;
import de.dafuqs.spectrum.registries.SpectrumBlockSetTypes;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.ButtonBlock;
import net.minecraft.util.DyeColor;

import java.util.Map;

public class ColoredWoodenButtonBlock extends ButtonBlock {
	
	private static final Map<DyeColor, ColoredWoodenButtonBlock> BLOCKS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredWoodenButtonBlock(Settings settings, DyeColor color) {
		super(settings, SpectrumBlockSetTypes.COLORED_WOOD, 30, true);
		this.color = color;
		BLOCKS.put(color, this);
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredWoodenButtonBlock byColor(DyeColor color) {
		return BLOCKS.get(color);
	}
	
}
