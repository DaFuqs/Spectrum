package de.dafuqs.spectrum.registries;

import de.dafuqs.fractal.api.*;
import de.dafuqs.spectrum.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class SpectrumItemGroups {

	public static final Identifier TEXTURE = SpectrumCommon.locate("textures/gui/item_group.png");

	public static final ItemGroup MAIN = FabricItemGroup.builder(SpectrumCommon.locate("main"))
			.icon(() -> new ItemStack(SpectrumBlocks.PEDESTAL_ALL_BASIC))
		    .noRenderedName()
			.build();

	public static final ItemSubGroup EQUIPMENT = ItemSubGroup.create(MAIN, SpectrumCommon.locate("equipment"), TEXTURE);
	public static final ItemSubGroup FUNCTIONAL = ItemSubGroup.create(MAIN, SpectrumCommon.locate("functional"), TEXTURE);
	public static final ItemSubGroup CUISINE = ItemSubGroup.create(MAIN, SpectrumCommon.locate("cuisine"), TEXTURE);
	public static final ItemSubGroup RESOURCES = ItemSubGroup.create(MAIN, SpectrumCommon.locate("resources"), TEXTURE);
	public static final ItemSubGroup PURE_RESOURCES = ItemSubGroup.create(MAIN, SpectrumCommon.locate("pure_resources"), TEXTURE);
	public static final ItemSubGroup BLOCKS = ItemSubGroup.create(MAIN, SpectrumCommon.locate("blocks"), TEXTURE);
	public static final ItemSubGroup DECORATION = ItemSubGroup.create(MAIN, SpectrumCommon.locate("decoration"), TEXTURE);
	public static final ItemSubGroup COLORED_WOOD = ItemSubGroup.create(MAIN, SpectrumCommon.locate("colored_wood"), TEXTURE);
	public static final ItemSubGroup MOB_HEADS = ItemSubGroup.create(MAIN, SpectrumCommon.locate("mob_heads"), TEXTURE);
	public static final ItemSubGroup CREATURES = ItemSubGroup.create(MAIN, SpectrumCommon.locate("creatures"), TEXTURE);
	public static final ItemSubGroup ENERGY = ItemSubGroup.create(MAIN, SpectrumCommon.locate("energy"), TEXTURE);
	public static final ItemSubGroup CREATIVE = ItemSubGroup.create(MAIN, SpectrumCommon.locate("creative"), TEXTURE);

}