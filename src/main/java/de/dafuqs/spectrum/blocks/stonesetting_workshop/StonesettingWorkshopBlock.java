package de.dafuqs.spectrum.blocks.stonesetting_workshop;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StonesettingWorkshopBlock extends BlockWithEntity {

    public StonesettingWorkshopBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (player.getStackInHand(hand).isEmpty())
            return super.onUse(state, world, pos, player, hand, hit);

        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StonesettingWorkshopBlockEntity workshop) {

            var stack = player.getStackInHand(hand);
            if (stack.isEmpty()) {



            }
            else {

                if (workshop.tryInsertItem(stack, player, hand))
                    return ActionResult.success(world.isClient());

            }
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!newState.getBlock().equals(this) && world.getBlockEntity(pos) instanceof StonesettingWorkshopBlockEntity workshop) { // happens when filling with fluid
            ItemScatterer.spawn(world, pos, workshop.getInventory());
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return super.getTicker(world, state, type);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StonesettingWorkshopBlockEntity(pos, state);
    }
}
