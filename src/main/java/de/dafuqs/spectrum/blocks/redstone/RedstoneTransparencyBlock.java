package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

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
	
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		return (state.get(RedstoneTransparencyBlock.TRANSPARENCY_STATE) != TransparencyState.SOLID) && stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
	}
	
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
	
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if ((state.get(TRANSPARENCY_STATE) == TransparencyState.NO_COLLISION)) {
			return VoxelShapes.empty();
		} else {
			return state.getOutlineShape(world, pos);
		}
	}
	
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return !(state.get(TRANSPARENCY_STATE) == TransparencyState.SOLID);
	}
	
	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		if ((state.get(TRANSPARENCY_STATE) == TransparencyState.SOLID)) {
			return world.getMaxLightLevel();
		} else {
			return super.getOpacity(state, world, pos);
		}
	}
	
	@Deprecated
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if ((state.get(TRANSPARENCY_STATE) == TransparencyState.NO_COLLISION)) {
			return VoxelShapes.fullCube();
		} else {
			return super.getOutlineShape(state, world, pos, context);
		}
	}
	
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		int power = ctx.getWorld().getReceivedRedstonePower(ctx.getBlockPos());
		return this.getDefaultState().with(TRANSPARENCY_STATE, getStateForRedstonePower(power));
	}
	
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
	
	private boolean setTransparencyStateBasedOnRedstone(World world, BlockPos blockPos, BlockState currentState) {
		int powerAtPos = world.getReceivedRedstonePower(blockPos);
		TransparencyState targetTransparencyState = getStateForRedstonePower(powerAtPos);
		
		if (currentState.get(TRANSPARENCY_STATE) != targetTransparencyState) {
			world.setBlockState(blockPos, currentState.with(TRANSPARENCY_STATE, targetTransparencyState));
			return true;
		}
		
		return false;
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
		
		private TransparencyState(String name) {
			this.name = name;
		}
		
		public String toString() {
			return this.name;
		}
		
		public String asString() {
			return this.name;
		}
	}
	
}
