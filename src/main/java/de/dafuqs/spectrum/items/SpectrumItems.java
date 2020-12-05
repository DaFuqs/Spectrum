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

public class SpectrumItems {

    public static FabricItemSettings bedrockItemSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP).rarity(Rarity.RARE);
    public static FabricItemSettings buildingBlockSettings = new FabricItemSettings().group(SpectrumItemGroups.ITEM_GROUP_BUILDING);

    // Bedrock Tools
    public static ToolItem BEDROCK_PICKAXE = new BedrockPickaxeItem(BedrockToolMaterial.INSTANCE, 1, -2.8F, bedrockItemSettings);
    public static ToolItem BEDROCK_AXE = new BedrockAxeItem(BedrockToolMaterial.INSTANCE, 6, -3.0F, bedrockItemSettings);
    public static ToolItem BEDROCK_SHOVEL = new BedrockShovelItem(BedrockToolMaterial.INSTANCE, 2, -3.0F, bedrockItemSettings);
    public static ToolItem BEDROCK_SWORD = new BedrockSwordItem(BedrockToolMaterial.INSTANCE, 5, -2.4F, bedrockItemSettings);
    public static ToolItem BEDROCK_HOE = new BedrockHoeItem(BedrockToolMaterial.INSTANCE, -2, -0.0F, bedrockItemSettings);

    // General bedrock tools
    /*public static ToolItem BEDROCK_SHEARS = new ShearsItem(BedrockToolMaterial.INSTANCE, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT));
    public static ToolItem FLINT_AND_BEDROCK = new SwordItem(BedrockToolMaterial.INSTANCE, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT));
    public static ToolItem BEDROCK_FISHING_ROD = new SwordItem(BedrockToolMaterial.INSTANCE, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT));
    public static ToolItem BEDROCK_SHIELD = new ShieldItem(BedrockToolMaterial.INSTANCE, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT));
    public static ToolItem BEDROCK_BOW = new SwordItem(BedrockToolMaterial.INSTANCE, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT));*/

    // Bedrock Armor
    public static final BedrockArmorMaterial BEDROCK_ARMOR_MATERIAL = new BedrockArmorMaterial();
    public static final Item BEDROCK_HELMET = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.HEAD, bedrockItemSettings);
    public static final Item BEDROCK_CHESTPLATE = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.CHEST, bedrockItemSettings);
    public static final Item BEDROCK_LEGGINGS = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.LEGS, bedrockItemSettings);
    public static final Item BEDROCK_BOOTS = new ArmorItem(BEDROCK_ARMOR_MATERIAL, EquipmentSlot.FEET, bedrockItemSettings);

    public static final Item SPAWNER = new Spawner(Blocks.SPAWNER, bedrockItemSettings.maxCount(64));
    public static final Item GLISTERING_MELON_SEEDS = new AliasedBlockItem(SpectrumBlocks.GLISTERING_MELON_STEM, bedrockItemSettings.maxCount(64));

    public static final Item CITRINE_SHARD_ITEM = new Item(buildingBlockSettings);
    public static final Item TOPAZ_SHARD_ITEM = new Item(buildingBlockSettings);
    public static final Item ONYX_SHARD_ITEM = new Item(buildingBlockSettings);
    public static final Item MOONSTONE_SHARD_ITEM = new Item(buildingBlockSettings);
    public static final Item RAINBOW_MOONSTONE_SHARD_ITEM = new Item(buildingBlockSettings);

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
        registerItem("rainbow_moonstone_shard", RAINBOW_MOONSTONE_SHARD_ITEM);
    }


}
