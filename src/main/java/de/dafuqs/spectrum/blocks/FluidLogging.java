package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.*;

public class FluidLogging {
	
	public enum State implements StringRepresentable {
		NOT_LOGGED("none", () -> Fluids.EMPTY, 0),
		WATER("water", () -> Fluids.WATER, 0),
		LAVA("lava", () -> Fluids.LAVA, 15),
		LIQUID_CRYSTAL("liquid_crystal", SpectrumFluids.LIQUID_CRYSTAL::get, SpectrumFluids.LIQUID_CRYSTAL_LIGHT_LEVEL),
		MIDNIGHT_SOLUTION("midnight_solution", SpectrumFluids.MIDNIGHT_SOLUTION::get, SpectrumFluids.MIDNIGHT_SOLUTION_LIGHT_LEVEL),
		DRAGONROT("dragonrot", SpectrumFluids.DRAGONROT::get, SpectrumFluids.DRAGONROT_LIGHT_LEVEL);
		
		private final String name;
		private final Supplier<Fluid> fluid;
		private final int luminance;
		
		State(String name, Supplier<Fluid> fluid, int luminance) {
			this.name = name;
			this.fluid = fluid;
			this.luminance = luminance;
		}
		
		@Override
		public String getSerializedName() {
			return this.name;
		}
		
		public FluidState getFluidState() {
			return this.fluid.get().defaultFluidState();
		}
		
		public int getLuminance() {
			return luminance;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
		public void onEntityCollision(BlockState state, Level world, BlockPos pos, Entity entity) {
			if(this.fluid.get() instanceof SpectrumFluid spectrumFluid) {
				spectrumFluid.onEntityCollision(state, world, pos, entity);
			}
		}
		
		public void updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
			Fluid fluid = this.fluid.get();
			if(fluid == Fluids.EMPTY) {
				return;
			}
			world.scheduleTick(pos, fluid, fluid.getTickDelay(world));
		}
		
		public FluidLogging.State getStateForPos(Level world, BlockPos blockPos) {
			FluidState fluidState = world.getFluidState(blockPos);
			if (fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8) {
				return FluidLogging.State.WATER;
			} else if (fluidState.getType().equals(Fluids.LAVA.getSource())) {
				return FluidLogging.State.LAVA;
			} else if (fluidState.getType().equals(SpectrumFluids.LIQUID_CRYSTAL.get().getSource())) {
				return FluidLogging.State.LIQUID_CRYSTAL;
			} else if (fluidState.getType().equals(SpectrumFluids.MIDNIGHT_SOLUTION.get().getSource())) {
				return FluidLogging.State.MIDNIGHT_SOLUTION;
			} else if (fluidState.getType().equals(SpectrumFluids.DRAGONROT.get().getSource())) {
				return FluidLogging.State.DRAGONROT;
			}
			return State.NOT_LOGGED;
		}
	}
	
	// public static final EnumProperty<State> ANY = EnumProperty.create("fluid_logged", State.class);
	public static final EnumProperty<State> AIR_WATER_LIQUID_CRYSTAL_DRAGONROT = EnumProperty.create("fluid_logged", State.class, State.NOT_LOGGED, State.WATER, State.LIQUID_CRYSTAL, State.DRAGONROT);
	public static final EnumProperty<State> WATER_LIQUID_CRYSTAL = EnumProperty.create("fluid_logged", State.class, State.WATER, State.LIQUID_CRYSTAL);
	
	public interface SpectrumFluidLoggable extends SpectrumFluidDrainable, SpectrumFluidFillable {
	
	}
	
	public interface SpectrumFluidFillable extends LiquidBlockContainer {
		
		EnumProperty<State> getFillableFluids();
		
		@Override
		default boolean canPlaceLiquid(@Nullable Player player, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
			return state.getValue(getFillableFluids()) == State.NOT_LOGGED
					&& (fluid == Fluids.WATER || fluid == Fluids.LAVA || fluid == SpectrumFluids.LIQUID_CRYSTAL.get() || fluid == SpectrumFluids.MIDNIGHT_SOLUTION.get() || fluid == SpectrumFluids.DRAGONROT.get());
		}
		
		@Override
		default boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
			if (state.getValue(getFillableFluids()) == State.NOT_LOGGED) {
				if (!world.isClientSide()) {
					if (fluidState.getType() == Fluids.WATER) {
						world.setBlock(pos, state.setValue(getFillableFluids(), State.WATER), Block.UPDATE_ALL);
						world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
					} else if (fluidState.getType() == Fluids.LAVA) {
						world.setBlock(pos, state.setValue(getFillableFluids(), State.LAVA), Block.UPDATE_ALL);
						world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
					} else if (fluidState.getType() == SpectrumFluids.LIQUID_CRYSTAL.get()) {
						world.setBlock(pos, state.setValue(getFillableFluids(), State.LIQUID_CRYSTAL), Block.UPDATE_ALL);
						world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
					} else if (fluidState.getType() == SpectrumFluids.MIDNIGHT_SOLUTION.get()) {
						world.setBlock(pos, state.setValue(getFillableFluids(), State.MIDNIGHT_SOLUTION), Block.UPDATE_ALL);
						world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
					} else if (fluidState.getType() == SpectrumFluids.DRAGONROT.get()) {
						world.setBlock(pos, state.setValue(getFillableFluids(), State.DRAGONROT), Block.UPDATE_ALL);
						world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
					}
				}
				
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	public interface SpectrumFluidDrainable extends BucketPickup {
		
		EnumProperty<State> getDrainableFluids();
		
		@Override
		default ItemStack pickupBlock(@Nullable Player player, LevelAccessor world, BlockPos pos, BlockState state) {
			State fluidLog = state.getValue(getDrainableFluids());
			
			Fluid fluid = fluidLog.fluid.get();
			if(fluid == Fluids.EMPTY) {
				return ItemStack.EMPTY;
			}
			if(!getDrainableFluids().getPossibleValues().contains(State.NOT_LOGGED)) {
				return ItemStack.EMPTY;
			}
			
			world.setBlock(pos, state.setValue(getDrainableFluids(), State.NOT_LOGGED), Block.UPDATE_ALL);
			if (!state.canSurvive(world, pos)) {
				world.destroyBlock(pos, true);
			}
			return new ItemStack(fluid.getBucket());
		}
		
		@Override
		default Optional<SoundEvent> getPickupSound() {
			return Fluids.WATER.getPickupSound();
		}
		
	}
	
}
