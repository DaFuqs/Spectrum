package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;

public class SpectrumItemTags {
	
	// "c" namespace
	public static final TagKey<Item> SKULLS = conventional("skulls");
	public static final TagKey<Item> FRUITS = conventional("foods/fruit");
	
	// "spectrum" namespace
	public static final TagKey<Item> FISHING_RODS = spectrum("fishing_rods");
	public static final TagKey<Item> COLORED_PLANKS = spectrum("colored_planks");
	public static final TagKey<Item> GEMSTONE_SHARDS = spectrum("gemstone_shards");
	public static final TagKey<Item> COMING_SOON_TOOLTIP = spectrum("coming_soon_tooltip");
	public static final TagKey<Item> ENCHANTABLE_BOOKS = spectrum("enchantable_books");
	public static final TagKey<Item> MEMORY_BONDING_AGENTS_CONCEALABLE = spectrum("memory_bonding_agents_concealable");
	public static final TagKey<Item> NO_CINDERHEARTH_DOUBLING = spectrum("no_cinderhearth_doubling");
	public static final TagKey<Item> SHOOTING_STARS = spectrum("shooting_stars");
	public static final TagKey<Item> GLASS_ARROWS = spectrum("glass_arrows");
	public static final TagKey<Item> KINDLING_FOOD = spectrum("kindling_food");
	public static final TagKey<Item> REQUIRES_OMNI_ACCELERATOR_PVP_ENABLED = spectrum("requires_omni_accelerator_pvp_enabled");
	public static final TagKey<Item> EMISSIVE = spectrum("emissive");
	public static final TagKey<Item> PASTEL_NODE_UPGRADES = spectrum("pastel_node_upgrades");
	public static final TagKey<Item> TAG_FILTERING_ITEMS = spectrum("tag_filtering_items");
	public static final TagKey<Item> PLAYER_ATTRIBUTED_PLACEMENT = spectrum("player_attributed_placement");
	public static final TagKey<Item> NATURES_STAFF_CONSUMABLE = spectrum("consumable/natures_staff");
	public static final TagKey<Item> RADIANCE_STAFF_CONSUMABLE = spectrum("consumable/radiance_staff");
	public static final TagKey<Item> GLOW_VISION_GOGGLES_CONSUMABLE = spectrum("consumable/glow_vision_goggles");
	public static final TagKey<Item> TRINKETS = spectrum("trinkets");
	public static final TagKey<Item> STORES_ITEMS_ADDED_TO_INVENTORY = spectrum("stores_items_added_to_inventory"); // TODO: this might be fun as an item component / enchantment actually
	public static final TagKey<Item> COLORING_BLACKLISTED = spectrum("coloring_blacklisted");
	
	private static TagKey<Item> spectrum(String id) {
		return TagKey.create(Registries.ITEM, SpectrumCommon.locate(id));
	}
	
	private static TagKey<Item> conventional(String id) {
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", id));
	}
	
}
