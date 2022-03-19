package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
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
	}
}
