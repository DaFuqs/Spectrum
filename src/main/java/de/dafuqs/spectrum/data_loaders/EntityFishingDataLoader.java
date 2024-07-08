package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.NbtHelper;
import de.dafuqs.spectrum.api.predicate.entity.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.resource.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.util.profiler.*;

import java.util.*;

public class EntityFishingDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	
	public static final String ID = "entity_fishing";
	public static final EntityFishingDataLoader INSTANCE = new EntityFishingDataLoader();
	
	protected static final List<EntityFishingEntry> ENTITY_FISHING_ENTRIES = new ArrayList<>();
	
	public record EntityFishingEntity(
			EntityType<?> entityType,
			Optional<NbtCompound> nbt
	) {
	}

	public record EntityFishingEntry(
			EntityFishingPredicate predicate,
			float entityChance,
			DataPool<EntityFishingEntity> weightedEntities
	) {
	}
	
	private EntityFishingDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		ENTITY_FISHING_ENTRIES.clear();
		prepared.forEach((identifier, jsonElement) -> {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			
			EntityFishingPredicate predicate = EntityFishingPredicate.fromJson(jsonObject.get("location").getAsJsonObject());
			float chance = JsonHelper.getFloat(jsonObject, "chance");
			JsonArray entityArray = JsonHelper.getArray(jsonObject, "entities");
			
			DataPool.Builder<EntityFishingEntity> entities = DataPool.builder();
			entityArray.forEach(entryElement -> {
				JsonObject entryObject = entryElement.getAsJsonObject();
				
				EntityType<?> entityType = Registries.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(entryObject, "id")));
				
				Optional<NbtCompound> nbt = NbtHelper.getNbtCompound(entryObject.get("nbt"));

				int weight = 1;
				if (JsonHelper.hasNumber(entryObject, "weight")) {
					weight = JsonHelper.getInt(entryObject, "weight");
				}
				entities.add(new EntityFishingEntity(entityType, nbt), weight);
			});
			
			ENTITY_FISHING_ENTRIES.add(new EntityFishingEntry(
					predicate,
					chance,
					entities.build()
			));
		});
	}
	
	@Override
	public Identifier getFabricId() {
		return SpectrumCommon.locate(ID);
	}
	
	public static Optional<EntityFishingEntity> tryCatchEntity(ServerWorld world, BlockPos pos, int bigCatchLevel) {
		for (EntityFishingEntry entry : ENTITY_FISHING_ENTRIES) {
			if (entry.predicate.test(world, pos)) {
				if (world.random.nextFloat() < entry.entityChance * (1 + bigCatchLevel)) {
					var x = entry.weightedEntities.getOrEmpty(world.random);
					if (x.isPresent()) {
						return Optional.of(x.get().getData());
					}
				}
				return Optional.empty();
			}
		}
		return Optional.empty();
	}
	
}