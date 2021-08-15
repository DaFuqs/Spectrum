package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class SpectrumItemTags {

    public static Tag<Item> COLORED_SAPLINGS;
    public static Tag<Item> GEMSTONE_SHARDS;
    public static Tag<Item> GEMSTONE_BUDS;
    public static Tag<Item> GEMSTONE_CLUSTERS;
    public static Tag<Item> GEMSTONE_POWDER;
    public static Tag<Item> PIGMENT;

    private static Tag<Item> register(String id) {
        return TagRegistry.item(new Identifier(SpectrumCommon.MOD_ID, id));
    }

    public static void register() {
        COLORED_SAPLINGS = register("colored_saplings");
        GEMSTONE_SHARDS = register("gemstone_shards");
        GEMSTONE_BUDS = register("gemstone_buds");
        GEMSTONE_CLUSTERS = register("gemstone_clusters");
        GEMSTONE_POWDER = register("gemstone_powder");
        PIGMENT = register("pigment");

    }
}
