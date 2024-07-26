package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.blocks.SpreadableFloraBlock;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class MossBallBlock extends SpreadableFloraBlock {

    private static final VoxelShape SHAPE = MossBallBlock.createCuboidShape(3.5, 0, 3.5, 12.5, 9, 12.5);

    public MossBallBlock(Settings settings) {
        super(3, settings);
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
}
