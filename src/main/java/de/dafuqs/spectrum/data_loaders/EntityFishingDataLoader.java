package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.predicate.location.*;
import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.profiling.*;
import net.minecraft.util.random.*;
import net.minecraft.world.entity.*;

import java.util.*;

public class EntityFishingDataLoader extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
	
	public static final String ID = "entity_fishing";
	public static final EntityFishingDataLoader INSTANCE = new EntityFishingDataLoader();
	
	protected static final List<EntityFishingEntry> ENTITY_FISHING_ENTRIES = new ArrayList<>();
	
	public record EntityFishingEntity(Holder<EntityType<?>> entityType, CompoundTag nbt) {
		
		public static final MapCodec<EntityFishingEntity> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().fieldOf("id").forGetter(EntityFishingEntity::entityType),
				CompoundTag.CODEC.optionalFieldOf("nbt", new CompoundTag()).forGetter(EntityFishingEntity::nbt)
		).apply(i, EntityFishingEntity::new));
		
		public static final Codec<WeightedEntry.Wrapper<EntityFishingEntity>> WEIGHTED_CODEC = RecordCodecBuilder.create(i -> i.group(
				CODEC.forGetter(WeightedEntry.Wrapper::data),
				Weight.CODEC.optionalFieldOf("id", Weight.of(1)).forGetter(WeightedEntry.Wrapper::weight)
		).apply(i, WeightedEntry.Wrapper::new));
		
	}
	
	public record EntityFishingEntry(List<WorldConditionsPredicate> predicates, float entityChance, WeightedRandomList<WeightedEntry.Wrapper<EntityFishingEntity>> weightedEntities) {
		
		public static final Codec<EntityFishingEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
				CodecHelper.singleOrList(WorldConditionsPredicate.CODEC).fieldOf("location").forGetter(EntityFishingEntry::predicates),
				Codec.FLOAT.fieldOf("chance").forGetter(EntityFishingEntry::entityChance),
				EntityFishingEntity.WEIGHTED_CODEC.listOf().xmap(WeightedRandomList::create, WeightedRandomList::unwrap).fieldOf("entities").forGetter(EntityFishingEntry::weightedEntities)
		).apply(i, EntityFishingEntry::new));
		
	}
	
	private EntityFishingDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager manager, ProfilerFiller profiler) {
		ENTITY_FISHING_ENTRIES.clear();
		prepared.forEach((identifier, jsonElement) ->
			CodecHelper.fromJson(EntityFishingEntry.CODEC, jsonElement.getAsJsonObject())
					.ifPresent(ENTITY_FISHING_ENTRIES::add));
	}
	
	@Override
	public ResourceLocation getFabricId() {
		return SpectrumCommon.locate(ID);
	}
	
	public static Optional<EntityFishingEntity> tryCatchEntity(ServerLevel world, BlockPos pos, int bigCatchLevel) {
		for (EntityFishingEntry entry : ENTITY_FISHING_ENTRIES) {
			if (entry.predicates.stream().anyMatch(p -> p.test(world, pos))) {
				if (world.random.nextFloat() < entry.entityChance * (1 + bigCatchLevel)) {
					var x = entry.weightedEntities.getRandom(world.random);
					if (x.isPresent()) {
						return Optional.of(x.get().data());
					}
				}
				return Optional.empty();
			}
		}
		return Optional.empty();
	}
	
}
