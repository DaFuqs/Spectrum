package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlock;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.gui.ItemGroupButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class SpectrumItemGroups {
	
	private static final Identifier ITEM_GROUP_BACKGROUND_TEXTURE_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/item_group.png");
	private static final Identifier ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/item_group_button.png");
	
	public static final OwoItemGroup ITEM_GROUP_GENERAL = new OwoItemGroup(new Identifier(SpectrumCommon.MOD_ID, "general")) {

		@Override
		protected void setup() {
			setCustomTexture(ITEM_GROUP_BACKGROUND_TEXTURE_IDENTIFIER);
			
			addTab(Icon.of(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST), "general", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			addTab(Icon.of(SpectrumItems.BEDROCK_PICKAXE), "tools", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			addTab(Icon.of(SpectrumBlocks.CITRINE_BLOCK), "worldgen", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			addTab(Icon.of(SpectrumItems.TOPAZ_SHARD), "items", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			
			addButton(ItemGroupButton.discord("https://discord.gg/VHUPpHrj")); // TODO: Add item group background texture, as soon as owo supports it
			addButton(ItemGroupButton.github("https://github.com/DaFuqs/Spectrum"));
			addButton(ItemGroupButton.curseforge("https://www.curseforge.com/minecraft/mc-mods/spectrum"));
			addButton(ItemGroupButton.modrinth("https://modrinth.com/mod/spectrum"));
		}
		
		@Override
		public void appendStacks(DefaultedList<ItemStack> stacks) {
			super.appendStacks(stacks);
			
			if(this.getSelectedTab() == ITEM_GROUP_GENERAL.getTab(1)) {
				// early game tools
				ItemStack knowledgeGemStack = new ItemStack(SpectrumItems.KNOWLEDGE_GEM);
				ExperienceStorageItem.addStoredExperience(knowledgeGemStack, 10000);
				stacks.add(knowledgeGemStack);
				
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
			}
		}
		
		@Override
		public ItemStack createIcon() {
			return new ItemStack(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST);
		}
	};
	
	public static final OwoItemGroup ITEM_GROUP_BLOCKS = new OwoItemGroup(new Identifier(SpectrumCommon.MOD_ID, "blocks")) {

		@Override
		protected void setup() {
			setCustomTexture(ITEM_GROUP_BACKGROUND_TEXTURE_IDENTIFIER);
			
			addTab(Icon.of(SpectrumBlocks.MOONSTONE_CHISELED_CALCITE), "decoration", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			addTab(Icon.of(SpectrumBlocks.LIME_LOG), "colored_wood", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			addTab(Icon.of(SpectrumBlocks.getMobHead(SpectrumSkullBlock.Type.PUFFERFISH)), "mob_heads", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			
			addButton(ItemGroupButton.discord("https://discord.gg/VHUPpHrj")); // TODO: Add item group background texture, as soon as owo supports it
			addButton(ItemGroupButton.github("https://github.com/DaFuqs/Spectrum"));
			addButton(ItemGroupButton.curseforge("https://www.curseforge.com/minecraft/mc-mods/spectrum"));
			addButton(ItemGroupButton.modrinth("https://modrinth.com/mod/spectrum"));
		}
		
		@Override
		public ItemStack createIcon() {
			return new ItemStack(SpectrumBlocks.MOONSTONE_CHISELED_CALCITE);
		}
		
	};

}
