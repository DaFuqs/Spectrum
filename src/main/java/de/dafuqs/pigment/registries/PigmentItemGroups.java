package de.dafuqs.pigment.registries;

import de.dafuqs.pigment.PigmentCommon;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class PigmentItemGroups {

    public static final ItemGroup ITEM_GROUP_GENERAL = FabricItemGroupBuilder.build(
            new Identifier(PigmentCommon.MOD_ID, "general"),
            () -> new ItemStack(PigmentBlocks.ALTAR));

    public static final ItemGroup ITEM_GROUP_WORLDGEN = FabricItemGroupBuilder.build(
            new Identifier(PigmentCommon.MOD_ID, "worldgen"),
            () -> new ItemStack(PigmentBlocks.CITRINE_BLOCK));

    public static final ItemGroup ITEM_GROUP_COLORED_WOOD = FabricItemGroupBuilder.build(
            new Identifier(PigmentCommon.MOD_ID, "colored_wood"),
            () -> new ItemStack(PigmentBlocks.LIME_LOG));

    public static final ItemGroup ITEM_GROUP_DECORATION = FabricItemGroupBuilder.build(
            new Identifier(PigmentCommon.MOD_ID, "decoration"),
            () -> new ItemStack(PigmentBlocks.MOONSTONE_CHISELED_CALCITE));

    public static final ItemGroup ITEM_GROUP_PREENCHANTED_TOOLS = FabricItemGroupBuilder.create(
            new Identifier(PigmentCommon.MOD_ID, "tools"))
            .icon(() -> new ItemStack(PigmentBlocks.ALTAR))
            .appendItems(stacks -> {
                // early game tools
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.MULTITOOL));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.LOOTING_FALCHION));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.SILKER_PICKAXE));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.FORTUNE_PICKAXE));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.VOIDING_PICKAXE));

                // bedrock tools
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_PICKAXE));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_AXE));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_SHOVEL));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_SWORD));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_HOE));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_BOW));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_CROSSBOW));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_SHEARS));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_FISHING_ROD));

                // bedrock armor
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_HELMET));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_CHESTPLATE));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_LEGGINGS));
                stacks.add(PigmentDefaultEnchantments.getEnchantedItemStack(PigmentItems.BEDROCK_BOOTS));
            })
            .build();

}
