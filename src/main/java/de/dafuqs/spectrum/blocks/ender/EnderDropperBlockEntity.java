package de.dafuqs.spectrum.blocks.ender;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EnderDropperBlockEntity extends DispenserBlockEntity implements PlayerOwnedWithName {
	
	private UUID ownerUUID;
	private String ownerName;
	
	public EnderDropperBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.ENDER_DROPPER, blockPos, blockState);
	}
	
	@Override
	protected Component getDefaultName() {
		if (hasOwner()) {
			return Component.translatable("block.spectrum.ender_dropper.owner", this.ownerName);
		} else {
			return Component.translatable("block.spectrum.ender_dropper");
		}
	}
	
	@Override
	public int getRandomSlot(RandomSource random) {
		return getInventory().map(inventory -> {
			int selectedIndex = -1;
			int chance = 1;
			
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				if (!(inventory.getItem(i)).isEmpty() && random.nextInt(chance++) == 0) {
					selectedIndex = i;
				}
			}
			
			return selectedIndex;
		}).orElse(-1);
	}
	
	@Override
	public ItemStack insertItem(ItemStack stack) {
		getInventory().ifPresent(inventory -> {
			int i = this.getMaxStackSize(stack);
			
			for (int j = 0; j < inventory.getContainerSize(); j++) {
				ItemStack itemStack = inventory.getItem(j);
				if (itemStack.isEmpty() || ItemStack.isSameItemSameComponents(stack, itemStack)) {
					int k = Math.min(stack.getCount(), i - itemStack.getCount());
					if (k > 0) {
						if (itemStack.isEmpty()) {
							this.setItem(j, stack.split(k));
						} else {
							stack.shrink(k);
							itemStack.grow(k);
						}
					}
					
					if (stack.isEmpty()) {
						break;
					}
				}
			}
		});
		return stack;
	}
	
	@Override
	public ItemStack getItem(int slot) {
		return getInventory().map(i -> i.getItem(slot)).orElse(ItemStack.EMPTY);
	}
	
	@Override
	public void setItem(int slot, ItemStack itemStack) {
		getInventory().ifPresent(i -> i.setItem(slot, itemStack));
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return getInventory().map(i -> i.removeItemNoUpdate(slot)).orElse(ItemStack.EMPTY);
	}
	
	@Override
	public ItemStack removeItem(int slot, int amount) {
		return getInventory().map(i -> i.removeItem(slot, amount)).orElse(ItemStack.EMPTY);
	}
	
	@Override
	public boolean isEmpty() {
		return getInventory().map(SimpleContainer::isEmpty).orElse(true);
	}
	
	@Override
	public boolean canTakeItem(Container hopperInventory, int slot, ItemStack stack) {
		return false;
	}
	
	private Optional<PlayerEnderChestContainer> getInventory() {
		var player = getOwnerIfOnline();
		if (player != null)
			return Optional.of(player.getEnderChestInventory());
		return Optional.empty();
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
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		this.ownerName = playerEntity.getName().getString();
		setChanged();
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
		this.ownerName = PlayerOwned.readOwnerName(nbt);
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		PlayerOwned.writeOwnerName(nbt, this.ownerName);
	}
	
	@Override
	protected NonNullList<ItemStack> getItems() {
		return getInventory().map(SimpleContainer::getItems).orElse(NonNullList.create());
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> inventory) {
		getInventory().ifPresent(inv -> {
			for (int i = 0; i < inventory.size(); i++)
				inv.setItem(i, inventory.get(i));
		});
	}
	
	@Override
	public int getContainerSize() {
		return getInventory().map(SimpleContainer::getContainerSize).orElse(0);
	}
	
	@Nullable
	public ResourceKey<LootTable> getLootTable() {
		return null;
	}
	
	@Override @Nullable
	public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
		return null;
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return null;
	}
	
}
