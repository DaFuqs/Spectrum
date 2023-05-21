package de.dafuqs.spectrum.blocks.mob_head;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class SpectrumWallSkullBlock extends WallSkullBlock {
	
	public SpectrumWallSkullBlock(SkullBlock.SkullType skullType, Settings settings) {
		super(skullType, settings);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SpectrumSkullBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return null;
	}
	
}
