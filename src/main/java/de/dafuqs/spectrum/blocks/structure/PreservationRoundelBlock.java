package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.blocks.item_roundel.ItemRoundelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class PreservationRoundelBlock extends ItemRoundelBlock {
	
	public PreservationRoundelBlock(Settings settings) {
		super(settings);
	}
	
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PreservationRoundelBlockEntity(pos, state);
	}
	
}
