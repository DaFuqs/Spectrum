package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SpectrumItemGroups {

    public static final ItemGroup ITEM_GROUP_GENERAL = FabricItemGroupBuilder.build(
            new Identifier(SpectrumCommon.MOD_ID, "general"),
            () -> new ItemStack(SpectrumBlocks.ALTAR));

    public static final ItemGroup ITEM_GROUP_WORLDGEN = FabricItemGroupBuilder.build(
            new Identifier(SpectrumCommon.MOD_ID, "worldgen"),
            () -> new ItemStack(SpectrumBlocks.CITRINE_BLOCK));

    public static final ItemGroup ITEM_GROUP_COLORED_WOOD = FabricItemGroupBuilder.build(
            new Identifier(SpectrumCommon.MOD_ID, "colored_wood"),
            () -> new ItemStack(SpectrumBlocks.LIME_LOG));

    public static final ItemGroup ITEM_GROUP_DECORATION = FabricItemGroupBuilder.build(
            new Identifier(SpectrumCommon.MOD_ID, "decoration"),
            () -> new ItemStack(SpectrumBlocks.MOONSTONE_CHISELED_CALCITE));


}
