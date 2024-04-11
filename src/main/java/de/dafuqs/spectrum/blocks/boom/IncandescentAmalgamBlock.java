package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import org.jetbrains.annotations.*;

public class IncandescentAmalgamBlock extends PlacedItemBlock implements Waterloggable, ExplosionAware {
	
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 9.0D, 12.0D);
	
	public IncandescentAmalgamBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false));
	}
	
	@Override
	protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(@NotNull BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(@NotNull BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (!state.get(WATERLOGGED)) {
			int r = random.nextInt(50);
			if (r < 10) {
				double posX = (double) pos.getX() + 0.25D + random.nextDouble() * 0.5D;
				double posY = (double) pos.getY() + random.nextDouble() * 0.5D;
				double posZ = (double) pos.getZ() + 0.25D + random.nextDouble() * 0.5D;
				world.addParticle(ParticleTypes.LAVA, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
				if (r == 0) {
					world.playSound(posX, posY, posZ, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
				}
			}
			if (random.nextInt(100) == 0) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
			}
		}
	}
	
	@Override
	public boolean shouldDropItemsOnExplosion(Explosion explosion) {
		return false;
	}

	@Override
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		super.onSteppedOn(world, pos, state, entity);
		if (!state.get(WATERLOGGED)) {
			explode(world, pos);
		}
	}
	
	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		super.onLandedUpon(world, state, pos, entity, fallDistance);
		if (!state.get(WATERLOGGED)) {
			explode(world, pos);
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
		super.onProjectileHit(world, state, hit, projectile);
		if (!state.get(WATERLOGGED)) {
			explode(world, hit.getBlockPos());
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
		if (!state.get(WATERLOGGED) && world.random.nextInt(10) == 0) {
			explode(world, pos);
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!state.get(WATERLOGGED)
				&& !player.isCreative()
				&& EnchantmentHelper.getLevel(SpectrumEnchantments.RESONANCE, player.getStackInHand(player.getActiveHand())) == 0) {

			explode(world, pos);
		}

		super.onBreak(world, pos, state, player);
	}

	protected static void explode(World world, BlockPos pos) {
		if (!world.isClient) {
			if (world.getBlockEntity(pos) instanceof PlacedItemBlockEntity placedItemBlockEntity) {
				PlayerEntity owner = placedItemBlockEntity.getOwnerIfOnline();
				ItemStack stack = placedItemBlockEntity.getStack();

				world.removeBlock(pos, false);
				explode(world, pos, owner, stack);
			}
		}
	}

	public static void explode(World world, BlockPos pos, Entity owner, ItemStack stack) {
		float power = 8.0F;
		if (stack.getItem() instanceof IncandescentAmalgamItem item) {
			power = item.getExplosionPower(stack, false);
		}
		world.createExplosion(owner, SpectrumDamageTypes.incandescence(world, owner), new ExplosionBehavior(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, power, true, World.ExplosionSourceType.BLOCK);
	}

	@Override
	public void beforeDestroyedByExplosion(World world, BlockPos pos, BlockState state, Explosion explosion) {
		explode(world, pos);
	}

}
