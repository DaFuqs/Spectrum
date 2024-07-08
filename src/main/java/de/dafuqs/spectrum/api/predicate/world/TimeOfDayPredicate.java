package de.dafuqs.spectrum.api.predicate.world;

import com.google.gson.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;

import java.util.*;

public class TimeOfDayPredicate implements WorldConditionPredicate {
	public static final TimeOfDayPredicate ANY = new TimeOfDayPredicate(null);

	public final TimeHelper.TimeOfDay timeOfDay;
	
	public TimeOfDayPredicate(TimeHelper.TimeOfDay timeOfDay) {
		this.timeOfDay = timeOfDay;
	}
	
	public static TimeOfDayPredicate fromJson(JsonObject json) {
        if (json == null || json.isJsonNull()) return ANY;
		return new TimeOfDayPredicate(TimeHelper.TimeOfDay.valueOf(json.get("time").getAsString().toUpperCase(Locale.ROOT)));
	}
	
	@Override
	public boolean test(ServerWorld world, BlockPos pos) {
		if (this == ANY) return true;
		TimeHelper.TimeOfDay worldTimeOfDay = TimeHelper.getTimeOfDay(world);
		switch (this.timeOfDay) {
			case DAY -> {
				return worldTimeOfDay.isDay();
			}
			case NIGHT -> {
				return worldTimeOfDay.isNight();
			}
			default -> {
				return this.timeOfDay == worldTimeOfDay;
			}
		}
	}
	
}