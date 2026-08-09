package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.*;
import net.minecraft.util.profiling.*;
import net.minecraft.util.random.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.material.*;

import java.util.*;

public class EntityFishingDataLoader extends SimpleJsonResourceReloadListener {
	
	public static final String ID = "entity_fishing";
	public static final EntityFishingDataLoader INSTANCE = new EntityFishingDataLoader();
	
	protected static final List<EntityFishingEntry> ENTITY_FISHING_ENTRIES = new ArrayList<>();
	protected static final List<EntityFishingEntry> ENTITY_FISHING_ENTRIES_WITH_DIMENSION = new ArrayList<>();
	
	public record EntityFishingEntity(Holder<EntityType<?>> entityType, CompoundTag nbt) {
		
		public static final MapCodec<EntityFishingEntity> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().fieldOf("entity_type").forGetter(EntityFishingEntity::entityType),
				CompoundTag.CODEC.optionalFieldOf("nbt", new CompoundTag()).forGetter(EntityFishingEntity::nbt)
		).apply(i, EntityFishingEntity::new));
		
		public static final Codec<WeightedEntry.Wrapper<EntityFishingEntity>> WEIGHTED_CODEC = RecordCodecBuilder.create(i -> i.group(
				CODEC.forGetter(WeightedEntry.Wrapper::data),
				Weight.CODEC.optionalFieldOf("weight", Weight.of(1)).forGetter(WeightedEntry.Wrapper::weight)
		).apply(i, WeightedEntry.Wrapper::new));
		
	}
	
	public record EntityFishingEntry(Collection<Fluid> fluids, Optional<ResourceKey<Level>> dimension, float chance, WeightedRandomList<WeightedEntry.Wrapper<EntityFishingEntity>> weightedEntities) {
		
		// Since tags are only populated after data loaders have run, we can't use FluidPredicates (or most other predicates) here
		// And have to resort to lists of fluids instead. Big sad.
		public static final Codec<EntityFishingEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
				BuiltInRegistries.FLUID.byNameCodec().listOf().xmap(list -> (Collection<Fluid>) list, set -> set.stream().toList()).fieldOf("fluids").forGetter(EntityFishingEntry::fluids),
				ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("dimension").forGetter(EntityFishingEntry::dimension),
				Codec.FLOAT.fieldOf("chance").forGetter(EntityFishingEntry::chance),
				EntityFishingEntity.WEIGHTED_CODEC.listOf().xmap(WeightedRandomList::create, WeightedRandomList::unwrap).fieldOf("entities").forGetter(EntityFishingEntry::weightedEntities)
		).apply(i, EntityFishingEntry::new));
		
		public boolean matchesFluid(Fluid fluid) {
			return fluids.contains(fluid);
		}
		
		public Optional<EntityFishingEntity> rollEntity(RandomSource random, int bigCatchLevel) {
			// the +1 is here so all Spectrum rods can fish up entities,
			// even without the Big Catch enchant. Big Catch only gives a bonus
			if (random.nextFloat() < chance * (bigCatchLevel + 1)) {
				Optional<WeightedEntry.Wrapper<EntityFishingEntity>> x = weightedEntities.getRandom(random);
				if (x.isPresent()) {
					return Optional.of(x.get().data());
				}
			}
			return Optional.empty();
		}
	}
	
	private EntityFishingDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager manager, ProfilerFiller profiler) {
		ENTITY_FISHING_ENTRIES.clear();
		ENTITY_FISHING_ENTRIES_WITH_DIMENSION.clear();
		
		prepared.forEach((resourceLocation, jsonElement) -> {
			DataResult<Pair<EntityFishingEntry, JsonElement>> r = EntityFishingEntry.CODEC.decode(JsonOps.INSTANCE, jsonElement.getAsJsonObject());
			
			if (r.isSuccess()) {
				EntityFishingEntry entry = r.getOrThrow().getFirst();
				if (entry.dimension.isPresent()) {
					ENTITY_FISHING_ENTRIES_WITH_DIMENSION.add(entry);
				} else {
					ENTITY_FISHING_ENTRIES.add(entry);
				}
			} else {
				SpectrumCommon.logError("Error loading entity_fishing file with id " + resourceLocation + ": " + r.error().get().message());
			}
		});
	}
	
	public static Optional<EntityFishingEntity> tryCatchEntity(ServerLevel world, BlockPos pos, int bigCatchLevel) {
		Fluid fluid = world.getFluidState(pos).getType();
		RandomSource randomSource = world.getRandom();
		
		// we test entries with a dimension set first
		// to make sure those are selected over the more generic "just the fluid" ones
		for (EntityFishingEntry entry : ENTITY_FISHING_ENTRIES_WITH_DIMENSION) {
			if (!entry.dimension.get().equals(world.dimension())) {
				continue;
			}
			if (entry.matchesFluid(fluid)) {
				return entry.rollEntity(randomSource, bigCatchLevel);
			}
		}
		
		for (EntityFishingEntry entry : ENTITY_FISHING_ENTRIES) {
			if (entry.matchesFluid(fluid)) {
				return entry.rollEntity(randomSource, bigCatchLevel);
			}
		}
		return Optional.empty();
	}
	
}