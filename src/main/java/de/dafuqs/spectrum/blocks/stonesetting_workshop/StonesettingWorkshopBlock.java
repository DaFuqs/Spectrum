/*package de.dafuqs.spectrum.blocks.stonesetting_workshop;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StonesettingWorkshopBlock extends BlockWithEntity {

    private static final VoxelShape SHAPE = Block.createCuboidShape(1, 0, 1, 15, 15, 15);

    public StonesettingWorkshopBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if(hand == Hand.OFF_HAND)
            return super.onUse(state, world, pos, player, hand, hit);

        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StonesettingWorkshopBlockEntity workshop) {

            var stack = player.getStackInHand(hand);
            if (stack.isEmpty()) {
                var extracted = workshop.tryExtractItem(player, hand);

                if (extracted)
                    return ActionResult.success(true);

            }
            else {
                var inserted = workshop.tryInsertItem(stack, player, hand);

                if (inserted)
                    return ActionResult.success(true);

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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
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
*/