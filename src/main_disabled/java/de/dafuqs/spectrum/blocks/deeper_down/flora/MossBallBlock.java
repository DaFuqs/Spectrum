package de.dafuqs.spectrum.blocks.deeper_down.flora;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;

public class MossBallBlock extends SpreadableFloraBlock {
	
	public static final MapCodec<MossBallBlock> CODEC = simpleCodec(MossBallBlock::new);
	
	private static final VoxelShape SHAPE = MossBallBlock.box(3.5, 0, 3.5, 12.5, 9, 12.5);
	
	public MossBallBlock(Properties settings) {
        super(3, settings);
    }

//    @Override
//    public MapCodec<? extends MossBallBlock> getCodec() {
//        //TODO: Make the codec
//        return CODEC;
//    }

    @Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		super.fallOn(world, state, pos, entity, fallDistance / 2F);
    }

    @Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		Vec3 vec3d = state.getOffset(world, pos);
		return SHAPE.move(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
	public float getMaxHorizontalOffset() {
        return 0.2F;
    }

    @Override
	public float getMaxVerticalOffset() {
        return 0.125F;
    }

    @Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
        return true;
    }
}
