package de.dafuqs.spectrum.mixin.compat.kubejs.absent;

import com.google.common.collect.*;
import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.*;
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
	@Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;", ordinal = 1, remap = false))
	private void shareEnchantmentUpgradeRecipes(CallbackInfo ci, @Share("collectedRecipes") LocalRef<ImmutableMap<Identifier, Recipe<?>>> collectedRecipes) {
		collectedRecipes.set(EnchantmentUpgradeRecipeSerializer.enchantmentUpgradeRecipesToInject.stream().collect(ImmutableMap.toImmutableMap(EnchantmentUpgradeRecipe::getId, enchantmentUpgradeRecipe -> enchantmentUpgradeRecipe)));
	}
	
	@ModifyReceiver(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;", ordinal = 1, remap = false))
	private Map<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>> injectRecipes(Map<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>> recipes, @Share("collectedRecipes") LocalRef<ImmutableMap<Identifier, Recipe<?>>> collectedRecipes) {
		recipes.computeIfAbsent(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, (recipeType) -> ImmutableMap.builder()).putAll(collectedRecipes.get());
		return recipes;
	}
	
	@ModifyReceiver(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;", remap = false))
	private ImmutableMap.Builder<Identifier, Recipe<?>> injectRecipesById(ImmutableMap.Builder<Identifier, Recipe<?>> recipesById, @Share("collectedRecipes") LocalRef<ImmutableMap<Identifier, Recipe<?>>> collectedRecipes) {
		recipesById.putAll(collectedRecipes.get());
		return recipesById;
	}
}
