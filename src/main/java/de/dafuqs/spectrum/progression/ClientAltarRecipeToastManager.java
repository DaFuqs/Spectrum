package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.recipe.altar.AltarCraftingRecipe;
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
public class ClientAltarRecipeToastManager {

    public static final HashMap<Identifier, List<AltarCraftingRecipe>> unlockableAltarRecipes = new HashMap<>();

    public static void registerUnlockableAltarRecipe(AltarCraftingRecipe recipe) {
        List<Identifier> requiredAdvancementIdentifiers = recipe.getRequiredAdvancementIdentifiers();
        if(requiredAdvancementIdentifiers.size() > 0) {
            for(Identifier requiredAdvancementIdentifier : requiredAdvancementIdentifiers) {
                if (unlockableAltarRecipes.containsKey(requiredAdvancementIdentifier)) {
                    if(!unlockableAltarRecipes.get(requiredAdvancementIdentifier).contains(recipe)) {
                        unlockableAltarRecipes.get(requiredAdvancementIdentifier).add(recipe);
                    }
                } else {
                    List<AltarCraftingRecipe> recipes = new ArrayList<>();
                    recipes.add(recipe);
                    unlockableAltarRecipes.put(requiredAdvancementIdentifier, recipes);
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
        if (unlockableAltarRecipes.containsKey(advancementIdentifier)) {
            for (AltarCraftingRecipe unlockedRecipe : unlockableAltarRecipes.get(advancementIdentifier)) {
                if (unlockedRecipe.shouldShowToastOnUnlock() && unlockedRecipe.canCraft(MinecraftClient.getInstance().player)) {
                    RecipeToast.showRecipeToast(MinecraftClient.getInstance(), new ItemStack(unlockedRecipe.getOutput().getItem()));
                }
            }
        }
    }

}