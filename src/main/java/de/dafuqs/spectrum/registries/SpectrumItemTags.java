package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;

public class SpectrumItemTags {
	
	public static final TagKey<Item> COLORED_SAPLINGS = of("colored_saplings");
	public static final TagKey<Item> COLORED_PLANKS = of("colored_planks");
	public static final TagKey<Item> GEMSTONE_SHARDS = of("gemstone_shards");
	public static final TagKey<Item> GEMSTONE_BUDS = of("gemstone_buds");
	public static final TagKey<Item> GEMSTONE_CLUSTERS = of("gemstone_clusters");
	public static final TagKey<Item> GEMSTONE_POWDERS = of("gemstone_powders");
	public static final TagKey<Item> PIGMENTS = of("pigments");
	public static final TagKey<Item> PEDESTALS = of("pedestals");
	public static final TagKey<Item> COMING_SOON_TOOLTIP = of("coming_soon_tooltip");
	public static final TagKey<Item> PIGLIN_SAFE_EQUIPMENT = of("piglin_safe_equipment");
	public static final TagKey<Item> ENCHANTABLE_BOOKS = of("enchantable_books");
	public static final TagKey<Item> MEMORY_BONDING_AGENTS_CONCEALABLE = of("memory_bonding_agents_concealable");
	public static final TagKey<Item> MOB_HEADS = of("mob_heads");
	public static final TagKey<Item> SPAWNERS = of("spawners");
	public static final TagKey<Item> INDESTRUCTIBLE_BLACKLISTED = of("indestructible_blacklisted");
	public static final TagKey<Item> NO_CINDERHEARTH_DOUBLING = of("no_cinderhearth_doubling");
	public static final TagKey<Item> SHOOTING_STARS = of("shooting_stars");
	public static final TagKey<Item> GLASS_ARROWS = of("glass_arrows");
	public static final TagKey<Item> KINDLING_FOOD = of("kindling_food");
	
	private static TagKey<Item> of(String id) {
		return TagKey.of(Registries.ITEM.getKey(), SpectrumCommon.locate(id));
	}
	
}
