package de.dafuqs.spectrum.helpers;

import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TimeHelper {
	
	public enum TimeOfDay {
		DAY,
		NIGHT,
		SUNRISE,
		SUNSET,
		MIDNIGHT
	}
	
	public static TimeOfDay getTimeOfDay(@NotNull World world) {
		return getTimeOfDay(world.getTimeOfDay());
	}
	
	public static TimeOfDay getTimeOfDay(long timeOfDay) {
		if(timeOfDay % 24000 > 0 && timeOfDay % 24000 < 12000) {
			return TimeOfDay.DAY;
		} else if(timeOfDay % 24000 >= 13000 && timeOfDay % 24000 < 23000) {
			return TimeOfDay.NIGHT;
		} else if(timeOfDay % 24000 >= 23000) {
			return TimeOfDay.MIDNIGHT;
		} else if(timeOfDay % 24000 >= 12000 && timeOfDay % 24000 < 13000) {
			return TimeOfDay.SUNSET;
		} else { // if(timeOfDay % 24000 >= 18000 && timeOfDay % 24000 < 19000) {
			return TimeOfDay.MIDNIGHT;
		}
	}
	
	public static boolean isBrightSunlight(World world) {
		TimeOfDay timeOfDay = getTimeOfDay(world);
		return timeOfDay == TimeOfDay.DAY && !world.isRaining();
	}
	
	public static long getDay(long time) {
		return time / 24000L % 2147483647L;
	}
	
}
