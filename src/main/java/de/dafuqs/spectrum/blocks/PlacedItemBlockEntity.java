package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;

public class PlacedItemBlockEntity extends BlockEntity {
	
	protected ItemStack stack = ItemStack.EMPTY;
	
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
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("stack", this.stack.writeNbt(new NbtCompound()));
	}
	
	public void setStack(ItemStack stack) {
		this.stack = stack;
		this.markDirty();
	}
	
	public ItemStack getStack() {
		return this.stack;
	}
	
}
