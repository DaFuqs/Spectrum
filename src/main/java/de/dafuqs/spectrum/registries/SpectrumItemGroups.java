package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlock;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SpectrumItemGroups {

    public static final ItemGroup ITEM_GROUP_GENERAL = FabricItemGroupBuilder.build(
            new Identifier(SpectrumCommon.MOD_ID, "general"),
            () -> new ItemStack(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST));

    public static final ItemGroup ITEM_GROUP_RESOURCES = FabricItemGroupBuilder.build(
            new Identifier(SpectrumCommon.MOD_ID, "resources"),
            () -> new ItemStack(SpectrumBlocks.CITRINE_BLOCK));

    public static final ItemGroup ITEM_GROUP_COLORED_WOOD = FabricItemGroupBuilder.build(
            new Identifier(SpectrumCommon.MOD_ID, "colored_wood"),
            () -> new ItemStack(SpectrumBlocks.LIME_LOG));

    public static final ItemGroup ITEM_GROUP_DECORATION = FabricItemGroupBuilder.build(
            new Identifier(SpectrumCommon.MOD_ID, "decoration"),
            () -> new ItemStack(SpectrumBlocks.MOONSTONE_CHISELED_CALCITE));

    public static final ItemGroup ITEM_GROUP_MOB_HEADS = FabricItemGroupBuilder.build(
            new Identifier(SpectrumCommon.MOD_ID, "mob_heads"),
            () -> new ItemStack(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.PUFFERFISH)));

    // Currently .appendItems() replaces the previous content completely
    // so we have to use a separate creative tab for all those tools
    // https://github.com/FabricMC/fabric/issues/324
    public static final ItemGroup ITEM_GROUP_PREENCHANTED_TOOLS = FabricItemGroupBuilder.create(
            new Identifier(SpectrumCommon.MOD_ID, "tools"))
            .icon(() -> SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_PICKAXE))
            .appendItems(stacks -> {
                // early game tools
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.MULTITOOL));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.LOOTING_FALCHION));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.SILKER_PICKAXE));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.FORTUNE_PICKAXE));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.VOIDING_PICKAXE));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.RESONANT_PICKAXE));

                // emergency armor
                stacks.add(new ItemStack(SpectrumItems.EMERGENCY_HELMET));
                stacks.add(new ItemStack(SpectrumItems.EMERGENCY_CHESTPLATE));
                stacks.add(new ItemStack(SpectrumItems.EMERGENCY_LEGGINGS));
                stacks.add(new ItemStack(SpectrumItems.EMERGENCY_BOOTS));

                // bedrock tools
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_PICKAXE));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_AXE));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_SHOVEL));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_SWORD));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_HOE));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_BOW));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_CROSSBOW));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_SHEARS));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_FISHING_ROD));

                // bedrock armor
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_HELMET));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_CHESTPLATE));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_LEGGINGS));
                stacks.add(SpectrumDefaultEnchantments.getEnchantedItemStack(SpectrumItems.BEDROCK_BOOTS));
            })
            .build();

}
