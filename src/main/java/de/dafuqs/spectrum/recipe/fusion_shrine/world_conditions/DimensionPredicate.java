package de.dafuqs.spectrum.recipe.fusion_shrine.world_conditions;

import com.google.gson.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;

import java.util.*;

public class DimensionPredicate implements WorldConditionPredicate {
	
	public final List<RegistryKey<World>> dimensionKeys;
	
	public DimensionPredicate(List<RegistryKey<World>> dimensionKeys) {
		this.dimensionKeys = dimensionKeys;
	}
	
	public static DimensionPredicate fromJson(JsonObject json) {
		List<RegistryKey<World>> dimensionKeys = new ArrayList<>();
		for (JsonElement element : json.get("worlds").getAsJsonArray()) {
			dimensionKeys.add(RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(element.getAsString())));
		}
		return new DimensionPredicate(dimensionKeys);
	}
	
	@Override
	public boolean test(ServerWorld world, BlockPos pos) {
		return this.dimensionKeys.contains(world.getRegistryKey());
	}
	
}