package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.progression.toast.MessageToast;
import de.dafuqs.spectrum.progression.toast.UnlockedRecipeGroupToast;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ClientRecipeToastManager {

	public static final HashMap<Identifier, List<PedestalCraftingRecipe>> unlockablePedestalRecipes = new HashMap<>();
	public static final HashMap<Identifier, List<FusionShrineRecipe>> unlockableFusionShrineRecipes = new HashMap<>();
	public static final HashMap<Identifier, List<EnchanterRecipe>> unlockableEnchanterRecipes = new HashMap<>();
	
	public static final HashMap<Identifier, Pair<ItemStack, String>> registeredMessageToasts = new HashMap<>() {{
		put(new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_shooting_stars"), new Pair<>(new ItemStack(Items.SPYGLASS), "shooting_stars_unlocked"));
		put(new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_overenchanting_with_enchanter"), new Pair<>(new ItemStack(SpectrumBlocks.ENCHANTER), "overchanting_unlocked"));
		put(new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_conflicted_enchanting_with_enchanter"), new Pair<>(new ItemStack(SpectrumBlocks.ENCHANTER), "enchant_conflicting_enchantments_unlocked"));
		put(new Identifier(SpectrumCommon.MOD_ID, "midgame/spectrum_midgame"), new Pair<>(new ItemStack(SpectrumBlocks.PEDESTAL_ONYX), "second_advancement_tree_unlocked"));
		put(new Identifier(SpectrumCommon.MOD_ID, "lategame/spectrum_lategame"), new Pair<>(new ItemStack(SpectrumBlocks.PEDESTAL_MOONSTONE), "third_advancement_tree_unlocked"));
	}};

	public static void registerUnlockablePedestalRecipe(@NotNull PedestalCraftingRecipe recipe) {
		List<Identifier> requiredAdvancementIdentifiers = recipe.getRequiredAdvancementIdentifiers();
		if(requiredAdvancementIdentifiers.size() > 0) {
			for(Identifier requiredAdvancementIdentifier : requiredAdvancementIdentifiers) {
				if (unlockablePedestalRecipes.containsKey(requiredAdvancementIdentifier)) {
					if(!unlockablePedestalRecipes.get(requiredAdvancementIdentifier).contains(recipe)) {
						unlockablePedestalRecipes.get(requiredAdvancementIdentifier).add(recipe);
					}
				} else {
					List<PedestalCraftingRecipe> recipes = new ArrayList<>();
					recipes.add(recipe);
					unlockablePedestalRecipes.put(requiredAdvancementIdentifier, recipes);
				}
			}
		}
	}

	public static void registerUnlockableFusionShrineRecipe(@NotNull FusionShrineRecipe recipe) {
		Identifier requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
		if(requiredAdvancementIdentifier != null) {
			if (unlockableFusionShrineRecipes.containsKey(requiredAdvancementIdentifier)) {
				if(!unlockableFusionShrineRecipes.get(requiredAdvancementIdentifier).contains(recipe)) {
					unlockableFusionShrineRecipes.get(requiredAdvancementIdentifier).add(recipe);
				}
			} else {
				List<FusionShrineRecipe> recipes = new ArrayList<>();
				recipes.add(recipe);
				unlockableFusionShrineRecipes.put(requiredAdvancementIdentifier, recipes);
			}
		}
	}
	
	public static void registerUnlockableEnchanterRecipe(@NotNull EnchanterRecipe recipe) {
		Identifier requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
		if(requiredAdvancementIdentifier != null) {
			if (unlockableEnchanterRecipes.containsKey(requiredAdvancementIdentifier)) {
				if(!unlockableEnchanterRecipes.get(requiredAdvancementIdentifier).contains(recipe)) {
					unlockableEnchanterRecipes.get(requiredAdvancementIdentifier).add(recipe);
				}
			} else {
				List<EnchanterRecipe> recipes = new ArrayList<>();
				recipes.add(recipe);
				unlockableEnchanterRecipes.put(requiredAdvancementIdentifier, recipes);
			}
		}
	}

	public static void process(List<Identifier> doneAdvancements, boolean showToast) {
		if(showToast) {
			List<PedestalCraftingRecipe> pedestalRecipes = new ArrayList<>();
			List<FusionShrineRecipe> fusionRecipes = new ArrayList<>();
			List<EnchanterRecipe> enchanterRecipes = new ArrayList<>();
			List<Pair<ItemStack, String>> messageToasts = new ArrayList<>();
			
			for (Identifier doneAdvancement : doneAdvancements) {
				if (unlockablePedestalRecipes.containsKey(doneAdvancement)) {
					for (PedestalCraftingRecipe unlockedRecipe : unlockablePedestalRecipes.get(doneAdvancement)) {
						if (unlockedRecipe.canPlayerCraft(MinecraftClient.getInstance().player)) {
							if(!pedestalRecipes.contains((unlockedRecipe))) {
								pedestalRecipes.add(unlockedRecipe);
							}
						}
					}
				}

				Optional<PedestalRecipeTier> newlyUnlockedRecipeTier = PedestalRecipeTier.hasJustUnlockedANewRecipeTier(doneAdvancement);
				if(newlyUnlockedRecipeTier.isPresent()) {
					for(PedestalCraftingRecipe alreadyUnlockedRecipe : getRecipesForTierWithAllConditionsMet(newlyUnlockedRecipeTier.get())) {
						if (!pedestalRecipes.contains((alreadyUnlockedRecipe))) {
							pedestalRecipes.add(alreadyUnlockedRecipe);
						}
					}
				}
				
				if (unlockableFusionShrineRecipes.containsKey(doneAdvancement)) {
					for (FusionShrineRecipe unlockedRecipe : unlockableFusionShrineRecipes.get(doneAdvancement)) {
						if(!fusionRecipes.contains((unlockedRecipe))) {
							fusionRecipes.add(unlockedRecipe);
						}
					}
				}
				
				if (unlockableEnchanterRecipes.containsKey(doneAdvancement)) {
					for (EnchanterRecipe unlockedRecipe : unlockableEnchanterRecipes.get(doneAdvancement)) {
						if(!enchanterRecipes.contains((unlockedRecipe))) {
							enchanterRecipes.add(unlockedRecipe);
						}
					}
				}
				
				if(registeredMessageToasts.containsKey(doneAdvancement)) {
					messageToasts.add(registeredMessageToasts.get(doneAdvancement));
				}
			}

			if(!pedestalRecipes.isEmpty()) {
				showGroupedRecipeUnlockToasts(pedestalRecipes, UnlockedRecipeGroupToast.UnlockedRecipeToastType.PEDESTAL);
			}
			if(!pedestalRecipes.isEmpty()) {
				showGroupedRecipeUnlockToasts(fusionRecipes, UnlockedRecipeGroupToast.UnlockedRecipeToastType.FUSION_SHRINE);
			}
			if(!enchanterRecipes.isEmpty()) {
				showGroupedRecipeUnlockToasts(enchanterRecipes, UnlockedRecipeGroupToast.UnlockedRecipeToastType.ENCHANTER);
			}
			for(Pair<ItemStack, String> messageToast : messageToasts) {
				MessageToast.showMessageToast(MinecraftClient.getInstance(), messageToast.getLeft(), messageToast.getRight());
			}
			
		}
	}

	/**
	 * When the player upgraded their pedestal and built the new structure
	 * show toasts for all recipes that he already meets the requirements for
	 * @param pedestalRecipeTier The new pedestal recipe tier the player unlocked
	 */
	private static @NotNull List<PedestalCraftingRecipe> getRecipesForTierWithAllConditionsMet(PedestalRecipeTier pedestalRecipeTier) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;

		List<PedestalCraftingRecipe> alreadyUnlockedRecipesAtNewTier = new ArrayList<>();
		for(List<PedestalCraftingRecipe> recipes : unlockablePedestalRecipes.values()) {
			for(PedestalCraftingRecipe recipe : recipes) {
				if(recipe.getTier() == pedestalRecipeTier
						&& !alreadyUnlockedRecipesAtNewTier.contains(recipe)
						&& recipe.hasUnlockedRequiredAdvancements(player)) {

					alreadyUnlockedRecipesAtNewTier.add(recipe);
				}
			}
		}
		return alreadyUnlockedRecipesAtNewTier;
	}

	// group the recipes based on their group
	// show only 1 toast for grouped recipes, if
	// at least 2 recipes of that group have been unlocked at once
	private static void showGroupedRecipeUnlockToasts(@NotNull List<? extends Recipe> recipes, UnlockedRecipeGroupToast.UnlockedRecipeToastType toastType) {
		HashMap<String, List<ItemStack>> groupedRecipes = new HashMap<>();
		for(Recipe recipe : recipes) {
			if(recipe.getGroup().isEmpty()) {
				ItemStack displayStack = recipe.getOutput().copy();
				displayStack.setCount(1);
				UnlockedRecipeGroupToast.showRecipeToast(MinecraftClient.getInstance(), displayStack, toastType);
			} else {
				if(groupedRecipes.containsKey(recipe.getGroup())) {
					groupedRecipes.get(recipe.getGroup()).add(recipe.getOutput());
				} else {
					List<ItemStack> newList = new ArrayList<>();
					newList.add(new ItemStack(recipe.getOutput().getItem()));
					groupedRecipes.put(recipe.getGroup(), newList);
				}
			}
		}

		for(String group : groupedRecipes.keySet()) {
			List<ItemStack> groupedList = groupedRecipes.get(group);
			if(groupedList.size() == 1) {
				UnlockedRecipeGroupToast.showRecipeToast(MinecraftClient.getInstance(), groupedList.get(0), toastType);
			} else {
				UnlockedRecipeGroupToast.showRecipeGroupToast(MinecraftClient.getInstance(), group, groupedRecipes.get(group), toastType);
			}
		}
	}

}