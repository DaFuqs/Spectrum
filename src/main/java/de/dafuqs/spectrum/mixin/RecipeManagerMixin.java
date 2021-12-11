package de.dafuqs.spectrum.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    
    @Shadow private Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes;
    
    @Shadow private Map<Identifier, Recipe<?>> recipesById;
    
    @Inject(method = "apply", at = @At("TAIL"))
    public void injectEnchantmentUpgradeRecipes(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        ImmutableMap<Identifier, Recipe<?>> collectedRecipes = EnchantmentUpgradeRecipeSerializer.enchantmentUpgradeRecipesToInject.stream().collect(ImmutableMap.toImmutableMap(EnchantmentUpgradeRecipe::getId, enchantmentUpgradeRecipe -> enchantmentUpgradeRecipe));
        
        HashMap<RecipeType<?>, Map<Identifier, Recipe<?>>> recipesHashMap = new HashMap<>();
        recipesHashMap.putAll(recipes);
        recipesHashMap.put(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, collectedRecipes);
        recipes = new ImmutableMap.Builder()
                        .putAll(recipesHashMap)
                        .build();
    
        HashMap<Identifier, Recipe<?>> recipesByIDHashMap = new HashMap<>();
        recipesByIDHashMap.putAll(recipesById);
        recipesByIDHashMap.putAll(collectedRecipes);
        recipesById = new ImmutableMap.Builder()
                .putAll(recipesByIDHashMap)
                .build();
        
    }
    
}