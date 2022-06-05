package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.blocks.CrackedEndPortalFrameBlock;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EndPortalCrackerItem extends Item {
	
	public EndPortalCrackerItem(Settings settings) {
		super(settings);
	}
	
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.isOf(Blocks.END_PORTAL_FRAME) || blockState.isOf(SpectrumBlocks.CRACKED_END_PORTAL_FRAME)) {
			if (world.isClient) {
				return ActionResult.SUCCESS;
			} else {
				BlockState targetBlockState;
				boolean facingVertical;
				if (blockState.isOf(Blocks.END_PORTAL_FRAME)) {
					Direction direction = blockState.get(EndPortalFrameBlock.FACING);
					facingVertical = direction.equals(Direction.EAST) || direction.equals(Direction.WEST);
					targetBlockState = SpectrumBlocks.CRACKED_END_PORTAL_FRAME.getDefaultState()
							.with(CrackedEndPortalFrameBlock.EYE_TYPE, CrackedEndPortalFrameBlock.EndPortalFrameEye.VANILLA_WITH_END_PORTAL_CRACKER)
							.with(CrackedEndPortalFrameBlock.FACING_VERTICAL, facingVertical);
				} else {
					facingVertical = blockState.get(CrackedEndPortalFrameBlock.FACING_VERTICAL);
					targetBlockState = SpectrumBlocks.CRACKED_END_PORTAL_FRAME.getDefaultState()
							.with(CrackedEndPortalFrameBlock.EYE_TYPE, CrackedEndPortalFrameBlock.EndPortalFrameEye.WITH_END_PORTAL_CRACKER)
							.with(CrackedEndPortalFrameBlock.FACING_VERTICAL, facingVertical);
				}
				
				Block.pushEntitiesUpBeforeBlockChange(blockState, targetBlockState, world, blockPos);
				world.setBlockState(blockPos, targetBlockState, 2);
				world.updateComparators(blockPos, Blocks.END_PORTAL_FRAME);
				context.getStack().decrement(1);
				world.syncWorldEvent(1503, blockPos, 0);
				
				return ActionResult.CONSUME;
			}
		} else {
			return ActionResult.PASS;
		}
	}
	
}
