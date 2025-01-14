package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;

public class SpectrumItemTags {
	
	// "c" namespace
	public static final TagKey<Item> SKULLS = common("skulls");
	public static final TagKey<Item> FRUITS = common("fruits");
	
	// "spectrum" namespace
	public static final TagKey<Item> COLORED_PLANKS = of("colored_planks");
	public static final TagKey<Item> GEMSTONE_SHARDS = of("gemstone_shards");
	public static final TagKey<Item> COMING_SOON_TOOLTIP = of("coming_soon_tooltip");
	public static final TagKey<Item> PIGLIN_SAFE_EQUIPMENT = of("piglin_safe_equipment");
	public static final TagKey<Item> ENCHANTABLE_BOOKS = of("enchantable_books");
	public static final TagKey<Item> MEMORY_BONDING_AGENTS_CONCEALABLE = of("memory_bonding_agents_concealable");
	public static final TagKey<Item> INDESTRUCTIBLE_BLACKLISTED = of("indestructible_blacklisted");
	public static final TagKey<Item> NO_CINDERHEARTH_DOUBLING = of("no_cinderhearth_doubling");
	public static final TagKey<Item> SHOOTING_STARS = of("shooting_stars");
	public static final TagKey<Item> GLASS_ARROWS = of("glass_arrows");
	public static final TagKey<Item> KINDLING_FOOD = of("kindling_food");
	public static final TagKey<Item> COLORED_FENCES = of("colored_fences");
	public static final TagKey<Item> COLORED_FENCE_GATES = of("colored_fence_gates");
	public static final TagKey<Item> REQUIRES_OMNI_ACCELERATOR_PVP_ENABLED = of("requires_omni_accelerator_pvp_enabled");
	public static final TagKey<Item> EMISSIVE = of("emissive");
	public static final TagKey<Item> PASTEL_NODE_UPGRADES = of("pastel_node_upgrades");
	public static final TagKey<Item> TAG_FILTERING_ITEMS = of("tag_filtering_items");
	public static final TagKey<Item> WEEPING_GALA_LOGS = of("weeping_gala_logs");

	private static TagKey<Item> of(String id) {
		return TagKey.of(RegistryKeys.ITEM, SpectrumCommon.locate(id));
	}
	
	private static TagKey<Item> common(String id) {
		return TagKey.of(RegistryKeys.ITEM, new Identifier("c", id));
	}
	
}
