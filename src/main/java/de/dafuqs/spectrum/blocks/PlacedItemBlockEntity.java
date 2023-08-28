package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlacedItemBlockEntity extends BlockEntity implements PlayerOwned {
	
	protected ItemStack stack = ItemStack.EMPTY;
	protected UUID ownerUUID;
	
	public PlacedItemBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	public PlacedItemBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PLACED_ITEM, pos, state);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.stack = ItemStack.fromNbt(nbt.getCompound("stack"));
		
		if (nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("stack", this.stack.writeNbt(new NbtCompound()));
		
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
	}
	
	public void setStack(ItemStack stack) {
		this.stack = stack;
		this.markDirty();
	}
	
	public ItemStack getStack() {
		return this.stack;
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(@NotNull PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		markDirty();
	}
	
}
