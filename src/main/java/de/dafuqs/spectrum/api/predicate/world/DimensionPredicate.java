package de.dafuqs.spectrum.api.predicate.world;

import com.google.gson.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class DimensionPredicate implements WorldConditionPredicate {
	public static final DimensionPredicate ANY = new DimensionPredicate(null);
	
	public final List<RegistryKey<World>> dimensionKeys;
	
	public DimensionPredicate(List<RegistryKey<World>> dimensionKeys) {
		this.dimensionKeys = dimensionKeys;
	}
	
	public static DimensionPredicate fromJson(JsonObject json) {
		if (json == null || json.isJsonNull()) return ANY;
		List<RegistryKey<World>> dimensionKeys = new ArrayList<>();
		for (JsonElement element : json.get("worlds").getAsJsonArray()) {
			dimensionKeys.add(RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(element.getAsString())));
		}
		return new DimensionPredicate(dimensionKeys);
	}
	
	@Override
	public boolean test(ServerWorld world, BlockPos pos) {
		if (this == ANY) return true;
		return this.dimensionKeys.contains(world.getRegistryKey());
	}
	
}