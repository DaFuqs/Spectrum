package de.dafuqs.spectrum.blocks.deeper_down.flora;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class GiantMossBallBlock extends MossBallBlock{

    public GiantMossBallBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
            entity.slowMovement(state, new Vec3d(0.9F, 0.75, 0.9F));
        }
    }

    @Override
    public float getMaxHorizontalModelOffset() {
        return 0.1F;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Vec3d vec3d = state.getModelOffset(world, pos);
        return VoxelShapes.fullCube().offset(vec3d.x, vec3d.y, vec3d.z);
    }
}
