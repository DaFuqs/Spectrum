package de.dafuqs.spectrum.api.recipe;

import de.dafuqs.revelationary.api.advancements.*;
import net.minecraft.locale.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.crafting.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.fml.loading.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface GatedRecipe<C extends RecipeInput> extends Recipe<C> {
	
	boolean isSecret();
	
	Optional<ResourceLocation> getRequiredAdvancementIdentifier();
	
	ResourceLocation getRecipeTypeUnlockIdentifier();
	
	String getRecipeTypeShortID();
	
	default boolean canPlayerCraft(Player playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, getRecipeTypeUnlockIdentifier())
				&& AdvancementHelper.hasAdvancement(playerEntity, getRequiredAdvancementIdentifier().orElse(null));
	}
	
	default Component getSingleUnlockToastString() {
		return Component.translatable("spectrum.toast." + getRecipeTypeShortID() + "_recipe_unlocked.title");
	}
	
	default Component getMultipleUnlockToastString() {
		return Component.translatable("spectrum.toast." + getRecipeTypeShortID() + "_recipes_unlocked.title");
	}
	
	default void registerInToastManager(RecipeType<?> recipeType, GatedRecipe<C> gatedRecipe) {
		if (FMLEnvironment.dist.isClient()) {
			registerInToastManagerClient(recipeType, gatedRecipe);
		}
	}
	
	private void registerInToastManagerClient(RecipeType<?> recipeType, GatedRecipe<C> gatedRecipe) {
		// TODO PORT
//		UnlockToastManager.registerGatedRecipe(recipeType, gatedRecipe);
	}
	
	default @Nullable Component getSecretHintText(ResourceLocation id) {
		if (isSecret()) {
			String secretHintLangKey = id.toLanguageKey("recipe", "hint").replace("/", ".");
			return Language.getInstance().has(secretHintLangKey) ? Component.translatable(secretHintLangKey) : null;
		}
		return null;
	}
	
}
