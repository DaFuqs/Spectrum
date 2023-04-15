package de.dafuqs.spectrum.recipe.fusion_shrine.world_conditions;

import com.google.gson.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;

import java.util.*;

public class WeatherPredicate implements WorldConditionPredicate {
	
	public enum WeatherCondition {
		CLEAR_SKY,
		RAIN, // rain or thunder
		STRICT_RAIN, // rain without thunder
		THUNDER,
		NOT_THUNDER
	}
	
	public WeatherCondition weatherCondition;
	
	public WeatherPredicate(WeatherCondition weatherCondition) {
		this.weatherCondition = weatherCondition;
	}
	
	public static WeatherPredicate fromJson(JsonObject json) {
		return new WeatherPredicate(WeatherCondition.valueOf(json.get("weather_condition").getAsString().toUpperCase(Locale.ROOT)));
	}
	
	@Override
	public boolean test(ServerWorld world, BlockPos pos) {
		switch (this.weatherCondition) {
			case CLEAR_SKY -> {
				return !world.isRaining();
			}
			case RAIN -> {
				return world.isRaining();
			}
			case STRICT_RAIN -> {
				return world.isRaining() && !world.isThundering();
			}
			case THUNDER -> {
				return world.isThundering();
			}
			case NOT_THUNDER -> {
				return !world.isThundering();
			}
		}
		return true;
	}
	
}