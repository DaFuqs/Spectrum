package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumFluids;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Random;

public abstract class MudFluid extends SpectrumFluid {
	
	@Override
	public Fluid getStill() {
		return SpectrumFluids.MUD;
	}
	
	@Override
	public Fluid getFlowing() {
		return SpectrumFluids.FLOWING_MUD;
	}
	
	@Override
	public Item getBucketItem() {
		return SpectrumItems.MUD_BUCKET;
	}
	
	@Override
	protected BlockState toBlockState(FluidState fluidState) {
		return SpectrumBlocks.MUD.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
	}
	
	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == SpectrumFluids.MUD || fluid == SpectrumFluids.FLOWING_MUD;
	}
	
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
		if (random.nextInt(1000) == 0) {
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.MUD_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
		}
	}
	
	@Override
	protected int getFlowSpeed(WorldView worldView) {
		return 1;
	}
	
	@Override
	protected int getLevelDecreasePerBlock(WorldView worldView) {
		return 3;
	}
	
	@Override
	public int getTickRate(WorldView worldView) {
		return 50;
	}
	
	@Override
	public ParticleEffect getParticle() {
		return ParticleTypes.DRIPPING_WATER;
	}
	
	public static class FlowingMud extends MudFluid {
		
		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}
		
		@Override
		public int getLevel(FluidState fluidState) {
			return fluidState.get(LEVEL);
		}
		
		@Override
		public boolean isStill(FluidState fluidState) {
			return false;
		}
		
	}
	
	public static class StillMud extends MudFluid {
		
		@Override
		public int getLevel(FluidState fluidState) {
			return 8;
		}
		
		@Override
		public boolean isStill(FluidState fluidState) {
			return true;
		}
		
	}
}