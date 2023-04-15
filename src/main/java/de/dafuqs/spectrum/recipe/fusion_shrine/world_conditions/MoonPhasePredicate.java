package de.dafuqs.spectrum.recipe.fusion_shrine.world_conditions;

import com.google.gson.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;

public class MoonPhasePredicate implements WorldConditionPredicate {
	
	public Integer moonPhase;
	
	public MoonPhasePredicate(Integer moonPhase) {
		this.moonPhase = moonPhase;
	}
	
	public static MoonPhasePredicate fromJson(JsonObject json) {
		JsonElement jsonElement = json.get("moon_phase");
		String s = jsonElement.getAsString();
		if ("full_moon".equals(s)) {
			return new MoonPhasePredicate(0);
		} else if ("new_moon".equals(s)) {
			return new MoonPhasePredicate(4);
		} else {
			return new MoonPhasePredicate(jsonElement.getAsInt());
		}
		
	}
	
	@Override
	public boolean test(ServerWorld world, BlockPos pos) {
		return this.moonPhase == world.getMoonPhase();
	}
	
}