package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.flammable.*;
import it.unimi.dsi.fastutil.objects.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class ColoredFenceBlock extends FlammableFenceBlock {
	
	private static final Map<InkColor, ColoredFenceBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredFenceBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		BLOCKS.put(color, this);
	}

//	@Override
//	public MapCodec<? extends ColoredFenceBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	public InkColor getColor() {
		return this.color;
	}

	public static @Nullable ColoredFenceBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
	
}
