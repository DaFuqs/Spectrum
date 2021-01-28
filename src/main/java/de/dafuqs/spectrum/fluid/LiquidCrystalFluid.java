package de.dafuqs.spectrum.fluid;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import de.dafuqs.spectrum.items.SpectrumItems;
import de.dafuqs.spectrum.sounds.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public abstract class LiquidCrystalFluid extends SpectrumFluid {

	@Override
	public Fluid getStill() {
		return SpectrumFluids.STILL_LIQUID_CRYSTAL;
	}

	@Override
	public Fluid getFlowing() {
		return SpectrumFluids.FLOWING_LIQUID_CRYSTAL;
	}

	@Override
	public Item getBucketItem() {
		return SpectrumItems.LIQUID_CRYSTAL_BUCKET;
	}

	@Override
	protected BlockState toBlockState(FluidState fluidState) {
		return SpectrumBlocks.LIQUID_CRYSTAL.getDefaultState().with(Properties.LEVEL_15, method_15741(fluidState));
	}

	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == SpectrumFluids.STILL_LIQUID_CRYSTAL || fluid == SpectrumFluids.FLOWING_LIQUID_CRYSTAL;
	}

	public Optional<SoundEvent> getFillSound() {
		return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
	}

	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
		if (random.nextInt(150) == 0) {
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.LIQUID_CRYSTAL_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
		}
	}

	public static class FlowingLiquidCrystal extends LiquidCrystalFluid {

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

	public static class StillLiquidCrystal extends LiquidCrystalFluid {

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