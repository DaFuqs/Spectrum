package de.dafuqs.spectrum.blocks.present;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class PresentBlockEntity extends BlockEntity implements PlayerOwnedWithName {
	
	protected ItemStack presentStack = ItemStack.EMPTY;
	private UUID openerUUID;
	protected int openingTicks = 0;

	public PresentBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESENT, pos, state);
	}
	
	public void triggerAdvancement() {
		UUID openerUUID = getOpenerUUID();
		if (openerUUID != null) {
			Player opener = PlayerOwned.getPlayerEntityIfOnline(openerUUID);
			if (opener != null) {
				Support.grantAdvancementCriterion((ServerPlayer) opener, "gift_or_open_present", "gifted_or_opened_present");
			}
		}
		
		UUID ownerUUID = getOwnerUUID();
		if (ownerUUID != null) {
			Player wrapper = PlayerOwned.getPlayerEntityIfOnline(ownerUUID);
			if (wrapper != null) {
				Support.grantAdvancementCriterion((ServerPlayer) wrapper, "gift_or_open_present", "gifted_or_opened_present");
			}
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.presentStack = ItemStack.parseOptional(registryLookup, nbt.getCompound("Present"));
		if (nbt.contains("OpenerUUID")) {
			this.openerUUID = nbt.getUUID("OpenerUUID");
		} else {
			this.openerUUID = null;
		}
		if (nbt.contains("OpeningTick", Tag.TAG_ANY_NUMERIC)) {
			this.openingTicks = nbt.getInt("OpeningTick");
		}
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		nbt.put("Present", this.presentStack.saveOptional(registryLookup));
		if (this.openerUUID != null) {
			nbt.putUUID("OpenerUUID", this.openerUUID);
		}
		if (this.openingTicks > 0) {
			nbt.putInt("OpeningTick", this.openingTicks);
		}
	}
	
	public int openingTick() {
		openingTicks++;
		setChanged();
		return this.openingTicks;
	}
	
	@Override
	public UUID getOwnerUUID() {
		return PresentBlockItem.getOwner(this.presentStack).flatMap(ResolvableProfile::id).orElse(null);
	}
	
	public ResolvableProfile getOwner() {
		return PresentBlockItem.getOwner(this.presentStack).orElse(null);
	}

	@Override
	public String getOwnerName() {
		return PresentBlockItem.getOwner(this.presentStack).flatMap(ResolvableProfile::name).orElse("???");
	}

	@Override
	public void setOwner(Player playerEntity) {
		PresentBlockItem.setOwner(this.presentStack, playerEntity);
		setChanged();
	}
	
	public void setOpenerUUID(Player opener) {
		this.openerUUID = opener.getUUID();
		setChanged();
	}
	
	public UUID getOpenerUUID() {
		return this.openerUUID;
	}
	
	public ItemStack retrievePresent() {
		return this.presentStack.copy();
	}
	
	public Map<Integer, Integer> getColors() {
		return PresentBlockItem.getWrapData(this.presentStack).colors();
	}

	public List<ItemStack> getStacks() {
		return PresentBlockItem.getBundledStacks(this.presentStack).toList();
	}

	public void setPresent(ItemStack present) {
		this.presentStack = present;
		setChanged();
	}
	
	public boolean isEmpty() {
		return PresentBlockItem.isEmpty(this.presentStack);
	}

}
