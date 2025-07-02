package de.dafuqs.spectrum.blocks.item_roundel;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

public class ItemRoundelBlockEntity extends InWorldInteractionBlockEntity {
	
	protected static final int INVENTORY_SIZE = 6;
	
	public ItemRoundelBlockEntity(BlockPos pos, BlockState state) {
		this(SpectrumBlockEntities.ITEM_ROUNDEL, pos, state, INVENTORY_SIZE);
	}
	
	public ItemRoundelBlockEntity(BlockEntityType<? extends ItemRoundelBlockEntity> blockEntityType, BlockPos pos, BlockState state, int inventorySize) {
		super(blockEntityType, pos, state, inventorySize);
	}
	
	public boolean renderStacksAsIndividualItems() {
		return false;
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		this.unpackLootTable(null);
		return super.getUpdateTag(registryLookup);
	}
}
