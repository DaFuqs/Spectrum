package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class SpectrumItemTags {
	
	public static Tag<Item> COLORED_SAPLINGS;
	public static Tag<Item> GEMSTONE_SHARDS;
	public static Tag<Item> GEMSTONE_BUDS;
	public static Tag<Item> GEMSTONE_CLUSTERS;
	public static Tag<Item> GEMSTONE_POWDERS;
	public static Tag<Item> PIGMENTS;
	public static Tag<Item> PEDESTALS;
	public static Tag<Item> COMING_SOON_TOOLTIP;
	public static Tag<Item> PIGLIN_SAFE_EQUIPMENT;

	private static Tag<Item> getReference(String id) {
		return TagRegistry.item(new Identifier(SpectrumCommon.MOD_ID, id));
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
