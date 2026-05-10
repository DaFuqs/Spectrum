package de.dafuqs.spectrum.blocks.amphora;

import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

public class AmphoraBlockEntity extends RandomizableContainerBlockEntity {
	
	private NonNullList<ItemStack> inventory;
	private final ContainerOpenersCounter stateManager;
	
	public AmphoraBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.AMPHORA.get(), pos, state);
		
		this.inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		this.stateManager = new ContainerOpenersCounter() {
			@Override
			protected void onOpen(Level world, BlockPos pos, BlockState state) {
				playSound(state, SoundEvents.BARREL_OPEN);
				setOpen(state, true);
			}
			
			@Override
			protected void onClose(Level world, BlockPos pos, BlockState state) {
				playSound(state, SoundEvents.BARREL_CLOSE);
				setOpen(state, false);
			}
			
			@Override
			protected void openerCountChanged(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
			}
			
			@Override
			protected boolean isOwnContainer(Player player) {
				if (player.containerMenu instanceof ChestMenu) {
					Container inventory = ((ChestMenu) player.containerMenu).getContainer();
					return inventory == AmphoraBlockEntity.this;
				} else {
					return false;
				}
			}
		};
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		if (!this.trySaveLootTable(nbt)) {
			ContainerHelper.saveAllItems(nbt, this.inventory, registryLookup);
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(nbt)) {
			ContainerHelper.loadAllItems(nbt, this.inventory, registryLookup);
		}
	}
	
	@Override
	public int getContainerSize() {
		return 54;
	}
	
	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.inventory;
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> list) {
		this.inventory = list;
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.amphora");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return GenericSpectrumContainerScreenHandler.createGeneric9x6(syncId, playerInventory, this, ScreenBackgroundVariant.EARLYGAME);
	}
	
	@Override
	public void startOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.stateManager.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}
	
	@Override
	public void stopOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.stateManager.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}
	
	public void tick() {
		if (!this.remove) {
			this.stateManager.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}
	
	void setOpen(BlockState state, boolean open) {
		if (this.level == null)
			return;
		this.level.setBlock(this.getBlockPos(), state.setValue(BarrelBlock.OPEN, open), 3);
	}
	
	void playSound(BlockState state, SoundEvent soundEvent) {
		if (this.level == null)
			return;
		Vec3i vec3i = (state.getValue(BarrelBlock.FACING)).getNormal();
		double d = (double) this.worldPosition.getX() + 0.5 + (double) vec3i.getX() / 2.0;
		double e = (double) this.worldPosition.getY() + 0.5 + (double) vec3i.getY() / 2.0;
		double f = (double) this.worldPosition.getZ() + 0.5 + (double) vec3i.getZ() / 2.0;
		this.level.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5F, this.level.getRandom().nextFloat() * 0.1F + 0.9F);
	}
	
}
