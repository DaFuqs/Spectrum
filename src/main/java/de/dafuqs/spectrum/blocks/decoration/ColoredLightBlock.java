package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Map;

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
