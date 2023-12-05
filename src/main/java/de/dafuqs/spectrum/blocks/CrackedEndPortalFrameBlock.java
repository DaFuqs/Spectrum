package de.dafuqs.spectrum.blocks;

import com.google.common.base.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.pattern.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.predicate.block.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;

public class CrackedEndPortalFrameBlock extends Block {
	
	public static final BooleanProperty FACING_VERTICAL;
	public static final EnumProperty<EndPortalFrameEye> EYE_TYPE;
	protected static final VoxelShape FRAME_SHAPE;
	protected static final VoxelShape EYE_SHAPE;
	protected static final VoxelShape FRAME_WITH_EYE_SHAPE;
	private static BlockPattern COMPLETED_FRAME;
	private static BlockPattern END_PORTAL;
	
	static {
		FACING_VERTICAL = BooleanProperty.of("facing_vertical");
		EYE_TYPE = EnumProperty.of("eye_type", CrackedEndPortalFrameBlock.EndPortalFrameEye.class);
		FRAME_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
		EYE_SHAPE = Block.createCuboidShape(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D);
		FRAME_WITH_EYE_SHAPE = VoxelShapes.union(FRAME_SHAPE, EYE_SHAPE);
	}
	
	public CrackedEndPortalFrameBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING_VERTICAL, false).with(EYE_TYPE, EndPortalFrameEye.NONE));
	}

	public static void checkAndFillEndPortal(World world, BlockPos blockPos) {
		BlockPattern.Result result = CrackedEndPortalFrameBlock.getCompletedFramePattern().searchAround(world, blockPos);
		if (result != null) {
			// since the custom portal does not have
			// fixed directions we can estimate the
			// portal position based on some simple checks instead
			BlockPos portalTopLeft = result.getFrontTopLeft().add(-3, 0, -3);
			if (world.getBlockState(portalTopLeft.add(7, 0, 0)).getBlock().equals(SpectrumBlocks.CRACKED_END_PORTAL_FRAME)) {
				portalTopLeft = portalTopLeft.add(4, 0, 0);
			} else if (world.getBlockState(portalTopLeft.add(0, 0, 7)).getBlock().equals(SpectrumBlocks.CRACKED_END_PORTAL_FRAME)) {
				portalTopLeft = portalTopLeft.add(0, 0, 4);
			}
			
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 3; ++j) {
					world.setBlockState(portalTopLeft.add(i, 0, j), Blocks.END_PORTAL.getDefaultState(), 2);
				}
			}
			
			world.syncGlobalEvent(WorldEvents.END_PORTAL_OPENED, portalTopLeft.add(1, 0, 1), 0);
		}
	}
	
	public static void destroyPortals(World world, BlockPos blockPos) {
		BlockPattern.Result result = CrackedEndPortalFrameBlock.getActiveEndPortalPattern().searchAround(world, blockPos);
		if (result != null) {
			// since the custom portal does not have
			// fixed directions we can estimate the
			// portal position based on some simple checks instead
			BlockPos portalTopLeft = result.getFrontTopLeft().add(-3, 0, -3);
			Block b1 = world.getBlockState(portalTopLeft.add(7, 0, 0)).getBlock();
			Block b2 = world.getBlockState(portalTopLeft.add(0, 0, 7)).getBlock();
			if (b1.equals(SpectrumBlocks.CRACKED_END_PORTAL_FRAME) || b1.equals(Blocks.END_PORTAL_FRAME)) {
				portalTopLeft = portalTopLeft.add(4, 0, 0);
			} else if (b2.equals(SpectrumBlocks.CRACKED_END_PORTAL_FRAME) || b2.equals(Blocks.END_PORTAL_FRAME)) {
				portalTopLeft = portalTopLeft.add(0, 0, 4);
			}
			
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 3; ++j) {
					world.setBlockState(portalTopLeft.add(i, 0, j), Blocks.AIR.getDefaultState(), 2);
				}
			}
			
			world.syncGlobalEvent(WorldEvents.END_PORTAL_OPENED, portalTopLeft.add(1, 0, 1), 0);
		}
	}
	
	public static BlockPattern getCompletedFramePattern() {
		if (COMPLETED_FRAME == null) {
			COMPLETED_FRAME = BlockPatternBuilder.start()
					.aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?")
					.where('?', CachedBlockPosition.matchesBlockState(BlockStatePredicate.ANY))
					.where('^', CachedBlockPosition.matchesBlockState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
									.with(EndPortalFrameBlock.EYE, Predicates.equalTo(true))
									.with(EndPortalFrameBlock.FACING, Predicates.equalTo(Direction.SOUTH))
									.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME)
											.with(EYE_TYPE, Predicates.equalTo(EndPortalFrameEye.WITH_EYE_OF_ENDER))
											.with(FACING_VERTICAL, Predicates.equalTo(false)))))
					.where('>', CachedBlockPosition.matchesBlockState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
									.with(EndPortalFrameBlock.EYE, Predicates.equalTo(true))
									.with(EndPortalFrameBlock.FACING, Predicates.equalTo(Direction.WEST))
									.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME)
											.with(EYE_TYPE, Predicates.equalTo(EndPortalFrameEye.WITH_EYE_OF_ENDER))
											.with(FACING_VERTICAL, Predicates.equalTo(true)))))
					.where('v', CachedBlockPosition.matchesBlockState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
									.with(EndPortalFrameBlock.EYE, Predicates.equalTo(true))
									.with(EndPortalFrameBlock.FACING, Predicates.equalTo(Direction.NORTH))
									.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME)
											.with(EYE_TYPE, Predicates.equalTo(EndPortalFrameEye.WITH_EYE_OF_ENDER))
											.with(FACING_VERTICAL, Predicates.equalTo(false)))))
					.where('<', CachedBlockPosition.matchesBlockState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
									.with(EndPortalFrameBlock.EYE, Predicates.equalTo(true))
									.with(EndPortalFrameBlock.FACING, Predicates.equalTo(Direction.EAST))
									.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME)
											.with(EYE_TYPE, Predicates.equalTo(EndPortalFrameEye.WITH_EYE_OF_ENDER))
											.with(FACING_VERTICAL, Predicates.equalTo(true)))))
					.build();
		}
		return COMPLETED_FRAME;
	}
	
	public static BlockPattern getActiveEndPortalPattern() {
		if (END_PORTAL == null) {
			END_PORTAL = BlockPatternBuilder.start()
					.aisle("?vvv?", ">ppp<", ">ppp<", ">ppp<", "?^^^?")
					.where('?', CachedBlockPosition.matchesBlockState(BlockStatePredicate.ANY))
					.where('^', CachedBlockPosition.matchesBlockState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
									.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME))))
					.where('>', CachedBlockPosition.matchesBlockState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
									.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME))))
					.where('v', CachedBlockPosition.matchesBlockState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
									.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME))))
					.where('<', CachedBlockPosition.matchesBlockState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
									.or(BlockStatePredicate.forBlock(SpectrumBlocks.CRACKED_END_PORTAL_FRAME))))
					.where('p', CachedBlockPosition.matchesBlockState(
							BlockStatePredicate.forBlock(Blocks.END_PORTAL)))
					.build();
		}
		return END_PORTAL;
	}
	
	@Override
	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(EYE_TYPE).hasEye() ? FRAME_WITH_EYE_SHAPE : FRAME_SHAPE;
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction facing = ctx.getPlayerFacing();
		boolean facingVertical = facing.equals(Direction.EAST) || facing.equals(Direction.WEST);
		return (this.getDefaultState().with(FACING_VERTICAL, facingVertical).with(EYE_TYPE, EndPortalFrameEye.NONE));
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING_VERTICAL, !state.get(FACING_VERTICAL));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING_VERTICAL, EYE_TYPE);
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(EYE_TYPE).getRedstonePower();
	}
	
	@Override
	@Deprecated
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		// when placed via perturbed eye => fuse
		if (state.get(EYE_TYPE).hasExplosions()) {
			world.createAndScheduleBlockTick(pos, this, 40);
		}
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(EYE_TYPE).hasExplosions()) {
			double d = (double) pos.getX() + random.nextDouble();
			double e = (double) pos.getY() + 1.05D;
			double f = (double) pos.getZ() + random.nextDouble();
			world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	@Deprecated
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(EYE_TYPE).hasExplosions()) {
			// 10% chance to break portal
			float randomFloat = random.nextFloat();
			if (randomFloat < 0.05) {
				world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 4, Explosion.DestructionType.DESTROY);
				destroyPortals(world, pos);
				world.breakBlock(pos, true);
			} else if (randomFloat < 0.2) {
				world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, Explosion.DestructionType.DESTROY);
			} else {
				double d = (double) pos.getX() + random.nextDouble();
				double e = (double) pos.getY() + 0.8D;
				double f = (double) pos.getZ() + random.nextDouble();
				world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0D, 0.0D, 0.0D);
			}
		}
		world.createAndScheduleBlockTick(pos, this, 10);
	}

	public enum EndPortalFrameEye implements StringIdentifiable {
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
		public String asString() {
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
