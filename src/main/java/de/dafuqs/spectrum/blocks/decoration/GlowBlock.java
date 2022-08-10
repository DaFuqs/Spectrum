package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.Map;

public class GlowBlock extends Block {
	
	private static final Map<DyeColor, GlowBlock> GLOWBLOCKS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public GlowBlock(Settings settings, DyeColor color) {
		super(settings);
		this.color = color;
		GLOWBLOCKS.put(color, this);
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static GlowBlock byColor(DyeColor color) {
		return GLOWBLOCKS.get(color);
	}
	
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1.0F;
	}
	
}
