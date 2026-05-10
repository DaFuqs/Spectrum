package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;

public abstract class InWorldInteractionBlock extends BaseEntityBlock {
	
	protected InWorldInteractionBlock(Properties settings) {
		super(settings);
	}

	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	// drop all currently stored stuff
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		Containers.dropContentsOnDestroy(state, newState, world, pos);
		// happens when filling with fluid, ...
		if (!state.is(newState.getBlock()) && world.getBlockEntity(pos) instanceof InWorldInteractionBlockEntity inWorldInteractionBlockEntity) {
			inWorldInteractionBlockEntity.inventoryChanged();
		}
		super.onRemove(state, world, pos, newState, moved);
	}

	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!world.isClientSide() && entity instanceof ItemEntity itemEntity) {
			ItemStack remainingStack = inputStack(world, pos, itemEntity.getItem());
			if (remainingStack.isEmpty()) {
				itemEntity.remove(Entity.RemovalReason.DISCARDED);
			} else {
				itemEntity.setItem(remainingStack);
			}
		} else {
			super.fallOn(world, state, pos, entity, fallDistance);
		}
	}
	
	public static void scatterContents(Level world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof Container inventory) {
			Containers.dropContents(world, pos, inventory);
			world.updateNeighbourForOutputSignal(pos, block);
			if (inventory instanceof InWorldInteractionBlockEntity inWorldInteractionBlockEntity) {
				inWorldInteractionBlockEntity.inventoryChanged();
			}
		}
	}
	
	public ItemStack inputStack(Level world, BlockPos pos, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof InWorldInteractionBlockEntity inWorldInteractionBlockEntity) {
			int previousCount = itemStack.getCount();
			ItemStack remainingStack = InventoryHelper.smartAddToInventory(itemStack, inWorldInteractionBlockEntity, null);

			if (remainingStack.getCount() != previousCount) {
				inWorldInteractionBlockEntity.inventoryChanged();
				world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8F, 0.8F + world.getRandom().nextFloat() * 0.6F);
			}
			return remainingStack;
		}
		return itemStack;
	}
	
	public boolean exchangeStack(Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack handStack, InWorldInteractionBlockEntity blockEntity) {
		return exchangeStack(world, pos, player, hand, handStack, blockEntity, 0);
	}
	
	public boolean exchangeStack(Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack handStack, InWorldInteractionBlockEntity blockEntity, int slot) {
		boolean itemsChanged = false;
		if (player.isShiftKeyDown()) {
			ItemStack retrievedStack = blockEntity.removeItemNoUpdate(slot);
			if (!retrievedStack.isEmpty()) {
				player.getInventory().placeItemBackInInventory(retrievedStack);
				itemsChanged = true;
			}
		} else {
			ItemStack currentStack = blockEntity.getItem(slot);
			if (!handStack.isEmpty() && !currentStack.isEmpty()) {
				if (ItemStack.isSameItemSameComponents(handStack, currentStack)) {
					InventoryHelper.setOrCombineStack(blockEntity, slot, handStack);
				} else {
					blockEntity.setItem(slot, handStack);
					player.setItemInHand(hand, currentStack);
				}
				itemsChanged = true;
			} else {
				if (!handStack.isEmpty()) {
					ItemStack singleStack = handStack.split(1);
					blockEntity.setItem(slot, singleStack);
					itemsChanged = true;
				}
				if (!currentStack.isEmpty()) {
					blockEntity.setItem(slot, ItemStack.EMPTY);
					player.getInventory().placeItemBackInInventory(currentStack);
					itemsChanged = true;
				}
			}
		}

		if (itemsChanged) {
			blockEntity.inventoryChanged();
			world.playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.8F, 0.8F + world.getRandom().nextFloat() * 0.6F);
		}
		return itemsChanged;
	}
	
	public boolean retrieveStack(Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack handStack, InWorldInteractionBlockEntity blockEntity, int slot) {
		ItemStack retrievedStack = blockEntity.removeItemNoUpdate(slot);
		if (retrievedStack.isEmpty()) {
			return false;
		}
		if (player.getItemInHand(hand).isEmpty()) {
			player.setItemInHand(hand, retrievedStack);
		} else {
			player.getInventory().placeItemBackInInventory(retrievedStack);
		}
		blockEntity.inventoryChanged();
		world.playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.8F, 0.8F + world.getRandom().nextFloat() * 0.6F);
		return true;
	}
	
	public boolean retrieveLastStack(Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack handStack, InWorldInteractionBlockEntity blockEntity) {
		for (int i = blockEntity.getContainerSize() - 1; i >= 0; i--) {
			if (retrieveStack(world, pos, player, hand, handStack, blockEntity, i)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean inputHandStack(Level world, Player player, InteractionHand hand, ItemStack handStack, InWorldInteractionBlockEntity blockEntity) {
		int previousCount = handStack.getCount();
		ItemStack remainingStack = InventoryHelper.smartAddToInventory(handStack, blockEntity, null);
		if (remainingStack.getCount() != previousCount) {
			player.setItemInHand(hand, remainingStack);
			world.playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.8F, 0.8F + world.getRandom().nextFloat() * 0.6F);
			blockEntity.inventoryChanged();
			return true;
		}
		return false;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
	}

}
