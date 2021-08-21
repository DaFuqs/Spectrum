package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import de.dafuqs.spectrum.toast.RecipeToast;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientPedestalRecipeToastManager {

    public static final HashMap<Identifier, List<PedestalCraftingRecipe>> unlockablePedestalRecipes = new HashMap<>();

    public static void registerUnlockablePedestalRecipe(PedestalCraftingRecipe recipe) {
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

    public static void process(List<Identifier> doneAdvancements, boolean showToast) {
        if(showToast) {
            for (Identifier doneAdvancement : doneAdvancements) {
                showToastsForAllRecipesWithAdvancement(doneAdvancement);
            }
        }
    }

    private static void showToastsForAllRecipesWithAdvancement(Identifier advancementIdentifier) {
        if (unlockablePedestalRecipes.containsKey(advancementIdentifier)) {
            for (PedestalCraftingRecipe unlockedRecipe : unlockablePedestalRecipes.get(advancementIdentifier)) {
                if (unlockedRecipe.shouldShowToastOnUnlock() && unlockedRecipe.canPlayerCraft(MinecraftClient.getInstance().player)) {
                    RecipeToast.showRecipeToast(MinecraftClient.getInstance(), new ItemStack(unlockedRecipe.getOutput().getItem()));
                }
            }
        }
    }

}