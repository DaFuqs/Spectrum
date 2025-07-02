package de.dafuqs.spectrum.blocks.end_portal;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.shapes.*;

public class CrackedEndPortalFrameBlock extends Block {
	
	public static final MapCodec<CrackedEndPortalFrameBlock> CODEC = simpleCodec(CrackedEndPortalFrameBlock::new);
	
	public static final BooleanProperty FACING_VERTICAL = BooleanProperty.create("facing_vertical");
	public static final EnumProperty<EndPortalFrameEye> EYE_TYPE = EnumProperty.create("eye_type", CrackedEndPortalFrameBlock.EndPortalFrameEye.class);
	protected static final VoxelShape FRAME_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
	protected static final VoxelShape EYE_SHAPE = Block.box(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	protected static final VoxelShape FRAME_WITH_EYE_SHAPE = Shapes.or(FRAME_SHAPE, EYE_SHAPE);

	@Override
	public MapCodec<? extends CrackedEndPortalFrameBlock> codec() {
		return CODEC;
	}
	
	public CrackedEndPortalFrameBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING_VERTICAL, false).setValue(EYE_TYPE, EndPortalFrameEye.NONE));
	}
	
	
	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(EYE_TYPE).hasEye() ? FRAME_WITH_EYE_SHAPE : FRAME_SHAPE;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction facing = ctx.getHorizontalDirection();
		boolean facingVertical = facing.equals(Direction.EAST) || facing.equals(Direction.WEST);
		return (this.defaultBlockState().setValue(FACING_VERTICAL, facingVertical).setValue(EYE_TYPE, EndPortalFrameEye.NONE));
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING_VERTICAL, !state.getValue(FACING_VERTICAL));
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING_VERTICAL, EYE_TYPE);
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return state.getValue(EYE_TYPE).getRedstonePower();
	}
	
	@Override
	@Deprecated
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		// when placed via perturbed eye => fuse
		if (state.getValue(EYE_TYPE).hasExplosions()) {
			world.scheduleTick(pos, this, 40);
		}
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (state.getValue(EYE_TYPE).hasExplosions()) {
			double d = (double) pos.getX() + random.nextDouble();
			double e = (double) pos.getY() + 1.05D;
			double f = (double) pos.getZ() + random.nextDouble();
			world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (state.getValue(EYE_TYPE).hasExplosions()) {
			// 10% chance to break portal
			float randomFloat = random.nextFloat();
			if (randomFloat < 0.05) {
				world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 4, Level.ExplosionInteraction.BLOCK);
				EndPortalShaper.destroyPortals(world, pos);
				world.destroyBlock(pos, true);
			} else if (randomFloat < 0.2) {
				world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, Level.ExplosionInteraction.BLOCK);
			} else {
				double d = (double) pos.getX() + random.nextDouble();
				double e = (double) pos.getY() + 0.8D;
				double f = (double) pos.getZ() + random.nextDouble();
				world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0D, 0.0D, 0.0D);
			}
		}
		world.scheduleTick(pos, this, 10);
	}
	
	public enum EndPortalFrameEye implements StringRepresentable {
		VANILLA_WITH_PERTURBED_EYE("vanilla_cracker", true, true, 8),
		NONE("none", false, false, 0),
		WITH_EYE_OF_ENDER("ender", true, false, 15),
		WITH_PERTURBED_EYE("cracker", true, true, 8);

		private final String name;
		private final boolean hasEye;
		private final boolean hasExplosions; // TIL `volatile` is a keyword in java
		private final int redstonePower;

		EndPortalFrameEye(String name, boolean hasEye, boolean hasExplosions, int redstonePower) {
			this.name = name;
			this.hasEye = hasEye;
			this.redstonePower = redstonePower;
			this.hasExplosions = hasExplosions;
		}

		public String toString() {
			return this.name;
		}
		
		@Override
		public String getSerializedName() {
			return this.name;
		}

		public boolean hasEye() {
			return hasEye;
		}

		public boolean hasExplosions() {
			return this.hasExplosions;
		}

		public int getRedstonePower() {
			return this.redstonePower;
		}

	}
	
}
