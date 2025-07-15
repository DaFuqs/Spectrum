package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class ColoredPressurePlateBlock extends PressurePlateBlock {
	
	private static final Map<InkColor, ColoredPressurePlateBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredPressurePlateBlock(Properties settings, InkColor color) {
		super(SpectrumBlockSetTypes.COLORED_WOOD, settings);
		this.color = color;
		BLOCKS.put(color, this);
	}

//	@Override
//	public MapCodec<? extends ColoredPressurePlateBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredPressurePlateBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
	
}
