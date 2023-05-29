package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.state.property.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class FluidLogging {
	
	public enum State implements StringIdentifiable {
		NOT_LOGGED("none"),
		WATER("water"),
		LIQUID_CRYSTAL("liquid_crystal");
		
		private final String name;
		
		State(String name) {
			this.name = name;
		}
		
		@Override
		public String asString() {
			return this.name;
		}
		
		public FluidState getFluidState() {
			switch (this) {
				case LIQUID_CRYSTAL -> {
					return SpectrumFluids.LIQUID_CRYSTAL.getStill(false);
				}
				case WATER -> {
					return Fluids.WATER.getStill(false);
				}
				default -> {
					return Fluids.EMPTY.getDefaultState();
				}
			}
		}
		
		public boolean isOf(Fluid fluid) {
			return this.getFluidState().isOf(fluid);
		}
		
		public boolean isIn(TagKey<Fluid> fluidTag) {
			return this.getFluidState().isIn(fluidTag);
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	public static final EnumProperty<State> ANY_INCLUDING_NONE = EnumProperty.of("fluid_logged", State.class);
	public static final EnumProperty<State> ANY_EXCLUDING_NONE = EnumProperty.of("fluid_logged", State.class, State.WATER, State.LIQUID_CRYSTAL);
	public static final EnumProperty<State> NONE_AND_CRYSTAL = EnumProperty.of("fluid_logged", State.class, State.NOT_LOGGED, State.LIQUID_CRYSTAL);
	
	public interface SpectrumFluidLoggable extends SpectrumFluidDrainable, SpectrumFluidFillable {
	
	}
	
	public interface SpectrumFluidFillable extends FluidFillable {
		
		@Override
		default boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
			return state.get(ANY_INCLUDING_NONE) == State.NOT_LOGGED && (fluid == Fluids.WATER || fluid == SpectrumFluids.LIQUID_CRYSTAL);
		}
		
		@Override
		default boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
			if (state.get(ANY_INCLUDING_NONE) == State.NOT_LOGGED) {
				if (!world.isClient()) {
					if (fluidState.getFluid() == Fluids.WATER) {
						world.setBlockState(pos, state.with(ANY_INCLUDING_NONE, State.WATER), Block.NOTIFY_ALL);
						world.scheduleFluidTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
					} else if (fluidState.getFluid() == SpectrumFluids.LIQUID_CRYSTAL) {
						world.setBlockState(pos, state.with(ANY_INCLUDING_NONE, State.LIQUID_CRYSTAL), Block.NOTIFY_ALL);
						world.scheduleFluidTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
					}
				}
				
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	public interface SpectrumFluidDrainable extends FluidDrainable {
		
		@Override
		default ItemStack tryDrainFluid(WorldAccess world, BlockPos pos, BlockState state) {
			State fluidLog = state.get(ANY_INCLUDING_NONE);
			
			if (fluidLog == State.WATER) {
				world.setBlockState(pos, state.with(ANY_INCLUDING_NONE, State.NOT_LOGGED), Block.NOTIFY_ALL);
				if (!state.canPlaceAt(world, pos)) {
					world.breakBlock(pos, true);
				}
				return new ItemStack(Items.WATER_BUCKET);
			} else if (fluidLog == State.LIQUID_CRYSTAL) {
				world.setBlockState(pos, state.with(ANY_INCLUDING_NONE, State.NOT_LOGGED), Block.NOTIFY_ALL);
				if (!state.canPlaceAt(world, pos)) {
					world.breakBlock(pos, true);
				}
				return new ItemStack(SpectrumItems.LIQUID_CRYSTAL_BUCKET);
			}
			
			return ItemStack.EMPTY;
		}
		
		@Override
		default Optional<SoundEvent> getBucketFillSound() {
			return Fluids.WATER.getBucketFillSound();
		}
		
	}
	
}
