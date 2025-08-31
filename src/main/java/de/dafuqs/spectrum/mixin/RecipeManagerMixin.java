package de.dafuqs.spectrum.mixin;

import com.google.common.collect.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
	@Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/common/collect/ImmutableMap;builder()Lcom/google/common/collect/ImmutableMap$Builder;", remap = false))
	public void injectEnchantmentUpgradeRecipes(CallbackInfo info, @Local(ordinal = 1) Map<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>> recipes, @Local(ordinal = 0) ImmutableMap.Builder<Identifier, Recipe<?>> recipesById) {
		ImmutableMap<Identifier, Recipe<?>> collectedRecipes = EnchantmentUpgradeRecipeSerializer.enchantmentUpgradeRecipesToInject.stream().collect(ImmutableMap.toImmutableMap(EnchantmentUpgradeRecipe::getId, enchantmentUpgradeRecipe -> enchantmentUpgradeRecipe));
		
		recipes.computeIfAbsent(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, (recipeType) -> ImmutableMap.builder()).putAll(collectedRecipes);
		recipesById.putAll(collectedRecipes);
	}
}
