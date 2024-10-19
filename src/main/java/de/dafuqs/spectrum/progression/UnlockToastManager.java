package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.progression.toast.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
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
	public static final Map<Identifier, Map<RecipeType<?>, List<GatedRecipe<?>>>> gatedRecipes = new HashMap<>();
	
	public static final Map<Identifier, Pair<ItemStack, String>> messageToasts = new HashMap<>() {{
		put(SpectrumAdvancements.UNLOCK_SHOOTING_STARS, new Pair<>(Items.SPYGLASS.getDefaultStack(), "shooting_stars_unlocked"));
		put(SpectrumAdvancements.OVERENCHANTING, new Pair<>(SpectrumBlocks.ENCHANTER.asItem().getDefaultStack(), "overchanting_unlocked"));
		put(SpectrumAdvancements.APPLY_CONFLICTING_ENCHANTMENTS, new Pair<>(SpectrumBlocks.ENCHANTER.asItem().getDefaultStack(), "enchant_conflicting_enchantments_unlocked"));
		put(SpectrumAdvancements.FOURTH_BREWING_SLOT, new Pair<>(SpectrumBlocks.POTION_WORKSHOP.asItem().getDefaultStack(), "fourth_potion_reagent_unlocked"));
		put(SpectrumAdvancements.MIDGAME, new Pair<>(SpectrumBlocks.PEDESTAL_ONYX.asItem().getDefaultStack(), "second_advancement_tree_unlocked"));
		put(SpectrumAdvancements.LATEGAME, new Pair<>(SpectrumBlocks.PEDESTAL_MOONSTONE.asItem().getDefaultStack(), "third_advancement_tree_unlocked"));
		put(SpectrumAdvancements.ASCEND_KINDLING, new Pair<>(SpectrumBlocks.PEDESTAL_MOONSTONE.asItem().getDefaultStack(), "ascend_kindling"));
		put(SpectrumAdvancements.VIVISECT_KINDLING, new Pair<>(SpectrumItems.DIVINATION_HEART.getDefaultStack(), "vivisect_kindling"));
		put(SpectrumAdvancements.PAINTBRUSH_COLORING, new Pair<>(SpectrumItems.PAINTBRUSH.getDefaultStack(), "block_coloring_unlocked"));
		put(SpectrumAdvancements.PAINTBRUSH_INK_SLINGING, new Pair<>(SpectrumItems.PAINTBRUSH.getDefaultStack(), "ink_slinging_unlocked"));
	}};
	
	public static void registerGatedRecipe(RecipeType<?> recipeType, GatedRecipe<?> gatedRecipe) {
		Identifier requiredAdvancementIdentifier = gatedRecipe.getRequiredAdvancementIdentifier();
		
		// secret recipes should not have a popup
		if (gatedRecipe.isSecret()) {
			return;
		}
		
		if (gatedRecipes.containsKey(requiredAdvancementIdentifier)) {
			Map<RecipeType<?>, List<GatedRecipe<?>>> recipeTypeListMap = gatedRecipes.get(requiredAdvancementIdentifier);
			if (recipeTypeListMap.containsKey(recipeType)) {
				List<GatedRecipe<?>> existingList = recipeTypeListMap.get(recipeType);
				if (!existingList.contains(gatedRecipe)) {
					existingList.add(gatedRecipe);
				}
			} else {
				List<GatedRecipe<?>> newList = new ArrayList<>();
				newList.add(gatedRecipe);
				recipeTypeListMap.put(recipeType, newList);
			}
		} else {
			Map<RecipeType<?>, List<GatedRecipe<?>>> recipeTypeListMap = new HashMap<>();
			List<GatedRecipe<?>> newList = new ArrayList<>();
			newList.add(gatedRecipe);
			recipeTypeListMap.put(recipeType, newList);
			gatedRecipes.put(requiredAdvancementIdentifier, recipeTypeListMap);
		}
	}
	
	public static void processAdvancements(Set<Identifier> doneAdvancements) {
		MinecraftClient client = MinecraftClient.getInstance();
		DynamicRegistryManager registryManager = client.world.getRegistryManager();
		
		int unlockedRecipeCount = 0;
		HashMap<RecipeType<?>, List<GatedRecipe<?>>> unlockedRecipesByType = new HashMap<>();
		List<Pair<ItemStack, String>> specialToasts = new ArrayList<>();
		
		for (Identifier doneAdvancement : doneAdvancements) {
			if (gatedRecipes.containsKey(doneAdvancement)) {
				Map<RecipeType<?>, List<GatedRecipe<?>>> recipesGatedByAdvancement = gatedRecipes.get(doneAdvancement);
				
				for (Map.Entry<RecipeType<?>, List<GatedRecipe<?>>> recipesByType : recipesGatedByAdvancement.entrySet()) {
					List<GatedRecipe<?>> newRecipes;
					if (unlockedRecipesByType.containsKey(recipesByType.getKey())) {
						newRecipes = unlockedRecipesByType.get(recipesByType.getKey());
					} else {
						newRecipes = new ArrayList<>();
					}
					
					for (GatedRecipe<?> unlockedRecipe : recipesByType.getValue()) {
						if (unlockedRecipe.canPlayerCraft(client.player)) {
							if (!newRecipes.contains((unlockedRecipe))) {
								newRecipes.add(unlockedRecipe);
								unlockedRecipeCount++;
							}
						}
					}
					unlockedRecipesByType.put(recipesByType.getKey(), newRecipes);
				}
			}
			
			Optional<PedestalRecipeTier> newlyUnlockedRecipeTier = PedestalRecipeTier.hasJustUnlockedANewRecipeTier(doneAdvancement);
			if (newlyUnlockedRecipeTier.isPresent()) {
				List<GatedRecipe<?>> unlockedPedestalRecipes;
				if (unlockedRecipesByType.containsKey(SpectrumRecipeTypes.PEDESTAL)) {
					unlockedPedestalRecipes = unlockedRecipesByType.get(SpectrumRecipeTypes.PEDESTAL);
				} else {
					unlockedPedestalRecipes = new ArrayList<>();
				}
				List<GatedRecipe<?>> pedestalRecipes = new ArrayList<>();
				for (Map<RecipeType<?>, List<GatedRecipe<?>>> recipesByType : gatedRecipes.values()) {
					if (recipesByType.containsKey(SpectrumRecipeTypes.PEDESTAL)) {
						pedestalRecipes.addAll(recipesByType.get(SpectrumRecipeTypes.PEDESTAL));
					}
				}
				
				for (PedestalRecipe alreadyUnlockedRecipe : getRecipesForTierWithAllConditionsMet(newlyUnlockedRecipeTier.get(), pedestalRecipes)) {
					if (!unlockedPedestalRecipes.contains(alreadyUnlockedRecipe)) {
						unlockedPedestalRecipes.add(alreadyUnlockedRecipe);
					}
				}
			}
			
			if (UnlockToastManager.messageToasts.containsKey(doneAdvancement)) {
				specialToasts.add(UnlockToastManager.messageToasts.get(doneAdvancement));
			}
		}
		
		if (unlockedRecipeCount > 50) {
			// the player unlocked a LOT of recipes at the same time (via command?)
			// => show a single toast. Nobody's going to remember all that stuff.
			// At that point it would be overwhelming / annoying
			List<ItemStack> allStacks = new ArrayList<>();
			for (List<GatedRecipe<?>> recipes : unlockedRecipesByType.values()) {
				for (GatedRecipe<?> recipe : recipes) {
					allStacks.add(recipe.getOutput(client.world.getRegistryManager()));
				}
			}
            UnlockedRecipeToast.showLotsOfRecipesToast(MinecraftClient.getInstance(), allStacks);
		} else {
			for (List<GatedRecipe<?>> unlockedRecipeList : unlockedRecipesByType.values()) {
				showGroupedRecipeUnlockToasts(registryManager, unlockedRecipeList);
			}
		}
		
		for (Pair<ItemStack, String> messageToast : specialToasts) {
			MessageToast.showMessageToast(MinecraftClient.getInstance(), messageToast.getLeft(), messageToast.getRight());
		}
	}
	
	private static void showGroupedRecipeUnlockToasts(DynamicRegistryManager registryManager, List<GatedRecipe<?>> unlockedRecipes) {
		if (unlockedRecipes.isEmpty()) {
			return;
		}
		
		
		Text singleText = unlockedRecipes.get(0).getSingleUnlockToastString();
		Text multipleText = unlockedRecipes.get(0).getMultipleUnlockToastString();
		
		List<ItemStack> singleRecipes = new ArrayList<>();
		HashMap<String, List<ItemStack>> groupedRecipes = new HashMap<>();
		
		for (GatedRecipe<?> recipe : unlockedRecipes) {
			if (!recipe.getOutput(registryManager).isEmpty()) { // weather recipes
				if (recipe.getGroup() == null) {
					SpectrumCommon.logWarning("Found a recipe with null group: " + recipe.getId().toString() + " Please report this. If you are Dafuqs and you are reading this: you messed up big time.");
				}
				
				if (recipe.getGroup().isEmpty()) {
					singleRecipes.add(recipe.getOutput(registryManager));
				} else {
					if (groupedRecipes.containsKey(recipe.getGroup())) {
						groupedRecipes.get(recipe.getGroup()).add(recipe.getOutput(registryManager));
					} else {
						List<ItemStack> newList = new ArrayList<>();
						newList.add(recipe.getOutput(registryManager));
						groupedRecipes.put(recipe.getGroup(), newList);
					}
				}
			}
		}

		// show grouped recipes
		if (!groupedRecipes.isEmpty()) {
			for (Map.Entry<String, List<ItemStack>> group : groupedRecipes.entrySet()) {
				List<ItemStack> groupedList = group.getValue();
				if (groupedList.size() == 1) {
					UnlockedRecipeToast.showRecipeToast(MinecraftClient.getInstance(), groupedList.get(0), singleText);
				} else {
					UnlockedRecipeToast.showRecipeGroupToast(MinecraftClient.getInstance(), group.getKey(), groupedList, multipleText);
				}
			}
		}

		// show singular recipes
		for (ItemStack singleStack : singleRecipes) {
			UnlockedRecipeToast.showRecipeToast(MinecraftClient.getInstance(), singleStack, singleText);
		}
	}
	
	/**
	 * When the player upgraded their pedestal and built the new structure
	 * show toasts for all recipes that he already meets the requirements for
	 *
	 * @param pedestalRecipeTier The new pedestal recipe tier the player unlocked
	 */
	private static @NotNull List<PedestalRecipe> getRecipesForTierWithAllConditionsMet(PedestalRecipeTier pedestalRecipeTier, List<GatedRecipe<?>> pedestalRecipes) {
		MinecraftClient client = MinecraftClient.getInstance();
		PlayerEntity player = client.player;
		
		List<PedestalRecipe> alreadyUnlockedRecipesAtNewTier = new ArrayList<>();
		for (GatedRecipe<?> recipe : pedestalRecipes) {
			PedestalRecipe pedestalRecipe = (PedestalRecipe) recipe;
			if (pedestalRecipe.getTier() == pedestalRecipeTier && !alreadyUnlockedRecipesAtNewTier.contains(recipe) && recipe.canPlayerCraft(player)) {
				alreadyUnlockedRecipesAtNewTier.add(pedestalRecipe);
			}
		}
		return alreadyUnlockedRecipesAtNewTier;
	}
	
}
