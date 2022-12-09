package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.helpers.Support;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

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
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		scatterContents(world, pos);
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
				player.giveItemStack(retrievedStack);
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
					player.giveItemStack(currentStack);
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
			Support.givePlayer(player, retrievedStack);
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
