package de.dafuqs.spectrum.blocks.amphora;

import de.dafuqs.spectrum.enums.ProgressionStage;
import de.dafuqs.spectrum.inventories.GenericSpectrumContainerScreenHandler;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class AmphoraBlockEntity extends LootableContainerBlockEntity {
	
	private DefaultedList<ItemStack> inventory;
	private final ViewerCountManager stateManager;
	
	public AmphoraBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.AMPHORA, pos, state);
		
		this.inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
		this.stateManager = new ViewerCountManager() {
			protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
				playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
				setOpen(state, true);
			}
			
			protected void onContainerClose(World world, BlockPos pos, BlockState state) {
				playSound(state, SoundEvents.BLOCK_BARREL_CLOSE);
				setOpen(state, false);
			}
			
			protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
			}
			
			protected boolean isPlayerViewing(PlayerEntity player) {
				if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
					Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
					return inventory == AmphoraBlockEntity.this;
				} else {
					return false;
				}
			}
		};
	}
	
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.inventory);
		}
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.inventory);
		}
	}
	
	public int size() {
		return 54;
	}
	
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}
	
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}
	
	protected Text getContainerName() {
		return Text.translatable("block.spectrum.amphora");
	}
	
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return GenericSpectrumContainerScreenHandler.createGeneric9x6(syncId, playerInventory, this, ProgressionStage.EARLYGAME);
	}
	
	public void onOpen(PlayerEntity player) {
		if (!this.removed && !player.isSpectator()) {
			this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
		}
	}
	
	public void onClose(PlayerEntity player) {
		if (!this.removed && !player.isSpectator()) {
			this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
		}
	}
	
	public void tick() {
		if (!this.removed) {
			this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
		}
	}
	
	void setOpen(BlockState state, boolean open) {
		this.world.setBlockState(this.getPos(), state.with(BarrelBlock.OPEN, open), 3);
	}
	
	void playSound(BlockState state, SoundEvent soundEvent) {
		Vec3i vec3i = (state.get(BarrelBlock.FACING)).getVector();
		double d = (double)this.pos.getX() + 0.5 + (double)vec3i.getX() / 2.0;
		double e = (double)this.pos.getY() + 0.5 + (double)vec3i.getY() / 2.0;
		double f = (double)this.pos.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
		this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
	}
	
}
