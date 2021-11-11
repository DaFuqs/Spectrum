package de.dafuqs.spectrum.recipe.fusion_shrine;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public enum FusionShrineRecipeWorldCondition {
	DAY,
	NIGHT,
	SUNRISE,
	SUNSET,
	MIDNIGHT,
	FULL_MOON,
	NEW_MOON,
	CLEAR_SKY,
	RAIN,
	THUNDER;

	public boolean isMetCurrently(World world) {
		switch (this) {
			case DAY -> {
				return world.getTimeOfDay() % 24000 > 0 && world.getTimeOfDay() % 24000 < 12000;
			}
			case NIGHT -> {
				return world.getTimeOfDay() % 24000 >= 13000 && world.getTimeOfDay() % 24000 < 23000;
			}
			case SUNRISE -> {
				return world.getTimeOfDay() % 24000 >= 23000;
			}
			case SUNSET -> {
				return world.getTimeOfDay() % 24000 >= 12000 && world.getTimeOfDay() % 24000 < 13000;
			}
			case MIDNIGHT -> {
				return world.getTimeOfDay() % 24000 >= 18000 && world.getTimeOfDay() % 24000 < 19000;
			}
			case FULL_MOON -> {
				return world.getMoonPhase() == 0;
			}
			case NEW_MOON -> {
				return world.getMoonPhase() == 5;
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
		}
		return false;
	}


}
