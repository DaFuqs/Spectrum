package de.dafuqs.spectrum.blocks.item_bowl;

import de.dafuqs.spectrum.InventoryHelper;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ItemBowlBlock extends BlockWithEntity {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D);
	
	public ItemBowlBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ItemBowlBlockEntity(pos, state);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
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
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			ItemStack handStack = player.getStackInHand(hand);
			
			// if the structure is valid the player can put / retrieve blocks into the shrine
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
				boolean itemsChanged = false;
				if (player.isSneaking()) {
					Inventory inventory = itemBowlBlockEntity.getInventory();
					ItemStack retrievedStack = inventory.removeStack(0);
					if (!retrievedStack.isEmpty()) {
						player.giveItemStack(retrievedStack);
						itemsChanged = true;
					}
				} else {
					Inventory inventory = itemBowlBlockEntity.getInventory();
					ItemStack currentStack = inventory.getStack(0);
					if(!handStack.isEmpty()) {
						ItemStack remainingStack = InventoryHelper.addToInventory(handStack, itemBowlBlockEntity.getInventory(), null);
						player.setStackInHand(hand, remainingStack);
						itemsChanged = true;
					}
					if (!currentStack.isEmpty()) {
						player.giveItemStack(currentStack);
						itemsChanged = true;
					}
				}
				
				if(itemsChanged) {
					itemBowlBlockEntity.updateInClientWorld();
					world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
				}
			}
			return ActionResult.CONSUME;
		}
	}
	
	public static void scatterContents(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
			ItemScatterer.spawn(world, pos, itemBowlBlockEntity.getInventory());
			world.updateComparators(pos, block);
		}
	}
	
}
