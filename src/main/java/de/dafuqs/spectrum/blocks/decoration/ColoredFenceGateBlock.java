package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.*;
import net.minecraft.block.*;
import net.minecraft.util.*;

import java.util.*;

public class ColoredFenceGateBlock extends FenceGateBlock {
	
	private static final Map<DyeColor, ColoredFenceGateBlock> BLOCKS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredFenceGateBlock(Settings settings, DyeColor color) {
		super(settings);
		this.color = color;
		BLOCKS.put(color, this);
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredFenceGateBlock byColor(DyeColor color) {
		return BLOCKS.get(color);
	}
	
}
