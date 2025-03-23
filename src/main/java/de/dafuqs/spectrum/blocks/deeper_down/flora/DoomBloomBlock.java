package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import org.jetbrains.annotations.*;

public class DoomBloomBlock extends FlowerBlock implements Fertilizable, ExplosionAware {
	
	public static final IntProperty AGE = Properties.AGE_4;
	public static final int AGE_MAX = Properties.AGE_4_MAX;
	protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 9.0, 12.0);
	protected static final double GROW_CHANCE = 0.2;
	
	public DoomBloomBlock(StatusEffect suspiciousStewEffect, int effectDuration, Settings settings) {
		super(suspiciousStewEffect, effectDuration, settings);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(AGE);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isIn(SpectrumBlockTags.DOOMBLOOM_PLANTABLE);
	}
	
	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
		return state.get(AGE) < Properties.AGE_4_MAX;
	}
	
	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (random.nextFloat() > GROW_CHANCE) {
			grow(world, random, pos, state);
		}
	}
	
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		if (canPlaceAt(state, world, pos)) {
			int age = state.get(AGE);
			if (age < Properties.AGE_4_MAX) {
				world.setBlockState(pos, state.with(AGE, age + 1));
				world.playSound(null, pos, state.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
		} else {
			Block.replace(state, Blocks.AIR.getDefaultState(), world, pos, 10, 512);
		}
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (state.get(AGE) == AGE_MAX) {
			int r = random.nextInt(100);
			if (r < 16) {
				double posX = (double) pos.getX() + 0.25D + random.nextDouble() * 0.5D;
				double posY = (double) pos.getY() + random.nextDouble() * 0.5D;
				double posZ = (double) pos.getZ() + 0.25D + random.nextDouble() * 0.5D;
				world.addParticle(ParticleTypes.LAVA, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
				if (r < 2) {
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
	public void beforeDestroyedByExplosion(World world, BlockPos pos, BlockState state, Explosion explosion) {
		explode(world, pos, state);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient()) {
			var velocity = entity.getVelocity().length();
			if (velocity > 0.235 && world.random.nextInt(20) <= velocity * 20 || entity.isOnFire()) {
				explode(world, pos, state);
			}
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
		super.onProjectileHit(world, state, hit, projectile);
		explode(world, hit.getBlockPos(), state);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
		if (world.random.nextInt(10) == 0) {
			explode(world, pos, state);
		}
	}
	
	// does not run in creative
	// => creative players can easily break it without causing an explosion
	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		super.afterBreak(world, player, pos, state, blockEntity, stack);
		if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0 && !stack.isIn(ConventionalItemTags.SHEARS)) {
			explode(world, pos, state);
		}
	}
	
	protected static void explode(World world, BlockPos pos, BlockState state) {
		if (state.get(AGE) == AGE_MAX) {
			world.removeBlock(pos, false);
			world.createExplosion(null, SpectrumDamageTypes.incandescence(world), new ExplosionBehavior(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.0F, true, World.ExplosionSourceType.BLOCK);
			if (!world.isClient) {
				dropStack(world, pos, new ItemStack(SpectrumItems.DOOMBLOOM_SEED, world.random.nextBetween(3, 7)));
			}
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		int age = state.get(AGE);
		if (age == AGE_MAX) {
			if (world.isClient) {
				return ActionResult.SUCCESS;
			} else {
				world.setBlockState(pos, state.with(AGE, 0));
				int randomCount = world.random.nextBetween(2, 3);
				player.getInventory().offerOrDrop(new ItemStack(Items.GUNPOWDER, randomCount));
				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 0.9F + world.random.nextFloat() * 0.2F);
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}

}
