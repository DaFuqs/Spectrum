package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.effect.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.registry.tag.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public class DragonrotFluidBlock extends SpectrumFluidBlock {

	public DragonrotFluidBlock(FlowableFluid fluid, Settings settings) {
		super(fluid, settings);
	}

	@Override
	public DefaultParticleType getSplashParticle() {
		return SpectrumParticleTypes.DRAGONROT;
	}

	@Override
	public Pair<DefaultParticleType, DefaultParticleType> getFishingParticles() {
		return new Pair<>(SpectrumParticleTypes.DRAGONROT, SpectrumParticleTypes.DRAGONROT_FISHING);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (this.receiveNeighborFluids(world, pos, state)) {
			world.scheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (!world.isClient) {
			if (entity instanceof LivingEntity livingEntity) {
				// just check every 20 ticks for performance
				if (!livingEntity.isDead() && world.getTime() % 20 == 0) {
					if (livingEntity.isSubmergedIn(SpectrumFluidTags.DRAGONROT)) {
						livingEntity.damage(SpectrumDamageSources.DRAGONROT, 6);
					} else {
						livingEntity.damage(SpectrumDamageSources.DRAGONROT, 3);
					}
					if (!livingEntity.isDead()) {
						StatusEffectInstance existingEffect = livingEntity.getStatusEffect(SpectrumStatusEffects.LIFE_DRAIN);
						if (existingEffect == null || existingEffect.getDuration() < 1000) {
							livingEntity.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.LIFE_DRAIN, 2000, 0));
						}
						existingEffect = livingEntity.getStatusEffect(SpectrumStatusEffects.DEADLY_POISON);
						if (existingEffect == null || existingEffect.getDuration() < 80) {
							livingEntity.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.DEADLY_POISON, 160, 0));
						}
					}
				}
			} else if (entity instanceof ItemEntity itemEntity && !itemEntity.cannotPickup()) {
				if (world.random.nextInt(200) == 0) {
					ItemStack itemStack = itemEntity.getStack();
					DragonrotConvertingRecipe recipe = getConversionRecipeFor(SpectrumRecipeTypes.DRAGONROT_CONVERTING, world, itemStack);
					if (recipe != null) {
						world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.NEUTRAL, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
						MultiblockCrafter.spawnItemStackAsEntitySplitViaMaxCount(world, itemEntity.getPos(), recipe.getOutput(), recipe.getOutput().getCount() * itemStack.getCount(), Vec3d.ZERO);
						itemEntity.discard();
					}
				}
			}
		}
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (!world.getBlockState(pos.up()).isSolidBlock(world, pos.up()) && random.nextFloat() < 0.03F) {
			world.addParticle(SpectrumParticleTypes.DRAGONROT, pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 0, random.nextDouble() * 0.1, 0);
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (this.receiveNeighborFluids(world, pos, state)) {
			world.scheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
		}
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	/**
	 * @param world The world
	 * @param pos   The position in the world
	 * @param state BlockState of the mud. Included the height/fluid level
	 * @return Dunno, actually. I just mod things.
	 */
	private boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state) {
		for (Direction direction : Direction.values()) {
			BlockPos blockPos = pos.offset(direction);
			if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
				world.setBlockState(pos, Blocks.ANDESITE.getDefaultState());
				this.playExtinguishSound(world, pos);
				return false;
			} else if (world.getFluidState(blockPos).isIn(FluidTags.LAVA)) {
				world.setBlockState(pos, Blocks.BLACKSTONE.getDefaultState());
				this.playExtinguishSound(world, pos);
				return false;
			} else if (world.getFluidState(blockPos).isIn(SpectrumFluidTags.MUD)) {
				world.setBlockState(pos, Blocks.MUD.getDefaultState());
				this.playExtinguishSound(world, pos);
				return false;
			} else if (world.getFluidState(blockPos).isIn(SpectrumFluidTags.LIQUID_CRYSTAL)) {
				world.setBlockState(pos, Blocks.DIORITE.getDefaultState());
				this.playExtinguishSound(world, pos);
				return false;
			}
		}
		return true;
	}

	private void playExtinguishSound(WorldAccess world, BlockPos pos) {
		world.syncWorldEvent(1501, pos, 0);
	}

}
