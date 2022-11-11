package de.dafuqs.spectrum.blocks.item_roundel;

import de.dafuqs.spectrum.blocks.InWorldInteractionBlockEntity;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class ItemRoundelBlockEntity extends InWorldInteractionBlockEntity {
	
	protected static final int INVENTORY_SIZE = 6;
	
	public ItemRoundelBlockEntity(BlockPos pos, BlockState state) {
		this(SpectrumBlockEntities.ITEM_ROUNDEL, pos, state);
	}
	
	public ItemRoundelBlockEntity(BlockEntityType blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state, INVENTORY_SIZE);
	}
	
}
