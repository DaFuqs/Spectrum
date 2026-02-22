package de.dafuqs.spectrum.api.recipe;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.progression.*;
import net.fabricmc.api.*;
import net.fabricmc.loader.api.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

public interface GatedRecipe<C extends Inventory> extends Recipe<C> {
	
	boolean isSecret();
	Identifier getRequiredAdvancementIdentifier();
	Identifier getRecipeTypeUnlockIdentifier();

	String getRecipeTypeShortID();

	default boolean canPlayerCraft(PlayerEntity playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, getRecipeTypeUnlockIdentifier())
				&& AdvancementHelper.hasAdvancement(playerEntity, getRequiredAdvancementIdentifier());
	}

	default Text getSingleUnlockToastString() {
		return Text.translatable("spectrum.toast." + getRecipeTypeShortID() + "_recipe_unlocked.title");
	}

	default Text getMultipleUnlockToastString() {
		return Text.translatable("spectrum.toast." + getRecipeTypeShortID() + "_recipes_unlocked.title");
	}

	default void registerInToastManager(RecipeType<?> recipeType, GatedRecipe<C> gatedRecipe) {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER) {
			registerInToastManagerClient(recipeType, gatedRecipe);
		}
	}
	
	@Environment(EnvType.CLIENT)
	private void registerInToastManagerClient(RecipeType<?> recipeType, GatedRecipe<C> gatedRecipe) {
		UnlockToastManager.registerGatedRecipe(recipeType, gatedRecipe);
	}
	
	default @Nullable Text getSecretHintText() {
		if (isSecret()) {
			String secretHintLangKey = getId().toTranslationKey("recipe", "hint").replace("/", ".");
			return Language.getInstance().hasTranslation(secretHintLangKey) ? Text.translatable(secretHintLangKey) : null;
		}
		return null;
	}
	
}
