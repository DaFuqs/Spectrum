package de.dafuqs.spectrum.blocks.deeper_down.flora;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v2.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class DoomBloomBlock extends FlowerBlock implements BonemealableBlock {
	
	public static final MapCodec<DoomBloomBlock> CODEC = RecordCodecBuilder.mapCodec((i) -> i.group(
			EFFECTS_FIELD.forGetter(FlowerBlock::getSuspiciousEffects),
			propertiesCodec()
	).apply(i, DoomBloomBlock::new));
	
	public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
	public static final int AGE_MAX = BlockStateProperties.MAX_AGE_4;
	protected static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 9.0, 12.0);
	protected static final double GROW_CHANCE = 0.2;
	
	public DoomBloomBlock(Holder<MobEffect> suspiciousStewEffect, int effectDuration, Properties settings) {
		this(makeEffectList(suspiciousStewEffect, effectDuration), settings);
	}
	
	public DoomBloomBlock(SuspiciousStewEffects stewEffects, Properties settings) {
		super(stewEffects, settings);
	}

	@Override
	public MapCodec<? extends DoomBloomBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(AGE);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return floor.is(SpectrumBlockTags.DOOMBLOOM_PLANTABLE);
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return state.getValue(AGE) < BlockStateProperties.MAX_AGE_4;
	}
	
	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (random.nextFloat() > GROW_CHANCE) {
			performBonemeal(world, random, pos, state);
		}
	}
	
	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		if (canSurvive(state, world, pos)) {
			int age = state.getValue(AGE);
			if (age < BlockStateProperties.MAX_AGE_4) {
				world.setBlockAndUpdate(pos, state.setValue(AGE, age + 1));
				world.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
			}
		} else {
			Block.updateOrDestroy(state, Blocks.AIR.defaultBlockState(), world, pos, 10, 512);
		}
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		if (state.getValue(AGE) == AGE_MAX) {
			int r = random.nextInt(100);
			if (r < 16) {
				double posX = (double) pos.getX() + 0.25D + random.nextDouble() * 0.5D;
				double posY = (double) pos.getY() + random.nextDouble() * 0.5D;
				double posZ = (double) pos.getZ() + 0.25D + random.nextDouble() * 0.5D;
				world.addParticle(ParticleTypes.LAVA, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
				if (r < 2) {
					world.playLocalSound(posX, posY, posZ, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
				}
			}
			if (random.nextInt(100) == 0) {
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
			}
		}
	}

	@Override
	public boolean dropFromExplosion(Explosion explosion) {
		return false;
	}
	
	@Override
	protected void onExplosionHit(BlockState state, Level world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
		explode(world, pos, state);
	}
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (!world.isClientSide()) {
			var velocity = entity.getDeltaMovement().length();
			if (velocity > 0.235 && world.random.nextInt(20) <= velocity * 20 || entity.isOnFire()) {
				explode(world, pos, state);
			}
		}
	}
	
	@Override
	public void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
		super.onProjectileHit(world, state, hit, projectile);
		explode(world, hit.getBlockPos(), state);
	}
	
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		super.neighborChanged(state, world, pos, block, fromPos, notify);
		if (world.random.nextInt(10) == 0) {
			explode(world, pos, state);
		}
	}
	
	// does not run in creative
	// => creative players can easily break it without causing an explosion
	@Override
	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		super.playerDestroy(world, player, pos, state, blockEntity, stack);
		if (SpectrumEnchantmentHelper.getLevel(world.registryAccess(), Enchantments.SILK_TOUCH, stack) == 0 && !stack.is(ConventionalItemTags.SHEAR_TOOLS)) {
			explode(world, pos, state);
		}
	}
	
	protected static void explode(Level world, BlockPos pos, BlockState state) {
		if (state.getValue(AGE) == AGE_MAX) {
			world.removeBlock(pos, false);
			world.explode(null, SpectrumDamageTypes.incandescence(world), new ExplosionDamageCalculator(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.0F, true, Level.ExplosionInteraction.BLOCK);
			if (!world.isClientSide) {
				popResource(world, pos, new ItemStack(SpectrumItems.DOOMBLOOM_SEED, world.random.nextIntBetweenInclusive(3, 7)));
			}
		}
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		int age = state.getValue(AGE);
		if (age == AGE_MAX) {
			if (world.isClientSide) {
				return InteractionResult.SUCCESS;
			} else {
				world.setBlockAndUpdate(pos, state.setValue(AGE, 0));
				int randomCount = world.random.nextIntBetweenInclusive(2, 3);
				player.getInventory().placeItemBackInInventory(new ItemStack(Items.GUNPOWDER, randomCount));
				world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.9F + world.random.nextFloat() * 0.2F);
				return InteractionResult.CONSUME;
			}
		}
		return InteractionResult.PASS;
	}

}
