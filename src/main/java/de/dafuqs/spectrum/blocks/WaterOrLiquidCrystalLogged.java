package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.registries.SpectrumFluids;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.Optional;

public interface WaterOrLiquidCrystalLogged extends Waterloggable {
	
	IntProperty FLUIDLOGGED = IntProperty.of("water_or_crystal_logged", 0, 2); // 0= not logged; 1=waterlogged; 2=crystallogged
	
	default boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return state.get(FLUIDLOGGED) == 0 && (fluid == Fluids.WATER || fluid == SpectrumFluids.LIQUID_CRYSTAL);
	}
	
	default boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		if (state.get(FLUIDLOGGED) == 0) {
			if (!world.isClient()) {
				if (fluidState.getFluid() == Fluids.WATER) {
					world.setBlockState(pos, state.with(FLUIDLOGGED, 1), 3);
					world.createAndScheduleFluidTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
					
				} else if (fluidState.getFluid() == SpectrumFluids.LIQUID_CRYSTAL) {
					world.setBlockState(pos, state.with(FLUIDLOGGED, 2), 3);
					world.createAndScheduleFluidTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
				}
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	default ItemStack tryDrainFluid(WorldAccess world, BlockPos pos, BlockState state) {
		int fluidLog = state.get(FLUIDLOGGED);
		
		if (fluidLog == 1) {
			world.setBlockState(pos, state.with(FLUIDLOGGED, 0), 3);
			if (!state.canPlaceAt(world, pos)) {
				world.breakBlock(pos, true);
			}
			return new ItemStack(Items.WATER_BUCKET);
		} else if (fluidLog == 2) {
			world.setBlockState(pos, state.with(FLUIDLOGGED, 0), 3);
			if (!state.canPlaceAt(world, pos)) {
				world.breakBlock(pos, true);
			}
			return new ItemStack(SpectrumFluids.LIQUID_CRYSTAL.getBucketItem());
		} else {
			return ItemStack.EMPTY;
		}
	}
	
	default Optional<SoundEvent> getBucketFillSound() {
		return Fluids.WATER.getBucketFillSound();
	}
	
}
