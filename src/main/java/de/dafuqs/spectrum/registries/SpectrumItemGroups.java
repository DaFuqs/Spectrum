package de.dafuqs.spectrum.registries;

import com.unascribed.lib39.fractal.api.*;
import de.dafuqs.spectrum.*;
import net.fabricmc.fabric.api.client.itemgroup.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class SpectrumItemGroups {
	
	protected static final Identifier ITEM_GROUP_BACKGROUND_TEXTURE_IDENTIFIER = SpectrumCommon.locate("textures/gui/item_group.png"); // TODO: unused. Either re-add or remove
	protected static final Identifier ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER = SpectrumCommon.locate("textures/gui/item_group_button.png");
	
	protected static final ItemGroup MAIN = FabricItemGroupBuilder.create(SpectrumCommon.locate("main"))
			.icon(() -> new ItemStack(SpectrumBlocks.PEDESTAL_ALL_BASIC))
			.build()
			.hideName();
	
	public static final ItemSubGroup EQUIPMENT = ItemSubGroup.create(MAIN, SpectrumCommon.locate("equipment"));
	public static final ItemSubGroup FUNCTIONAL = ItemSubGroup.create(MAIN, SpectrumCommon.locate("functional"));
	public static final ItemSubGroup CONSUMABLES = ItemSubGroup.create(MAIN, SpectrumCommon.locate("consumables"));
	public static final ItemSubGroup RESOURCES = ItemSubGroup.create(MAIN, SpectrumCommon.locate("resources"));
	public static final ItemSubGroup PURE_RESOURCES = ItemSubGroup.create(MAIN, SpectrumCommon.locate("pure_resources"));
	public static final ItemSubGroup BLOCKS = ItemSubGroup.create(MAIN, SpectrumCommon.locate("blocks"));
	public static final ItemSubGroup DECORATION = ItemSubGroup.create(MAIN, SpectrumCommon.locate("decoration"));
	public static final ItemSubGroup COLORED_WOOD = ItemSubGroup.create(MAIN, SpectrumCommon.locate("colored_wood"));
	public static final ItemSubGroup MOB_HEADS = ItemSubGroup.create(MAIN, SpectrumCommon.locate("mob_heads"));
	public static final ItemSubGroup CREATURES = ItemSubGroup.create(MAIN, SpectrumCommon.locate("creatures"));
	public static final ItemSubGroup ENERGY = ItemSubGroup.create(MAIN, SpectrumCommon.locate("energy"));
	public static final ItemSubGroup CREATIVE = ItemSubGroup.create(MAIN, SpectrumCommon.locate("creative"));
	
}