package de.dafuqs.spectrum.data_loaders;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.predicate.FluidPredicate;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityFishingDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {

    public static final String ID = "entity_fishing";
    public static final EntityFishingDataLoader INSTANCE = new EntityFishingDataLoader();
    
    protected static final List<EntityFishingEntry> ENTITY_FISHING_ENTRIES = new ArrayList<>();
    
    public record EntityFishingEntry(FluidPredicate fluidPredicate, float entityChance, int weightSum, DataPool<EntityType> weightedEntityTypes) {

    }
    
    private EntityFishingDataLoader() {
        super(new Gson(), ID);
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        prepared.forEach((identifier, jsonElement) -> {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            
            FluidPredicate fluidPredicate = FluidPredicate.fromJson(jsonObject.get("fluid"));
            float chance = JsonHelper.getFloat(jsonObject, "chance");
            JsonArray entityArray = JsonHelper.getArray(jsonObject, "entities");
    
            AtomicInteger weightSum = new AtomicInteger();
            DataPool.Builder<EntityType> entityTypesList = DataPool.builder();
            entityArray.forEach(entryElement -> {
                JsonObject entryObject = entryElement.getAsJsonObject();
                
                EntityType entityType = Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(entryObject, "id")));
                int weight = 1;
                if(JsonHelper.hasNumber(jsonObject, "weight")) {
                    weight = JsonHelper.getInt(entryObject, "weight");
                }
                weightSum.addAndGet(weight);
                entityTypesList.add(entityType, weight);
            });
            
            ENTITY_FISHING_ENTRIES.add(new EntityFishingEntry(fluidPredicate, chance, weightSum.get(), entityTypesList.build()));
        });
    }

    @Override
    public Identifier getFabricId() {
        return SpectrumCommon.locate(ID);
    }
    
    public static Optional<EntityType> tryCatchEntity(ServerWorld world, BlockPos pos) {
        for(EntityFishingEntry entry : ENTITY_FISHING_ENTRIES) {
            if(entry.fluidPredicate.test(world, pos)) {
                if(world.random.nextFloat() < entry.entityChance) {
                    Optional<Weighted.Present<EntityType>> x = entry.weightedEntityTypes.getOrEmpty(world.random);
                    if(x.isPresent()) {
                        return Optional.of(x.get().getData());
                    }
                }
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
    
}