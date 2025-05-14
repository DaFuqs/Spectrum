package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlacedItemBlockEntity extends BlockEntity implements PlayerOwned {
	
	protected ItemStack stack = ItemStack.EMPTY;
	protected UUID ownerUUID;
	
	public PlacedItemBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	public PlacedItemBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PLACED_ITEM, pos, state);
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.stack = ItemStack.parse(registryLookup, nbt.getCompound("stack")).orElse(ItemStack.EMPTY);
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		nbt.put("stack", this.stack.save(registryLookup));
		
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
	}
	
	public void setStack(ItemStack stack) {
		this.stack = stack;
		this.setChanged();
	}
	
	public ItemStack getStack() {
		return this.stack;
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(@NotNull Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		setChanged();
	}
	
}
