package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.blocks.item_roundel.ItemRoundelBlock;
import de.dafuqs.spectrum.blocks.item_roundel.ItemRoundelBlockEntity;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PreservationRoundelBlock extends ItemRoundelBlock {
	
	public PreservationRoundelBlock(Settings settings) {
		super(settings);
	}
	
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PreservationRoundelBlockEntity(pos, state);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof ItemRoundelBlockEntity itemRoundelBlockEntity) {
				ItemStack handStack = player.getStackInHand(hand);
				if (player.isSneaking() || handStack.isEmpty()) {
					retrieveLastStack(world, pos, player, hand, handStack, itemRoundelBlockEntity);
				} else {
					int countBefore = handStack.getCount();
					ItemStack leftoverStack = InventoryHelper.addToInventoryUpToSingleStackWithMaxTotalCount(handStack, itemRoundelBlockEntity, PreservationRoundelBlockEntity.INVENTORY_SIZE);
					player.setStackInHand(hand, leftoverStack);
					if (countBefore != leftoverStack.getCount()) {
						world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
						itemRoundelBlockEntity.inventoryChanged();
					}
				}
			}
			return ActionResult.CONSUME;
		}
	}
	
}
