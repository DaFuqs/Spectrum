package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.util.*;

import java.util.*;

public class ColoredPressurePlateBlock extends PressurePlateBlock {
	
	private static final Map<DyeColor, ColoredPressurePlateBlock> BLOCKS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredPressurePlateBlock(PressurePlateBlock.ActivationRule type, Settings settings, DyeColor color) {
		super(type, settings, SpectrumBlockSetTypes.COLORED_WOOD);
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
