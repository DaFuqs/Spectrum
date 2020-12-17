package de.dafuqs.spectrum.fluid;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import de.dafuqs.spectrum.items.SpectrumItems;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;

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
		// method_15741 converts the LEVEL_1_8 of the fluid state to the LEVEL_15 the fluid block uses
		return SpectrumBlocks.LIQUID_CRYSTAL.getDefaultState().with(Properties.LEVEL_15, method_15741(fluidState));
	}

	public static class FlowingLiquidCrystal extends LiquidCrystalFluid {

		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder)
		{
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