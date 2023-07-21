package de.dafuqs.spectrum.predicate.entity;

import de.dafuqs.spectrum.predicate.block.BiomePredicate;
import de.dafuqs.spectrum.predicate.block.LightPredicate;
import de.dafuqs.spectrum.predicate.world.CommandPredicate;
import de.dafuqs.spectrum.predicate.world.DimensionPredicate;
import de.dafuqs.spectrum.predicate.world.MoonPhasePredicate;
import de.dafuqs.spectrum.predicate.world.TimeOfDayPredicate;
import de.dafuqs.spectrum.predicate.world.WeatherPredicate;
import net.minecraft.predicate.FluidPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class EntityFishingPredicate {
	private final FluidPredicate fluidPredicate;
	private final BiomePredicate biomePredicate;
	private final LightPredicate lightPredicate;
	private final DimensionPredicate dimensionPredicate;
	private final MoonPhasePredicate moonPhasePredicate;
	private final TimeOfDayPredicate timeOfDayPredicate;
	private final WeatherPredicate weatherPredicate;
	private final CommandPredicate commandPredicate;
	
	public EntityFishingPredicate(
		FluidPredicate fluidPredicate,
		BiomePredicate biomePredicate,
		LightPredicate lightPredicate,
		DimensionPredicate dimensionPredicate,
		MoonPhasePredicate moonPhasePredicate,
		TimeOfDayPredicate timeOfDayPredicate,
		WeatherPredicate weatherPredicate,
		CommandPredicate commandPredicate)
	{
		this.fluidPredicate = fluidPredicate;
		this.biomePredicate = biomePredicate;
		this.lightPredicate = lightPredicate;
		this.dimensionPredicate = dimensionPredicate;
		this.moonPhasePredicate = moonPhasePredicate;
		this.timeOfDayPredicate = timeOfDayPredicate;
		this.weatherPredicate = weatherPredicate;
		this.commandPredicate = commandPredicate;
	}
	
	public boolean test(ServerWorld world, BlockPos pos) {
		return (
			this.fluidPredicate.test(world, pos) &&
			this.biomePredicate.test(world, pos) &&
			this.lightPredicate.test(world, pos) &&
			this.dimensionPredicate.test(world, pos) &&
			this.moonPhasePredicate.test(world, pos) &&
			this.timeOfDayPredicate.test(world, pos) &&
			this.weatherPredicate.test(world, pos) &&
			this.commandPredicate.test(world, pos)
		);
	}
}
