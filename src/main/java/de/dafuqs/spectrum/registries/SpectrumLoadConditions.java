package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.fabricmc.fabric.api.resource.conditions.v1.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;

import java.util.*;

public class SpectrumLoadConditions {
	
	public record SpectrumTagsPopulatedResourceCondition(ResourceLocation registry, List<ResourceLocation> tags) implements ResourceCondition {
		public static final MapCodec<SpectrumTagsPopulatedResourceCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
			return instance.group(ResourceLocation.CODEC.fieldOf("registry").orElse(
							Registries.ITEM.location()).forGetter(SpectrumTagsPopulatedResourceCondition::registry),
					ResourceLocation.CODEC.listOf().fieldOf("values").forGetter(SpectrumTagsPopulatedResourceCondition::tags)
			).apply(instance, SpectrumTagsPopulatedResourceCondition::new);
		});
		
		public ResourceConditionType<?> getType() {
			return SpectrumLoadConditions.TAGS_POPULATED;
		}
		
		public boolean test(HolderLookup.Provider registryLookup) {
			return tagsPopulated(registryLookup, this.registry(), this.tags());
		}
		
		public static boolean tagsPopulated(HolderLookup.Provider registryLookup, ResourceLocation registryId, List<ResourceLocation> tags) {
			ResourceKey<Registry<Registry<?>>> registryKey = ResourceKey.createRegistryKey(registryId);
			HolderLookup.RegistryLookup<Registry<?>> wrapper = registryLookup.lookupOrThrow(registryKey);
			
			for (ResourceLocation tag : tags) {
				TagKey<Registry<?>> tagKey = TagKey.create(registryKey, tag);
				Optional<HolderSet.Named<Registry<?>>> optional = wrapper.get(tagKey);
				if (optional.isEmpty()) {
					return false;
				}
				HolderSet.Named<Registry<?>> entry = optional.get();
				if (entry.size() == 0) {
					return false;
				}
			}
			
			return true;
		}
		
		public ResourceLocation registry() {
			return this.registry;
		}
		
		public List<ResourceLocation> tags() {
			return this.tags;
		}
	}
	
	public static final ResourceConditionType<SpectrumTagsPopulatedResourceCondition> TAGS_POPULATED = createResourceConditionType("tags_populated", SpectrumTagsPopulatedResourceCondition.CODEC);
	
	private static <T extends ResourceCondition> ResourceConditionType<T> createResourceConditionType(String name, MapCodec<T> codec) {
		return ResourceConditionType.create(SpectrumCommon.locate(name), codec);
	}
	
	public static void register() {
		ResourceConditions.register(TAGS_POPULATED);
	}
	
	
}
