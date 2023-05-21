package de.dafuqs.spectrum.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.listener.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.world.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

public abstract class InWorldInteractionBlockEntity extends BlockEntity implements ImplementedInventory {
	
	private final int inventorySize;
	private DefaultedList<ItemStack> items;
	
	public InWorldInteractionBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
		super(type, pos, state);
		this.inventorySize = inventorySize;
		this.items = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
	}
	
	// interaction methods
	public void updateInClientWorld() {
		((ServerWorld) world).getChunkManager().markForUpdate(pos);
	}
	
	// Called when the chunk is first loaded to initialize this be
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.items = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
		Inventories.readNbt(nbt, items);
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, items);
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	@Override
	public DefaultedList<ItemStack> getItems() {
		return items;
	}
	
	@Override
	public void inventoryChanged() {
		this.markDirty();
		if (world != null && !world.isClient) {
			updateInClientWorld();
		}
	}
	
}
