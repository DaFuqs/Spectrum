package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public class BookAnvilCrushingPageRenderer extends BookGatedRecipePageRenderer<AnvilCrushingRecipe, BookGatedRecipePage<AnvilCrushingRecipe>> {
	
	private static final ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/anvil_crushing.png");
	
	public BookAnvilCrushingPageRenderer(BookGatedRecipePage<AnvilCrushingRecipe> page) {
		super(page);
	}
	
	@Override
	protected int getRecipeHeight() {
		return 73;
	}
	
	@Override
	protected void drawRecipe(GuiGraphics drawContext, RecipeHolder<AnvilCrushingRecipe> recipeEntry, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		AnvilCrushingRecipe recipe = recipeEntry.value();
		Level world = Minecraft.getInstance().level;
		if (world == null) return;
		
		RenderSystem.enableBlend();
		drawContext.blit(BACKGROUND_TEXTURE, recipeX, recipeY + 4, 0, 0, 84, 48, 256, 256);
		
		renderTitle(drawContext, recipeY, second);
		
		// the ingredients
		parentScreen.renderIngredient(drawContext, recipeX + 16, recipeY + 35, mouseX, mouseY, recipe.getIngredients().getFirst());
		
		// the anvil
		parentScreen.renderItemStack(drawContext, recipeX + 16, recipeY + 15, mouseX, mouseY, recipe.getToastSymbol());
		
		// the output
		parentScreen.renderItemStack(drawContext, recipeX + 64, recipeY + 29, mouseX, mouseY, recipe.getResultItem(world.registryAccess()));
	}
	
}
