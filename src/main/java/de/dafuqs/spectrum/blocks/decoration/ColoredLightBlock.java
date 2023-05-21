package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

import java.util.*;

public class ColoredLightBlock extends RedstoneLampBlock {
	
	private static final Map<DyeColor, ColoredLightBlock> LIGHTS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public ColoredLightBlock(Settings settings, DyeColor color) {
		super(settings);
		this.color = color;
		LIGHTS.put(color, this);
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static ColoredLightBlock byColor(DyeColor color) {
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
	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}
	
}
