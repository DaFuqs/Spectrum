package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.fabricmc.fabric.api.resource.conditions.v1.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;

import java.util.*;

public class SpectrumLoadConditions {
	
	public record SpectrumTagsPopulatedResourceCondition(Identifier registry, List<Identifier> tags) implements ResourceCondition {
		public static final MapCodec<SpectrumTagsPopulatedResourceCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
			return instance.group(Identifier.CODEC.fieldOf("registry").orElse(
							RegistryKeys.ITEM.getValue()).forGetter(SpectrumTagsPopulatedResourceCondition::registry),
					Identifier.CODEC.listOf().fieldOf("values").forGetter(SpectrumTagsPopulatedResourceCondition::tags)
			).apply(instance, SpectrumTagsPopulatedResourceCondition::new);
		});
		
		public ResourceConditionType<?> getType() {
			return SpectrumLoadConditions.TAGS_POPULATED;
		}
		
		public boolean test(RegistryWrapper.WrapperLookup registryLookup) {
			return tagsPopulated(registryLookup, this.registry(), this.tags());
		}
		
		public static boolean tagsPopulated(RegistryWrapper.WrapperLookup registryLookup, Identifier registryId, List<Identifier> tags) {
			RegistryKey<Registry<Registry<?>>> registryKey = RegistryKey.ofRegistry(registryId);
			RegistryWrapper.Impl<Registry<?>> wrapper = registryLookup.getWrapperOrThrow(registryKey);
			
			for (Identifier tag : tags) {
				TagKey<Registry<?>> tagKey = TagKey.of(registryKey, tag);
				Optional<RegistryEntryList.Named<Registry<?>>> optional = wrapper.getOptional(tagKey);
				if (optional.isEmpty()) {
					return false;
				}
				RegistryEntryList.Named<Registry<?>> entry = optional.get();
				if (entry.size() == 0) {
					return false;
				}
			}
			
			return true;
		}
		
		public Identifier registry() {
			return this.registry;
		}
		
		public List<Identifier> tags() {
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
