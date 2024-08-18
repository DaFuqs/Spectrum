package de.dafuqs.spectrum.api.recipe;

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
	
	boolean canPlayerCraft(PlayerEntity playerEntity);
	
	boolean isSecret();
	
	Identifier getRequiredAdvancementIdentifier();
	
	Identifier getRecipeTypeUnlockIdentifier();
	
	Text getSingleUnlockToastString();
	
	Text getMultipleUnlockToastString();
	
	default void registerInToastManager(RecipeType<?> recipeType, GatedRecipe gatedRecipe) {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER) {
			registerInToastManagerClient(recipeType, gatedRecipe);
		}
	}
	
	@Environment(EnvType.CLIENT)
	private void registerInToastManagerClient(RecipeType<?> recipeType, GatedRecipe gatedRecipe) {
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
