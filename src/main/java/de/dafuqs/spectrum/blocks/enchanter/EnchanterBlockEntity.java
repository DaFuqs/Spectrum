package de.dafuqs.spectrum.blocks.enchanter;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;

public class EnchanterBlockEntity extends BlockEntity {
	
	public static final int INVENTORY_SIZE = 2; // 0: any itemstack, 1: Knowledge Drop;
	protected SimpleInventory inventory;
	@Nullable
	private Direction itemFacing;
	
	public EnchanterBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.ENCHANTER, pos, state);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		this.inventory.readNbtList(nbt.getList("inventory", 10));
		if(nbt.contains("item_facing", NbtElement.STRING_TYPE)) {
			this.itemFacing = Direction.valueOf(nbt.getString("item_facing").toUpperCase(Locale.ROOT));
		}
	}
	
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("inventory", this.inventory.toNbtList());
		if(this.itemFacing != null) {
			nbt.putString("item_facing", this.itemFacing.toString().toUpperCase(Locale.ROOT));
		}
	}
	
	// Called when the chunk is first loaded to initialize this be
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	public void updateInClientWorld() {
		world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NO_REDRAW);
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public void setItemFacingDirection(Direction facingDirection) {
		this.itemFacing = facingDirection;
	}
	
	public Direction getItemFacingDirection() {
		// if placed via pipe or other sources
		return Objects.requireNonNullElse(this.itemFacing, Direction.NORTH);
	}
	
}
