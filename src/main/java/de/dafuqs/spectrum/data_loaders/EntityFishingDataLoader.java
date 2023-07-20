package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.NbtHelper;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.*;
import net.minecraft.resource.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.util.profiler.*;
import net.minecraft.util.registry.*;

import java.util.*;
import java.util.concurrent.atomic.*;

public class EntityFishingDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	
	public static final String ID = "entity_fishing";
	public static final EntityFishingDataLoader INSTANCE = new EntityFishingDataLoader();
	
	protected static final List<EntityFishingEntry> ENTITY_FISHING_ENTRIES = new ArrayList<>();
	
	public record EntityFishingEntity(
		EntityType<?> entityType,
		Optional<NbtCompound> nbt
	) { }
	
	public record EntityFishingEntry(
		FluidPredicate fluidPredicate,
		float entityChance,
		DataPool<EntityFishingEntity> weightedEntities
	) { }
	
	private EntityFishingDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		ENTITY_FISHING_ENTRIES.clear();
		prepared.forEach((identifier, jsonElement) -> {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			
			FluidPredicate fluidPredicate = FluidPredicate.fromJson(jsonObject.get("fluid"));
			float chance = JsonHelper.getFloat(jsonObject, "chance");
			JsonArray entityArray = JsonHelper.getArray(jsonObject, "entities");
			
			AtomicInteger weightSum = new AtomicInteger();
			DataPool.Builder<EntityFishingEntity> entities = DataPool.builder();
			entityArray.forEach(entryElement -> {
				JsonObject entryObject = entryElement.getAsJsonObject();
				
				EntityType<?> entityType = Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(entryObject, "id")));
				
				Optional<NbtCompound> nbt = Optional.empty();
				if (JsonHelper.hasJsonObject(entryObject, "nbt")) {
					JsonObject nbtObject = JsonHelper.getObject(entryObject, "nbt");
					nbt = Optional.of(NbtHelper.fromJsonObject(nbtObject));
				}
				
				int weight = 1;
				if (JsonHelper.hasNumber(entryObject, "weight")) {
					weight = JsonHelper.getInt(entryObject, "weight");
				}
				weightSum.addAndGet(weight);
				entities.add(new EntityFishingEntity(entityType, nbt), weight);
			});
			
			ENTITY_FISHING_ENTRIES.add(new EntityFishingEntry(fluidPredicate, chance, entities.build()));
		});
	}
	
	@Override
	public Identifier getFabricId() {
		return SpectrumCommon.locate(ID);
	}
	
	public static Optional<EntityFishingEntity> tryCatchEntity(ServerWorld world, BlockPos pos, int bigCatchLevel) {
		for (EntityFishingEntry entry : ENTITY_FISHING_ENTRIES) {
			if (entry.fluidPredicate.test(world, pos)) {
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