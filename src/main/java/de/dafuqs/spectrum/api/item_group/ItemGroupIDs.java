package de.dafuqs.spectrum.api.item_group;

import de.dafuqs.fractal.api.*;
import de.dafuqs.spectrum.*;
import net.minecraft.resources.*;

public class ItemGroupIDs {
	
	public static final ResourceLocation MAIN_GROUP_ID = SpectrumCommon.locate("main");
	
	public static final ResourceLocation SUBTAB_EQUIPMENT = SpectrumCommon.locate("equipment");
	public static final ResourceLocation SUBTAB_FUNCTIONAL = SpectrumCommon.locate("functional");
	public static final ResourceLocation SUBTAB_CUISINE = SpectrumCommon.locate("cuisine");
	public static final ResourceLocation SUBTAB_RESOURCES = SpectrumCommon.locate("resources");
	public static final ResourceLocation SUBTAB_PURE_RESOURCES = SpectrumCommon.locate("pure_resources");
	public static final ResourceLocation SUBTAB_BLOCKS = SpectrumCommon.locate("blocks");
	public static final ResourceLocation SUBTAB_DECORATION = SpectrumCommon.locate("decoration");
	public static final ResourceLocation SUBTAB_COLORED_WOOD = SpectrumCommon.locate("colored_wood");
	public static final ResourceLocation SUBTAB_MOB_HEADS = SpectrumCommon.locate("mob_heads");
	public static final ResourceLocation SUBTAB_CREATURES = SpectrumCommon.locate("creatures");
	public static final ResourceLocation SUBTAB_ENERGY = SpectrumCommon.locate("energy");
	public static final ResourceLocation SUBTAB_CREATIVE = SpectrumCommon.locate("creative");
	
	
	public static final ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/creative_item_group.png");
	
	// Sprites (put into \resources\assets\fractal\textures\gui\sprites\container\creative_inventory)
	public static final ResourceLocation SCROLLBAR_ENABLED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/scrollbar_enabled");
	public static final ResourceLocation SCROLLBAR_DISABLED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/scrollbar_disabled");
	
	public static final ResourceLocation SUBTAB_SELECTED_TEXTURE_LEFT = SpectrumCommon.locate("container/creative_inventory/subtab_selected_left");
	public static final ResourceLocation SUBTAB_UNSELECTED_TEXTURE_LEFT = SpectrumCommon.locate("container/creative_inventory/subtab_unselected_left");
	public static final ResourceLocation SUBTAB_SELECTED_TEXTURE_RIGHT = SpectrumCommon.locate("container/creative_inventory/subtab_selected_right");
	public static final ResourceLocation SUBTAB_UNSELECTED_TEXTURE_RIGHT = SpectrumCommon.locate("container/creative_inventory/subtab_unselected_right");
	
	public static final ResourceLocation TAB_TOP_FIRST_SELECTED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/tab_top_first_selected");
	public static final ResourceLocation TAB_TOP_SELECTED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/tab_top_selected");
	public static final ResourceLocation TAB_TOP_LAST_SELECTED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/tab_top_last_selected");
	public static final ResourceLocation TAB_TOP_FIRST_UNSELECTED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/tab_top_first_unselected");
	public static final ResourceLocation TAB_TOP_UNSELECTED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/tab_top_unselected");
	public static final ResourceLocation TAB_TOP_LAST_UNSELECTED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/tab_top_last_unselected");
	public static final ResourceLocation TAB_BOTTOM_FIRST_SELECTED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/tab_bottom_first_selected");
	public static final ResourceLocation TAB_BOTTOM_SELECTED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/tab_bottom_selected");
	public static final ResourceLocation TAB_BOTTOM_LAST_SELECTED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/tab_bottom_last_selected");
	public static final ResourceLocation TAB_BOTTOM_FIRST_UNSELECTED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/tab_bottom_first_unselected");
	public static final ResourceLocation TAB_BOTTOM_UNSELECTED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/tab_bottom_unselected");
	public static final ResourceLocation TAB_BOTTOM_LAST_UNSELECTED_TEXTURE = SpectrumCommon.locate("container/creative_inventory/tab_bottom_last_unselected");
	
	public static final ItemSubGroupStyle STYLE = new ItemSubGroupStyle.Builder()
			.background(BACKGROUND_TEXTURE)
			.scrollbar(SCROLLBAR_ENABLED_TEXTURE, SCROLLBAR_DISABLED_TEXTURE)
			.subtab(SUBTAB_SELECTED_TEXTURE_LEFT, SUBTAB_UNSELECTED_TEXTURE_LEFT, SUBTAB_SELECTED_TEXTURE_RIGHT, SUBTAB_UNSELECTED_TEXTURE_RIGHT)
			.tab(TAB_TOP_FIRST_SELECTED_TEXTURE, TAB_TOP_SELECTED_TEXTURE, TAB_TOP_LAST_SELECTED_TEXTURE, TAB_TOP_FIRST_UNSELECTED_TEXTURE, TAB_TOP_UNSELECTED_TEXTURE, TAB_TOP_LAST_UNSELECTED_TEXTURE,
					TAB_BOTTOM_FIRST_SELECTED_TEXTURE, TAB_BOTTOM_SELECTED_TEXTURE, TAB_BOTTOM_LAST_SELECTED_TEXTURE, TAB_BOTTOM_FIRST_UNSELECTED_TEXTURE, TAB_BOTTOM_UNSELECTED_TEXTURE, TAB_BOTTOM_LAST_UNSELECTED_TEXTURE)
			.build();
	
}