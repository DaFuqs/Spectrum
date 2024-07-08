package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.api.block.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.nbt.*;
import net.minecraft.network.listener.*;
import net.minecraft.network.packet.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

public abstract class InWorldInteractionBlockEntity extends BlockEntity implements ImplementedInventory {
	
	private final int inventorySize;
	protected DefaultedList<ItemStack> items;
	@Nullable
	protected Identifier lootTableId;
	protected long lootTableSeed;
	
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
	
	protected boolean deserializeLootTable(NbtCompound nbt) {
		if (nbt.contains("LootTable", NbtElement.STRING_TYPE)) {
			this.lootTableId = new Identifier(nbt.getString("LootTable"));
			this.lootTableSeed = nbt.getLong("LootTableSeed");
			return true;
		}
		
		return false;
	}

	protected boolean serializeLootTable(NbtCompound nbt) {
		if (this.lootTableId == null) {
			return false;
		}
		
		nbt.putString("LootTable", this.lootTableId.toString());
		if (this.lootTableSeed != 0L) {
			nbt.putLong("LootTableSeed", this.lootTableSeed);
		}
		
		return true;
	}

	public void checkLootInteraction(@Nullable PlayerEntity player) {
		var world = this.getWorld();
		if (world != null && this.lootTableId != null && world.getServer() != null) {
			LootTable lootTable = world.getServer().getLootManager().getLootTable(this.lootTableId);
			if (player instanceof ServerPlayerEntity serverPlayerEntity) {
				Criteria.PLAYER_GENERATES_CONTAINER_LOOT.trigger(serverPlayerEntity, this.lootTableId);
			}
			
			this.lootTableId = null;
			var builder = new LootContextParameterSet.Builder((ServerWorld) world)
					.add(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.pos));
			if (player != null) {
				builder.luck(player.getLuck()).add(LootContextParameters.THIS_ENTITY, player);
			}

			if (lootTableSeed != 0) {
				lootTable.supplyInventory(this, builder.build(LootContextTypes.CHEST), lootTableSeed);
			} else {
				lootTable.supplyInventory(this, builder.build(LootContextTypes.CHEST), this.getWorld().getRandom().nextLong());
			}

			this.markDirty();
		}
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
