package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.flammable.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;

import java.util.*;

public class ColoredFenceGateBlock extends FlammableFenceGateBlock {
	
	private static final Map<InkColor, ColoredFenceGateBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredFenceGateBlock(Properties settings, InkColor color) {
		super(SpectrumWoodTypes.COLORED_WOOD, settings);
		this.color = color;
		BLOCKS.put(color, this);
	}

//	@Override
//	public MapCodec<? extends ColoredFenceGateBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredFenceGateBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
	
}
