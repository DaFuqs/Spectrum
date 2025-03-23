package de.dafuqs.spectrum.blocks;

import com.google.common.collect.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.primordial_fire_burning.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.registry.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class PrimordialFireBlock extends AbstractFireBlock {
	
	public static boolean EXPLOSION_CAUSES_PRIMORDIAL_FIRE_FLAG = false;
	
	public static final BooleanProperty NORTH = ConnectingBlock.NORTH;
	public static final BooleanProperty EAST = ConnectingBlock.EAST;
	public static final BooleanProperty SOUTH = ConnectingBlock.SOUTH;
	public static final BooleanProperty WEST = ConnectingBlock.WEST;
	public static final BooleanProperty UP = ConnectingBlock.UP;
	private static final Map<Direction, BooleanProperty> DIRECTION_PROPERTIES = ConnectingBlock.FACING_PROPERTIES.entrySet().stream().filter((entry) -> entry.getKey() != Direction.DOWN).collect(Util.toMap());
	private static final VoxelShape UP_SHAPE = Block.createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
	private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
	private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
	private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
	private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
	
	private final Map<BlockState, VoxelShape> shapesByState;
	private static final float DAMAGE = 0.2F;
	
	public PrimordialFireBlock(Settings settings) {
		super(settings, DAMAGE);
		this.setDefaultState(this.stateManager.getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false));
		this.shapesByState = ImmutableMap.copyOf(this.stateManager.getStates().stream().collect(Collectors.toMap(Function.identity(), PrimordialFireBlock::getShapeForState)));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, SOUTH, WEST, UP);
	}
	
	public static VoxelShape getShapeForState(BlockState state) {
		VoxelShape voxelShape = VoxelShapes.empty();
		if (state.get(UP)) {
			voxelShape = UP_SHAPE;
		}
		if (state.get(NORTH)) {
			voxelShape = VoxelShapes.union(voxelShape, NORTH_SHAPE);
		}
		if (state.get(SOUTH)) {
			voxelShape = VoxelShapes.union(voxelShape, SOUTH_SHAPE);
		}
		if (state.get(EAST)) {
			voxelShape = VoxelShapes.union(voxelShape, EAST_SHAPE);
		}
		if (state.get(WEST)) {
			voxelShape = VoxelShapes.union(voxelShape, WEST_SHAPE);
		}
		return voxelShape.isEmpty() ? BASE_SHAPE : voxelShape;
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return this.canPlaceAt(state, world, pos) ? getStateForPosition(world, pos) : Blocks.AIR.getDefaultState();
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return this.shapesByState.get(state);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getStateForPosition(ctx.getWorld(), ctx.getBlockPos());
	}
	
	public BlockState getStateForPosition(BlockView world, BlockPos pos) {
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		if (!this.isFlammable(blockState) && !blockState.isSideSolidFullSquare(world, blockPos, Direction.UP)) {
			BlockState blockState2 = this.getDefaultState();
			for (Direction direction : Direction.values()) {
				BooleanProperty booleanProperty = DIRECTION_PROPERTIES.get(direction);
				if (booleanProperty != null) {
					blockState2 = blockState2.with(booleanProperty, this.isFlammable(world.getBlockState(pos.offset(direction))));
				}
			}
			
			return blockState2;
		} else {
			return this.getDefaultState();
		}
	}
	
	public static boolean tryPlacePrimordialFire(World world, BlockPos pos, Direction direction) {
		BlockState state = world.getBlockState(pos);
		
		if (PrimordialFireBlock.canPlaceAt(world, pos, direction)) {
			if (world.setBlockState(pos, SpectrumBlocks.PRIMORDIAL_FIRE.getStateForPosition(world, pos))) {
				world.emitGameEvent(null, GameEvent.BLOCK_PLACE, pos);
				return true;
			}
		} else if (!CampfireBlock.canBeLit(state) && !CandleBlock.canBeLit(state) && !CandleCakeBlock.canBeLit(state)) {
			if (state.getBlock() instanceof TntBlock) {
				TntBlock.primeTnt(world, pos);
				world.removeBlock(pos, false);
				return true;
			} else {
				return false;
			}
		} else if (world.setBlockState(pos, state.with(Properties.LIT, true))) {
			world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
			return true;
		}
		return false;
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			entity.damage(SpectrumDamageTypes.primordialFire(world, null), DAMAGE);
			OnPrimordialFireComponent.addPrimordialFireTicks(livingEntity, 5);
		}
		if (world.getTime() % 20 == 0 && entity instanceof ItemEntity itemEntity) {
			PrimordialFireBurningRecipe.processItemEntity(world, itemEntity);
		}
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.down();
		return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, Direction.UP) || this.areBlocksAroundFlammable(world, pos);
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
		world.scheduleBlockTick(pos, this, getFireTickDelay(world.random));
		
		if (world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
			if (!state.canPlaceAt(world, pos)) {
				world.removeBlock(pos, false);
			}
			
			BlockState blockState = world.getBlockState(pos.down());
			boolean isAboveInfiniburnBlock = blockState.isIn(world.getDimension().infiniburn()) || blockState.isIn(SpectrumBlockTags.PRIMORDIAL_FIRE_BASE_BLOCKS);
			if (!isAboveInfiniburnBlock && random.nextFloat() < 0.01F) {
				world.removeBlock(pos, false);
			} else {
				if (!isAboveInfiniburnBlock) {
					if (!this.areBlocksAroundFlammable(world, pos)) {
						BlockPos blockPos = pos.down();
						if (!world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, Direction.UP)) {
							world.removeBlock(pos, false);
						}
						if (random.nextInt(10) == 0 && !this.isFlammable(world.getBlockState(pos.down()))) {
							world.removeBlock(pos, false);
							return;
						}
						return;
					}
				}
				
				boolean biomeHasIncreasedFireBurnout = world.getBiome(pos).isIn(BiomeTags.INCREASED_FIRE_BURNOUT);
				int spreadReduction = biomeHasIncreasedFireBurnout ? -50 : 0;
				this.trySpreadingFire(world, pos.east(), 300 + spreadReduction, random);
				this.trySpreadingFire(world, pos.west(), 300 + spreadReduction, random);
				this.trySpreadingFire(world, pos.down(), 250 + spreadReduction, random);
				this.trySpreadingFire(world, pos.up(), 250 + spreadReduction, random);
				this.trySpreadingFire(world, pos.north(), 300 + spreadReduction, random);
				this.trySpreadingFire(world, pos.south(), 300 + spreadReduction, random);
				
				BlockPos.Mutable mutable = new BlockPos.Mutable();
				for (int x = -1; x <= 1; ++x) {
					for (int z = -1; z <= 1; ++z) {
						for (int y = -1; y <= 4; ++y) {
							if (x != 0 || y != 0 || z != 0) {
								int o = 100;
								if (y > 1) {
									o += (y - 1) * 100;
								}
								mutable.set(pos, x, y, z);
								int burnChance = this.getBurnChance(world, mutable);
								if (burnChance > 0) {
									int q = (burnChance + 40 + world.getDifficulty().getId() * 7) / 30;
									if (q > 0 && random.nextInt(o) <= q) {
										world.setBlockState(mutable, getStateForPosition(world, mutable), 3);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private int getSpreadChance(BlockState state) {
		return state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED) ? 0 : FlammableBlockRegistry.getDefaultInstance().get(state.getBlock()).getSpreadChance();
	}
	
	private int getBurnChance(BlockState state) {
		return state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED) ? 0 : FlammableBlockRegistry.getDefaultInstance().get(state.getBlock()).getBurnChance();
	}
	
	private void trySpreadingFire(World world, BlockPos pos, int spreadFactor, Random random) {
		if (!GenericClaimModsCompat.canBreak(world, pos, null)) {
			return;
		}
		
		int spreadChance = this.getSpreadChance(world.getBlockState(pos));
		if (random.nextInt(spreadFactor) < spreadChance) {
			BlockState currentState = world.getBlockState(pos);
			if (random.nextBoolean()) {
				if (PrimordialFireBurningRecipe.processBlock(world, pos, currentState)) {
					return;
				}
				
				// replace the current block with fire
				world.setBlockState(pos, getStateForPosition(world, pos), 3);
			}
			
			if (currentState.getBlock() instanceof TntBlock) {
				TntBlock.primeTnt(world, pos);
			}
		}
	}
	
	private boolean areBlocksAroundFlammable(BlockView world, BlockPos pos) {
		for (Direction direction : Direction.values()) {
			if (this.isFlammable(world.getBlockState(pos.offset(direction)))) {
				return true;
			}
		}
		return false;
	}
	
	private int getBurnChance(WorldView world, BlockPos pos) {
		if (!world.isAir(pos)) {
			return 0;
		} else {
			int i = 0;
			for (Direction direction : Direction.values()) {
				BlockState blockState = world.getBlockState(pos.offset(direction));
				i = Math.max(this.getBurnChance(blockState), i);
			}
			return i;
		}
	}
	
	@Override
	protected boolean isFlammable(BlockState state) {
		return this.getBurnChance(state) > 0;
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		world.scheduleBlockTick(pos, this, getFireTickDelay(world.random));
	}
	
	private static int getFireTickDelay(Random random) {
		return 20 + random.nextInt(10);
	}
	
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(24) == 0) {
			world.playSound((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SpectrumSoundEvents.PRIMORDIAL_FIRE_CRACKLE, SoundCategory.BLOCKS, 0.175F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
		}
		
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		int i;
		double d;
		double e;
		double f;
		
		if (blockState.isSideSolidFullSquare(world, blockPos, Direction.UP)) {
			var particle = this.isFlammable(blockState) ? SpectrumParticleTypes.PRIMORDIAL_SIGNAL_SMOKE : SpectrumParticleTypes.PRIMORDIAL_COSY_SMOKE;
			for (i = 0; i < 2; ++i) {
				d = (double) pos.getX() + 0.5 + random.nextDouble() / 4.0 * (double) (random.nextBoolean() ? 1 : -1);
				e = (double) pos.getY() + 0.15;
				f = (double) pos.getZ() + 0.5 + random.nextDouble() / 4.0 * (double) (random.nextBoolean() ? 1 : -1);
				world.addParticle(particle, d, e, f, 0.0015, 0.07, 0.0015);
			}
		}
		
		if (!this.isFlammable(blockState) && !blockState.isSideSolidFullSquare(world, blockPos, Direction.UP)) {
			if (this.isFlammable(world.getBlockState(pos.west()))) {
				for (i = 0; i < 2; ++i) {
					d = (double) pos.getX() + random.nextDouble() * 0.10000000149011612;
					e = (double) pos.getY() + random.nextDouble();
					f = (double) pos.getZ() + random.nextDouble();
					world.addParticle(SpectrumParticleTypes.PRIMORDIAL_SMOKE, d, e, f, 0.0, 0.0, 0.0);
				}
			}
			
			if (this.isFlammable(world.getBlockState(pos.east()))) {
				for (i = 0; i < 2; ++i) {
					d = (double) (pos.getX() + 1) - random.nextDouble() * 0.10000000149011612;
					e = (double) pos.getY() + random.nextDouble();
					f = (double) pos.getZ() + random.nextDouble();
					world.addParticle(SpectrumParticleTypes.PRIMORDIAL_SMOKE, d, e, f, 0.0, 0.0, 0.0);
				}
			}
			
			if (this.isFlammable(world.getBlockState(pos.north()))) {
				for (i = 0; i < 2; ++i) {
					d = (double) pos.getX() + random.nextDouble();
					e = (double) pos.getY() + random.nextDouble();
					f = (double) pos.getZ() + random.nextDouble() * 0.10000000149011612;
					world.addParticle(SpectrumParticleTypes.PRIMORDIAL_SMOKE, d, e, f, 0.0, 0.0, 0.0);
				}
			}
			
			if (this.isFlammable(world.getBlockState(pos.south()))) {
				for (i = 0; i < 2; ++i) {
					d = (double) pos.getX() + random.nextDouble();
					e = (double) pos.getY() + random.nextDouble();
					f = (double) (pos.getZ() + 1) - random.nextDouble() * 0.10000000149011612;
					world.addParticle(SpectrumParticleTypes.PRIMORDIAL_SMOKE, d, e, f, 0.0, 0.0, 0.0);
				}
			}
			
			if (this.isFlammable(world.getBlockState(pos.up()))) {
				for (i = 0; i < 2; ++i) {
					d = (double) pos.getX() + random.nextDouble();
					e = (double) (pos.getY() + 1) - random.nextDouble() * 0.10000000149011612;
					f = (double) pos.getZ() + random.nextDouble();
					world.addParticle(SpectrumParticleTypes.PRIMORDIAL_SMOKE, d, e, f, 0.0, 0.0, 0.0);
				}
			}
		} else {
			for (i = 0; i < 3; ++i) {
				d = (double) pos.getX() + random.nextDouble();
				e = (double) pos.getY() + random.nextDouble() * 0.5 + 0.5;
				f = (double) pos.getZ() + random.nextDouble();
				world.addParticle(SpectrumParticleTypes.PRIMORDIAL_SMOKE, d, e, f, 0.0, 0.0, 0.0);
			}
		}
		
	}
	
}
