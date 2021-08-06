package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.recipe.altar.AltarCraftingRecipe;
import de.dafuqs.spectrum.toast.RecipeToast;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.Advancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ClientAltarRecipeToastManager {

    private static final HashMap<Identifier, List<AltarCraftingRecipe>> unlockableAltarRecipes = new HashMap<>();

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

    public static void checkAltarRecipesForNewAdvancements(AdvancementUpdateS2CPacket packet, boolean showToast) {
        if(showToast) {
            for (Map.Entry<Identifier, Advancement.Task> earnedEntry : packet.getAdvancementsToEarn().entrySet()) {
                Identifier earnedAdvancementIdentifier = earnedEntry.getKey();
                if(Support.hasAdvancement(MinecraftClient.getInstance().player, earnedAdvancementIdentifier)) {
                    showToastsForAllRecipesWithAdvancement(earnedAdvancementIdentifier);
                }
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

    public static void clearRecipes() {
        unlockableAltarRecipes.clear();
    }
}