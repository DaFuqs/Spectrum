package de.dafuqs.spectrum.recipe;

import de.dafuqs.spectrum.progression.*;
import net.fabricmc.api.*;
import net.fabricmc.loader.api.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public interface GatedRecipe extends Recipe<Inventory> {
	
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
	
	
}
