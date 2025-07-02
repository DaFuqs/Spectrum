package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.fabricmc.api.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.shapes.*;

public class AlternatePlayerOnlyGlassBlock extends TransparentBlock {
	
	private final Block alternateBlock;
	
	// used for tinted glass to make light not shine through
	private final boolean tinted;
	
	public AlternatePlayerOnlyGlassBlock(BlockBehaviour.Properties settings, Block block, boolean tinted) {
		super(settings);
		this.alternateBlock = block;
		this.tinted = tinted;
	}
	
	@Override
	public MapCodec<? extends AlternatePlayerOnlyGlassBlock> codec() {
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
		if (context instanceof EntityCollisionContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if (entity instanceof Player) {
				return Shapes.empty();
			}
		}
		return state.getShape(world, pos);
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
	@Environment(EnvType.CLIENT)
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		if (stateFrom.is(this) || stateFrom.getBlock() == alternateBlock) {
			return true;
		}
		
		return super.skipRendering(state, stateFrom, direction);
	}
	
	public Block getAlternateBlock() {
		return this.alternateBlock;
	}
	
}
