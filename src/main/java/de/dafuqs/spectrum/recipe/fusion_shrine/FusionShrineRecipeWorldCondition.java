package de.dafuqs.spectrum.recipe.fusion_shrine;

import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.world.*;
import net.minecraft.world.dimension.*;

public enum FusionShrineRecipeWorldCondition {
	DAY,
	NOON,
	NIGHT,
	SUNRISE,
	SUNSET,
	MIDNIGHT,
	FULL_MOON,
	NEW_MOON,
	CLEAR_SKY,
	RAIN,
	THUNDER,
	NOT_THUNDER,
	IS_OVERWORLD,
	IS_NETHER,
	IS_END,
	IS_DEEPER_DOWN;

	public boolean isMetCurrently(World world) {
		switch (this) {
			case DAY -> {
				return TimeHelper.getTimeOfDay(world).isDay();
			}
			case NOON -> {
				return TimeHelper.getTimeOfDay(world) == TimeHelper.TimeOfDay.NOON;
			}
			case NIGHT -> {
				return TimeHelper.getTimeOfDay(world).isNight();
			}
			case SUNRISE -> {
				return TimeHelper.getTimeOfDay(world) == TimeHelper.TimeOfDay.SUNRISE;
			}
			case SUNSET -> {
				return TimeHelper.getTimeOfDay(world) == TimeHelper.TimeOfDay.SUNSET;
			}
			case MIDNIGHT -> {
				return TimeHelper.getTimeOfDay(world) == TimeHelper.TimeOfDay.MIDNIGHT;
			}
			case FULL_MOON -> {
				return world.getMoonPhase() == 0;
			}
			case NEW_MOON -> {
				return world.getMoonPhase() == 4;
			}
			case CLEAR_SKY -> {
				return !world.isRaining();
			}
			case RAIN -> {
				return world.isRaining();
			}
			case THUNDER -> {
				return world.isThundering();
			}
			case NOT_THUNDER -> {
				return !world.isThundering();
			}
			case IS_OVERWORLD -> {
				return world.getRegistryKey().getValue().equals(DimensionTypes.OVERWORLD_ID);
			}
			case IS_NETHER -> {
				return world.getRegistryKey().getValue().equals(DimensionTypes.THE_NETHER_ID);
			}
			case IS_END -> {
				return world.getRegistryKey().getValue().equals(DimensionTypes.THE_END_ID);
			}
			case IS_DEEPER_DOWN -> {
				return world.getRegistryKey().equals(DDDimension.DEEPER_DOWN_DIMENSION_KEY);
			}
		}
		return false;
	}
	
	
}
