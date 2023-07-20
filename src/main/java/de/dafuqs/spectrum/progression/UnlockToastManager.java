package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.progression.toast.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class UnlockToastManager {
	// Advancement Identifier + Recipe Type => Recipe
	public static final Map<Identifier, Map<RecipeType, List<GatedRecipe>>> gatedRecipes = new HashMap<>();
	
	public static final HashMap<Identifier, Pair<ItemStack, String>> messageToasts = new HashMap<>() {{
		put(SpectrumCommon.locate("milestones/unlock_shooting_stars"), new Pair<>(Items.SPYGLASS.getDefaultStack(), "shooting_stars_unlocked"));
		put(SpectrumCommon.locate("milestones/unlock_overenchanting_with_enchanter"), new Pair<>(SpectrumBlocks.ENCHANTER.asItem().getDefaultStack(), "overchanting_unlocked"));
		put(SpectrumCommon.locate("milestones/unlock_conflicted_enchanting_with_enchanter"), new Pair<>(SpectrumBlocks.ENCHANTER.asItem().getDefaultStack(), "enchant_conflicting_enchantments_unlocked"));
		put(SpectrumCommon.locate("milestones/unlock_fourth_potion_workshop_reagent_slot"), new Pair<>(SpectrumBlocks.POTION_WORKSHOP.asItem().getDefaultStack(), "fourth_potion_reagent_unlocked"));
		put(SpectrumCommon.locate("midgame/spectrum_midgame"), new Pair<>(SpectrumBlocks.PEDESTAL_ONYX.asItem().getDefaultStack(), "second_advancement_tree_unlocked"));
		put(SpectrumCommon.locate("lategame/spectrum_lategame"), new Pair<>(SpectrumBlocks.PEDESTAL_MOONSTONE.asItem().getDefaultStack(), "third_advancement_tree_unlocked"));
		put(PaintbrushItem.UNLOCK_COLORING_ADVANCEMENT_ID, new Pair<>(SpectrumItems.PAINTBRUSH.getDefaultStack(), "block_coloring_unlocked"));
		put(PaintbrushItem.UNLOCK_INK_SLINGING_ADVANCEMENT_ID, new Pair<>(SpectrumItems.PAINTBRUSH.getDefaultStack(), "paint_flinging_unlocked"));
	}};
	
	public static void registerGatedRecipe(RecipeType recipeType, GatedRecipe gatedRecipe) {
		Identifier requiredAdvancementIdentifier = gatedRecipe.getRequiredAdvancementIdentifier();
		
		if (gatedRecipes.containsKey(requiredAdvancementIdentifier)) {
			Map<RecipeType, List<GatedRecipe>> recipeTypeListMap = gatedRecipes.get(requiredAdvancementIdentifier);
			if (recipeTypeListMap.containsKey(recipeType)) {
				List<GatedRecipe> existingList = recipeTypeListMap.get(recipeType);
				if (!existingList.contains(gatedRecipe)) {
					existingList.add(gatedRecipe);
				}
			} else {
				List<GatedRecipe> newList = new ArrayList<>();
				newList.add(gatedRecipe);
				recipeTypeListMap.put(recipeType, newList);
			}
		} else {
			Map<RecipeType, List<GatedRecipe>> recipeTypeListMap = new HashMap<>();
			List<GatedRecipe> newList = new ArrayList<>();
			newList.add(gatedRecipe);
			recipeTypeListMap.put(recipeType, newList);
			gatedRecipes.put(requiredAdvancementIdentifier, recipeTypeListMap);
		}
	}
	
	public static void processAdvancements(Set<Identifier> doneAdvancements) {
		HashMap<RecipeType, List<GatedRecipe>> unlockedRecipesByType = new HashMap<>();
		List<Pair<ItemStack, String>> specialToasts = new ArrayList<>();
		
		for (Identifier doneAdvancement : doneAdvancements) {
			if (gatedRecipes.containsKey(doneAdvancement)) {
				Map<RecipeType, List<GatedRecipe>> recipesGatedByAdvancement = gatedRecipes.get(doneAdvancement);
				
				for (Map.Entry<RecipeType, List<GatedRecipe>> recipesByType : recipesGatedByAdvancement.entrySet()) {
					List<GatedRecipe> newRecipes;
					if (unlockedRecipesByType.containsKey(recipesByType.getKey())) {
						newRecipes = unlockedRecipesByType.get(recipesByType.getKey());
					} else {
						newRecipes = new ArrayList<>();
					}
					
					for (GatedRecipe unlockedRecipe : recipesByType.getValue()) {
						if (unlockedRecipe.canPlayerCraft(MinecraftClient.getInstance().player)) {
							if (!newRecipes.contains((unlockedRecipe))) {
								newRecipes.add(unlockedRecipe);
							}
						}
					}
					unlockedRecipesByType.put(recipesByType.getKey(), newRecipes);
				}
			}
			
			Optional<PedestalRecipeTier> newlyUnlockedRecipeTier = PedestalRecipeTier.hasJustUnlockedANewRecipeTier(doneAdvancement);
			if (newlyUnlockedRecipeTier.isPresent()) {
				List<GatedRecipe> unlockedPedestalRecipes;
				if (unlockedRecipesByType.containsKey(SpectrumRecipeTypes.PEDESTAL)) {
					unlockedPedestalRecipes = unlockedRecipesByType.get(SpectrumRecipeTypes.PEDESTAL);
				} else {
					unlockedPedestalRecipes = new ArrayList<>();
				}
				List<GatedRecipe> pedestalRecipes = new ArrayList<>();
				for (Map<RecipeType, List<GatedRecipe>> recipesByType : gatedRecipes.values()) {
					if (recipesByType.containsKey(SpectrumRecipeTypes.PEDESTAL)) {
						pedestalRecipes.addAll(recipesByType.get(SpectrumRecipeTypes.PEDESTAL));
					}
				}
				
				for (PedestalCraftingRecipe alreadyUnlockedRecipe : getRecipesForTierWithAllConditionsMet(newlyUnlockedRecipeTier.get(), pedestalRecipes)) {
					if (!unlockedPedestalRecipes.contains(alreadyUnlockedRecipe)) {
						unlockedPedestalRecipes.add(alreadyUnlockedRecipe);
					}
				}
			}
			
			if (UnlockToastManager.messageToasts.containsKey(doneAdvancement)) {
				specialToasts.add(UnlockToastManager.messageToasts.get(doneAdvancement));
			}
		}
		
		for (List<GatedRecipe> unlockedRecipeList : unlockedRecipesByType.values()) {
			showGroupedRecipeUnlockToasts(unlockedRecipeList);
		}
		
		for (Pair<ItemStack, String> messageToast : specialToasts) {
			MessageToast.showMessageToast(MinecraftClient.getInstance(), messageToast.getLeft(), messageToast.getRight());
		}
	}
	
	private static void showGroupedRecipeUnlockToasts(List<GatedRecipe> unlockedRecipes) {
		if (!unlockedRecipes.isEmpty()) {
			Text singleText = unlockedRecipes.get(0).getSingleUnlockToastString();
			Text multipleText = unlockedRecipes.get(0).getMultipleUnlockToastString();
			
			HashMap<String, List<ItemStack>> groupedRecipes = new HashMap<>();
			
			for (GatedRecipe recipe : unlockedRecipes) {
				if (!recipe.getOutput(DynamicRegistryManager.EMPTY).isEmpty()) { // weather recipes
					if (recipe.getGroup() == null) {
						SpectrumCommon.logWarning("Found a recipe with null group: " + recipe.getId().toString() + " Please report this. If you are Dafuqs and you are reading this: you messed up big time.");
					}
					
					if (recipe.getGroup().isEmpty()) {
						ItemStack displayStack = recipe.getOutput(DynamicRegistryManager.EMPTY).copy();
						displayStack.setCount(1);
						UnlockedRecipeGroupToast.showRecipeToast(MinecraftClient.getInstance(), displayStack, singleText);
					} else {
						if (groupedRecipes.containsKey(recipe.getGroup())) {
							groupedRecipes.get(recipe.getGroup()).add(recipe.getOutput(DynamicRegistryManager.EMPTY));
						} else {
							List<ItemStack> newList = new ArrayList<>();
							newList.add(recipe.getOutput(DynamicRegistryManager.EMPTY));
							groupedRecipes.put(recipe.getGroup(), newList);
						}
					}
				}
			}
			
			if (!groupedRecipes.isEmpty()) {
				for (String group : groupedRecipes.keySet()) {
					List<ItemStack> groupedList = groupedRecipes.get(group);
					if (groupedList.size() == 1) {
						UnlockedRecipeGroupToast.showRecipeToast(MinecraftClient.getInstance(), groupedList.get(0), singleText);
					} else {
						UnlockedRecipeGroupToast.showRecipeGroupToast(MinecraftClient.getInstance(), group, groupedRecipes.get(group), multipleText);
					}
				}
			}
		}
	}
	
	/**
	 * When the player upgraded their pedestal and built the new structure
	 * show toasts for all recipes that he already meets the requirements for
	 *
	 * @param pedestalRecipeTier The new pedestal recipe tier the player unlocked
	 */
	private static @NotNull List<PedestalCraftingRecipe> getRecipesForTierWithAllConditionsMet(PedestalRecipeTier pedestalRecipeTier, List<GatedRecipe> pedestalRecipes) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		
		List<PedestalCraftingRecipe> alreadyUnlockedRecipesAtNewTier = new ArrayList<>();
		for (GatedRecipe recipe : pedestalRecipes) {
			PedestalCraftingRecipe pedestalCraftingRecipe = (PedestalCraftingRecipe) recipe;
			if (pedestalCraftingRecipe.getTier() == pedestalRecipeTier && !alreadyUnlockedRecipesAtNewTier.contains(recipe) && recipe.canPlayerCraft(player)) {
				alreadyUnlockedRecipesAtNewTier.add(pedestalCraftingRecipe);
			}
		}
		return alreadyUnlockedRecipesAtNewTier;
	}
	
}