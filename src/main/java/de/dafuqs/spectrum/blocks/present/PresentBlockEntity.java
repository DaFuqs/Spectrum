package de.dafuqs.spectrum.blocks.present;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;

import java.util.*;

public class PresentBlockEntity extends BlockEntity implements PlayerOwnedWithName {
	
	protected final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(PresentItem.MAX_STORAGE_STACKS, ItemStack.EMPTY);
	protected Map<DyeColor, Integer> colors = new HashMap<>();
	
	private UUID ownerUUID;
	private String ownerName;
	private UUID openerUUID;
	
	protected int openingTicks = 0;
	
	public PresentBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESENT, pos, state);
	}
	
	public void setDataFromPresentStack(ItemStack stack) {
		List<ItemStack> s = PresentItem.getBundledStacks(stack).toList();
		for (int i = 0; i < PresentItem.MAX_STORAGE_STACKS && i < s.size(); i++) {
			this.stacks.set(i, s.get(i));
		}
		this.colors = PresentItem.getColors(stack);
		
		Optional<Pair<UUID, String>> wrapper = PresentItem.getWrapper(stack);
		if (wrapper.isPresent()) {
			this.ownerUUID = wrapper.get().getLeft();
			this.ownerName = wrapper.get().getRight();
		}
		this.markDirty();
	}
	
	public void triggerAdvancement() {
		UUID openerUUID = getOpenerUUID();
		if (openerUUID != null) {
			PlayerEntity opener = PlayerOwned.getPlayerEntityIfOnline(openerUUID);
			if (opener != null) {
				Support.grantAdvancementCriterion((ServerPlayerEntity) opener, "gift_or_open_present", "gifted_or_opened_present");
			}
		}
		
		UUID wrapperUUID = getOwnerUUID();
		if (wrapperUUID != null) {
			PlayerEntity wrapper = PlayerOwned.getPlayerEntityIfOnline(wrapperUUID);
			if (wrapper != null) {
				Support.grantAdvancementCriterion((ServerPlayerEntity) wrapper, "gift_or_open_present", "gifted_or_opened_present");
			}
		}
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, this.stacks);
		this.colors = PresentItem.getColors(nbt);
		if (nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if (nbt.contains("OwnerName")) {
			this.ownerName = nbt.getString("OwnerName");
		} else {
			this.ownerName = "???";
		}
		if (nbt.contains("OpenerUUID")) {
			this.openerUUID = nbt.getUuid("OpenerUUID");
		} else {
			this.openerUUID = null;
		}
		if (nbt.contains("OpeningTick", NbtElement.INT_TYPE)) {
			this.openingTicks = nbt.getInt("OpeningTick");
		}
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.stacks.isEmpty()) {
			Inventories.writeNbt(nbt, this.stacks);
		}
		if (!this.colors.isEmpty()) {
			PresentItem.setColors(nbt, this.colors);
		}
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.ownerName != null) {
			nbt.putString("OwnerName", this.ownerName);
		}
		if (this.openerUUID != null) {
			nbt.putUuid("OpenerUUID", this.openerUUID);
		}
		if (this.openingTicks > 0) {
			nbt.putInt("OpeningTick", this.openingTicks);
		}
	}
	
	public int openingTick() {
		openingTicks++;
		markDirty();
		return this.openingTicks;
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
		this.ownerName = playerEntity.getName().getString();
	}
	
	public void setOpenerUUID(PlayerEntity opener) {
		this.openerUUID = opener.getUuid();
		markDirty();
	}
	
	public UUID getOpenerUUID() {
		return this.openerUUID;
	}
	
	public ItemStack retrievePresent(PresentBlock.Variant variant) {
		ItemStack stack = SpectrumBlocks.PRESENT.asItem().getDefaultStack();
		PresentItem.wrap(stack, variant, this.colors);
		if (this.ownerUUID != null && this.ownerName != null) {
			PresentItem.setWrapper(stack, this.ownerUUID, this.ownerName);
		}
		return stack;
	}
	
	public boolean isEmpty() {
		for (int i = 0; i < PresentItem.MAX_STORAGE_STACKS; i++) {
			if (!stacks.get(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
}
