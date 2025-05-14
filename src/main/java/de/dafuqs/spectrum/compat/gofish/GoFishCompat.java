package de.dafuqs.spectrum.compat.gofish;

import net.fabricmc.loader.api.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.storage.loot.*;

public class GoFishCompat {
	
	public static final String MOD_ID = "go-fish";
	public static final String NAMESPACE = "gofish";
	
	public static final ResourceKey<LootTable> DEFAULT_CRATES_LOOT_TABLE_ID = lootTableKey("gameplay/fishing/crates");
	public static final ResourceKey<LootTable> NETHER_CRATES_LOOT_TABLE_ID = lootTableKey("gameplay/fishing/nether/crates");
	public static final ResourceKey<LootTable> END_CRATES_LOOT_TABLE_ID = lootTableKey("gameplay/fishing/end/crates");
	
	public static final ResourceKey<LootTable> NETHER_FISH_LOOT_TABLE_ID = lootTableKey("gameplay/fishing/nether/fish");
	public static final ResourceKey<LootTable> END_FISH_LOOT_TABLE_ID = lootTableKey("gameplay/fishing/end/fish");
	
	public static final ResourceKey<Enchantment> DEEPFRY_ENCHANTMENT_ID = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(NAMESPACE, "deepfry"));
	
	public static ResourceKey<LootTable> lootTableKey(String id) {
		return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(NAMESPACE, id));
	}
	
	public static boolean isLoaded() {
		return FabricLoader.getInstance().isModLoaded(MOD_ID);
	}
	
	public static boolean hasDeepfry(ItemStack itemStack) {
		if (!isLoaded()) {
			return false;
		}
		
		ItemEnchantments enchantments = itemStack.getEnchantments();
		for (Holder<Enchantment> enchantment : enchantments.keySet()) {
			if (enchantment.is(DEEPFRY_ENCHANTMENT_ID)) {
				return true;
			}
		}
		return false;
	}
	
}
