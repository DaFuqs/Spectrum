package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class ColoredWoodenButtonBlock extends ButtonBlock {
	
	private static final Map<InkColor, ColoredWoodenButtonBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredWoodenButtonBlock(Properties settings, InkColor color) {
		super(SpectrumBlockSetTypes.COLORED_WOOD, 30, settings);
		this.color = color;
		BLOCKS.put(color, this);
	}

//	@Override
//	public MapCodec<? extends ColoredWoodenButtonBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredWoodenButtonBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
	
}
