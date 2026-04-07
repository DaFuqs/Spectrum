package de.dafuqs.spectrum.blocks.present;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class PresentBlockEntity extends PlacedItemBlockEntity implements PlayerOwnedWithName {
	
	protected int openingTicks = 0;
	
	public PresentBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESENT.get(), pos, state);
	}
	
	public void triggerAdvancement() {
		UUID openerUUID = getOpenerUUID();
		Level level = this.getLevel();
		if (openerUUID != null) {
			Player opener = PlayerOwned.getPlayerIfOnline(level, openerUUID);
			if (opener != null) {
				Support.grantAdvancementCriterion((ServerPlayer) opener, "gift_or_open_present", "gifted_or_opened_present");
			}
		}
		
		UUID ownerUUID = getOwnerUUID();
		if (ownerUUID != null) {
			Player wrapper = PlayerOwned.getPlayerIfOnline(level, ownerUUID);
			if (wrapper != null) {
				Support.grantAdvancementCriterion((ServerPlayer) wrapper, "gift_or_open_present", "gifted_or_opened_present");
			}
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		if (nbt.contains("OpeningTick", Tag.TAG_ANY_NUMERIC)) {
			this.openingTicks = nbt.getInt("OpeningTick");
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
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
		return PresentBlockItem.getOwner(this.stack).flatMap(ResolvableProfile::id).orElse(null);
	}
	
	public ResolvableProfile getOwner() {
		return PresentBlockItem.getOwner(this.stack).orElse(null);
	}
	
	@Override
	public String getOwnerName() {
		return PresentBlockItem.getOwner(this.stack).flatMap(ResolvableProfile::name).orElse("???");
	}
	
	@Override
	public void setOwner(Player playerEntity) {
		PresentBlockItem.setOwner(this.stack, playerEntity);
		setChanged();
	}
	
	public void setOpenerUUID(Player opener) {
		this.ownerUUID = opener.getUUID();
		setChanged();
	}
	
	public UUID getOpenerUUID() {
		return this.ownerUUID;
	}
	
	public ItemStack retrievePresent() {
		return this.stack.copy();
	}
	
	public List<ItemStack> getStacks() {
		return PresentBlockItem.getBundledStacks(this.stack).toList();
	}
	
	public void setPresent(ItemStack present) {
		this.stack = present.copy();
		setChanged();
	}
	
	public boolean isEmpty() {
		return PresentBlockItem.isEmpty(this.stack);
	}
	
}
