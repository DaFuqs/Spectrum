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
import net.minecraft.sound.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public class LiquidCrystalFluidBlock extends SpectrumFluidBlock {
	
	public static final int LUMINANCE = 11;
	
	public LiquidCrystalFluidBlock(FlowableFluid fluid, Settings settings) {
		super(fluid, settings);
	}
	
	@Override
	public DefaultParticleType getSplashParticle() {
		return SpectrumParticleTypes.LIQUID_CRYSTAL_FISHING;
	}
	
	@Override
	public Pair<DefaultParticleType, DefaultParticleType> getFishingParticles() {
		return new Pair<>(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, SpectrumParticleTypes.LIQUID_CRYSTAL_FISHING);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (this.receiveNeighborFluids(world, pos, state)) {
			world.scheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
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
		return true;
	}
	
	/**
	 * Entities colliding with liquid crystal will get a slight regeneration effect
	 */
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (!world.isClient) {
			if (entity instanceof LivingEntity livingEntity) {
				// just check every x ticks for performance and slow healing
				if (world.getTime() % 200 == 0) {
					StatusEffectInstance regenerationInstance = livingEntity.getStatusEffect(StatusEffects.REGENERATION);
					if (regenerationInstance == null) {
						StatusEffectInstance newRegenerationInstance = new StatusEffectInstance(StatusEffects.REGENERATION, 80);
						livingEntity.addStatusEffect(newRegenerationInstance);
					}
				}
			} else if (entity instanceof ItemEntity itemEntity && !itemEntity.cannotPickup()) {
				if (world.random.nextInt(200) == 0) {
					ItemStack itemStack = itemEntity.getStack();
					LiquidCrystalConvertingRecipe recipe = getConversionRecipeFor(SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING, world, itemStack);
					if (recipe != null) {
						world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.NEUTRAL, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
						MultiblockCrafter.spawnItemStackAsEntitySplitViaMaxCount(world, itemEntity.getPos(), recipe.getOutput(world.getRegistryManager()), recipe.getOutput(world.getRegistryManager()).getCount() * itemStack.getCount(), Vec3d.ZERO);
						itemEntity.discard();
					}
				}
			}
		}
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (random.nextFloat() < 0.10F) {
			world.addParticle(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0, random.nextDouble() * 0.1, 0);
		}
	}
	
	/**
	 * @param world The world
	 * @param pos   The position in the world
	 * @param state BlockState of the liquid crystal. Included the height/fluid level
	 * @return Dunno, actually. I just mod things.
	 */
	private boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state) {
		for (Direction direction : Direction.values()) {
			BlockPos blockPos = pos.offset(direction);
			if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
				Block block = world.getFluidState(pos).isStill() ? SpectrumBlocks.FROSTBITE_CRYSTAL : Blocks.CALCITE;
				world.setBlockState(pos, block.getDefaultState());
				this.playExtinguishSound(world, pos);
				return false;
			}
			if (world.getFluidState(blockPos).isIn(FluidTags.LAVA)) {
				Block block;
				if (world.getFluidState(pos).isStill()) {
					block = SpectrumBlocks.BLAZING_CRYSTAL;
				} else {
					block = Blocks.COBBLED_DEEPSLATE;
				}
				world.setBlockState(pos, block.getDefaultState());
				this.playExtinguishSound(world, pos);
				return false;
			}
			if (world.getFluidState(blockPos).isIn(SpectrumFluidTags.MUD)) {
				world.setBlockState(pos, Blocks.CLAY.getDefaultState());
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
