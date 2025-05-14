package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.inventories.*;
import net.fabricmc.api.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.nbt.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

@EnvironmentInterfaces({@EnvironmentInterface(
		value = EnvType.CLIENT,
		itf = LidBlockEntity.class
)})
public abstract class SpectrumChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {
	
	public final ContainerOpenersCounter stateManager;
	protected final ChestLidController lidAnimator;
	protected NonNullList<ItemStack> inventory;
	
	protected SpectrumChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
		this.inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		this.lidAnimator = new ChestLidController();
		
		this.stateManager = new ContainerOpenersCounter() {
			@Override
			protected void onOpen(Level world, BlockPos pos, BlockState state) {
				playSound(world, pos, getOpenSound());
				onOpenSpectrum();
			}
			
			@Override
			protected void onClose(Level world, BlockPos pos, BlockState state) {
				playSound(world, pos, getCloseSound());
				onCloseSpectrum();
			}
			
			@Override
			protected void openerCountChanged(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
				onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
			}
			
			@Override
			protected boolean isOwnContainer(Player player) {
				AbstractContainerMenu screenHandler = player.containerMenu;
				
				Container inventory = null;
				if (screenHandler instanceof ChestMenu) {
					inventory = ((ChestMenu) screenHandler).getContainer();
				} else if (screenHandler instanceof FabricationChestScreenHandler fabricationChestScreenHandler) {
					inventory = fabricationChestScreenHandler.getInventory();
				} else if (screenHandler instanceof BlackHoleChestScreenHandler blackHoleChestScreenHandler) {
					inventory = blackHoleChestScreenHandler.getInventory();
				} else if (screenHandler instanceof CompactingChestScreenHandler compactingChestScreenHandler) {
					inventory = compactingChestScreenHandler.getInventory();
				}
				
				return inventory == SpectrumChestBlockEntity.this;
			}
		};
	}
	
	private static void playSound(Level world, BlockPos pos, SoundEvent soundEvent) {
		world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundEvent, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
	}
	
	@SuppressWarnings("unused")
	public static void clientTick(Level world, BlockPos pos, BlockState state, SpectrumChestBlockEntity blockEntity) {
		blockEntity.lidAnimator.tickLid();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public float getOpenNess(float tickDelta) {
		return this.lidAnimator.getOpenness(tickDelta);
	}
	
	public void onOpenSpectrum() {
	
	}
	
	public void onCloseSpectrum() {
	
	}
	
	@Override
	public boolean triggerEvent(int type, int data) {
		if (type == 1) {
			this.lidAnimator.shouldBeOpen(data > 0);
			return true;
		} else {
			return super.triggerEvent(type, data);
		}
	}
	
	protected void onInvOpenOrClose(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
		Block block = state.getBlock();
		world.blockEvent(pos, block, 1, newViewerCount);
	}
	
	@Override
	public void startOpen(Player player) {
		if (!player.isSpectator()) {
			this.stateManager.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
		
	}
	
	@Override
	public void stopOpen(Player player) {
		if (!player.isSpectator()) {
			this.stateManager.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}
	
	@Override
	public void setItem(int slot, ItemStack stack) {
		super.setItem(slot, stack);
	}
	
	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.inventory;
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> list) {
		this.inventory = list;
	}
	
	public void onScheduledTick() {
		this.stateManager.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
	}
	
	// TODO Should the loot table NBT only be maintained for TreasureChestBlockEntity?
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.loadAdditional(tag, registryLookup);
		this.tryLoadLootTable(tag);
		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, this.inventory, registryLookup);
	}
	
	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.saveAdditional(tag, registryLookup);
		this.trySaveLootTable(tag);
		if (!this.inventory.isEmpty()) {
			ContainerHelper.saveAllItems(tag, this.inventory, registryLookup);
		}
	}
	
	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
		super.collectImplicitComponents(componentMapBuilder);
		componentMapBuilder.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(this.lootTable, this.lootTableSeed));
	}
	
	public SoundEvent getOpenSound() {
		return SoundEvents.CHEST_OPEN;
	}
	
	public SoundEvent getCloseSound() {
		return SoundEvents.CHEST_CLOSE;
	}
	
}
