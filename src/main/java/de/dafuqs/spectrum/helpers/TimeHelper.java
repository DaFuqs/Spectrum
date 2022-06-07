package de.dafuqs.spectrum.helpers;

import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TimeHelper {
	
	public static TimeOfDay getTimeOfDay(@NotNull World world) {
		return getTimeOfDay(world.getTimeOfDay());
	}
	
	public static TimeOfDay getTimeOfDay(long timeOfDay) {
		long timeMod = timeOfDay % 24000;
		if (timeMod >= 6000 && timeMod < 7000) {
			return TimeOfDay.NOON;
		} else if (timeMod >= 0 && timeMod < 12000) {
			return TimeOfDay.DAY;
		} else if (timeMod >= 12000 && timeMod < 13000) {
			return TimeOfDay.SUNSET;
		} else if (timeMod >= 23000) {
			return TimeOfDay.SUNRISE;
		} else if (timeMod >= 18000 && timeMod < 19000) {
			return TimeOfDay.MIDNIGHT;
		} else {
			return TimeOfDay.NIGHT;
		}
	}
	
	public static boolean isBrightSunlight(World world) {
		TimeOfDay timeOfDay = getTimeOfDay(world);
		return timeOfDay.isDay() && !world.isRaining();
	}
	
	public static long getDay(long time) {
		return time / 24000L % 2147483647L;
	}
	
	public enum TimeOfDay {
		DAY,
		NOON,
		NIGHT,
		SUNRISE,
		SUNSET,
		MIDNIGHT;
		
		public boolean isNight() {
			return this == NIGHT || this == MIDNIGHT;
		}
		
		public boolean isDay() {
			return this == DAY || this == NOON;
		}
	}
	
}
