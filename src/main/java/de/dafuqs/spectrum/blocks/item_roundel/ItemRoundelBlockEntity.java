package de.dafuqs.spectrum.blocks.item_roundel;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.inventory.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;

public class ItemRoundelBlockEntity extends InWorldInteractionBlockEntity {
	
	protected static final int INVENTORY_SIZE = 6;
	
	public ItemRoundelBlockEntity(BlockPos pos, BlockState state) {
		this(SpectrumBlockEntities.ITEM_ROUNDEL, pos, state, INVENTORY_SIZE);
	}
	
	public ItemRoundelBlockEntity(BlockEntityType blockEntityType, BlockPos pos, BlockState state, int inventorySize) {
		super(blockEntityType, pos, state, inventorySize);
	}
	
	public boolean renderStacksAsIndividualItems() {
		return false;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.items);
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.items);
		}
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		this.checkLootInteraction(null);
		return super.toInitialChunkDataNbt();
	}
}
