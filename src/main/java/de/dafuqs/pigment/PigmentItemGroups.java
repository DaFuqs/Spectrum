package de.dafuqs.pigment;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.PigmentBlocks;
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


}
