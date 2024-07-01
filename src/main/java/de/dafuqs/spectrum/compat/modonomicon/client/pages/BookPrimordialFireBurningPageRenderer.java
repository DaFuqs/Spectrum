package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.recipe.primordial_fire_burning.*;
import net.minecraft.client.gui.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;

public class BookPrimordialFireBurningPageRenderer<R extends PrimordialFireBurningRecipe, T extends BookGatedRecipePage<R>> extends BookGatedRecipePageRenderer<R, T> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/primordial_fire.png");
	
	public BookPrimordialFireBurningPageRenderer(T page) {
		super(page);
	}
	
	@Override
	protected int getRecipeHeight() {
		return 58;
	}
	
	@Override
	protected void drawRecipe(DrawContext drawContext, R recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		World world = parentScreen.getMinecraft().world;
		if (world == null) return;
		
		renderTitle(drawContext, recipeY, second);
		
		RenderSystem.enableBlend();
		drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX + 13, recipeY + 6, 0, 0, 57, 40, 64, 64);
		
		// the ingredient
		DefaultedList<Ingredient> ingredients = recipe.getIngredients();
		parentScreen.renderIngredient(drawContext, recipeX + 16, recipeY + 8, mouseX, mouseY, ingredients.get(0));
		
		// the output
		parentScreen.renderItemStack(drawContext, recipeX + 51, recipeY + 8, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));
	}
	
}
