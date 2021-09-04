package de.dafuqs.spectrum.blocks.fusion_shrine;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.fabricmc.fabric.mixin.transfer.BucketItemAccessor;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FusionShrineBlock extends BlockWithEntity {

    public static final IntProperty LIGHT_LEVEL = IntProperty.of("light_level", 0, 15);
    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D);

    public FusionShrineBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(LIGHT_LEVEL, 0));
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_LEVEL);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FusionShrineBlockEntity(pos, state);
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            ItemStack itemStack = player.getStackInHand(hand);

            if (itemStack.getItem() instanceof BucketItem) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
                    Fluid storedFluid = fusionShrineBlockEntity.getFluid();
                    Fluid bucketFluid = ((BucketItemAccessor) itemStack.getItem()).fabric_getFluid();
                    if (storedFluid == Fluids.EMPTY && bucketFluid != Fluids.EMPTY) {
                        fusionShrineBlockEntity.setFluid(bucketFluid);
                        setLightForFluid(world, pos, bucketFluid);
                        if (!player.isCreative()) {
                            player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                        }
                    } else if (storedFluid != Fluids.EMPTY && bucketFluid == Fluids.EMPTY) {
                        fusionShrineBlockEntity.setFluid(Fluids.EMPTY);
                        world.setBlockState(pos, world.getBlockState(pos).with(LIGHT_LEVEL, 0));
                        if (!player.isCreative()) {
                            player.setStackInHand(hand, new ItemStack(storedFluid.getBucketItem()));
                        }
                    }
                }
            }
            return ActionResult.CONSUME;
        }
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, SpectrumBlockEntityRegistry.FUSION_SHRINE, FusionShrineBlockEntity::tick);
    }

    public void setLightForFluid(World world, BlockPos blockPos, Fluid fluid) {
        if(SpectrumCommon.fluidLuminance.containsKey(fluid)) {
           int light = SpectrumCommon.fluidLuminance.get(fluid);
           world.setBlockState(blockPos, world.getBlockState(blockPos).with(LIGHT_LEVEL, light));
        }
    }


}
