package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.spectrum.api.energy.color.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;

import java.util.*;

public class ColoredLightBlock extends RedstoneLampBlock {
	
	private static final Map<InkColor, ColoredLightBlock> LIGHTS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredLightBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		LIGHTS.put(color, this);
	}

//	@Override
//	public MapCodec<? extends ColoredLightBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredLightBlock byColor(InkColor color) {
		return LIGHTS.get(color);
	}
	
	/**
	 * Disable culling for this block
	 * => the translucent outlines will be rendered
	 * even if the side is obstructed by a block
	 * (disabling culling is not nice for performance,
	 * but usually most sides will be visible either way)
	 */
	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
		return Shapes.empty();
	}
	
}
