package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.helpers.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public abstract class InWorldInteractionBlock extends BlockWithEntity {
	
	protected InWorldInteractionBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	// drop all currently stored stuff
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) { // happens when filling with fluid, ...
			scatterContents(world, pos);
			world.updateComparators(pos, this);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!world.isClient && entity instanceof ItemEntity itemEntity) {
			ItemStack remainingStack = inputStack(world, pos, itemEntity.getStack());
			if (remainingStack.isEmpty()) {
				itemEntity.remove(Entity.RemovalReason.DISCARDED);
			} else {
				itemEntity.setStack(remainingStack);
			}
		} else {
			super.onLandedUpon(world, state, pos, entity, fallDistance);
		}
	}
	
	public static void scatterContents(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof Inventory inventory) {
			ItemScatterer.spawn(world, pos, inventory);
			world.updateComparators(pos, block);
		}
	}
	
	public ItemStack inputStack(World world, BlockPos pos, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof InWorldInteractionBlockEntity inWorldInteractionBlockEntity) {
			int previousCount = itemStack.getCount();
			ItemStack remainingStack = InventoryHelper.smartAddToInventory(itemStack, inWorldInteractionBlockEntity, null);
			
			if (remainingStack.getCount() != previousCount) {
				inWorldInteractionBlockEntity.markDirty();
				inWorldInteractionBlockEntity.updateInClientWorld();
				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
			}
			return remainingStack;
		}
		return itemStack;
	}
	
	public boolean exchangeStack(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack handStack, InWorldInteractionBlockEntity blockEntity) {
		return exchangeStack(world, pos, player, hand, handStack, blockEntity, 0);
	}
	
	public boolean exchangeStack(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack handStack, InWorldInteractionBlockEntity blockEntity, int slot) {
		boolean itemsChanged = false;
		if (player.isSneaking()) {
			ItemStack retrievedStack = blockEntity.removeStack(slot);
			if (!retrievedStack.isEmpty()) {
				player.getInventory().offerOrDrop(retrievedStack);
				itemsChanged = true;
			}
		} else {
			ItemStack currentStack = blockEntity.getStack(slot);
			if (!handStack.isEmpty() && !currentStack.isEmpty()) {
				blockEntity.setStack(slot, handStack);
				player.setStackInHand(hand, currentStack);
				itemsChanged = true;
			} else {
				if (!handStack.isEmpty()) {
					ItemStack remainingStack = InventoryHelper.setOrCombineStack(blockEntity, slot, handStack);
					player.setStackInHand(hand, remainingStack);
					itemsChanged = true;
				}
				if (!currentStack.isEmpty()) {
					blockEntity.setStack(slot, ItemStack.EMPTY);
					player.getInventory().offerOrDrop(currentStack);
					itemsChanged = true;
				}
			}
		}
		
		if (itemsChanged) {
			world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
		}
		return itemsChanged;
	}
	
	public boolean retrieveStack(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack handStack, InWorldInteractionBlockEntity blockEntity, int slot) {
		ItemStack retrievedStack = blockEntity.removeStack(slot);
		if (retrievedStack.isEmpty()) {
			return false;
		}
		if (player.getStackInHand(hand).isEmpty()) {
			player.setStackInHand(hand, retrievedStack);
		} else {
			player.getInventory().offerOrDrop(retrievedStack);
		}
		world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
		return true;
	}
	
	public boolean retrieveLastStack(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack handStack, InWorldInteractionBlockEntity blockEntity) {
		for (int i = blockEntity.size() - 1; i >= 0; i--) {
			if (retrieveStack(world, pos, player, hand, handStack, blockEntity, i)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean inputHandStack(World world, PlayerEntity player, Hand hand, ItemStack handStack, InWorldInteractionBlockEntity blockEntity) {
		int previousCount = handStack.getCount();
		ItemStack remainingStack = InventoryHelper.smartAddToInventory(handStack, blockEntity, null);
		if (remainingStack.getCount() != previousCount) {
			player.setStackInHand(hand, remainingStack);
			world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorOutput(BlockState state, @NotNull World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}
	
}
