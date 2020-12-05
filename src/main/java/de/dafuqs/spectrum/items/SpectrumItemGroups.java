package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SpectrumItemGroups {

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
            new Identifier(SpectrumCommon.MOD_ID, "general"),
            () -> new ItemStack(SpectrumItems.BEDROCK_PICKAXE));

    public static final ItemGroup ITEM_GROUP_BUILDING = FabricItemGroupBuilder.build(
            new Identifier(SpectrumCommon.MOD_ID, "building"),
            () -> new ItemStack(SpectrumBlocks.CITRINE_BLOCK));

}
