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

import static de.dafuqs.spectrum.fluid.SpectrumFluids.STILL_LIQUID_CRYSTAL;

public class SpectrumItems {

    public static FabricItemSettings toolItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_TOOLS).rarity(Rarity.COMMON);
    public static FabricItemSettings spectrumUncommonItemSettings = toolItemSettings.rarity(Rarity.UNCOMMON);
    public static FabricItemSettings spectrumRareItemSettings = toolItemSettings.rarity(Rarity.RARE);

    // Bedrock Tools
    public static ToolItem BEDROCK_PICKAXE = new BedrockPickaxeItem(BedrockToolMaterial.INSTANCE, 1, -2.8F, spectrumRareItemSettings);
    public static ToolItem BEDROCK_AXE = new BedrockAxeItem(BedrockToolMaterial.INSTANCE, 6, -3.0F, spectrumRareItemSettings);
    public static ToolItem BEDROCK_SHOVEL = new BedrockShovelItem(BedrockToolMaterial.INSTANCE, 2, -3.0F, spectrumRareItemSettings);
    public static ToolItem BEDROCK_SWORD = new BedrockSwordItem(BedrockToolMaterial.INSTANCE, 5, -2.4F, spectrumRareItemSettings);
    public static ToolItem BEDROCK_HOE = new BedrockHoeItem(BedrockToolMaterial.INSTANCE, -2, -0.0F, spectrumRareItemSettings);

    // General bedrock tools
    /*public static ToolItem BEDROCK_SHEARS = new ShearsItem(BedrockToolMaterial.INSTANCE, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT));
    public static ToolItem FLINT_AND_BEDROCK = new SwordItem(BedrockToolMaterial.INSTANCE, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT));
    public static ToolItem BEDROCK_FISHING_ROD = new SwordItem(BedrockToolMaterial.INSTANCE, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT));
    public static ToolItem BEDROCK_SHIELD = new ShieldItem(BedrockToolMaterial.INSTANCE, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT));
    public static ToolItem BEDROCK_BOW = new SwordItem(BedrockToolMaterial.INSTANCE, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT));*/

    // Bedrock Armor
    public static final BedrockArmorMaterial BEDROCK_ARMOR_MATERIAL = new BedrockArmorMaterial();
    public static final Item BEDROCK_HELMET = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.HEAD, spectrumRareItemSettings);
    public static final Item BEDROCK_CHESTPLATE = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.CHEST, spectrumRareItemSettings);
    public static final Item BEDROCK_LEGGINGS = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.LEGS, spectrumRareItemSettings);
    public static final Item BEDROCK_BOOTS = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.FEET, spectrumRareItemSettings);

    public static final Item SPAWNER = new Spawner(Blocks.SPAWNER, spectrumUncommonItemSettings.maxCount(64));

    public static final Item GLISTERING_MELON_SEEDS = new AliasedBlockItem(SpectrumBlocks.GLISTERING_MELON_STEM, spectrumUncommonItemSettings.maxCount(64));

    public static final Item CITRINE_SHARD_ITEM = new Item(toolItemSettings);
    public static final Item TOPAZ_SHARD_ITEM = new Item(toolItemSettings);
    public static final Item ONYX_SHARD_ITEM = new Item(toolItemSettings);
    public static final Item MOONSTONE_SHARD_ITEM = new Item(toolItemSettings);
    public static final Item RAINBOW_MOONSTONE_ITEM = new Item(toolItemSettings);

    // DECAY DROPS
    public static final Item VEGETAL = new Item(toolItemSettings);
    public static final Item CORRUPTED_OBSIDIAN_DUST = new Item(spectrumUncommonItemSettings);
    public static final Item CORRUPTED_BEDROCK_DUST = new Item(spectrumRareItemSettings);

    // FLUIDS
    public static final Item LIQUID_CRYSTAL_BUCKET = new BucketItem(STILL_LIQUID_CRYSTAL, toolItemSettings.recipeRemainder(Items.BUCKET).maxCount(1));



    private static void registerItem(String name, Item item) {
        Registry.register(Registry.ITEM, new Identifier(SpectrumCommon.MOD_ID, name), item);
    }

    public static void register() {
        registerItem("bedrock_pickaxe", BEDROCK_PICKAXE);
        registerItem("bedrock_axe", BEDROCK_AXE);
        registerItem("bedrock_shovel", BEDROCK_SHOVEL);
        registerItem("bedrock_sword", BEDROCK_SWORD);
        registerItem("bedrock_hoe", BEDROCK_HOE);

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
    }


}
