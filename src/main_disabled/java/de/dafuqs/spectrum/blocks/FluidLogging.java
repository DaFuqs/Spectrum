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
import org.jetbrains.annotations.*;

import java.util.*;

public class FluidLogging {
	
	public enum State implements StringRepresentable {
		NOT_LOGGED("none", 0),
		WATER("water", 0),
		LIQUID_CRYSTAL("liquid_crystal", LiquidCrystalFluidBlock.LUMINANCE);
		
		private final String name;
		private final int luminance;
		
		State(String name, int luminance) {
			this.name = name;
			this.luminance = luminance;
		}
		
		@Override
		public String getSerializedName() {
			return this.name;
		}
		
		public FluidState getFluidState() {
			switch (this) {
				case LIQUID_CRYSTAL -> {
					return SpectrumFluids.LIQUID_CRYSTAL.getSource(false);
				}
				case WATER -> {
					return Fluids.WATER.getSource(false);
				}
				default -> {
					return Fluids.EMPTY.defaultFluidState();
				}
			}
		}
		
		public static State getForFluidState(FluidState fluidState) {
			if (fluidState.getType() == SpectrumFluids.LIQUID_CRYSTAL) {
				return LIQUID_CRYSTAL;
			} else if (fluidState.is(FluidTags.WATER)) {
				return WATER;
			}
			
			return NOT_LOGGED;
		}
		
		public int getLuminance() {
			return luminance;
		}
		
		public boolean isOf(Fluid fluid) {
			return this.getFluidState().is(fluid);
		}
		
		public boolean isIn(TagKey<Fluid> fluidTag) {
			return this.getFluidState().is(fluidTag);
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
		public void onEntityCollision(BlockState state, Level world, BlockPos pos, Entity entity) {
			if (this == State.LIQUID_CRYSTAL) {
				SpectrumFluids.LIQUID_CRYSTAL.onEntityCollision(state, world, pos, entity);
			}
		}
	}
	
	public static final EnumProperty<State> ANY_INCLUDING_NONE = EnumProperty.create("fluid_logged", State.class);
	public static final EnumProperty<State> ANY_EXCLUDING_NONE = EnumProperty.create("fluid_logged", State.class, State.WATER, State.LIQUID_CRYSTAL);
	public static final EnumProperty<State> NONE_AND_CRYSTAL = EnumProperty.create("fluid_logged", State.class, State.NOT_LOGGED, State.LIQUID_CRYSTAL);
	
	public interface SpectrumFluidLoggable extends SpectrumFluidDrainable, SpectrumFluidFillable {
	
	}
	
	public interface SpectrumFluidFillable extends LiquidBlockContainer {
		
		@Override
		default boolean canPlaceLiquid(@Nullable Player player, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
			return state.getValue(ANY_INCLUDING_NONE) == State.NOT_LOGGED && (fluid == Fluids.WATER || fluid == SpectrumFluids.LIQUID_CRYSTAL);
		}
		
		@Override
		default boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
			if (state.getValue(ANY_INCLUDING_NONE) == State.NOT_LOGGED) {
				if (!world.isClientSide()) {
					if (fluidState.getType() == Fluids.WATER) {
						world.setBlock(pos, state.setValue(ANY_INCLUDING_NONE, State.WATER), Block.UPDATE_ALL);
						world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
					} else if (fluidState.getType() == SpectrumFluids.LIQUID_CRYSTAL) {
						world.setBlock(pos, state.setValue(ANY_INCLUDING_NONE, State.LIQUID_CRYSTAL), Block.UPDATE_ALL);
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
		
		@Override
		default ItemStack pickupBlock(@Nullable Player player, LevelAccessor world, BlockPos pos, BlockState state) {
			State fluidLog = state.getValue(ANY_INCLUDING_NONE);
			
			if (fluidLog == State.WATER) {
				world.setBlock(pos, state.setValue(ANY_INCLUDING_NONE, State.NOT_LOGGED), Block.UPDATE_ALL);
				if (!state.canSurvive(world, pos)) {
					world.destroyBlock(pos, true);
				}
				return new ItemStack(Items.WATER_BUCKET);
			} else if (fluidLog == State.LIQUID_CRYSTAL) {
				world.setBlock(pos, state.setValue(ANY_INCLUDING_NONE, State.NOT_LOGGED), Block.UPDATE_ALL);
				if (!state.canSurvive(world, pos)) {
					world.destroyBlock(pos, true);
				}
				return new ItemStack(SpectrumItems.LIQUID_CRYSTAL_BUCKET);
			}
			
			return ItemStack.EMPTY;
		}
		
		@Override
		default Optional<SoundEvent> getPickupSound() {
			return Fluids.WATER.getPickupSound();
		}
		
	}
	
}
