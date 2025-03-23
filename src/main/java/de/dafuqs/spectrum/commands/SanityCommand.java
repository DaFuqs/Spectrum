package de.dafuqs.spectrum.commands;

import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.book.conditions.*;
import com.klikli_dev.modonomicon.book.conditions.context.*;
import com.klikli_dev.modonomicon.book.entries.*;
import com.klikli_dev.modonomicon.book.page.*;
import com.klikli_dev.modonomicon.data.*;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.tree.*;
import de.dafuqs.revelationary.*;
import de.dafuqs.revelationary.advancement_criteria.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.deeper_down.flora.*;
import de.dafuqs.spectrum.blocks.gemstone.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.mininglevel.v1.*;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.*;
import net.minecraft.server.command.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.apache.commons.lang3.*;

import java.util.*;

public class SanityCommand {
	
	private static final Identifier WIP_ADVANCEMENT_ID = SpectrumCommon.locate("__wip");
	
	private static final List<Identifier> ADVANCEMENT_GATING_WARNING_WHITELIST = List.of(
			SpectrumCommon.locate("find_preservation_ruins"),                     // does not have a prerequisite
			SpectrumCommon.locate("fail_to_glitch_into_preservation_ruin"),       // does not have a prerequisite
			SpectrumCommon.locate("midgame/craft_blacklisted_memory_success"),    // its parent is 2 parents in
			SpectrumCommon.locate("lategame/collect_myceylon"),                   // its parent is 2 parents in
			SpectrumCommon.locate("lategame/strike_up_hummingstone_hymn")         // its parent is 2 parents in
	);
	
	private static final List<Identifier> GUIDEBOOK_WARNING_WHITELIST = List.of(
			SpectrumCommon.locate("cuisine/cookbooks/brewers_handbook")           // "*_fluid" mod compat recipe page
	);
	
	public static void register(LiteralCommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> sanity = CommandManager.literal("sanity")
				.requires((source) -> source.hasPermissionLevel(2))
				.executes((context) -> execute(context.getSource(), SpectrumCommon.MOD_ID)).build();
		ArgumentCommandNode<ServerCommandSource, String> modId = CommandManager.argument("mod_id", StringArgumentType.word())
				.executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "mod_id"))).build();
		
		sanity.addChild(modId);
		root.addChild(sanity);
	}
	
	private static int execute(ServerCommandSource source, String modId) {
		SpectrumCommon.logInfo("##### SANITY CHECK START ######");

		// All blocks that do not have a mineable tag
		for (Map.Entry<RegistryKey<Block>, Block> entry : Registries.BLOCK.getEntrySet()) {
			RegistryKey<Block> registryKey = entry.getKey();
			if (registryKey.getValue().getNamespace().equals(modId)) {
				BlockState blockState = entry.getValue().getDefaultState();

				// unbreakable or instabreak blocks do not need to have an entry
				if (blockState.getBlock().getHardness() <= 0) {
					continue;
				}

				if (!blockState.isIn(BlockTags.PICKAXE_MINEABLE)
						&& !blockState.isIn(BlockTags.AXE_MINEABLE)
						&& !blockState.isIn(BlockTags.SHOVEL_MINEABLE)
						&& !blockState.isIn(BlockTags.HOE_MINEABLE)
						&& !blockState.isIn(FabricMineableTags.SHEARS_MINEABLE)
						&& !blockState.isIn(FabricMineableTags.SWORD_MINEABLE)
						&& !blockState.isIn(BlockTags.SWORD_EFFICIENT)
						&& !blockState.isIn(SpectrumBlockTags.EXEMPT_FROM_MINEABLE_DEBUG_CHECK)) {
					SpectrumCommon.logWarning("[SANITY: Mineable Tags] Block " + registryKey.getValue() + " is not contained in a any vanilla mineable tag.");
				}
			}
		}

		// All blocks without a loot table
		for (Map.Entry<RegistryKey<Block>, Block> entry : Registries.BLOCK.getEntrySet()) {
			RegistryKey<Block> registryKey = entry.getKey();
			if (registryKey.getValue().getNamespace().equals(modId)) {
				Block block = entry.getValue();
				
				if (block instanceof PlacedItemBlock) {
					continue; // that one always drops itself via code
				}
				if (block instanceof SpectrumBuddingBlock) {
					continue; // does not have any drop by default
				}
				if (block instanceof WeepingGalaFrondsBlock) {
					continue; // Fronds do not drop anything by default
				}
				
				BlockState blockState = entry.getValue().getDefaultState();
				Identifier lootTableID = block.getLootTableId();
				
				// unbreakable blocks do not need to have a loot table
				if (blockState.getBlock().getHardness() <= -1) {
					continue;
				}
				
				if (!blockState.isIn(SpectrumBlockTags.EXEMPT_FROM_LOOT_TABLE_DEBUG_CHECK)) {
					if (lootTableID.equals(LootTables.EMPTY) || lootTableID.getPath().equals("blocks/air")) {
						SpectrumCommon.logWarning("[SANITY: Loot Tables] Block " + registryKey.getValue() + " has a non-existent loot table (" + lootTableID + ")");
					} else {
						LootTable lootTable = source.getWorld().getServer().getLootManager().getLootTable(lootTableID);
						LootPool[] lootPools = lootTable.pools;
						if (lootPools.length == 0) {
							SpectrumCommon.logWarning("[SANITY: Loot Tables] Block " + registryKey.getValue() + " has an empty loot table");
						}
					}
				}
			}
		}
		
		// Statistic: Build an empty map of maps for counting used gem colors for each tier
		// This info can be used to balance usage a bit
		Map<PedestalRecipeTier, Map<GemstoneColor, Integer>> usedColorsForEachTier = new HashMap<>();
		for (PedestalRecipeTier pedestalRecipeTier : PedestalRecipeTier.values()) {
			Map<GemstoneColor, Integer> colorMap = new HashMap<>();
			for (GemstoneColor gemstoneColor : BuiltinGemstoneColor.values()) {
				colorMap.put(gemstoneColor, 0);
			}
			usedColorsForEachTier.put(pedestalRecipeTier, colorMap);
		}

		MinecraftServer minecraftServer = source.getWorld().getServer();
		RecipeManager recipeManager = minecraftServer.getRecipeManager();
		ServerAdvancementLoader advancementLoader = minecraftServer.getAdvancementLoader();

		// Pedestal recipes that use gemstone powder not available at that tier yet
		for (PedestalRecipe pedestalRecipe : recipeManager.listAllOfType(SpectrumRecipeTypes.PEDESTAL)) {
			if (pedestalRecipe.getTier() == PedestalRecipeTier.BASIC || pedestalRecipe.getTier() == PedestalRecipeTier.SIMPLE) {
				if (pedestalRecipe.getPowderInputs().getOrDefault(BuiltinGemstoneColor.BLACK, 0) > 0) {
					SpectrumCommon.logWarning("[SANITY: Pedestal Recipe Ingredients] Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() + "' is using onyx powder as input! Players will not have access to Onyx at that tier");
				}
			}
			if (pedestalRecipe.getTier() != PedestalRecipeTier.COMPLEX) {
				if (pedestalRecipe.getPowderInputs().getOrDefault(BuiltinGemstoneColor.WHITE, 0) > 0) {
					SpectrumCommon.logWarning("[SANITY: Pedestal Recipe Ingredients] Pedestal recipe '" + pedestalRecipe.getId() + "' of tier '" + pedestalRecipe.getTier() + "' is using moonstone powder as input! Players will not have access to Moonstone at that tier");
				}
			}
			for (Map.Entry<GemstoneColor, Integer> powderInput : pedestalRecipe.getPowderInputs().entrySet()) {
				usedColorsForEachTier.get(pedestalRecipe.getTier()).put(powderInput.getKey(), usedColorsForEachTier.get(pedestalRecipe.getTier()).get(powderInput.getKey()) + powderInput.getValue());
			}
		}
		
		// Checking for missing translation strings
		for (Map.Entry<RegistryKey<Item>, Item> item : Registries.ITEM.getEntrySet()) {
			if (!item.getKey().getValue().getNamespace().equals(modId)) {
				continue;
			}
			
			if (!Language.getInstance().hasTranslation(item.getValue().getTranslationKey())) {
				SpectrumCommon.logWarning("[SANITY: Item Lang] Missing translation string " + item.getValue().getTranslationKey());
			}
		}
		for (Map.Entry<RegistryKey<Block>, Block> block : Registries.BLOCK.getEntrySet()) {
			if (!block.getKey().getValue().getNamespace().equals(modId)) {
				continue;
			}
			if (!Language.getInstance().hasTranslation(block.getValue().getTranslationKey())) {
				SpectrumCommon.logWarning("[SANITY: Block Lang] Missing translation string " + block.getValue().getTranslationKey());
			}
		}
		for (Map.Entry<RegistryKey<EntityType<?>>, EntityType<?>> entityType : Registries.ENTITY_TYPE.getEntrySet()) {
			if (!entityType.getKey().getValue().getNamespace().equals(modId)) {
				continue;
			}
			if (!Language.getInstance().hasTranslation(entityType.getValue().getTranslationKey())) {
				SpectrumCommon.logWarning("[SANITY: EntityType Lang] Missing translation string " + entityType.getValue().getTranslationKey());
			}
		}
		for (Map.Entry<RegistryKey<Enchantment>, Enchantment> entityType : Registries.ENCHANTMENT.getEntrySet()) {
			if (!entityType.getKey().getValue().getNamespace().equals(modId)) {
				continue;
			}
			if (!Language.getInstance().hasTranslation(entityType.getValue().getTranslationKey())) {
				SpectrumCommon.logWarning("[SANITY: Enchantment Lang] Missing translation string " + entityType.getValue().getTranslationKey());
			}
			if (!Language.getInstance().hasTranslation(entityType.getValue().getTranslationKey() + ".desc")) {
				SpectrumCommon.logWarning("[SANITY: Enchantment Lang] Missing description string " + entityType.getValue().getTranslationKey() + ".desc");
			}
		}
		for (Map.Entry<RegistryKey<StatusEffect>, StatusEffect> entityType : Registries.STATUS_EFFECT.getEntrySet()) {
			if (!entityType.getKey().getValue().getNamespace().equals(modId)) {
				continue;
			}
			if (!Language.getInstance().hasTranslation(entityType.getValue().getTranslationKey())) {
				SpectrumCommon.logWarning("[SANITY: Status Effect Lang] Missing translation string " + entityType.getValue().getTranslationKey());
			}
		}
		for (Map.Entry<RegistryKey<EntityAttribute>, EntityAttribute> entityType : Registries.ATTRIBUTE.getEntrySet()) {
			if (!entityType.getKey().getValue().getNamespace().equals(modId)) {
				continue;
			}
			if (!Language.getInstance().hasTranslation(entityType.getValue().getTranslationKey())) {
				SpectrumCommon.logWarning("[SANITY: Attribute Lang] Missing translation string " + entityType.getValue().getTranslationKey());
			}
		}
		for (Map.Entry<RegistryKey<EntityAttribute>, EntityAttribute> entityType : Registries.ATTRIBUTE.getEntrySet()) {
			if (!entityType.getKey().getValue().getNamespace().equals(modId)) {
				continue;
			}
			if (!Language.getInstance().hasTranslation(entityType.getValue().getTranslationKey())) {
				SpectrumCommon.logWarning("[SANITY: Attribute Lang] Missing translation string " + entityType.getValue().getTranslationKey());
			}
			if (!Language.getInstance().hasTranslation(entityType.getValue().getTranslationKey() + ".desc")) {
				SpectrumCommon.logWarning("[SANITY: Attribute Lang] Missing description string " + entityType.getValue().getTranslationKey() + ".desc");
			}
		}
		
		
		// recipe groups without localization
		Set<String> recipeGroups = new HashSet<>();
		recipeManager.keys().forEach(identifier -> {
			Optional<? extends Recipe<?>> recipe = recipeManager.get(identifier);
			if (recipe.isPresent()) {
				if (recipe.get() instanceof GatedSpectrumRecipe<?> gatedSpectrumRecipe) {
					String group = gatedSpectrumRecipe.getGroup();
					if (group == null) {
						SpectrumCommon.logWarning("Recipe with null group found! :" + gatedSpectrumRecipe.getId());
					} else if (!group.isEmpty()) {
						recipeGroups.add(group);
					}
				}
			}
		});
		for (String recipeGroup : recipeGroups) {
			if (!Language.getInstance().hasTranslation("recipeGroup.spectrum." + recipeGroup)) {
				SpectrumCommon.logWarning("[SANITY: Recipe Group Lang] Recipe group " + recipeGroup + " is not localized.");
			}
		}
		
		// Impossible to unlock recipes
		for (Recipe<?> recipe : recipeManager.values()) {
			if (recipe instanceof GatedRecipe<?> gatedRecipe) {
				Identifier advancementIdentifier = gatedRecipe.getRequiredAdvancementIdentifier();
				if (advancementIdentifier != null && advancementLoader.get(advancementIdentifier) == null) {
					SpectrumCommon.logWarning("[SANITY: " + gatedRecipe.getRecipeTypeShortID() + " Recipe Unlocks] Advancement '" + gatedRecipe.getRequiredAdvancementIdentifier() + "' in recipe '" + recipe.getId() + "' does not exist");
				}
			}
		}
		
		// Recipes that spawn effects based on item color,
		// but input / output items do not have a color registered
		DynamicRegistryManager registryManager = source.getRegistryManager();
		testIngredientsAndOutputInColorRegistry(SpectrumRecipeTypes.FUSION_SHRINE, "Fusion Shrine", recipeManager, registryManager);
		testIngredientsAndOutputInColorRegistry(SpectrumRecipeTypes.ENCHANTER, "Enchanting", recipeManager, registryManager);
		testIngredientsAndOutputInColorRegistry(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, "Enchantment Upgrade", recipeManager, registryManager);
		testIngredientsAndOutputInColorRegistry(SpectrumRecipeTypes.SPIRIT_INSTILLING, "Spirit Instiller", recipeManager, registryManager);
		
		
		// Impossible to unlock block cloaks
		for (Map.Entry<Identifier, List<BlockState>> cloaks : RevelationRegistry.getBlockStateEntries().entrySet()) {
			if (advancementLoader.get(cloaks.getKey()) == null) {
				SpectrumCommon.logWarning("[SANITY: Block Cloaks] Advancement '" + cloaks.getKey().toString() + "' for block cloaking does not exist. Registered cloaks: " + cloaks.getValue().size());
			}
		}
		for (Map.Entry<Identifier, List<Item>> cloaks : RevelationRegistry.getItemEntries().entrySet()) {
			if (advancementLoader.get(cloaks.getKey()) == null) {
				SpectrumCommon.logWarning("[SANITY: Item Cloaks] Advancement '" + cloaks.getKey().toString() + "' for item cloaking does not exist. Registered cloaks: " + cloaks.getValue().size());
			}
		}
		
		for (Advancement advancement : advancementLoader.getAdvancements()) {
			for (AdvancementCriterion criterion : advancement.getCriteria().values()) {
				CriterionConditions conditions = criterion.getConditions();

				// "has advancement" criteria with nonexistent advancements
				if (conditions instanceof AdvancementGottenCriterion.Conditions hasAdvancementConditions) {
					Identifier advancementIdentifier = hasAdvancementConditions.getAdvancementIdentifier();
					if (advancementIdentifier.equals(WIP_ADVANCEMENT_ID)) {
						continue;
					}
					Advancement advancementCriterionAdvancement = advancementLoader.get(advancementIdentifier);
					if (advancementCriterionAdvancement == null) {
						SpectrumCommon.logWarning("[SANITY: Has_Advancement Criteria] Advancement '" + advancement.getId() + "' references advancement '" + advancementIdentifier + "' that does not exist");
					}
					// "advancement count" criteria with nonexistent advancements
				} else if (conditions instanceof AdvancementCountCriterion.Conditions hasAdvancementConditions) {
					for (Identifier advancementIdentifier : hasAdvancementConditions.getAdvancementIdentifiers()) {
						if (advancementIdentifier.equals(WIP_ADVANCEMENT_ID)) {
							continue;
						}
						Advancement advancementCriterionAdvancement = advancementLoader.get(advancementIdentifier);
						if (advancementCriterionAdvancement == null) {
							SpectrumCommon.logWarning("[SANITY: Advancement_Count Criteria] Advancement '" + advancement.getId() + "' references advancement '" + advancementIdentifier + "' that does not exist");
						}
					}
				}
			}
		}
		
		// advancements that don't require their parent (or parents of their parents, for 'collecting' type advancements)
		for (Advancement advancement : advancementLoader.getAdvancements()) {
			String path = advancement.getId().getPath();
			if (advancement.getId().getNamespace().equals(modId) && !path.startsWith("hidden") && !path.startsWith("progression") && !path.startsWith("milestones") && advancement.getParent() != null) {
				Identifier previousAdvancementIdentifier = null;
				for (String[] requirement : advancement.getRequirements()) {
					if (requirement.length > 0 && requirement[0].equals("gotten_previous")) {
						CriterionConditions conditions = advancement.getCriteria().get("gotten_previous").getConditions();
						if (conditions instanceof AdvancementGottenCriterion.Conditions advancementConditions) {
							previousAdvancementIdentifier = advancementConditions.getAdvancementIdentifier();
							break;
						} else {
							SpectrumCommon.logWarning("[SANITY: Advancement Gating] Advancement '" + advancement.getId() + "' has a \"gotten_previous\" requirement, but its not revelationary:advancement_gotten");
						}
					}
				}
				if (!ADVANCEMENT_GATING_WARNING_WHITELIST.contains(advancement.getId())) {
					if (previousAdvancementIdentifier == null) {
						SpectrumCommon.logWarning("[SANITY: Advancement Gating] Advancement '" + advancement.getId() + "' does not have its parent set as requirement");
					} else {
						Advancement parent = advancement.getParent();
						if (parent.getId().equals(previousAdvancementIdentifier)) {
							continue;
						}
						if (parent.getParent() != null && parent.getParent().getId().equals(previousAdvancementIdentifier)) {
							continue; // "collect stuff" advancements with its 2nd parent being the requirement
						}
						SpectrumCommon.logWarning("[SANITY: Advancement Gating] Advancement '" + advancement.getId() + "' has its \"gotten_previous\" advancement set to something else than their parent. Intended?");
					}
				}
			}
		}

		// Pedestal Recipes in wrong data folder
		for (PedestalRecipe recipe : recipeManager.listAllOfType(SpectrumRecipeTypes.PEDESTAL)) {
			Identifier id = recipe.getId();
			if (id.getPath().startsWith("mod_integration/") || id.getPath().contains("/glass/") || id.getPath().contains("/saplings/") || id.getPath().contains("/detectors/") || id.getPath().contains("/gemstone_lights/") || id.getPath().contains("/decostones/")
					|| id.getPath().contains("/runes/") || id.getPath().contains("/pastel_network/") || id.getPath().contains("/gemstone_chimes/") || id.getPath().contains("/pastel_network/") || id.getPath().contains("/semi_permeable_glass/")
					|| id.getPath().contains("/colored_lamps/") || id.getPath().contains("/colored_spore_blossoms/") || id.getPath().contains("/glowblocks/")) {
				continue;
			}

			if (recipe.getTier() == PedestalRecipeTier.BASIC && !id.getPath().contains("/tier1/")) {
				SpectrumCommon.logWarning("[SANITY: Pedestal Recipes] BASIC recipe not in the correct tier folder: '" + id + "'");
			} else if (recipe.getTier() == PedestalRecipeTier.SIMPLE && !id.getPath().contains("/tier2/")) {
				SpectrumCommon.logWarning("[SANITY: Pedestal Recipes] SIMPLE recipe not in the correct tier folder: '" + id + "'");
			} else if (recipe.getTier() == PedestalRecipeTier.ADVANCED && !id.getPath().contains("/tier3/")) {
				SpectrumCommon.logWarning("[SANITY: Pedestal Recipes] ADVANCED recipe not in the correct tier folder: '" + id + "'");
			} else if (recipe.getTier() == PedestalRecipeTier.COMPLEX && !id.getPath().contains("/tier4/")) {
				SpectrumCommon.logWarning("[SANITY: Pedestal Recipes] COMPLEX recipe not in the correct tier folder: '" + id + "'");
			}
		}

		// Item Crushing recipes with nonexistent sounds
		for (AnvilCrushingRecipe anvilCrushingRecipe : recipeManager.listAllOfType(SpectrumRecipeTypes.ANVIL_CRUSHING)) {
			SoundEvent soundEvent = anvilCrushingRecipe.getSoundEvent();
			if (soundEvent == null) {
				SpectrumCommon.logWarning("[SANITY: Item Crushing] Recipe '" + anvilCrushingRecipe.getId() + "' has a nonexistent sound set");
			}
		}

		// Enchantments with nonexistent unlock enchantment
		for (Map.Entry<RegistryKey<Enchantment>, Enchantment> enchantment : Registries.ENCHANTMENT.getEntrySet()) {
			if (enchantment.getValue() instanceof SpectrumEnchantment spectrumEnchantment) {
				Identifier advancementIdentifier = spectrumEnchantment.getUnlockAdvancementIdentifier();
				Advancement advancementCriterionAdvancement = advancementLoader.get(advancementIdentifier);
				if (advancementCriterionAdvancement == null) {
					SpectrumCommon.logWarning("[SANITY: Enchantments] Enchantment '" + enchantment.getKey().getValue() + "' references advancement '" + advancementIdentifier + "' that does not exist");
				}
			}
		}

		// ExtendedEnchantables with enchantability <= 0 (unable to be enchanted) or not set to be enchantable
		for (Map.Entry<RegistryKey<Item>, Item> item : Registries.ITEM.getEntrySet()) {
			Item i = item.getValue();
			if (i instanceof ExtendedEnchantable) {
				if (!new ItemStack(i).isEnchantable()) {
					SpectrumCommon.logWarning("[SANITY: Enchantability] Item '" + item.getKey().getValue() + "' is not set to be enchantable.");
				}
				if (i.getEnchantability() < 1) {
					SpectrumCommon.logWarning("[SANITY: Enchantability] Item '" + item.getKey().getValue() + "' is ExtendedEnchantable, but has enchantability of < 1");
				}
			}
		}
		
		// Enchantments without a recipe
		Map<Enchantment, DyeColor> craftingColors = new HashMap<>();
		Map<Enchantment, DyeColor> upgradeColors = new HashMap<>();
		for (EnchanterRecipe recipe : recipeManager.listAllOfType(SpectrumRecipeTypes.ENCHANTER)) {
			ItemStack output = recipe.getOutput(source.getRegistryManager());
			if (output.getItem() == Items.ENCHANTED_BOOK) {
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(output);
				if (!enchantments.isEmpty()) {
					for (Ingredient ingredient : recipe.getIngredients()) {
						for (ItemStack matchingStack : ingredient.getMatchingStacks()) {
							if (matchingStack.getItem() instanceof PigmentItem pigmentItem) {
								craftingColors.put(enchantments.keySet().stream().toList().get(0), pigmentItem.getColor());
							}
						}
					}
				}
			}
		}
		for (EnchantmentUpgradeRecipe recipe : recipeManager.listAllOfType(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE)) {
			ItemStack output = recipe.getOutput(source.getRegistryManager());
			if (output.getItem() == Items.ENCHANTED_BOOK) {
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(output);
				if (!enchantments.isEmpty() && recipe.getRequiredItem() instanceof PigmentItem pigmentItem) {
					upgradeColors.put(enchantments.keySet().stream().toList().get(0), pigmentItem.getColor());
				}
			}
		}
		for (Map.Entry<RegistryKey<Enchantment>, Enchantment> entry : Registries.ENCHANTMENT.getEntrySet()) {
			Enchantment enchantment = entry.getValue();
			if (!craftingColors.containsKey(enchantment)) {
				SpectrumCommon.logWarning("[SANITY: Enchantment Recipes] Enchantment '" + entry.getKey().getValue() + "' does not have a crafting recipe");
			}
			if (!upgradeColors.containsKey(enchantment) && enchantment.getMaxLevel() > 1) {
				SpectrumCommon.logWarning("[SANITY: Enchantment Recipes] Enchantment '" + entry.getKey().getValue() + "' does not have a upgrading recipe");
			}
			if (craftingColors.containsKey(enchantment) && upgradeColors.containsKey(enchantment) && craftingColors.get(enchantment) != upgradeColors.get(enchantment)) {
				SpectrumCommon.logWarning("[SANITY: Enchantment Recipes] Enchantment recipes for '" + entry.getKey().getValue() + "' use different pigments");
			}
		}
		for (Map.Entry<RegistryKey<Enchantment>, Enchantment> entry : Registries.ENCHANTMENT.getEntrySet()) {
			Enchantment enchantment = entry.getValue();
			if (entry.getKey().getValue().getNamespace().equals(modId) && !SpectrumEnchantmentTags.isIn(SpectrumEnchantmentTags.SPECTRUM_ENCHANTMENT, enchantment)) {
				SpectrumCommon.logWarning("[SANITY: Enchantment Tags] Enchantment '" + entry.getKey().getValue() + "' is missing in the spectrum:enchantments tag");
			}
		}
		
		// Trinkets that have invalid equip advancement and thus can't be equipped
		for (Map.Entry<RegistryKey<Item>, Item> item : Registries.ITEM.getEntrySet()) {
			if (item.getValue() instanceof SpectrumTrinketItem trinketItem) {
				Identifier advancementIdentifier = trinketItem.getUnlockIdentifier();
				Advancement advancementCriterionAdvancement = advancementLoader.get(advancementIdentifier);
				if (advancementCriterionAdvancement == null) {
					SpectrumCommon.logWarning("[SANITY: Trinkets] Trinket '" + item.getKey().getValue() + "' references advancement '" + advancementIdentifier + "' that does not exist");
				}
			}
		}
		
		// items / blocks missing in the creative tab (this will also omit them from most recipe viewers)
		Collection<ItemStack> itemGroupStacks = SpectrumItemGroups.MAIN.getSearchTabStacks();
		for (Map.Entry<RegistryKey<Item>, Item> item : Registries.ITEM.getEntrySet()) {
			if (item.getKey().getValue().getNamespace().equals(modId) && !item.getValue().getRegistryEntry().isIn(SpectrumItemTags.COMING_SOON_TOOLTIP)) {
				boolean found = false;
				for(ItemStack stack : itemGroupStacks) {
					if(stack.isOf(item.getValue())) {
						found = true;
						break;
					}
				}
				
				if (!found) {
					SpectrumCommon.logWarning("[SANITY: ItemGroups] Item '" + item.getKey().getValue() + "' is missing from the Spectrum item group.");
				}
			}
		}
		
		// Guidebook entries/pages that may be un-unlockable
		Book book = BookDataManager.get().getBook(GuidebookItem.GUIDEBOOK_ID);
		for (Map.Entry<Identifier, BookEntry> entry : book.getEntries().entrySet()) {
			if (GUIDEBOOK_WARNING_WHITELIST.contains(entry.getKey())) {
				continue;
			}
			
			BookCondition condition = entry.getValue().getCondition();
			BookConditionContext context = new BookConditionCategoryContext(book, entry.getValue().getCategory());
			if (!condition.test(context, source.getPlayer())) {
				SpectrumCommon.logWarning("[SANITY: Guidebook] Entry '" + entry.getKey() + "' is still locked for the executing player. Does the player have the required advancements? Can it be unlocked?");
			}
			for (BookPage page : entry.getValue().getPages()) {
				if (!page.getCondition().test(context, source.getPlayer())) {
					SpectrumCommon.logWarning("[SANITY: Guidebook] Entry '" + entry.getKey() + "' page " + page.getPageNumber() + " of type '" + page.getType() + "' is still locked for the executing player. Does the player have the required advancements? Can it be unlocked? Or is it only a missing mod compat recipe page?");
				}
				if (page instanceof BookHintPage bookHintPage) {
					Identifier completionAdvancement = bookHintPage.getCompletionAdvancement();
					Advancement advancement = advancementLoader.get(completionAdvancement);
					if (advancement == null) {
						SpectrumCommon.logWarning("[SANITY: Guidebook] Hint Page '" + entry.getKey() + "' page " + page.getPageNumber() + " is missing it's completion advancement '" + completionAdvancement + "'");
					}
				}
			}
		}
		
		SpectrumCommon.logInfo("##### SANITY CHECK FINISHED ######");

		SpectrumCommon.logInfo("##### SANITY CHECK PEDESTAL RECIPE STATISTICS ######");
		for (PedestalRecipeTier pedestalRecipeTier : PedestalRecipeTier.values()) {
			Map<GemstoneColor, Integer> entry = usedColorsForEachTier.get(pedestalRecipeTier);
			SpectrumCommon.logInfo("[SANITY: Pedestal Recipe Gemstone Usages] Gemstone Powder for tier " + StringUtils.leftPad(pedestalRecipeTier.toString(), 8) +
					": C:" + StringUtils.leftPad(entry.get(BuiltinGemstoneColor.CYAN).toString(), 4) +
					" M:" + StringUtils.leftPad(entry.get(BuiltinGemstoneColor.MAGENTA).toString(), 4) +
					" Y:" + StringUtils.leftPad(entry.get(BuiltinGemstoneColor.YELLOW).toString(), 4) +
					" K:" + StringUtils.leftPad(entry.get(BuiltinGemstoneColor.BLACK).toString(), 4) +
					" W:" + StringUtils.leftPad(entry.get(BuiltinGemstoneColor.WHITE).toString(), 4));
		}

		if (source.getEntity() instanceof ServerPlayerEntity serverPlayerEntity) {
			serverPlayerEntity.sendMessage(Text.translatable("commands.spectrum.progression_sanity.success"), false);
		}

		return 0;
	}
	
	private static <R extends GatedRecipe<C>, C extends Inventory> void testIngredientsAndOutputInColorRegistry(RecipeType<R> recipeType, String name, RecipeManager recipeManager, DynamicRegistryManager registryManager) {
		for (GatedRecipe<C> recipe : recipeManager.listAllOfType(recipeType)) {
			for (Ingredient inputIngredient : recipe.getIngredients()) {
				for (ItemStack matchingItemStack : inputIngredient.getMatchingStacks()) {
					if (ColorRegistry.ITEM_COLORS.getMapping(matchingItemStack.getItem()).isEmpty()) {
						SpectrumCommon.logWarning("[SANITY: " + name + " Recipe] Input '" + Registries.ITEM.getId(matchingItemStack.getItem()) + "' in recipe '" + recipe.getId() + "', does not exist in the item color registry. Add it for nice effects!");
					}
				}
			}
			Item outputItem = recipe.getOutput(registryManager).getItem();
			if (outputItem != null && outputItem != Items.AIR && ColorRegistry.ITEM_COLORS.getMapping(outputItem).isEmpty()) {
				SpectrumCommon.logWarning("[SANITY: " + name + " Recipe] Output '" + Registries.ITEM.getId(outputItem) + "' in recipe '" + recipe.getId() + "', does not exist in the item color registry. Add it for nice effects!");
			}
		}
	}

}
