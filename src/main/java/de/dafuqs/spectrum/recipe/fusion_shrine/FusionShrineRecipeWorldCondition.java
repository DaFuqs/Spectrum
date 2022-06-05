package de.dafuqs.spectrum.recipe.fusion_shrine;

import de.dafuqs.spectrum.helpers.TimeHelper;
import net.minecraft.world.World;

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
	NOT_THUNDER;
	
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
		}
		return false;
	}
	
	
}
