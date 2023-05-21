package de.dafuqs.spectrum.recipe.fusion_shrine.world_conditions;

import com.google.gson.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;

import java.util.*;

public class TimeOfDayPredicate implements WorldConditionPredicate {
	
	public final TimeHelper.TimeOfDay timeOfDay;
	
	public TimeOfDayPredicate(TimeHelper.TimeOfDay timeOfDay) {
		this.timeOfDay = timeOfDay;
	}
	
	public static TimeOfDayPredicate fromJson(JsonObject json) {
		return new TimeOfDayPredicate(TimeHelper.TimeOfDay.valueOf(json.get("time").getAsString().toUpperCase(Locale.ROOT)));
	}
	
	@Override
	public boolean test(ServerWorld world, BlockPos pos) {
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