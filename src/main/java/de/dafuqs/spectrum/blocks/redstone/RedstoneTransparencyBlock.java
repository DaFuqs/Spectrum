package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class RedstoneTransparencyBlock extends Block {
	
	public static final EnumProperty<TransparencyState> TRANSPARENCY_STATE = EnumProperty.of("transparency_state", TransparencyState.class);
	
	public RedstoneTransparencyBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(TRANSPARENCY_STATE, TransparencyState.SOLID));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(TRANSPARENCY_STATE);
	}
	
	@Override
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		return (state.get(RedstoneTransparencyBlock.TRANSPARENCY_STATE) != TransparencyState.SOLID) && stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
	}
	
	@Override
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		switch (state.get(TRANSPARENCY_STATE)) {
			case SOLID -> {
				return 0.0F;
			}
			case TRANSLUCENT -> {
				return 0.5F;
			}
			default -> {
				return 1.0F;
			}
		}
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return (state.get(TRANSPARENCY_STATE) == TransparencyState.NO_COLLISION);
	}
	
	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if ((state.get(TRANSPARENCY_STATE) == TransparencyState.NO_COLLISION)) {
			return VoxelShapes.empty();
		} else {
			return state.getOutlineShape(world, pos);
		}
	}
	
	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return !(state.get(TRANSPARENCY_STATE) == TransparencyState.SOLID);
	}
	
	@Override
	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		if ((state.get(TRANSPARENCY_STATE) == TransparencyState.SOLID)) {
			return world.getMaxLightLevel();
		} else {
			return super.getOpacity(state, world, pos);
		}
	}
	
	@Override
	@Deprecated
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if ((state.get(TRANSPARENCY_STATE) == TransparencyState.NO_COLLISION)) {
			return VoxelShapes.fullCube();
		} else {
			return super.getOutlineShape(state, world, pos, context);
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		int power = ctx.getWorld().getReceivedRedstonePower(ctx.getBlockPos());
		return this.getDefaultState().with(TRANSPARENCY_STATE, getStateForRedstonePower(power));
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (!world.isClient) {
			BlockState fromPosBlockState = world.getBlockState(fromPos);
			if (fromPosBlockState.getBlock() instanceof RedstoneTransparencyBlock) {
				TransparencyState sourceTransparencyState = fromPosBlockState.get(TRANSPARENCY_STATE);
				
				if (sourceTransparencyState != state.get(TRANSPARENCY_STATE)) {
					world.setBlockState(pos, world.getBlockState(pos).with(TRANSPARENCY_STATE, sourceTransparencyState));
				}
			} else {
				if (!fromPosBlockState.isAir()) {
					setTransparencyStateBasedOnRedstone(world, pos, state);
				}
			}
		}
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
	}
	
	private void setTransparencyStateBasedOnRedstone(World world, BlockPos blockPos, BlockState currentState) {
		int powerAtPos = world.getReceivedRedstonePower(blockPos);
		TransparencyState targetTransparencyState = getStateForRedstonePower(powerAtPos);
		
		if (currentState.get(TRANSPARENCY_STATE) != targetTransparencyState) {
			world.setBlockState(blockPos, currentState.with(TRANSPARENCY_STATE, targetTransparencyState));
		}
		
	}
	
	private TransparencyState getStateForRedstonePower(int power) {
		if (power == 15) {
			return TransparencyState.NO_COLLISION;
		} else if (power == 0) {
			return TransparencyState.SOLID;
		} else {
			return TransparencyState.TRANSLUCENT;
		}
	}
	
	public enum TransparencyState implements StringIdentifiable {
		SOLID("solid"),
		TRANSLUCENT("translucent"),
		NO_COLLISION("no_collision");
		
		private final String name;
		
		TransparencyState(String name) {
			this.name = name;
		}
		
		public String toString() {
			return this.name;
		}
		
		@Override
		public String asString() {
			return this.name;
		}
	}
	
}
