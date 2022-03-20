package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterEnchantable;
import de.dafuqs.spectrum.enchantments.SpectrumEnchantment;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.mixin.accessors.LootTableAccessor;
import de.dafuqs.spectrum.progression.BlockCloakManager;
import de.dafuqs.spectrum.progression.advancement.HasAdvancementCriterion;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopBrewingRecipe;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopCraftingRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.color.ColorRegistry;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressionSanityCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register((CommandManager.literal("spectrum_test_progression_sanity").requires((source) -> {
			return source.hasPermissionLevel(2);
		}).executes((context) -> {
			return execute(context.getSource());
		})));
	}

	private static int execute(ServerCommandSource source) {
		SpectrumCommon.log(Level.INFO, "##### SANITY CHECK START ######");

		// All blocks that do not have a mineable tag
		for (Map.Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntrySet()) {
			RegistryKey<Block> registryKey = entry.getKey();
			if (registryKey.getValue().getNamespace().equals(SpectrumCommon.MOD_ID)) {
				BlockState blockState = entry.getValue().getDefaultState();
				if (!blockState.isIn(BlockTags.PICKAXE_MINEABLE)
						&& !blockState.isIn(BlockTags.AXE_MINEABLE)
						&& !blockState.isIn(BlockTags.SHOVEL_MINEABLE)
						&& !blockState.isIn(BlockTags.HOE_MINEABLE)
						&& !blockState.isIn(SpectrumBlockTags.EXEMPT_FROM_MINEABLE_DEBUG_CHECK)) {
					SpectrumCommon.log(Level.WARN, "[SANITY: Mineable Tags] Block " + registryKey.getValue() + " is not contained in a any vanilla mineable tag.");
				}
			}
		}
		
		// All blocks without a loot table
		for (Map.Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntrySet()) {
			RegistryKey<Block> registryKey = entry.getKey();
			if (registryKey.getValue().getNamespace().equals(SpectrumCommon.MOD_ID)) {
				Block block = entry.getValue();
				BlockState blockState = entry.getValue().getDefaultState();
				Identifier lootTableID = block.getLootTableId();
				if(!blockState.isIn(SpectrumBlockTags.EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK)) {
					if (lootTableID.equals(LootTables.EMPTY) || lootTableID.getPath().equals("blocks/air")) {
						SpectrumCommon.log(Level.WARN, "[SANITY: Loot Tables] Block " + registryKey.getValue() + "has a non-existent loot table");
					} else {
						LootTable lootTable = source.getWorld().getServer().getLootManager().getTable(lootTableID);
						LootPool[] lootPools = ((LootTableAccessor) lootTable).getPools();
						if (lootPools.length == 0) {
							SpectrumCommon.log(Level.WARN, "[SANITY: Loot Tables] Block " + registryKey.getValue() + " has an empty loot table");
						}
					}
				}
			}
		}
		
		// Statistic: Build an empty hashmap of hashmaps for counting used gem colors for each tier
		// This info can be used to balance the usage times a bit
		HashMap<PedestalRecipeTier, HashMap<GemstoneColor, Integer>> usedColorsForEachTier = new HashMap<>();
		for (PedestalRecipeTier pedestalRecipeTier : PedestalRecipeTier.values()) {
			HashMap<GemstoneColor, Integer> colorMap = new HashMap<>();
			for(GemstoneColor gemstoneColor : GemstoneColor.values()) {
				colorMap.put(gemstoneColor, 0);
			}
			usedColorsForEachTier.put(pedestalRecipeTier, colorMap);
		}

		// Pedestal recipes that use gemstone powder not available at that tier yet
		for(PedestalCraftingRecipe pedestalRecipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.PEDESTAL)) {
			/* There are some recipes that use advanced ingredients by design
			   despite being of a low tier, like black colored lamps.
			   While the player does not have access to that yet it is no problem at all
			   To exclude those recipes in these warnings there is a boolean flag in the recipe jsons
			 */
			if (pedestalRecipe.getTier() == PedestalRecipeTier.BASIC || pedestalRecipe.getTier() == PedestalRecipeTier.SIMPLE) {
				if (pedestalRecipe.getGemstonePowderInputs().get(GemstoneColor.BLACK) > 0) {
					SpectrumCommon.log(Level.WARN, "[SANITY: Pedestal Recipe Ingredients] Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() + "' is using onyx powder as input! Players will not have access to Onyx at that tier");
				}
				if (pedestalRecipe.getGemstonePowderInputs().get(GemstoneColor.WHITE) > 0) {
					SpectrumCommon.log(Level.WARN, "[SANITY: Pedestal Recipe Ingredients] Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() + "' is using moonstone powder as input! Players will not have access to Moonstone at that tier");
				}
			} else if (pedestalRecipe.getTier() == PedestalRecipeTier.ADVANCED) {
				if (pedestalRecipe.getGemstonePowderInputs().get(GemstoneColor.WHITE) > 0) {
					SpectrumCommon.log(Level.WARN, "[SANITY: Pedestal Recipe Ingredients] Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() + "' is using moonstone powder as input! Players will not have access to Moonstone at that tier");
				}
			}
			for(Map.Entry<GemstoneColor, Integer> gemstoneDustInput : pedestalRecipe.getGemstonePowderInputs().entrySet()) {
				usedColorsForEachTier.get(pedestalRecipe.getTier()).put(gemstoneDustInput.getKey(), usedColorsForEachTier.get(pedestalRecipe.getTier()).get(gemstoneDustInput.getKey()) + gemstoneDustInput.getValue());
			}
		}
		
		// Impossible to unlock pedestal recipes
		for(PedestalCraftingRecipe recipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.PEDESTAL)) {
			List<Identifier> advancementIdentifiers = recipe.getRequiredAdvancementIdentifiers();
			if(advancementIdentifiers == null || advancementIdentifiers.isEmpty()) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Pedestal Recipe Unlocks] Pedestal recipe '" + recipe.getId() + "' has no required advancements set!");
			} else {
				for (Identifier advancementIdentifier : advancementIdentifiers) {
					if (!doesAdvancementExist(advancementIdentifier)) {
						SpectrumCommon.log(Level.WARN, "[SANITY: Pedestal Recipe Unlocks] Advancement '" + advancementIdentifier + "' in '" + recipe.getId() + "' does not exist");
					}
				}
			}
		}

		// Impossible to unlock fusion shrine recipes
		for(FusionShrineRecipe recipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.FUSION_SHRINE)) {
			if(!doesAdvancementExist(recipe.getRequiredAdvancementIdentifier())) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Fusion Shrine Recipe Unlocks] Advancement '" + recipe.getRequiredAdvancementIdentifier() + "' in recipe '" + recipe.getId() + "' does not exist");
			}
			for(Ingredient inputIngredient : recipe.getIngredients()) {
				for(ItemStack matchingItemStack : inputIngredient.getMatchingStacks()) {
					if (ColorRegistry.ITEM_COLORS.getMapping(matchingItemStack.getItem()).isEmpty()) {
						SpectrumCommon.log(Level.WARN, "[SANITY: Fusion Shrine Recipe] Input '" + Registry.ITEM.getId(matchingItemStack.getItem()) + "' in recipe '" + recipe.getId() + "', does not exist in the item color registry. Add it for nice effects!");
					}
				}
			}
			Item outputItem = recipe.getOutput().getItem();
			if(outputItem != Items.AIR && ColorRegistry.ITEM_COLORS.getMapping(outputItem).isEmpty()) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Fusion Shrine Recipe] Output '" + Registry.ITEM.getId(outputItem) + "' in recipe '" + recipe.getId() + "', does not exist in the item color registry. Add it for nice effects!");
			}
		}
		
		// Impossible to unlock potion workshop brewing recipes
		for(PotionWorkshopBrewingRecipe recipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.POTION_WORKSHOP_BREWING)) {
			if(!doesAdvancementExist(recipe.getRequiredAdvancementIdentifier())) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Potion Workshop Brewing Unlocks] Advancement '" + recipe.getRequiredAdvancementIdentifier() + "' in recipe '" + recipe.getId() + "' does not exist");
			}
		}
		
		// Impossible to unlock potion workshop crafting recipes
		for(PotionWorkshopCraftingRecipe recipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING)) {
			if(!doesAdvancementExist(recipe.getRequiredAdvancementIdentifier())) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Potion Workshop Crafting Unlocks] Advancement '" + recipe.getRequiredAdvancementIdentifier() + "' in recipe '" + recipe.getId() + "' does not exist");
			}
		}
		
		// Impossible to unlock spirit instiller recipes
		for(SpiritInstillerRecipe recipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.SPIRIT_INSTILLER_RECIPE)) {
			if(!doesAdvancementExist(recipe.getRequiredAdvancementIdentifier())) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Spirit Instiller Recipe Unlocks] Advancement '" + recipe.getRequiredAdvancementIdentifier() + "' in recipe '" + recipe.getId() + "' does not exist");
			}
		}
		
		// Enchanting recipes
		for(EnchanterRecipe enchanterRecipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.ENCHANTER)) {
			if(!doesAdvancementExist(enchanterRecipe.getRequiredAdvancementIdentifier())) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Enchanting Recipe Unlocks] Advancement '" + enchanterRecipe.getRequiredAdvancementIdentifier() + "' in recipe '" + enchanterRecipe.getId() + "' does not exist");
			}
			for(Ingredient inputIngredient : enchanterRecipe.getIngredients()) {
				for(ItemStack matchingItemStack : inputIngredient.getMatchingStacks()) {
					if (ColorRegistry.ITEM_COLORS.getMapping(matchingItemStack.getItem()).isEmpty()) {
						SpectrumCommon.log(Level.WARN, "[SANITY: Enchanting Recipe] Input '" + Registry.ITEM.getId(matchingItemStack.getItem()) + "' in recipe '" + enchanterRecipe.getId() + "', does not exist in the item color registry. Add it for nice effects!");
					}
				}
			}
			Item outputItem = enchanterRecipe.getOutput().getItem();
			if(ColorRegistry.ITEM_COLORS.getMapping(outputItem).isEmpty()) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Enchanting Recipe] Output '" + Registry.ITEM.getId(outputItem) + "' in recipe '" + enchanterRecipe.getId() + "', does not exist in the item color registry. Add it for nice effects!");
			}
		}
		
		// Enchantment upgrade recipes
		for(EnchantmentUpgradeRecipe enchantmentUpgradeRecipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE)) {
			if(!doesAdvancementExist(enchantmentUpgradeRecipe.getRequiredAdvancementIdentifier())) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Enchantment Upgrade Recipe Unlocks] Advancement '" + enchantmentUpgradeRecipe.getRequiredAdvancementIdentifier() + "' in recipe '" + enchantmentUpgradeRecipe.getId() + "' does not exist");
			}
			for(Ingredient inputIngredient : enchantmentUpgradeRecipe.getIngredients()) {
				for(ItemStack matchingItemStack : inputIngredient.getMatchingStacks()) {
					if (ColorRegistry.ITEM_COLORS.getMapping(matchingItemStack.getItem()).isEmpty()) {
						SpectrumCommon.log(Level.WARN, "[SANITY: Enchantment Upgrade Recipe] Input '" + Registry.ITEM.getId(matchingItemStack.getItem()) + "' in recipe '" + enchantmentUpgradeRecipe.getId() + "', does not exist in the item color registry. Add it for nice effects!");
					}
				}
			}
			Item outputItem = enchantmentUpgradeRecipe.getOutput().getItem();
			if(ColorRegistry.ITEM_COLORS.getMapping(outputItem).isEmpty()) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Enchantment Upgrade Recipe] Output '" + Registry.ITEM.getId(outputItem) + "' in recipe '" + enchantmentUpgradeRecipe.getId() + "', does not exist in the item color registry. Add it for nice effects!");
			}
		}

		// Impossible to unlock block cloaks
		for(Map.Entry<Identifier, List<Cloakable>> cloaks : BlockCloakManager.getAdvancementIdentifiersAndRegisteredCloaks().entrySet()) {
			if(!doesAdvancementExist(cloaks.getKey())) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Block Cloaks] Advancement '" + cloaks.getKey().toString() + "' for block / item cloaking does not exist. Registered cloaks: " + cloaks.getValue().size());
			}
		}

		// "has advancement" criteria with nonexistent advancements
		for(Advancement advancement : SpectrumCommon.minecraftServer.getAdvancementLoader().getAdvancements()) {
			for(AdvancementCriterion criterion : advancement.getCriteria().values()) {
				CriterionConditions conditions = criterion.getConditions();
				Identifier id = conditions.getId();
				if(id.equals(HasAdvancementCriterion.ID) && conditions instanceof  HasAdvancementCriterion.Conditions hasAdvancementConditions) {
					Identifier advancementIdentifier = hasAdvancementConditions.getAdvancementIdentifier();
					Advancement advancementCriterionAdvancement = SpectrumCommon.minecraftServer.getAdvancementLoader().get(advancementIdentifier);
					if(advancementCriterionAdvancement == null) {
						SpectrumCommon.log(Level.WARN, "[SANITY: Has_Advancement Criteria] Advancement '" + advancement.getId() + "' references advancement '" + advancementIdentifier  + "' that does not exist");
					}
				}
			}
		}
		
		// advancements that dont require parent
		for(Advancement advancement : SpectrumCommon.minecraftServer.getAdvancementLoader().getAdvancements()) {
			if(advancement.getId().getNamespace().equals(SpectrumCommon.MOD_ID) && !advancement.getId().getPath().contains("hidden") && !advancement.getId().getPath().contains("progression") && !advancement.getId().getPath().contains("milestones") && advancement.getParent() != null) {
				Identifier previousAdvancementIdentifier = null;
				for(String[] requirement : advancement.getRequirements()) {
					if(requirement.length > 0 && requirement[0].equals("gotten_previous")) {
						CriterionConditions conditions = advancement.getCriteria().get("gotten_previous").getConditions();
						if(conditions instanceof HasAdvancementCriterion.Conditions advancementConditions) {
							previousAdvancementIdentifier = advancementConditions.getAdvancementIdentifier();
							break;
						} else {
							SpectrumCommon.log(Level.WARN, "[SANITY: Advancement Gating] Advancement '" + advancement.getId() + "' has a \"gotten_previous\" requirement, but its not spectrum:has_advancement?");
						}
					}
				}
				if(previousAdvancementIdentifier == null) {
					SpectrumCommon.log(Level.WARN, "[SANITY: Advancement Gating] Advancement '" + advancement.getId() + "' has not set its parent set as requirement");
				} else {
					if(!advancement.getParent().getId().equals(previousAdvancementIdentifier)) {
						SpectrumCommon.log(Level.WARN, "[SANITY: Advancement Gating] Advancement '" + advancement.getId() + "' has its \"gotten_previous\" advancement set to something else than their parent. Intended?");
					}
				}
			}
		}
		
		// Anvil Crushing recipes with nonexistent sounds
		for(AnvilCrushingRecipe anvilCrushingRecipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.ANVIL_CRUSHING)) {
			SoundEvent soundEvent = anvilCrushingRecipe.getSoundEvent();
			if(soundEvent == null) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Item Crushing] Recipe '" + anvilCrushingRecipe.getId() + "' has a nonexistent sound set");
			}
		}
		
		// Enchantments with nonexistent Advancement cloak
		for(Map.Entry<RegistryKey<Enchantment>, Enchantment> enchantment : Registry.ENCHANTMENT.getEntrySet()) {
			if(enchantment.getValue() instanceof SpectrumEnchantment spectrumEnchantment) {
				Identifier advancementIdentifier = spectrumEnchantment.getUnlockAdvancementIdentifier();
				Advancement advancementCriterionAdvancement = SpectrumCommon.minecraftServer.getAdvancementLoader().get(advancementIdentifier);
				if(advancementCriterionAdvancement == null) {
					SpectrumCommon.log(Level.WARN, "[SANITY: Enchantments] Enchantment '" + enchantment.getKey().getValue() + "' references advancement '" + advancementIdentifier  + "' that does not exist");
				}
			}
		}
		
		// EnchanterEnchantables with enchantability <= 0
		for(Map.Entry<RegistryKey<Item>, Item> item : Registry.ITEM.getEntrySet()) {
			Item i = item.getValue();
			if(i instanceof EnchanterEnchantable && i.getEnchantability() < 1) {
				SpectrumCommon.log(Level.WARN, "[SANITY: Enchantability] Item '" + item.getKey().getValue() + "' is EnchanterEnchantable, but has enchantability of < 1");
			}
		}
		
		SpectrumCommon.log(Level.INFO, "##### SANITY CHECK FINISHED ######");
		
		SpectrumCommon.log(Level.INFO, "##### SANITY CHECK PEDESTAL RECIPE STATISTICS ######");
		for(PedestalRecipeTier pedestalRecipeTier : PedestalRecipeTier.values()) {
			HashMap<GemstoneColor, Integer> entry = usedColorsForEachTier.get(pedestalRecipeTier);
			SpectrumCommon.log(Level.INFO, "[SANITY: Pedestal Recipe Gemstone Usages] Gemstone Powder for tier " + StringUtils.leftPad(pedestalRecipeTier.toString(), 8) +
					": C:" + StringUtils.leftPad(entry.get(GemstoneColor.CYAN).toString(), 3) +
					" M:" + StringUtils.leftPad(entry.get(GemstoneColor.MAGENTA).toString(), 3) +
					" Y:" + StringUtils.leftPad(entry.get(GemstoneColor.YELLOW).toString(), 3) +
					" K:" + StringUtils.leftPad(entry.get(GemstoneColor.BLACK).toString(), 3) +
					" W:" + StringUtils.leftPad(entry.get(GemstoneColor.WHITE).toString(), 3));
		}
		
		if(source.getEntity() instanceof ServerPlayerEntity serverPlayerEntity) {
			serverPlayerEntity.sendMessage(new TranslatableText("commands.spectrum.progression_sanity.success"), false);
		}
		
		return 0;
	}

	private static boolean doesAdvancementExist(Identifier identifier) {
		return SpectrumCommon.minecraftServer.getAdvancementLoader().get(identifier) != null;
	}

}
