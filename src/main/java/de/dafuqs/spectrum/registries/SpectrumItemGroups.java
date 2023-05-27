package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import io.wispforest.owo.itemgroup.*;
import io.wispforest.owo.itemgroup.gui.*;
import net.minecraft.util.*;

public class SpectrumItemGroups {
	
	private static final Identifier ITEM_GROUP_BACKGROUND_TEXTURE_IDENTIFIER = SpectrumCommon.locate("textures/gui/item_group.png");
	private static final Identifier ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER = SpectrumCommon.locate("textures/gui/item_group_button.png");
	
	public static final OwoItemGroup ITEM_GROUP_GENERAL = OwoItemGroup.builder(SpectrumCommon.locate("general"), () -> Icon.of(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST))
			.initializer(owoItemGroup -> {
				owoItemGroup.addTab(Icon.of(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST), "general", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER, false);
				owoItemGroup.addTab(Icon.of(SpectrumItems.BEDROCK_PICKAXE), "equipment", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER, false);
				owoItemGroup.addTab(Icon.of(SpectrumItems.RESTORATION_TEA), "consumables", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER, false);
				owoItemGroup.addTab(Icon.of(SpectrumBlocks.CITRINE_BLOCK), "resources", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER, false);

				owoItemGroup.addButton(ItemGroupButton.discord(owoItemGroup, "https://discord.com/invite/EXU9XFXT8a"));
				owoItemGroup.addButton(ItemGroupButton.github(owoItemGroup, "https://github.com/DaFuqs/Spectrum"));
				owoItemGroup.addButton(ItemGroupButton.curseforge(owoItemGroup, "https://www.curseforge.com/minecraft/mc-mods/spectrum"));
				owoItemGroup.addButton(ItemGroupButton.modrinth(owoItemGroup, "https://modrinth.com/mod/spectrum"));
			})
			.build();

	public static final OwoItemGroup ITEM_GROUP_BLOCKS = OwoItemGroup.builder(SpectrumCommon.locate("blocks"), () -> Icon.of(SpectrumBlocks.MOONSTONE_CHISELED_CALCITE))
			.customTexture(ITEM_GROUP_BACKGROUND_TEXTURE_IDENTIFIER)
			.initializer(owoItemGroup -> {
				owoItemGroup.addTab(Icon.of(SpectrumBlocks.MOONSTONE_CHISELED_CALCITE), "decoration", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER, false);
				owoItemGroup.addTab(Icon.of(SpectrumBlocks.LIME_LOG), "colored_wood", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER, false);
				owoItemGroup.addTab(Icon.of(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.PUFFERFISH)), "mob_heads", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER, false);
				owoItemGroup.addTab(Icon.of(SpectrumItems.BOTTOMLESS_BUNDLE), "predefined_items", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER, false);

				owoItemGroup.addButton(ItemGroupButton.discord(owoItemGroup, "https://discord.gg/VHUPpHrj"));
				owoItemGroup.addButton(ItemGroupButton.github(owoItemGroup, "https://github.com/DaFuqs/Spectrum"));
				owoItemGroup.addButton(ItemGroupButton.curseforge(owoItemGroup, "https://www.curseforge.com/minecraft/mc-mods/spectrum"));
				owoItemGroup.addButton(ItemGroupButton.modrinth(owoItemGroup, "https://modrinth.com/mod/spectrum"));
			})
			.build();
}
