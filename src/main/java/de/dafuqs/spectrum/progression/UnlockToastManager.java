package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.items.magic_items.PaintBrushItem;
import de.dafuqs.spectrum.progression.toast.MessageToast;
import de.dafuqs.spectrum.progression.toast.UnlockedRecipeGroupToast;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

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
		put(PaintBrushItem.UNLOCK_COLORING_ADVANCEMENT_ID, new Pair<>(SpectrumItems.PAINTBRUSH.getDefaultStack(), "block_coloring_unlocked"));
		put(PaintBrushItem.UNLOCK_PAINT_SLINGING_ADVANCEMENT_ID, new Pair<>(SpectrumItems.PAINTBRUSH.getDefaultStack(), "paint_flinging_unlocked"));
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
			TranslatableText singleText = unlockedRecipes.get(0).getSingleUnlockToastString();
			TranslatableText multipleText = unlockedRecipes.get(0).getMultipleUnlockToastString();
			
			HashMap<String, List<ItemStack>> groupedRecipes = new HashMap<>();
			
			for (GatedRecipe recipe : unlockedRecipes) {
				if (!recipe.getOutput().isEmpty()) { // weather recipes
					if (recipe.getGroup().isEmpty()) {
						ItemStack displayStack = recipe.getOutput().copy();
						displayStack.setCount(1);
						UnlockedRecipeGroupToast.showRecipeToast(MinecraftClient.getInstance(), displayStack, singleText);
					} else {
						if (groupedRecipes.containsKey(recipe.getGroup())) {
							groupedRecipes.get(recipe.getGroup()).add(recipe.getOutput());
						} else {
							List<ItemStack> newList = new ArrayList<>();
							newList.add(new ItemStack(recipe.getOutput().getItem()));
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