package de.dafuqs.spectrum.blocks;

import com.google.common.base.Predicates;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Random;

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
		this.setDefaultState(((
				this.stateManager.getDefaultState())
				.with(FACING_VERTICAL, false))
				.with(EYE_TYPE, EndPortalFrameEye.NONE));
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
			
			world.syncGlobalEvent(1038, portalTopLeft.add(1, 0, 1), 0);
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
			
			world.syncGlobalEvent(1038, portalTopLeft.add(1, 0, 1), 0);
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
	
	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(EYE_TYPE).equals(EndPortalFrameEye.NONE) ? FRAME_SHAPE : FRAME_WITH_EYE_SHAPE;
	}
	
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction facing = ctx.getPlayerFacing();
		boolean facingVertical = facing.equals(Direction.EAST) || facing.equals(Direction.WEST);
		return (this.getDefaultState().with(FACING_VERTICAL, facingVertical).with(EYE_TYPE, EndPortalFrameEye.NONE));
	}
	
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING_VERTICAL, !state.get(FACING_VERTICAL));
	}
	
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state;
	}
	
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING_VERTICAL, EYE_TYPE);
	}
	
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Deprecated
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
	
	}
	
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(EYE_TYPE).equals(EndPortalFrameEye.WITH_EYE_OF_ENDER) ? 15 : 0;
	}
	
	@Override
	@Deprecated
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		// when placed via end portal cracker => fuse
		if (isVolatile(state)) {
			world.createAndScheduleBlockTick(pos, this, 40);
		}
	}
	
	public boolean isVolatile(BlockState blockState) {
		EndPortalFrameEye endPortalFrameEye = blockState.get(EYE_TYPE);
		return endPortalFrameEye.equals(EndPortalFrameEye.VANILLA_WITH_END_PORTAL_CRACKER) || endPortalFrameEye.equals(EndPortalFrameEye.WITH_END_PORTAL_CRACKER);
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (isVolatile(world.getBlockState(pos))) {
			double d = (double) pos.getX() + random.nextDouble();
			double e = (double) pos.getY() + 1.05D;
			double f = (double) pos.getZ() + random.nextDouble();
			world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Deprecated
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (isVolatile(state)) {
			// 10% chance to break portal
			float randomFloat = random.nextFloat();
			if (randomFloat < 0.05) {
				world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2, Explosion.DestructionType.BREAK);
				destroyPortals(world, pos);
				world.breakBlock(pos, true);
			} else if (randomFloat < 0.2) {
				world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, Explosion.DestructionType.BREAK);
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
		VANILLA_WITH_END_PORTAL_CRACKER("vanilla_cracker"),
		NONE("none"),
		WITH_EYE_OF_ENDER("ender"),
		WITH_END_PORTAL_CRACKER("cracker");
		
		private final String name;
		
		EndPortalFrameEye(String name) {
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
