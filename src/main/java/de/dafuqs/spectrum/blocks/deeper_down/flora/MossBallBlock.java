package de.dafuqs.spectrum.blocks.deeper_down.flora;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class MossBallBlock extends PlantBlock implements Fertilizable {

    private static final VoxelShape SHAPE = MossBallBlock.createCuboidShape(3.5, 0, 3.5, 12.5, 9, 12.5);

    public MossBallBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.onLandedUpon(world, state, pos, entity, fallDistance / 2F);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Vec3d vec3d = state.getModelOffset(world, pos);
        return SHAPE.offset(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public float getMaxHorizontalModelOffset() {
        return 0.2F;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return true;
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        var tries = MathHelper.nextInt(random, 0, 3);

        for (int i = 0; i < tries; i++) {
            var column = pos.add(MathHelper.nextInt(random, -7, 7), 1, MathHelper.nextInt(random, -7, 7));

            for (int offset = 0; offset < 4; offset++) {
                var plantPos = column.down(offset);

                if (canPlantOnTop(world.getBlockState(plantPos), world, plantPos) && world.isAir(plantPos.up())) {
                    world.setBlockState(plantPos.up(), getDefaultState());
                    break;
                }
            }
        }
    }
}
