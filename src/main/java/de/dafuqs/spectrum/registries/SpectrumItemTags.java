package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumItemTags {
	
	public static TagKey<Item> COLORED_SAPLINGS = getReference("colored_saplings");
	public static TagKey<Item> GEMSTONE_SHARDS = getReference("gemstone_shards");
	public static TagKey<Item> GEMSTONE_BUDS = getReference("gemstone_buds");
	public static TagKey<Item> GEMSTONE_CLUSTERS = getReference("gemstone_clusters");
	public static TagKey<Item> GEMSTONE_POWDERS = getReference("gemstone_powders");
	public static TagKey<Item> PIGMENTS = getReference("pigments");
	public static TagKey<Item> PEDESTALS = getReference("pedestals");
	public static TagKey<Item> COMING_SOON_TOOLTIP = getReference("coming_soon_tooltip");
	public static TagKey<Item> PIGLIN_SAFE_EQUIPMENT = getReference("piglin_safe_equipment");
	public static TagKey<Item> ENCHANTABLE_BOOKS = getReference("enchantable_books");
	public static TagKey<Item> MEMORY_BONDING_AGENTS_CONCEALABLE = getReference("memory_bonding_agents_concealable");
	public static TagKey<Item> MOB_HEADS = getReference("mob_heads");
	public static TagKey<Item> SPAWNERS = getReference("spawners");
	public static TagKey<Item> INDESTRUCTIBLE_BLACKLISTED = getReference("indestructible_blacklisted");
	
	private static TagKey<Item> getReference(String id) {
		return TagKey.of(Registry.ITEM_KEY, new Identifier(SpectrumCommon.MOD_ID, id));
	}
	
}
