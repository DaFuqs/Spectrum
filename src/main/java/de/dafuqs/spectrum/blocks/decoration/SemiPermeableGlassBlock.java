package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.*;

public class SemiPermeableGlassBlock extends TransparentBlock {
	
	private final Block alternateBlock;
	
	// used for tinted glass to make light not shine through
	private final boolean tinted;
	
	public SemiPermeableGlassBlock(BlockBehaviour.Properties settings, Block block, boolean tinted) {
		super(settings);
		this.alternateBlock = block;
		this.tinted = tinted;
	}

	@Override
	public @Nullable MapCodec<? extends SemiPermeableGlassBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	
	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return isPlayerOrPlayerRiderCollision(context) ? Shapes.empty() : state.getShape(world, pos);
	}
	
	public static boolean isPlayerOrPlayerRiderCollision(CollisionContext context) {
		if (context instanceof EntityCollisionContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			return entity != null && (entity instanceof Player || entity.getFirstPassenger() instanceof Player);
		}
		return false;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return !tinted;
	}
	
	@Override
	public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
		if (tinted) {
			return world.getMaxLightLevel();
		} else {
			return super.getLightBlock(state, world, pos);
		}
	}
	
	@Override
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		if (stateFrom.is(this) || stateFrom.getBlock() == alternateBlock) {
			return true;
		}
		
		return super.skipRendering(state, stateFrom, direction);
	}
	
}
