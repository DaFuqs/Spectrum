package de.dafuqs.pigment.items.misc;

import de.dafuqs.pigment.PigmentBlocks;
import de.dafuqs.pigment.blocks.CrackedEndPortalFrameBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EndPortalCrackerItem extends Item {

    public EndPortalCrackerItem(Settings settings) {
        super(settings);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(Blocks.END_PORTAL_FRAME)) {
            if (world.isClient) {
                return ActionResult.SUCCESS;
            } else {
                testAndDestroyExistingPortalBlocks(world, blockPos);

                BlockState targetBlockState = PigmentBlocks.CRACKED_END_PORTAL_FRAME.getDefaultState().with(CrackedEndPortalFrameBlock.EYE_TYPE, CrackedEndPortalFrameBlock.EndPortalFrameEye.END_PORTAL_CRACKER);
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

    private void testAndDestroyExistingPortalBlocks(World world, BlockPos blockPos) {
        BlockPattern.Result result = EndPortalFrameBlock.getCompletedFramePattern().searchAround(world, blockPos);
        BlockPos blockPos2;
        if (result != null) {
            blockPos2 = result.getFrontTopLeft().add(-3, 0, -3);
            for(int i = 0; i < 3; ++i) {
                for(int j = 0; j < 3; ++j) {
                    world.setBlockState(blockPos2.add(i, 0, j), Blocks.AIR.getDefaultState(), 2);
                }
            }
            world.syncGlobalEvent(1038, blockPos2.add(1, 0, 1), 0);
        }
    }

}
