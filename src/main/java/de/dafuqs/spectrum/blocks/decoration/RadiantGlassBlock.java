package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class RadiantGlassBlock extends GlassBlock {
	
	public RadiantGlassBlock(Settings settings) {
		super(settings);
	}
	
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		if (stateFrom.isOf(this) || stateFrom.isOf(SpectrumBlocks.RADIANT_PLAYER_ONLY_GLASS)) {
			return true;
		}
		return super.isSideInvisible(state, stateFrom, direction);
	}
	
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}
	
}
