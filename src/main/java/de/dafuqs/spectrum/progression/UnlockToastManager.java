package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.progression.toast.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class UnlockToastManager {
	// Advancement Identifier + Recipe Variant => Recipe
	public static final Map<ResourceLocation, Map<RecipeType<?>, Set<GatedRecipe<?>>>> gatedRecipes = new HashMap<>();
	
	public static final Map<ResourceLocation, Tuple<ItemStack, String>> MESSAGE_TOASTS = new HashMap<>() {{
		put(SpectrumAdvancements.UNLOCK_SHOOTING_STARS, new Tuple<>(Items.SPYGLASS.getDefaultInstance(), "shooting_stars_unlocked"));
		put(SpectrumAdvancements.OVERENCHANTING, new Tuple<>(SpectrumBlocks.ENCHANTER.asItem().getDefaultInstance(), "overchanting_unlocked"));
		put(SpectrumAdvancements.APPLY_CONFLICTING_ENCHANTMENTS, new Tuple<>(SpectrumBlocks.ENCHANTER.asItem().getDefaultInstance(), "enchant_conflicting_enchantments_unlocked"));
		put(SpectrumAdvancements.FOURTH_BREWING_SLOT, new Tuple<>(SpectrumBlocks.POTION_WORKSHOP.asItem().getDefaultInstance(), "fourth_potion_reagent_unlocked"));
		put(SpectrumAdvancements.MIDGAME, new Tuple<>(SpectrumBlocks.PEDESTAL_ONYX.asItem().getDefaultInstance(), "second_advancement_tree_unlocked"));
		put(SpectrumAdvancements.LATEGAME, new Tuple<>(SpectrumBlocks.PEDESTAL_MOONSTONE.asItem().getDefaultInstance(), "third_advancement_tree_unlocked"));
		put(SpectrumAdvancements.ASCEND_KINDLING, new Tuple<>(SpectrumBlocks.PEDESTAL_MOONSTONE.asItem().getDefaultInstance(), "ascend_kindling"));
		put(SpectrumAdvancements.VIVISECT_KINDLING, new Tuple<>(SpectrumItems.DIVINATION_HEART.getDefaultInstance(), "vivisect_kindling"));
		put(SpectrumAdvancements.PAINTBRUSH_COLORING, new Tuple<>(SpectrumItems.PAINTBRUSH.getDefaultInstance(), "block_coloring_unlocked"));
		put(SpectrumAdvancements.PAINTBRUSH_INK_SLINGING, new Tuple<>(SpectrumItems.PAINTBRUSH.getDefaultInstance(), "ink_slinging_unlocked"));
		put(SpectrumAdvancements.PASTEL_NODE_COLORING, new Tuple<>(SpectrumBlocks.SENDER_NODE.asItem().getDefaultInstance(), "pastel_node_coloring"));
	}};
	
	public static void clear() {
		gatedRecipes.clear();
	}
	
	public static void registerGatedRecipe(RecipeType<?> recipeType, GatedRecipe<?> gatedRecipe) {
		ResourceLocation requiredAdvancementIdentifier = gatedRecipe.getRequiredAdvancementIdentifier().orElse(null);
		
		// secret recipes should not have a popup
		if (gatedRecipe.isSecret()) {
			return;
		}
		
		if (gatedRecipes.containsKey(requiredAdvancementIdentifier)) {
			Map<RecipeType<?>, Set<GatedRecipe<?>>> recipeTypeListMap = gatedRecipes.get(requiredAdvancementIdentifier);
			if (recipeTypeListMap.containsKey(recipeType)) {
				Set<GatedRecipe<?>> existingSet = recipeTypeListMap.get(recipeType);
				existingSet.add(gatedRecipe);
			} else {
				Set<GatedRecipe<?>> newList = new ObjectArraySet<>();
				newList.add(gatedRecipe);
				recipeTypeListMap.put(recipeType, newList);
			}
		} else {
			Map<RecipeType<?>, Set<GatedRecipe<?>>> recipeTypeListSet = new HashMap<>();
			Set<GatedRecipe<?>> newSet = new ObjectArraySet<>();
			newSet.add(gatedRecipe);
			recipeTypeListSet.put(recipeType, newSet);
			gatedRecipes.put(requiredAdvancementIdentifier, recipeTypeListSet);
		}
	}
	
	public static void processAdvancements(Set<ResourceLocation> doneAdvancements) {
		Minecraft client = Minecraft.getInstance();
		if (client.level == null) return;
		RegistryAccess registryManager = client.level.registryAccess();
		
		int unlockedRecipeCount = 0;
		HashMap<RecipeType<?>, List<GatedRecipe<?>>> unlockedRecipesByType = new HashMap<>();
		List<Tuple<ItemStack, String>> specialToasts = new ArrayList<>();
		
		for (ResourceLocation doneAdvancement : doneAdvancements) {
			if (gatedRecipes.containsKey(doneAdvancement)) {
				Map<RecipeType<?>, Set<GatedRecipe<?>>> recipesGatedByAdvancement = gatedRecipes.get(doneAdvancement);
				
				for (Map.Entry<RecipeType<?>, Set<GatedRecipe<?>>> recipesByType : recipesGatedByAdvancement.entrySet()) {
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
				for (Map<RecipeType<?>, Set<GatedRecipe<?>>> recipesByType : gatedRecipes.values()) {
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
			
			if (UnlockToastManager.MESSAGE_TOASTS.containsKey(doneAdvancement)) {
				specialToasts.add(UnlockToastManager.MESSAGE_TOASTS.get(doneAdvancement));
			}
		}
		
		if (unlockedRecipeCount > 50) {
			// the player unlocked a LOT of recipes at the same time (via command?)
			// => show a single toast. Nobody's going to remember all that stuff.
			// At that point it would be overwhelming / annoying
			List<ItemStack> allStacks = new ArrayList<>();
			for (List<GatedRecipe<?>> recipes : unlockedRecipesByType.values()) {
				for (GatedRecipe<?> recipe : recipes) {
					allStacks.add(recipe.getResultItem(client.level.registryAccess()));
				}
			}
			UnlockedRecipeToast.showLotsOfRecipesToast(Minecraft.getInstance(), allStacks);
		} else {
			for (List<GatedRecipe<?>> unlockedRecipeList : unlockedRecipesByType.values()) {
				showGroupedRecipeUnlockToasts(registryManager, unlockedRecipeList);
			}
		}
		
		for (Tuple<ItemStack, String> messageToast : specialToasts) {
			MessageToast.showMessageToast(Minecraft.getInstance(), messageToast.getA(), messageToast.getB());
		}
	}
	
	private static void showGroupedRecipeUnlockToasts(RegistryAccess registryManager, List<GatedRecipe<?>> unlockedRecipes) {
		if (unlockedRecipes.isEmpty()) {
			return;
		}
		
		
		Component singleText = unlockedRecipes.getFirst().getSingleUnlockToastString();
		Component multipleText = unlockedRecipes.getFirst().getMultipleUnlockToastString();
		
		List<ItemStack> singleRecipes = new ArrayList<>();
		HashMap<String, List<ItemStack>> groupedRecipes = new HashMap<>();
		
		for (GatedRecipe<?> recipe : unlockedRecipes) {
			if (!recipe.getResultItem(registryManager).isEmpty()) { // weather recipes
				// FIXME - Better place to log this?
				//if (recipe.getGroup() == null) {
					//SpectrumCommon.logWarning("Found a recipe with null group: " + recipe.getId().toString() + " Please report this. If you are DaFuqs and you are reading this: you messed up big time.");
				//}
				
				if (recipe.getGroup().isEmpty()) {
					singleRecipes.add(recipe.getResultItem(registryManager));
				} else {
					if (groupedRecipes.containsKey(recipe.getGroup())) {
						groupedRecipes.get(recipe.getGroup()).add(recipe.getResultItem(registryManager));
					} else {
						List<ItemStack> newList = new ArrayList<>();
						newList.add(recipe.getResultItem(registryManager));
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
					UnlockedRecipeToast.showRecipeToast(Minecraft.getInstance(), groupedList.getFirst(), singleText);
				} else {
					UnlockedRecipeToast.showRecipeGroupToast(Minecraft.getInstance(), group.getKey(), groupedList, multipleText);
				}
			}
		}
		
		// show singular recipes
		for (ItemStack singleStack : singleRecipes) {
			UnlockedRecipeToast.showRecipeToast(Minecraft.getInstance(), singleStack, singleText);
		}
	}
	
	/**
	 * When the player upgraded their pedestal and built the new structure
	 * show toasts for all recipes that he already meets the requirements for
	 *
	 * @param pedestalRecipeTier The new pedestal recipe tier the player unlocked
	 */
	private static @NotNull List<PedestalRecipe> getRecipesForTierWithAllConditionsMet(PedestalRecipeTier pedestalRecipeTier, List<GatedRecipe<?>> pedestalRecipes) {
		Minecraft client = Minecraft.getInstance();
		Player player = client.player;
		
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
