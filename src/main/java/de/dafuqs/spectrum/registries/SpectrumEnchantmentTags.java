package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.enchantment.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.registry.tag.*;

import java.util.*;

public class SpectrumEnchantmentTags {
	
	public static final TagKey<Enchantment> SPECTRUM_ENCHANTMENT = of("enchantments");
	
	public static final TagKey<Enchantment> ENCHANTER_BLACKLIST = of("enchanter_copying_and_applying_blacklisted");
	
	private static TagKey<Enchantment> of(String id) {
		return TagKey.of(RegistryKeys.ENCHANTMENT, SpectrumCommon.locate(id));
	}
	
	public static boolean isIn(TagKey<Enchantment> tag, Enchantment enchantment) {
		Optional<RegistryKey<Enchantment>> optionalKey = Registries.ENCHANTMENT.getKey(enchantment);
		if (optionalKey.isEmpty()) {
			return false;
		}
		Optional<RegistryEntry.Reference<Enchantment>> registryEntry = Registries.ENCHANTMENT.getEntry(optionalKey.get());
		return registryEntry.filter(enchantmentReference -> Registries.ENCHANTMENT.getOrCreateEntryList(tag).contains(enchantmentReference)).isPresent();
	}
	
}
