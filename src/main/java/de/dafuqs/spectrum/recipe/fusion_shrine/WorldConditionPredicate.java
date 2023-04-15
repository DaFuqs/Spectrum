package de.dafuqs.spectrum.recipe.fusion_shrine;

import com.google.gson.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.world_conditions.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface WorldConditionPredicate {
	
	WorldConditionPredicate ANY = (world, pos) -> true;
	
	interface Deserializer {
		WorldConditionPredicate deserialize(JsonObject json);
	}
	
	final class Deserializers {
		public static final Map<String, Deserializer> TYPES = new HashMap<>();
		
		public static final Deserializer ANY = register("dimension", (json) -> WorldConditionPredicate.ANY);
		public static final Deserializer DIMENSION = register("dimension", DimensionPredicate::fromJson);
		public static final Deserializer MOON_PHASE = register("moon_phase", MoonPhasePredicate::fromJson);
		public static final Deserializer TIME_OF_DAY = register("time_of_day", TimeOfDayPredicate::fromJson);
		public static final Deserializer WEATHER = register("weather", WeatherPredicate::fromJson);
		public static final Deserializer COMMAND = register("command", CommandPredicate::fromJson);
	}
	
	static WorldConditionPredicate.Deserializer register(String id, WorldConditionPredicate.Deserializer deserializer) {
		Deserializers.TYPES.put(id, deserializer);
		return deserializer;
	}
	
	static WorldConditionPredicate fromJson(@Nullable JsonElement json) {
		if (json instanceof JsonObject jsonObject) {
			String string = JsonHelper.getString(jsonObject, "type", null);
			if (string == null) {
				return ANY;
			} else {
				Deserializer deserializer = Deserializers.TYPES.get(string);
				if (deserializer == null) {
					throw new JsonSyntaxException("Unknown sub-predicate type: " + string);
				} else {
					return deserializer.deserialize(jsonObject);
				}
			}
		}
		return ANY;
	}
	
	boolean test(ServerWorld world, BlockPos pos);
	
}
