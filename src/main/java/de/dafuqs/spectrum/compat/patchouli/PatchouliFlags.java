package de.dafuqs.spectrum.compat.patchouli;

import net.minecraft.enchantment.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;
import vazkii.patchouli.api.*;

import java.util.*;

public class PatchouliFlags {
	
	public static void register() {
		for (Map.Entry<RegistryKey<Enchantment>, Enchantment> entry : Registry.ENCHANTMENT.getEntrySet()) {
			Identifier id = entry.getKey().getValue();
			PatchouliAPI.get().setConfigFlag("spectrum:enchantment_exists_" + id.getNamespace() + "_" + id.getPath(), true);
		}
	}
	
}
