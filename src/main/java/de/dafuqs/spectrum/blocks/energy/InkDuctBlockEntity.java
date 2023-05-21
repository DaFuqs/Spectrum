package de.dafuqs.spectrum.blocks.energy;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.*;

public class InkDuctBlockEntity extends BlockEntity {
	
	public static final int RANGE = 64;
	
	public InkDuctBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}
	
	public void canSee(InkDuctBlockEntity duct) {
		duct.pos.isWithinDistance(pos, RANGE);
	}
	
	private boolean canTransferTo(BlockEntity blockEntity) {
		return blockEntity instanceof InkDuctBlockEntity;
	}
	
	public int getRange() {
		return RANGE;
	}
}
