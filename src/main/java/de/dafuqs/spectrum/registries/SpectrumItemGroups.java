package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlock;
import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.helpers.SpectrumEnchantmentHelper;
import de.dafuqs.spectrum.items.magic_items.BottomlessBundleItem;
import de.dafuqs.spectrum.items.magic_items.KnowledgeGemItem;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipeSerializer;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.gui.ItemGroupButton;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.Map;

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
			
			addButton(ItemGroupButton.discord("https://discord.com/invite/EXU9XFXT8a")); // TODO: Add item group background texture, as soon as owo supports it
			addButton(ItemGroupButton.github("https://github.com/DaFuqs/Spectrum"));
			addButton(ItemGroupButton.curseforge("https://www.curseforge.com/minecraft/mc-mods/spectrum"));
			addButton(ItemGroupButton.modrinth("https://modrinth.com/mod/spectrum"));
		}
		
		@Override
		public void appendStacks(DefaultedList<ItemStack> stacks) {
			super.appendStacks(stacks);
			
			if(this.getSelectedTab() == ITEM_GROUP_GENERAL.getTab(1)) {
				// early game tools
				stacks.add(SpectrumItems.MULTITOOL.getDefaultStack());
				stacks.add(SpectrumItems.LOOTING_FALCHION.getDefaultStack());
				stacks.add(SpectrumItems.SILKER_PICKAXE.getDefaultStack());
				stacks.add(SpectrumItems.FORTUNE_PICKAXE.getDefaultStack());
				stacks.add(SpectrumItems.VOIDING_PICKAXE.getDefaultStack());
				stacks.add(SpectrumItems.RESONANT_PICKAXE.getDefaultStack());
				
				// emergency armor
				stacks.add(SpectrumItems.EMERGENCY_HELMET.getDefaultStack());
				stacks.add(SpectrumItems.EMERGENCY_CHESTPLATE.getDefaultStack());
				stacks.add(SpectrumItems.EMERGENCY_LEGGINGS.getDefaultStack());
				stacks.add(SpectrumItems.EMERGENCY_BOOTS.getDefaultStack());
				
				// bedrock tools
				stacks.add(SpectrumItems.BEDROCK_PICKAXE.getDefaultStack());
				stacks.add(SpectrumItems.BEDROCK_AXE.getDefaultStack());
				stacks.add(SpectrumItems.BEDROCK_SHOVEL.getDefaultStack());
				stacks.add(SpectrumItems.BEDROCK_SWORD.getDefaultStack());
				stacks.add(SpectrumItems.BEDROCK_HOE.getDefaultStack());
				stacks.add(SpectrumItems.BEDROCK_BOW.getDefaultStack());
				stacks.add(SpectrumItems.BEDROCK_CROSSBOW.getDefaultStack());
				stacks.add(SpectrumItems.BEDROCK_SHEARS.getDefaultStack());
				stacks.add(SpectrumItems.BEDROCK_FISHING_ROD.getDefaultStack());
				
				// bedrock armor
				stacks.add(SpectrumItems.BEDROCK_HELMET.getDefaultStack());
				stacks.add(SpectrumItems.BEDROCK_CHESTPLATE.getDefaultStack());
				stacks.add(SpectrumItems.BEDROCK_LEGGINGS.getDefaultStack());
				stacks.add(SpectrumItems.BEDROCK_BOOTS.getDefaultStack());
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
			addTab(Icon.of(SpectrumItems.BOTTOMLESS_BUNDLE), "predefined_items", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			
			addButton(ItemGroupButton.discord("https://discord.gg/VHUPpHrj")); // TODO: Add item group background texture, as soon as owo supports it
			addButton(ItemGroupButton.github("https://github.com/DaFuqs/Spectrum"));
			addButton(ItemGroupButton.curseforge("https://www.curseforge.com/minecraft/mc-mods/spectrum"));
			addButton(ItemGroupButton.modrinth("https://modrinth.com/mod/spectrum"));
		}
		
		@Override
		public void appendStacks(DefaultedList<ItemStack> stacks) {
			super.appendStacks(stacks);
			
			if(this.getSelectedTab() == ITEM_GROUP_BLOCKS.getTab(3)) {
				// fully filled Knowledge Gem
				stacks.add(KnowledgeGemItem.getKnowledgeDropStackWithXP(10000));
				
				for(CMYKColor color : CMYKColor.all()) {
					stacks.add(SpectrumItems.INK_FLASK.getFullStack(color));
				}
				stacks.add(SpectrumItems.INK_ASSORTMENT.getFullStack());
				stacks.add(SpectrumItems.PIGMENT_PALETTE.getFullStack());
				stacks.add(SpectrumItems.ARTISTS_PALETTE.getFullStack());

				// Bottomless Bundles willed with useful, basic materials
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.COBBLESTONE.getDefaultStack(), 20000));
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.STONE.getDefaultStack(), 20000));
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.DEEPSLATE.getDefaultStack(), 20000));
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.OAK_PLANKS.getDefaultStack(), 20000));
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.SAND.getDefaultStack(), 20000));
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.GRAVEL.getDefaultStack(), 20000));
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.ARROW.getDefaultStack(), 20000));
				
				// Fully Enchanted Enchanter Enchantables
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.BOTTOMLESS_BUNDLE, Enchantments.POWER, SpectrumEnchantments.VOIDING));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.ENDER_SPLICE, Enchantments.UNBREAKING, SpectrumEnchantments.RESONANCE));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.EXCHANGE_STAFF, Enchantments.FORTUNE));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.EXCHANGE_STAFF, Enchantments.SILK_TOUCH));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.KNOWLEDGE_GEM, Enchantments.EFFICIENCY, Enchantments.QUICK_CHARGE));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.NATURES_STAFF, Enchantments.EFFICIENCY));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.RADIANCE_STAFF, Enchantments.INFINITY));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.GLEAMING_PIN, Enchantments.POWER));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.SEVEN_LEAGUE_BOOTS, Enchantments.POWER));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.TAKE_OFF_BELT, Enchantments.POWER, Enchantments.FEATHER_FALLING));
				
				// Enchanted books with the max upgrade level available via Enchantment Upgrading
				HashMap<Enchantment, Integer> highestEnchantmentLevels = new HashMap<>();
				for(EnchantmentUpgradeRecipe enchantmentUpgradeRecipe : EnchantmentUpgradeRecipeSerializer.enchantmentUpgradeRecipesToInject) {
					Enchantment enchantment = enchantmentUpgradeRecipe.getEnchantment();
					int destinationLevel = enchantmentUpgradeRecipe.getEnchantmentDestinationLevel();
					if(highestEnchantmentLevels.containsKey(enchantment)) {
						if(highestEnchantmentLevels.get(enchantment) < destinationLevel) {
							highestEnchantmentLevels.put(enchantment, destinationLevel);
						}
					} else {
						highestEnchantmentLevels.put(enchantment, destinationLevel);
					}
				}
				for(Map.Entry<Enchantment, Integer> s : highestEnchantmentLevels.entrySet()) {
					if(s.getValue() > s.getKey().getMaxLevel()) {
						stacks.add(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(s.getKey(), s.getValue())));
					}
				}
				
				// all memories that have spirit instiller recipes
				Item memoryItem = SpectrumBlocks.MEMORY.asItem();
				for(SpiritInstillerRecipe recipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.SPIRIT_INSTILLER_RECIPE)) {
					if(recipe.getOutput().isOf(memoryItem)) {
						stacks.add(recipe.getOutput());
					}
				}
			}
		}
		
		@Override
		public ItemStack createIcon() {
			return new ItemStack(SpectrumBlocks.MOONSTONE_CHISELED_CALCITE);
		}
		
	};

}
