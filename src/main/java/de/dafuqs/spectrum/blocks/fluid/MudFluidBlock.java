package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
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

public class MudFluidBlock extends FluidBlock {
	
	public MudFluidBlock(FlowableFluid fluid, Settings settings) {
		super(fluid, settings);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (this.receiveNeighborFluids(world, pos, state)) {
			world.createAndScheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
		}
	}
	
	/**
	 * Entities colliding with mud will get a slowness effect
	 * and losing their breath far quicker
	 */
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		if (entity instanceof LivingEntity livingEntity) {
			
			// the entity is hurt at air == -20 and then reset to air = 0
			// this way the entity loses its breath way faster, but gets damaged just as slow afterwards
			if (livingEntity.isSubmergedIn(SpectrumFluidTags.MUD) && world.getTime() % 2 == 0 && livingEntity.getAir() > 0) {
				livingEntity.setAir(livingEntity.getAir() - 1);
			}
			
			// just check every 20 ticks for performance
			if (world.getTime() % 20 == 0) {
				StatusEffectInstance slownessInstance = livingEntity.getStatusEffect(StatusEffects.SLOWNESS);
				if (slownessInstance == null || slownessInstance.getDuration() < 20) {
					StatusEffectInstance newSlownessInstance = new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 3);
					livingEntity.addStatusEffect(newSlownessInstance);
				}
			}
		}
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (!world.getBlockState(pos.up()).isSolidBlock(world, pos.up()) && random.nextFloat() < 0.03F) {
			world.addParticle(SpectrumParticleTypes.MUD_POP, pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 0, random.nextDouble() * 0.1, 0);
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
	 * @param world The world
	 * @param pos   The position in the world
	 * @param state BlockState of the mud. Included the height/fluid level
	 * @return Dunno, actually. I just mod things.
	 */
	private boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state) {
		for (Direction direction : Direction.values()) {
			BlockPos blockPos = pos.offset(direction);
			if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
				world.setBlockState(pos, Blocks.DIRT.getDefaultState());
				this.playExtinguishSound(world, pos);
				return false;
			}
			if (world.getFluidState(blockPos).isIn(FluidTags.LAVA)) {
				world.setBlockState(pos, Blocks.COARSE_DIRT.getDefaultState());
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
