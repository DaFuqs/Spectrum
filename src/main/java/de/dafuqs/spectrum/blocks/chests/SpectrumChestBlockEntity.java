package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.inventories.CompactingChestScreenHandler;
import de.dafuqs.spectrum.inventories.RestockingChestScreenHandler;
import de.dafuqs.spectrum.inventories.SuckingChestScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.api.EnvironmentInterfaces;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestLidAnimator;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@EnvironmentInterfaces({@EnvironmentInterface(
		value = EnvType.CLIENT,
		itf = ChestAnimationProgress.class
)})
public abstract class SpectrumChestBlockEntity extends LootableContainerBlockEntity implements ChestAnimationProgress {
	
	public final ViewerCountManager stateManager;
	protected final ChestLidAnimator lidAnimator;
	protected DefaultedList<ItemStack> inventory;
	
	protected SpectrumChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
		this.inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
		this.lidAnimator = new ChestLidAnimator();
		
		this.stateManager = new ViewerCountManager() {
			protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
				playSound(world, pos, state, getOpenSound());
				onOpen();
			}
			
			protected void onContainerClose(World world, BlockPos pos, BlockState state) {
				playSound(world, pos, state, getCloseSound());
				onClose();
			}
			
			protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
				onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
			}
			
			protected boolean isPlayerViewing(PlayerEntity player) {
				ScreenHandler screenHandler = player.currentScreenHandler;
				
				Inventory inventory = null;
				if (screenHandler instanceof GenericContainerScreenHandler) {
					inventory = ((GenericContainerScreenHandler) screenHandler).getInventory();
				} else if (screenHandler instanceof RestockingChestScreenHandler restockingChestScreenHandler) {
					inventory = restockingChestScreenHandler.getInventory();
				} else if (screenHandler instanceof SuckingChestScreenHandler suckingChestScreenHandler) {
					inventory = suckingChestScreenHandler.getInventory();
				} else if (screenHandler instanceof CompactingChestScreenHandler compactingChestScreenHandler) {
					inventory = compactingChestScreenHandler.getInventory();
				}
				
				return inventory == SpectrumChestBlockEntity.this;
			}
		};
	}
	
	private static void playSound(World world, BlockPos pos, BlockState state, SoundEvent soundEvent) {
		world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundEvent, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
	}
	
	public static void clientTick(World world, BlockPos pos, BlockState state, SpectrumChestBlockEntity blockEntity) {
		blockEntity.lidAnimator.step();
	}
	
	@Environment(EnvType.CLIENT)
	public float getAnimationProgress(float tickDelta) {
		return this.lidAnimator.getProgress(tickDelta);
	}
	
	public void onOpen() {
	
	}
	
	public void onClose() {
	
	}
	
	public boolean onSyncedBlockEvent(int type, int data) {
		if (type == 1) {
			this.lidAnimator.setOpen(data > 0);
			return true;
		} else {
			return super.onSyncedBlockEvent(type, data);
		}
	}
	
	protected void onInvOpenOrClose(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
		Block block = state.getBlock();
		world.addSyncedBlockEvent(pos, block, 1, newViewerCount);
	}
	
	public void onOpen(PlayerEntity player) {
		if (!player.isSpectator()) {
			this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
		}
		
	}
	
	public void onClose(PlayerEntity player) {
		if (!player.isSpectator()) {
			this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
		}
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		super.setStack(slot, stack);
	}
	
	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}
	
	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}
	
	public void onScheduledTick() {
		this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
	}
	
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		
		if (!this.deserializeLootTable(tag)) {
			Inventories.readNbt(tag, this.inventory);
		}
	}
	
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		if (!this.serializeLootTable(tag)) {
			Inventories.writeNbt(tag, this.inventory);
		}
	}
	
	public SoundEvent getOpenSound() {
		return SoundEvents.BLOCK_CHEST_OPEN;
	}
	
	public SoundEvent getCloseSound() {
		return SoundEvents.BLOCK_CHEST_CLOSE;
	}
	
}
