package de.dafuqs.spectrum.recipe;

import de.dafuqs.spectrum.progression.UnlockToastManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface GatedRecipe extends Recipe<Inventory> {
	
	boolean canPlayerCraft(PlayerEntity playerEntity);
	
	Identifier getRequiredAdvancementIdentifier();
	boolean isSecret();
	
	default void registerInToastManager(RecipeType recipeType, GatedRecipe gatedRecipe) {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER) {
			registerInToastManagerClient(recipeType, gatedRecipe);
		}
	}
	
	@Environment(EnvType.CLIENT)
	private void registerInToastManagerClient(RecipeType recipeType, GatedRecipe gatedRecipe) {
		UnlockToastManager.registerGatedRecipe(recipeType, gatedRecipe);
	}
	
	Text getSingleUnlockToastString();
	
	Text getMultipleUnlockToastString();
	
}
