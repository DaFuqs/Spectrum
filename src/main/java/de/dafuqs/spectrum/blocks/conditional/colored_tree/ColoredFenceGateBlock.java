package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.flammable.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.world.level.block.state.properties.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class ColoredFenceGateBlock extends FlammableFenceGateBlock {
	
	private static final Map<InkColor, ColoredFenceGateBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredFenceGateBlock(Properties settings, WoodType woodType, InkColor color) {
		super(woodType, settings);
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

	public static @Nullable ColoredFenceGateBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
	
}
