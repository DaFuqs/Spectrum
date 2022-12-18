package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class SpectrumItemTags {
	
	public static final TagKey<Item> COLORED_SAPLINGS = getReference("colored_saplings");
	public static final TagKey<Item> COLORED_PLANKS = getReference("colored_planks");
	public static final TagKey<Item> GEMSTONE_SHARDS = getReference("gemstone_shards");
	public static final TagKey<Item> GEMSTONE_BUDS = getReference("gemstone_buds");
	public static final TagKey<Item> GEMSTONE_CLUSTERS = getReference("gemstone_clusters");
	public static final TagKey<Item> GEMSTONE_POWDERS = getReference("gemstone_powders");
	public static final TagKey<Item> PIGMENTS = getReference("pigments");
	public static final TagKey<Item> PEDESTALS = getReference("pedestals");
	public static final TagKey<Item> COMING_SOON_TOOLTIP = getReference("coming_soon_tooltip");
	public static final TagKey<Item> PIGLIN_SAFE_EQUIPMENT = getReference("piglin_safe_equipment");
	public static final TagKey<Item> ENCHANTABLE_BOOKS = getReference("enchantable_books");
	public static final TagKey<Item> MEMORY_BONDING_AGENTS_CONCEALABLE = getReference("memory_bonding_agents_concealable");
	public static final TagKey<Item> MOB_HEADS = getReference("mob_heads");
	public static final TagKey<Item> SPAWNERS = getReference("spawners");
	public static final TagKey<Item> INDESTRUCTIBLE_BLACKLISTED = getReference("indestructible_blacklisted");
	public static final TagKey<Item> NO_CINDERHEARTH_DOUBLING = getReference("no_cinderhearth_doubling");
	public static final TagKey<Item> SHOOTING_STARS = getReference("shooting_stars");
	public static final TagKey<Item> GLASS_ARROWS = getReference("glass_arrows");
	
	private static TagKey<Item> getReference(String id) {
		return TagKey.of(Registry.ITEM_KEY, SpectrumCommon.locate(id));
	}
	
}
