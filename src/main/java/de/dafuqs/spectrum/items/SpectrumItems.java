package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import de.dafuqs.spectrum.items.armor.BedrockArmorMaterial;
import de.dafuqs.spectrum.items.misc.Spawner;
import de.dafuqs.spectrum.items.tools.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import static de.dafuqs.spectrum.blocks.fluid.SpectrumFluids.STILL_LIQUID_CRYSTAL;

public class SpectrumItems {

    public static FabricItemSettings generalItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL);
    public static FabricItemSettings spectrumWorldgenItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_WORLDGEN);
    public static FabricItemSettings spectrumUncommonItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).rarity(Rarity.UNCOMMON);
    public static FabricItemSettings spectrumRareItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).rarity(Rarity.RARE);
    public static FabricItemSettings spectrumBedrockItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_GENERAL).rarity(Rarity.RARE).fireproof();

    // Bedrock Tools
    public static ToolItem BEDROCK_PICKAXE = new BedrockPickaxeItem(BedrockToolMaterial.INSTANCE, 1, -2.8F, spectrumBedrockItemSettings);
    public static ToolItem BEDROCK_AXE = new BedrockAxeItem(BedrockToolMaterial.INSTANCE, 6, -3.0F, spectrumBedrockItemSettings);
    public static ToolItem BEDROCK_SHOVEL = new BedrockShovelItem(BedrockToolMaterial.INSTANCE, 2, -3.0F, spectrumBedrockItemSettings);
    public static ToolItem BEDROCK_SWORD = new BedrockSwordItem(BedrockToolMaterial.INSTANCE, 5, -2.4F, spectrumBedrockItemSettings);
    public static ToolItem BEDROCK_HOE = new BedrockHoeItem(BedrockToolMaterial.INSTANCE, -2, -0.0F, spectrumBedrockItemSettings);

    // Bedrock Items
    public static BedrockBowItem BEDROCK_BOW = new BedrockBowItem(spectrumBedrockItemSettings);
    public static BedrockCrossbowItem BEDROCK_CROSSBOW = new BedrockCrossbowItem(spectrumBedrockItemSettings);
    public static BedrockShearsItem BEDROCK_SHEARS = new BedrockShearsItem(spectrumBedrockItemSettings); // TODO: wait for fabric pull request to get shears drop: https://github.com/FabricMC/fabric/pull/1287
    public static FishingRodItem BEDROCK_FISHING_ROD = new BedrockFishingRodItem(spectrumBedrockItemSettings);

    // Bedrock Armor
    public static final BedrockArmorMaterial BEDROCK_ARMOR_MATERIAL = new BedrockArmorMaterial();
    public static final Item BEDROCK_HELMET = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.HEAD, spectrumBedrockItemSettings);
    public static final Item BEDROCK_CHESTPLATE = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.CHEST, spectrumBedrockItemSettings);
    public static final Item BEDROCK_LEGGINGS = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.LEGS, spectrumBedrockItemSettings);
    public static final Item BEDROCK_BOOTS = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.FEET, spectrumBedrockItemSettings);

    // Gem shards
    public static final Item CITRINE_SHARD_ITEM = new Item(spectrumWorldgenItemSettings);
    public static final Item TOPAZ_SHARD_ITEM = new Item(spectrumWorldgenItemSettings);
    public static final Item ONYX_SHARD_ITEM = new Item(spectrumWorldgenItemSettings);
    public static final Item MOONSTONE_SHARD_ITEM = new Item(spectrumWorldgenItemSettings);
    public static final Item RAINBOW_MOONSTONE_ITEM = new Item(spectrumWorldgenItemSettings);

    // DECAY DROPS
    public static final Item VEGETAL = new Item(generalItemSettings);
    public static final Item CORRUPTED_OBSIDIAN_DUST = new Item(spectrumUncommonItemSettings);
    public static final Item CORRUPTED_BEDROCK_DUST = new Item(spectrumRareItemSettings);

    // FLUIDS
    public static final Item LIQUID_CRYSTAL_BUCKET = new BucketItem(STILL_LIQUID_CRYSTAL, generalItemSettings.recipeRemainder(Items.BUCKET).maxCount(1));

    public static final Item SPAWNER = new Spawner(Blocks.SPAWNER, spectrumUncommonItemSettings.maxCount(64));
    public static final Item GLISTERING_MELON_SEEDS = new AliasedBlockItem(SpectrumBlocks.GLISTERING_MELON_STEM, spectrumUncommonItemSettings.maxCount(64));

    // ORE
    public static final Item SPARKLESTONE_GEM = new Item(spectrumWorldgenItemSettings);
    public static final Item RAW_KOENIGSBLAU = new Item(spectrumWorldgenItemSettings);
    public static final Item SHAPED_KOENIGSBLAU = new Item(spectrumWorldgenItemSettings);

    private static void registerItem(String name, Item item) {
        Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), item);
    }

    public static void register() {
        registerItem("bedrock_pickaxe", BEDROCK_PICKAXE);
        registerItem("bedrock_axe", BEDROCK_AXE);
        registerItem("bedrock_shovel", BEDROCK_SHOVEL);
        registerItem("bedrock_sword", BEDROCK_SWORD);
        registerItem("bedrock_hoe", BEDROCK_HOE);

        registerItem("bedrock_bow", BEDROCK_BOW);
        registerItem("bedrock_crossbow", BEDROCK_CROSSBOW);
        registerItem("bedrock_shears", BEDROCK_SHEARS);
        registerItem("bedrock_fishing_rod", BEDROCK_FISHING_ROD);

        registerItem("bedrock_helmet", BEDROCK_HELMET);
        registerItem("bedrock_chestplate", BEDROCK_CHESTPLATE);
        registerItem("bedrock_leggings", BEDROCK_LEGGINGS);
        registerItem("bedrock_boots", BEDROCK_BOOTS);

        registerItem("spawner", SPAWNER);
        registerItem("glistering_melon_seeds", GLISTERING_MELON_SEEDS);

        registerItem("citrine_shard", CITRINE_SHARD_ITEM);
        registerItem("topaz_shard", TOPAZ_SHARD_ITEM);
        registerItem("onyx_shard", ONYX_SHARD_ITEM);
        registerItem("moonstone_shard", MOONSTONE_SHARD_ITEM);
        registerItem("rainbow_moonstone_shard", RAINBOW_MOONSTONE_ITEM);

        registerItem("vegetal", VEGETAL);
        registerItem("corrupted_obsidian_dust", CORRUPTED_OBSIDIAN_DUST);
        registerItem("corrupted_bedrock_dust", CORRUPTED_BEDROCK_DUST);

        registerItem("liquid_crystal_bucket", LIQUID_CRYSTAL_BUCKET);

        registerItem("sparklestone_gem", SPARKLESTONE_GEM);
        registerItem("raw_koenigsblau", RAW_KOENIGSBLAU);
        registerItem("shaped_koenigsblau", SHAPED_KOENIGSBLAU);
    }


}
