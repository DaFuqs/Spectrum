package de.dafuqs.spectrum.blocks.deeper_down.flora;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;

public class GiantMossBallBlock extends MossBallBlock {
	
	public static final MapCodec<GiantMossBallBlock> CODEC = simpleCodec(GiantMossBallBlock::new);
	
	public GiantMossBallBlock(Properties settings) {
		super(settings);
	}

//    @Override
//    public MapCodec<? extends GiantMossBallBlock> getCodec() {
//        //TODO: Make the codec
//        return CODEC;
//    }
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
			entity.makeStuckInBlock(state, new Vec3(0.9F, 0.334, 0.9F));
		}
	}
	
	@Override
	public float getMaxHorizontalOffset() {
		return 0.1F;
	}
	
	@Override
	public float getMaxVerticalOffset() {
		return 0.25F;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		Vec3 vec3d = state.getOffset(world, pos);
		return Shapes.block().move(vec3d.x, vec3d.y, vec3d.z);
	}
}
