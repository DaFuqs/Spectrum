package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumItemTags {
	
	public static TagKey<Item> COLORED_SAPLINGS;
	public static TagKey<Item> GEMSTONE_SHARDS;
	public static TagKey<Item> GEMSTONE_BUDS;
	public static TagKey<Item> GEMSTONE_CLUSTERS;
	public static TagKey<Item> GEMSTONE_POWDERS;
	public static TagKey<Item> PIGMENTS;
	public static TagKey<Item> PEDESTALS;
	public static TagKey<Item> COMING_SOON_TOOLTIP;
	public static TagKey<Item> PIGLIN_SAFE_EQUIPMENT;
	public static TagKey<Item> ENCHANTABLE_BOOKS;
	public static TagKey<Item> MEMORY_BONDING_AGENTS_CONCEILABLE;
	public static TagKey<Item> MOB_HEADS;
	public static TagKey<Item> SPAWNERS;
	
	private static TagKey<Item> getReference(String id) {
		return TagKey.of(Registry.ITEM_KEY, new Identifier(SpectrumCommon.MOD_ID, id));
	}
	
	public static void getReferences() {
		COLORED_SAPLINGS = getReference("colored_saplings");
		GEMSTONE_SHARDS = getReference("gemstone_shards");
		GEMSTONE_BUDS = getReference("gemstone_buds");
		GEMSTONE_CLUSTERS = getReference("gemstone_clusters");
		GEMSTONE_POWDERS = getReference("gemstone_powders");
		PIGMENTS = getReference("pigments");
		PEDESTALS = getReference("pedestals");
		COMING_SOON_TOOLTIP = getReference("coming_soon_tooltip");
		PIGLIN_SAFE_EQUIPMENT = getReference("piglin_safe_equipment");
		ENCHANTABLE_BOOKS = getReference("enchantable_books");
		MEMORY_BONDING_AGENTS_CONCEILABLE = getReference("memory_bonding_agents_conceilable");
		MOB_HEADS = getReference("mob_heads");
		SPAWNERS = getReference("spawners");
	}
}
