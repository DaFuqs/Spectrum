package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.*;
import net.minecraft.block.*;
import net.minecraft.util.*;

import java.util.*;

public class ColoredWoodenButtonBlock extends WoodenButtonBlock {
	
	private static final Map<DyeColor, ColoredWoodenButtonBlock> BLOCKS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredWoodenButtonBlock(Settings settings, DyeColor color) {
		super(settings);
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
