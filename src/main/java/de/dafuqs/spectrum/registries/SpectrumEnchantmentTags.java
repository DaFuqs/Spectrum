package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.enchantment.*;
import net.minecraft.tag.*;
import net.minecraft.util.registry.*;

import java.util.*;

public class SpectrumEnchantmentTags {
	
	public static final TagKey<Enchantment> SPECTRUM_ENCHANTMENT = of("enchantments");
	
	private static TagKey<Enchantment> of(String id) {
		return TagKey.of(Registry.ENCHANTMENT_KEY, SpectrumCommon.locate(id));
	}
	
	public static boolean isIn(TagKey<Enchantment> tag, Enchantment enchantment) {
		Optional<RegistryKey<Enchantment>> optionalKey = Registry.ENCHANTMENT.getKey(enchantment);
		if (optionalKey.isEmpty()) {
			return false;
		}
		Optional<RegistryEntry<Enchantment>> registryEntry = Registry.ENCHANTMENT.getEntry(optionalKey.get());
		if (registryEntry.isEmpty()) {
			return false;
		}
		return Registry.ENCHANTMENT.getOrCreateEntryList(tag).contains(registryEntry.get());
	}
	
}
