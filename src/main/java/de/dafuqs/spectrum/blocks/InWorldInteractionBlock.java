package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.blocks.item_roundel.ItemRoundelBlockEntity;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class InWorldInteractionBlock extends BlockWithEntity  {
	
	protected InWorldInteractionBlock(Settings settings) {
		super(settings);
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
			ItemStack remainingStack = inputItem(world, pos, itemEntity.getStack());
			if (remainingStack.isEmpty()) {
				itemEntity.remove(Entity.RemovalReason.DISCARDED);
			} else {
				itemEntity.setStack(remainingStack);
			}
		} else {
			super.onLandedUpon(world, state, pos, entity, fallDistance);
		}
	}
	
	public void scatterContents(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ItemRoundelBlockEntity itemBowlBlockEntity) {
			ItemScatterer.spawn(world, pos, itemBowlBlockEntity);
			world.updateComparators(pos, block);
		}
	}
	
	public ItemStack inputItem(World world, BlockPos pos, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof InWorldInteractionBlockEntity inventory) {
			int previousCount = itemStack.getCount();
			ItemStack remainingStack = InventoryHelper.smartAddToInventory(itemStack, inventory, null);
			
			if (remainingStack.getCount() != previousCount) {
				inventory.markDirty();
				inventory.updateInClientWorld(world, pos);
				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
			}
			return remainingStack;
		}
		return itemStack;
	}
	
	public void exchangeSingle(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack handStack, InWorldInteractionBlockEntity blockEntity) {
		boolean itemsChanged = false;
		if (player.isSneaking()) {
			ItemStack retrievedStack = blockEntity.removeStack(0);
			if (!retrievedStack.isEmpty()) {
				player.giveItemStack(retrievedStack);
				itemsChanged = true;
			}
		} else {
			ItemStack currentStack = blockEntity.getStack(0);
			if (!handStack.isEmpty() && !currentStack.isEmpty()) {
				blockEntity.setStack(0, handStack);
				player.setStackInHand(hand, currentStack);
				itemsChanged = true;
			} else {
				if (!handStack.isEmpty()) {
					ItemStack remainingStack = InventoryHelper.smartAddToInventory(handStack, blockEntity, null);
					player.setStackInHand(hand, remainingStack);
					itemsChanged = true;
				}
				if (!currentStack.isEmpty()) {
					player.giveItemStack(currentStack);
					itemsChanged = true;
				}
			}
		}
		
		if (itemsChanged) {
			blockEntity.markDirty();
			blockEntity.updateInClientWorld(world, pos);
			world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
		}
	}
	
}
