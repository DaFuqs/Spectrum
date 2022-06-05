package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class LiquidCrystalFluidBlock extends FluidBlock {
	
	public LiquidCrystalFluidBlock(FlowableFluid fluid, Settings settings) {
		super(fluid, settings);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (this.receiveNeighborFluids(world, pos, state)) {
			world.createAndScheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
		}
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (this.receiveNeighborFluids(world, pos, state)) {
			world.createAndScheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
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
		if (entity instanceof LivingEntity livingEntity) {
			
			// just check every 100 ticks for performance and slow healing
			if (world.getTime() % 200 == 0) {
				StatusEffectInstance regenerationInstance = livingEntity.getStatusEffect(StatusEffects.REGENERATION);
				if (regenerationInstance == null) {
					StatusEffectInstance newRegenerationInstance = new StatusEffectInstance(StatusEffects.REGENERATION, 80);
					livingEntity.addStatusEffect(newRegenerationInstance);
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
					// blackstone in the nether, deepslate everywhere else
					if (world.getDimension().isUltrawarm()) {
						block = Blocks.BLACKSTONE;
					} else {
						block = Blocks.COBBLED_DEEPSLATE;
					}
				}
				world.setBlockState(pos, block.getDefaultState());
				this.playExtinguishSound(world, pos);
				return false;
			}
			if (world.getFluidState(blockPos).isIn(SpectrumFluidTags.MUD)) {
				Block block = Blocks.CLAY;
				world.setBlockState(pos, block.getDefaultState());
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
