package de.dafuqs.spectrum.blocks.ender;

import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.interfaces.PlayerOwnedWithName;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class EnderDropperBlockEntity extends BlockEntity implements PlayerOwnedWithName {

	private UUID ownerUUID;
	private String ownerName;

	public EnderDropperBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.ENDER_DROPPER, blockPos, blockState);
	}

	protected Text getContainerName() {
		if(this.ownerName == null) {
			return new TranslatableText("block.spectrum.ender_dropper");
		} else {
			return new TranslatableText("block.spectrum.ender_dropper_with_owner", this.ownerName);
		}
	}

	public int chooseNonEmptySlot() {
		if(this.hasOwner()) {
			PlayerEntity playerEntity = world.getPlayerByUuid(this.ownerUUID);
			if (playerEntity == null) {
				return -1; // player not online => no drop
			} else {
				int i = -1;
				int j = 1;

				EnderChestInventory enderInventory = playerEntity.getEnderChestInventory();
				for (int k = 0; k < enderInventory.size(); ++k) {
					if (!(enderInventory.getStack(k)).isEmpty() && world.random.nextInt(j++) == 0) {
						i = k;
					}
				}

				return i;
			}
		} else {
			return -1; // no owner
		}
	}

	public ItemStack getStack(int slot) {
		PlayerEntity playerEntity = world.getPlayerByUuid(this.ownerUUID);
		EnderChestInventory enderInventory = playerEntity.getEnderChestInventory();
		return enderInventory.getStack(slot);
	}

	public void setStack(int slot, ItemStack itemStack) {
		PlayerEntity playerEntity = world.getPlayerByUuid(this.ownerUUID);
		EnderChestInventory enderInventory = playerEntity.getEnderChestInventory();
		enderInventory.setStack(slot, itemStack);
	}

	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}

	@Override
	public String getOwnerName() {
		return this.ownerName;
	}

	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		this.ownerName = playerEntity.getName().asString();
	}

	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);

		if(tag.contains("OwnerUUID")) {
			this.ownerUUID = tag.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if(tag.contains("OwnerName")) {
			this.ownerName = tag.getString("OwnerName");
		} else {
			this.ownerName = null;
		}
	}

	public NbtCompound writeNbt(NbtCompound tag) {
		super.writeNbt(tag);

		if(this.ownerUUID != null) {
			tag.putUuid("OwnerUUID", this.ownerUUID);
		}
		if(this.ownerName != null) {
			tag.putString("OwnerName", this.ownerName);
		}

		return tag;
	}

}
