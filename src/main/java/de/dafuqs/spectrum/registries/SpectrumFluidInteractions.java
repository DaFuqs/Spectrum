package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.blocks.fluid.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.fluids.*;

public class SpectrumFluidInteractions {
	
	public static void register() {
		// LIQUID CRYSTAL
		FluidInteractionRegistry.addInteraction(SpectrumFluids.LIQUID_CRYSTAL_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
						NeoForgeMod.WATER_TYPE.value(),
						(fluidState) -> fluidState.isSource() ? SpectrumBlocks.FROSTBITE_CRYSTAL.get().defaultBlockState() : Blocks.CLAY.defaultBlockState()));
		FluidInteractionRegistry.addInteraction(NeoForgeMod.WATER_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
						SpectrumFluids.LIQUID_CRYSTAL_TYPE.get(),
						Blocks.CLAY.defaultBlockState()));
		
		FluidInteractionRegistry.addInteraction(SpectrumFluids.LIQUID_CRYSTAL_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
				NeoForgeMod.LAVA_TYPE.value(),
				(fluidState) -> fluidState.isSource() ? SpectrumBlocks.BLAZING_CRYSTAL.get().defaultBlockState() : Blocks.CALCITE.defaultBlockState()));
		FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
						SpectrumFluids.LIQUID_CRYSTAL_TYPE.get(),
						Blocks.CALCITE.defaultBlockState()));
		
		
		// MIDNIGHT SOLUTION
		FluidInteractionRegistry.addInteraction(SpectrumFluids.MIDNIGHT_SOLUTION_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				NeoForgeMod.WATER_TYPE.value(),
				MidnightSolutionFluidBlock.SPREAD_BLOCKSTATE));
		FluidInteractionRegistry.addInteraction(NeoForgeMod.WATER_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				SpectrumFluids.MIDNIGHT_SOLUTION_TYPE.value(),
				MidnightSolutionFluidBlock.SPREAD_BLOCKSTATE));
		
		FluidInteractionRegistry.addInteraction(SpectrumFluids.MIDNIGHT_SOLUTION_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				NeoForgeMod.LAVA_TYPE.value(),
				Blocks.BLACKSTONE.defaultBlockState()));
		FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				SpectrumFluids.MIDNIGHT_SOLUTION_TYPE.get(),
				Blocks.BLACKSTONE.defaultBlockState()));
		
		FluidInteractionRegistry.addInteraction(SpectrumFluids.MIDNIGHT_SOLUTION_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				(level, currentPos, relativePos, currentState) -> MidnightSolutionFluid.canBeTakenOver(level, relativePos),
				(level, currentPos, relativePos, currentState) -> {
					level.setBlockAndUpdate(currentPos, EventHooks.fireFluidPlaceBlockEvent(level, currentPos, currentPos, SpectrumBlocks.MIDNIGHT_SOLUTION.get().defaultBlockState()));
					level.scheduleTick(currentPos, SpectrumFluids.MIDNIGHT_SOLUTION.get(), SpectrumFluids.MIDNIGHT_SOLUTION.get().getTickDelay(level));
					level.levelEvent(LevelEvent.LAVA_FIZZ, currentPos, 0);
				}
		));
		
		
		// DRAGONROT
		FluidInteractionRegistry.addInteraction(SpectrumFluids.DRAGONROT_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				NeoForgeMod.LAVA_TYPE.value(),
				Blocks.TERRACOTTA.defaultBlockState()));
		FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				SpectrumFluids.DRAGONROT_TYPE.get(),
				Blocks.TERRACOTTA.defaultBlockState()));
		
		FluidInteractionRegistry.addInteraction(SpectrumFluids.DRAGONROT_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				NeoForgeMod.WATER_TYPE.value(),
				SpectrumBlocks.SLUSH.get().defaultBlockState()));
		FluidInteractionRegistry.addInteraction(NeoForgeMod.WATER_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				SpectrumFluids.DRAGONROT_TYPE.get(),
				SpectrumBlocks.SLUSH.get().defaultBlockState()));
		
		FluidInteractionRegistry.addInteraction(SpectrumFluids.DRAGONROT_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				SpectrumFluids.LIQUID_CRYSTAL_TYPE.value(),
				Blocks.MUD.defaultBlockState()));
		FluidInteractionRegistry.addInteraction(SpectrumFluids.LIQUID_CRYSTAL_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				SpectrumFluids.DRAGONROT_TYPE.get(),
				Blocks.MUD.defaultBlockState()));
		
		FluidInteractionRegistry.addInteraction(SpectrumFluids.DRAGONROT_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				SpectrumFluids.MIDNIGHT_SOLUTION_TYPE.value(),
				SpectrumBlocks.ROTTEN_GROUND.get().defaultBlockState()));
		FluidInteractionRegistry.addInteraction(SpectrumFluids.MIDNIGHT_SOLUTION_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
				SpectrumFluids.DRAGONROT_TYPE.get(),
				SpectrumBlocks.ROTTEN_GROUND.get().defaultBlockState()));
	}
	
}
