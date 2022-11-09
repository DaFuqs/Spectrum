package de.dafuqs.spectrum.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class InWorldInteractionBlockEntity extends BlockEntity implements Inventory {
	
	private final int inventorySize;
	protected SimpleInventory inventory;
	
	public InWorldInteractionBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
		super(type, pos, state);
		this.inventorySize = inventorySize;
		this.inventory = new SimpleInventory(inventorySize);
	}
	
	// interaction methods
	public void updateInClientWorld(World world, BlockPos pos) {
		world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NO_REDRAW);
	}
	
	// Called when the chunk is first loaded to initialize this be
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = new SimpleInventory(inventorySize);
		this.inventory.readNbtList(nbt.getList("inventory", 10));
	}
	
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("inventory", this.inventory.toNbtList());
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	// Inventory
	@Override
	public int size() {
		return inventory.size();
	}
	
	@Override
	public boolean isEmpty() {
		return inventory.isEmpty();
	}
	
	@Override
	public ItemStack getStack(int slot) {
		return inventory.getStack(slot);
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount) {
		return inventory.removeStack(slot);
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		return inventory.removeStack(slot);
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.setStack(slot, stack);
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return inventory.canPlayerUse(player);
	}
	
	@Override
	public void clear() {
		inventory.clear();
	}
	
}
