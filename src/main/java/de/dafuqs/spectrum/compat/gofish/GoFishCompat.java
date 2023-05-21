package de.dafuqs.spectrum.compat.gofish;

import net.fabricmc.loader.api.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.util.*;

public class GoFishCompat {
	
	public static final Identifier DEFAULT_CRATES_LOOT_TABLE_ID = new Identifier("gofish", "gameplay/fishing/crates");
	public static final Identifier NETHER_CRATES_LOOT_TABLE_ID = new Identifier("gofish", "gameplay/fishing/nether/crates");
	public static final Identifier END_CRATES_LOOT_TABLE_ID = new Identifier("gofish", "gameplay/fishing/end/crates");
	
	public static final Identifier NETHER_FISH_LOOT_TABLE_ID = new Identifier("gofish", "gameplay/fishing/nether/fish");
	public static final Identifier END_FISH_LOOT_TABLE_ID = new Identifier("gofish", "gameplay/fishing/end/fish");
	
	public static final Identifier DEEPFRY_ENCHANTMENT_ID = new Identifier("gofish", "deepfry");
	
	public static boolean isLoaded() {
		return FabricLoader.getInstance().isModLoaded("go-fish");
	}
	
	public static boolean hasDeepfry(ItemStack itemStack) {
		if (!isLoaded()) {
			return false;
		}
		
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(itemStack);
		for (Enchantment enchantment : enchantments.keySet()) {
			if (isDeepfry(enchantment)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isDeepfry(Enchantment enchantment) {
		Identifier id = EnchantmentHelper.getEnchantmentId(enchantment);
		return id != null && id.equals(GoFishCompat.DEEPFRY_ENCHANTMENT_ID);
	}
	
}
