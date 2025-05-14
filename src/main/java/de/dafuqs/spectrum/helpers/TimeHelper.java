package de.dafuqs.spectrum.helpers;

import net.minecraft.util.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

public class TimeHelper {
	
	public static final long EPOCH_DAY_MILLIS = 86400000;
	
	public static TimeOfDay getTimeOfDay(@NotNull Level world) {
		return getTimeOfDay(world.getDayTime());
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
	
	public static boolean isBrightSunlight(Level world) {
		TimeOfDay timeOfDay = getTimeOfDay(world);
		return timeOfDay.isDay() && !world.isRaining();
	}
	
	public static long getDay(long time) {
		return time / 24000L % 2147483647L;
	}
	
	public enum TimeOfDay implements StringRepresentable {
		DAY(0, 12000),
		NIGHT(12000, 24000),
		NOON(6000, 7000),
		SUNSET(12000, 13000),
		MIDNIGHT(18000, 19000),
		SUNRISE(23000, 24000);
		
		public final int from;
		public final int to;
		
		TimeOfDay(int from, int to) {
			this.from = from;
			this.to = to;
		}
		
		public boolean isNight() {
			return this == NIGHT || this == MIDNIGHT;
		}
		
		public boolean isDay() {
			return this == DAY || this == NOON;
		}
		
		@Override
		public String getSerializedName() {
			return name().toLowerCase();
		}
	}
	
	public static float minecraftDaysFromSeconds(long seconds) {
		return seconds / 1200F;
	}
	
	public static long secondsFromMinecraftDays(int minecraftDays) {
		return minecraftDays * 1200L;
	}
	
}
