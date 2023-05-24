package de.dafuqs.spectrum.compat.patchouli;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Map;

public class PatchouliFlags {
	
	public static void register() {
		for (Map.Entry<RegistryKey<Enchantment>, Enchantment> entry : Registries.ENCHANTMENT.getEntrySet()) {
			Identifier id = entry.getKey().getValue();
			PatchouliAPI.get().setConfigFlag("spectrum:enchantment_exists_" + id.getNamespace() + "_" + id.getPath(), true);
		}
	}
	
}
