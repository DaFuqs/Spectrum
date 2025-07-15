package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.recipe.primordial_fire_burning.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public class BookPrimordialFireBurningPageRenderer<R extends PrimordialFireBurningRecipe, T extends BookGatedRecipePage<R>> extends BookGatedRecipePageRenderer<R, T> {
	
	private static final ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/primordial_fire.png");
	
	public BookPrimordialFireBurningPageRenderer(T page) {
		super(page);
	}
	
	@Override
	protected int getRecipeHeight() {
		return 58;
	}
	
	@Override
	protected void drawRecipe(GuiGraphics drawContext, RecipeHolder<R> recipeEntry, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		R recipe = recipeEntry.value();
		Level world = Minecraft.getInstance().level;
		if (world == null) return;
		
		renderTitle(drawContext, recipeY, second);
		
		RenderSystem.enableBlend();
		drawContext.blit(BACKGROUND_TEXTURE, recipeX + 13, recipeY + 6, 0, 0, 57, 40, 64, 64);
		
		// the ingredient
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		parentScreen.renderIngredient(drawContext, recipeX + 16, recipeY + 8, mouseX, mouseY, ingredients.getFirst());
		
		// the output
		parentScreen.renderItemStack(drawContext, recipeX + 51, recipeY + 8, mouseX, mouseY, recipe.getResultItem(world.registryAccess()));
	}
	
}
