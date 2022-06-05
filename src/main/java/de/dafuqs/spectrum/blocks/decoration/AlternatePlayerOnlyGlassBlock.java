package de.dafuqs.spectrum.blocks.decoration;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class AlternatePlayerOnlyGlassBlock extends GlassBlock {
	
	private final Block alternateBlock;
	
	// used for tinted glass to make light not shine through
	private final boolean tinted;
	
	public AlternatePlayerOnlyGlassBlock(Settings settings, Block block, boolean tinted) {
		super(settings);
		this.alternateBlock = block;
		this.tinted = tinted;
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context instanceof EntityShapeContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if (entity instanceof PlayerEntity) {
				return VoxelShapes.empty();
			}
		}
		return state.getOutlineShape(world, pos);
	}
	
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return !tinted;
	}
	
	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		if (tinted) {
			return world.getMaxLightLevel();
		} else {
			return super.getOpacity(state, world, pos);
		}
	}
	
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		if (stateFrom.isOf(this) || stateFrom.getBlock() == alternateBlock) {
			return true;
		}
		
		return super.isSideInvisible(state, stateFrom, direction);
	}
	
}
