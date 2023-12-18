package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.structure.*;

import java.util.*;

public class SpectrumStructureTags {
	
	public static final TagKey<Structure> MYSTERIOUS_COMPASS_LOCATED = of("mysterious_compass_located");
	
	private static TagKey<Structure> of(String id) {
		return TagKey.of(RegistryKeys.STRUCTURE, SpectrumCommon.locate(id));
	}
	
	public static Optional<RegistryEntryList.Named<Structure>> entriesOf(World world, TagKey<Structure> tag) {
		Registry<Structure> registry = world.getRegistryManager().get(RegistryKeys.STRUCTURE);
		return registry.getEntryList(tag);
	}
	
	public static boolean isIn(World world, Identifier id, TagKey<Structure> tag) {
		Registry<Structure> registry = world.getRegistryManager().get(RegistryKeys.STRUCTURE);
		Structure structure = registry.get(id);
		Optional<RegistryEntryList.Named<Structure>> tagEntries = entriesOf(world, tag);
		
		if (tagEntries.isPresent()) {
			for (RegistryEntry<Structure> entry : tagEntries.get()) {
				if (entry.value().equals(structure)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
