package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MidnightSolutionFluidBlock extends FluidBlock {

	public MidnightSolutionFluidBlock(FlowableFluid fluid, Settings settings) {
		super(fluid, settings);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.createAndScheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		if(entity instanceof LivingEntity livingEntity) {
			
			if(!livingEntity.isDead()) {
				if (livingEntity.isSubmergedIn(SpectrumFluidTags.MIDNIGHT_SOLUTION) && world.getTime() % 20 == 0) {
					livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0));
					livingEntity.damage(SpectrumDamageSources.MIDNIGHT_SOLUTION, 2);
				} else {
					livingEntity.damage(SpectrumDamageSources.MIDNIGHT_SOLUTION, 1);
				}
				if (livingEntity.isDead()) {
					livingEntity.dropStack(SpectrumItems.MIDNIGHT_CHIP.getDefaultStack());
				}
			}
		}
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if(!world.getBlockState(pos.up()).isSolidBlock(world, pos.up()) && random.nextFloat() < 0.03F) {
			world.addParticle(SpectrumParticleTypes.VOID_FOG, pos.getX() + random.nextDouble(), pos.getY()+1, pos.getZ() + random.nextDouble(), 0, random.nextDouble() * 0.1, 0);
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		world.createAndScheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	/*public static boolean tryConvertNeighbor(@NotNull World world, BlockPos pos, BlockPos fromPos) {
		FluidState fluidState = world.getFluidState(fromPos);
		if (!fluidState.isEmpty() && !fluidState.isIn(SpectrumFluidTags.MIDNIGHT_SOLUTION)) {
			world.setBlockState(fromPos, SpectrumBlocks.MIDNIGHT_SOLUTION.getDefaultState());
			playExtinguishSound(world, fromPos);
			return true;
		}
		return false;
	}*/

	private static void playExtinguishSound(@NotNull WorldAccess world, BlockPos pos) {
		world.syncWorldEvent(1501, pos, 0);
	}

}
