package de.dafuqs.spectrum.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.dafuqs.revelationary.RevelationRegistry;
import de.dafuqs.revelationary.advancement_criteria.AdvancementGottenCriterion;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterEnchantable;
import de.dafuqs.spectrum.enchantments.SpectrumEnchantment;
import de.dafuqs.spectrum.enums.BuiltinGemstoneColor;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.mixin.accessors.LootTableAccessor;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
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
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressionSanityCommand {
	
	private static final List<Identifier> advancementGatingWarningWhitelist = new ArrayList<>() {{
		add(SpectrumCommon.locate("find_ancient_ruins"));
		add(SpectrumCommon.locate("hook_entity_with_molten_rod"));
		add(SpectrumCommon.locate("midgame/take_off_belt_overcharged"));
		add(SpectrumCommon.locate("midgame/craft_blacklisted_memory_fail"));
		add(SpectrumCommon.locate("midgame/craft_blacklisted_memory_success"));
		add(SpectrumCommon.locate("midgame/build_cinderhearth_structure_without_lava"));
		add(SpectrumCommon.locate("tap_aged_air"));
	}};
	
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register((CommandManager.literal("spectrum_test_progression_sanity").requires((source) -> {
			return source.hasPermissionLevel(2);
		}).executes((context) -> {
			return execute(context.getSource());
		})));
	}
	
	private static int execute(ServerCommandSource source) {
		SpectrumCommon.logInfo("##### SANITY CHECK START ######");
		
		// All blocks that do not have a mineable tag
		for (Map.Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntrySet()) {
			RegistryKey<Block> registryKey = entry.getKey();
			if (registryKey.getValue().getNamespace().equals(SpectrumCommon.MOD_ID)) {
				BlockState blockState = entry.getValue().getDefaultState();
				
				// unbreakable and break-instantly blocks do not need to have an entry
				if(blockState.getBlock().getHardness() <= 0) {
					continue;
				}
				
				if (!blockState.isIn(BlockTags.PICKAXE_MINEABLE)
						&& !blockState.isIn(BlockTags.AXE_MINEABLE)
						&& !blockState.isIn(BlockTags.SHOVEL_MINEABLE)
						&& !blockState.isIn(BlockTags.HOE_MINEABLE)
						&& !blockState.isIn(SpectrumBlockTags.EXEMPT_FROM_MINEABLE_DEBUG_CHECK)) {
					SpectrumCommon.logWarning("[SANITY: Mineable Tags] Block " + registryKey.getValue() + " is not contained in a any vanilla mineable tag.");
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
				if (!blockState.isIn(SpectrumBlockTags.EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK)) {
					if (lootTableID.equals(LootTables.EMPTY) || lootTableID.getPath().equals("blocks/air")) {
						SpectrumCommon.logWarning("[SANITY: Loot Tables] Block " + registryKey.getValue() + " has a non-existent loot table");
					} else {
						LootTable lootTable = source.getWorld().getServer().getLootManager().getTable(lootTableID);
						LootPool[] lootPools = ((LootTableAccessor) lootTable).getPools();
						if (lootPools.length == 0) {
							SpectrumCommon.logWarning("[SANITY: Loot Tables] Block " + registryKey.getValue() + " has an empty loot table");
						}
					}
				}
			}
		}
		
		// Statistic: Build an empty hashmap of hashmaps for counting used gem colors for each tier
		// This info can be used to balance usage a bit
		HashMap<PedestalRecipeTier, HashMap<BuiltinGemstoneColor, Integer>> usedColorsForEachTier = new HashMap<>();
		for (PedestalRecipeTier pedestalRecipeTier : PedestalRecipeTier.values()) {
			HashMap<BuiltinGemstoneColor, Integer> colorMap = new HashMap<>();
			for (BuiltinGemstoneColor gemstoneColor : BuiltinGemstoneColor.values()) {
				colorMap.put(gemstoneColor, 0);
			}
			usedColorsForEachTier.put(pedestalRecipeTier, colorMap);
		}
		
		MinecraftServer minecraftServer = source.getWorld().getServer();
		RecipeManager recipeManager = minecraftServer.getRecipeManager();
		ServerAdvancementLoader advancementLoader = minecraftServer.getAdvancementLoader();
		
		// Pedestal recipes that use gemstone powder not available at that tier yet
		for (PedestalCraftingRecipe pedestalRecipe : recipeManager.listAllOfType(SpectrumRecipeTypes.PEDESTAL)) {
			/* There are some recipes that use advanced ingredients by design
			   despite being of a low tier, like black colored lamps.
			   While the player does not have access to that yet it is no problem at all
			   To exclude those recipes in these warnings there is a boolean flag in the recipe jsons
			 */
			if (pedestalRecipe.getTier() == PedestalRecipeTier.BASIC || pedestalRecipe.getTier() == PedestalRecipeTier.SIMPLE) {
				if (pedestalRecipe.getGemstonePowderInputs().getOrDefault(BuiltinGemstoneColor.BLACK, 0) > 0) {
					SpectrumCommon.logWarning("[SANITY: Pedestal Recipe Ingredients] Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() + "' is using onyx powder as input! Players will not have access to Onyx at that tier");
				}
				if (pedestalRecipe.getGemstonePowderInputs().getOrDefault(BuiltinGemstoneColor.WHITE, 0) > 0) {
					SpectrumCommon.logWarning("[SANITY: Pedestal Recipe Ingredients] Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() + "' is using moonstone powder as input! Players will not have access to Moonstone at that tier");
				}
			} else if (pedestalRecipe.getTier() == PedestalRecipeTier.ADVANCED) {
				if (pedestalRecipe.getGemstonePowderInputs().getOrDefault(BuiltinGemstoneColor.WHITE, 0) > 0) {
					SpectrumCommon.logWarning("[SANITY: Pedestal Recipe Ingredients] Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() + "' is using moonstone powder as input! Players will not have access to Moonstone at that tier");
				}
			}
			for (Map.Entry<BuiltinGemstoneColor, Integer> gemstoneDustInput : pedestalRecipe.getGemstonePowderInputs().entrySet()) {
				usedColorsForEachTier.get(pedestalRecipe.getTier()).put(gemstoneDustInput.getKey(), usedColorsForEachTier.get(pedestalRecipe.getTier()).get(gemstoneDustInput.getKey()) + gemstoneDustInput.getValue());
			}
		}
		
		// Impossible to unlock recipes
		testRecipeUnlocks(SpectrumRecipeTypes.PEDESTAL, "Pedestal", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.ANVIL_CRUSHING, "Anvil Crushing", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.FUSION_SHRINE, "Fusion Shrine", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.ENCHANTER, "Enchanting", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, "Enchantment Upgrade", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, "Potion Workshop Brewing", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, "Potion Workshop Reagent", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.POTION_WORKSHOP_REACTING, "Potion Workshop Crafting", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING, "Midnight Solution Converting", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.SPIRIT_INSTILLING, "Spirit Instilling", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.INK_CONVERTING, "Ink Converting", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.CRYSTALLARIEUM, "Crystallarieum", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.CINDERHEARTH, "Cinderhearth", recipeManager, advancementLoader);
		testRecipeUnlocks(SpectrumRecipeTypes.TITRATION_BARREL, "Titration Barrel", recipeManager, advancementLoader);
		
		testIngredientsAndOutputInColorRegistry(SpectrumRecipeTypes.FUSION_SHRINE, "Fusion Shrine", recipeManager, advancementLoader);
		testIngredientsAndOutputInColorRegistry(SpectrumRecipeTypes.ENCHANTER, "Enchanting", recipeManager, advancementLoader);
		testIngredientsAndOutputInColorRegistry(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, "Enchantment Upgrade", recipeManager, advancementLoader);
		
		
		// Impossible to unlock block cloaks
		for (Map.Entry<Identifier, List<BlockState>> cloaks : RevelationRegistry.getBlockStateEntries().entrySet()) {
			if (advancementLoader.get(cloaks.getKey()) == null) {
				SpectrumCommon.logWarning("[SANITY: Block Cloaks] Advancement '" + cloaks.getKey().toString() + "' for block / item cloaking does not exist. Registered cloaks: " + cloaks.getValue().size());
			}
		}
		
		// "has advancement" criteria with nonexistent advancements
		for (Advancement advancement : advancementLoader.getAdvancements()) {
			for (AdvancementCriterion criterion : advancement.getCriteria().values()) {
				CriterionConditions conditions = criterion.getConditions();
				Identifier id = conditions.getId();
				if (id.equals(AdvancementGottenCriterion.ID) && conditions instanceof AdvancementGottenCriterion.Conditions hasAdvancementConditions) {
					Identifier advancementIdentifier = hasAdvancementConditions.getAdvancementIdentifier();
					Advancement advancementCriterionAdvancement = advancementLoader.get(advancementIdentifier);
					if (advancementCriterionAdvancement == null) {
						SpectrumCommon.logWarning("[SANITY: Has_Advancement Criteria] Advancement '" + advancement.getId() + "' references advancement '" + advancementIdentifier + "' that does not exist");
					}
				}
			}
		}
		
		// advancements that dont require parent
		for (Advancement advancement : advancementLoader.getAdvancements()) {
			if (advancement.getId().getNamespace().equals(SpectrumCommon.MOD_ID) && !advancement.getId().getPath().contains("hidden") && !advancement.getId().getPath().contains("progression") && !advancement.getId().getPath().contains("milestones") && advancement.getParent() != null) {
				Identifier previousAdvancementIdentifier = null;
				for (String[] requirement : advancement.getRequirements()) {
					if (requirement.length > 0 && requirement[0].equals("gotten_previous")) {
						CriterionConditions conditions = advancement.getCriteria().get("gotten_previous").getConditions();
						if (conditions instanceof AdvancementGottenCriterion.Conditions advancementConditions) {
							previousAdvancementIdentifier = advancementConditions.getAdvancementIdentifier();
							break;
						} else {
							SpectrumCommon.logWarning("[SANITY: Advancement Gating] Advancement '" + advancement.getId() + "' has a \"gotten_previous\" requirement, but its not spectrum:has_advancement?");
						}
					}
				}
				if (!advancementGatingWarningWhitelist.contains(advancement.getId())) {
					if (previousAdvancementIdentifier == null) {
						SpectrumCommon.logWarning("[SANITY: Advancement Gating] Advancement '" + advancement.getId() + "' does not have its parent set as requirement");
					} else {
						if (!advancement.getParent().getId().equals(previousAdvancementIdentifier)) {
							SpectrumCommon.logWarning("[SANITY: Advancement Gating] Advancement '" + advancement.getId() + "' has its \"gotten_previous\" advancement set to something else than their parent. Intended?");
						}
					}
				}
			}
		}
		
		// Pedestal Recipes in wrong data folder
		for (PedestalCraftingRecipe recipe : recipeManager.listAllOfType(SpectrumRecipeTypes.PEDESTAL)) {
			Identifier id = recipe.getId();
			if(id.getPath().contains("/glass/") || id.getPath().contains("/saplings/") || id.getPath().contains("/detectors/") || id.getPath().contains("/gem_lamps/") || id.getPath().contains("/decostones/")
					|| id.getPath().contains("/runes/") || id.getPath().contains("/pastel_network/") || id.getPath().contains("/gemstone_chimes/")|| id.getPath().contains("/pastel_network/") || id.getPath().contains("/player_only_glass/")) {
				continue;
			}
			
			if(recipe.getTier() == PedestalRecipeTier.BASIC && !id.getPath().contains("/tier1/")) {
				SpectrumCommon.logWarning("[SANITY: Pedestal Recipes] BASIC Recipe not in the correct tier folder: '" + id + "'");
			} else if(recipe.getTier() == PedestalRecipeTier.SIMPLE && !id.getPath().contains("/tier2/")) {
				SpectrumCommon.logWarning("[SANITY: Pedestal Recipes] SIMPLE Recipe not in the correct tier folder: '" + id + "'");
			} else if(recipe.getTier() == PedestalRecipeTier.ADVANCED && !id.getPath().contains("/tier3/")) {
				SpectrumCommon.logWarning("[SANITY: Pedestal Recipes] ADVANCED Recipe not in the correct tier folder: '" + id + "'");
			} else if(recipe.getTier() == PedestalRecipeTier.COMPLEX && !id.getPath().contains("/tier4/")) {
				SpectrumCommon.logWarning("[SANITY: Pedestal Recipes] COMPLEX Recipe not in the correct tier folder: '" + id + "'");
			}
		}
		
		// Item Crushing recipes with nonexistent sounds
		for (AnvilCrushingRecipe anvilCrushingRecipe : recipeManager.listAllOfType(SpectrumRecipeTypes.ANVIL_CRUSHING)) {
			SoundEvent soundEvent = anvilCrushingRecipe.getSoundEvent();
			if (soundEvent == null) {
				SpectrumCommon.logWarning("[SANITY: Item Crushing] Recipe '" + anvilCrushingRecipe.getId() + "' has a nonexistent sound set");
			}
		}
		
		// Enchantments with nonexistent Advancement cloak
		for (Map.Entry<RegistryKey<Enchantment>, Enchantment> enchantment : Registry.ENCHANTMENT.getEntrySet()) {
			if (enchantment.getValue() instanceof SpectrumEnchantment spectrumEnchantment) {
				Identifier advancementIdentifier = spectrumEnchantment.getUnlockAdvancementIdentifier();
				Advancement advancementCriterionAdvancement = advancementLoader.get(advancementIdentifier);
				if (advancementCriterionAdvancement == null) {
					SpectrumCommon.logWarning("[SANITY: Enchantments] Enchantment '" + enchantment.getKey().getValue() + "' references advancement '" + advancementIdentifier + "' that does not exist");
				}
			}
		}
		
		// EnchanterEnchantables with enchantability <= 0
		for (Map.Entry<RegistryKey<Item>, Item> item : Registry.ITEM.getEntrySet()) {
			Item i = item.getValue();
			if (i instanceof EnchanterEnchantable && i.getEnchantability() < 1) {
				SpectrumCommon.logWarning("[SANITY: Enchantability] Item '" + item.getKey().getValue() + "' is EnchanterEnchantable, but has enchantability of < 1");
			}
		}
		
		SpectrumCommon.logInfo("##### SANITY CHECK FINISHED ######");
		
		SpectrumCommon.logInfo("##### SANITY CHECK PEDESTAL RECIPE STATISTICS ######");
		for (PedestalRecipeTier pedestalRecipeTier : PedestalRecipeTier.values()) {
			HashMap<BuiltinGemstoneColor, Integer> entry = usedColorsForEachTier.get(pedestalRecipeTier);
			SpectrumCommon.logInfo("[SANITY: Pedestal Recipe Gemstone Usages] Gemstone Powder for tier " + StringUtils.leftPad(pedestalRecipeTier.toString(), 8) +
					": C:" + StringUtils.leftPad(entry.get(BuiltinGemstoneColor.CYAN).toString(), 3) +
					" M:" + StringUtils.leftPad(entry.get(BuiltinGemstoneColor.MAGENTA).toString(), 3) +
					" Y:" + StringUtils.leftPad(entry.get(BuiltinGemstoneColor.YELLOW).toString(), 3) +
					" K:" + StringUtils.leftPad(entry.get(BuiltinGemstoneColor.BLACK).toString(), 3) +
					" W:" + StringUtils.leftPad(entry.get(BuiltinGemstoneColor.WHITE).toString(), 3));
		}
		
		if (source.getEntity() instanceof ServerPlayerEntity serverPlayerEntity) {
			serverPlayerEntity.sendMessage(new TranslatableText("commands.spectrum.progression_sanity.success"), false);
		}
		
		return 0;
	}
	
	private static <R extends GatedRecipe> void testRecipeUnlocks(RecipeType<R> recipeType, String name, RecipeManager recipeManager, ServerAdvancementLoader advancementLoader) {
		for (GatedRecipe recipe : recipeManager.listAllOfType(recipeType)) {
			Identifier advancementIdentifier = recipe.getRequiredAdvancementIdentifier();
			if (advancementIdentifier != null && advancementLoader.get(advancementIdentifier) == null) {
				SpectrumCommon.logWarning("[SANITY: "+ name + " Recipe Unlocks] Advancement '" + recipe.getRequiredAdvancementIdentifier() + "' in recipe '" + recipe.getId() + "' does not exist");
			}
		}
	}
	
	private static <R extends GatedRecipe> void testIngredientsAndOutputInColorRegistry(RecipeType<R> recipeType, String name, RecipeManager recipeManager, ServerAdvancementLoader advancementLoader) {
		for (GatedRecipe recipe : recipeManager.listAllOfType(recipeType)) {
			for (Ingredient inputIngredient : recipe.getIngredients()) {
				for (ItemStack matchingItemStack : inputIngredient.getMatchingStacks()) {
					if (ColorRegistry.ITEM_COLORS.getMapping(matchingItemStack.getItem()).isEmpty()) {
						SpectrumCommon.logWarning("[SANITY: " + name + " Recipe] Input '" + Registry.ITEM.getId(matchingItemStack.getItem()) + "' in recipe '" + recipe.getId() + "', does not exist in the item color registry. Add it for nice effects!");
					}
				}
			}
			Item outputItem = recipe.getOutput().getItem();
			if (outputItem != null && outputItem != Items.AIR && ColorRegistry.ITEM_COLORS.getMapping(outputItem).isEmpty()) {
				SpectrumCommon.logWarning("[SANITY: " + name + " Recipe] Output '" + Registry.ITEM.getId(outputItem) + "' in recipe '" + recipe.getId() + "', does not exist in the item color registry. Add it for nice effects!");
			}
		}
	}
	
}
