package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public class BookEnchanterCraftingPageRenderer extends BookGatedRecipePageRenderer<EnchanterRecipe, BookGatedRecipePage<EnchanterRecipe>> {
	
	private static final ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/enchanter_crafting.png");
	private static final ItemStack ENCHANTER = SpectrumBlocks.ENCHANTER.asItem().getDefaultInstance();
	
	public BookEnchanterCraftingPageRenderer(BookGatedRecipePage<EnchanterRecipe> page) {
		super(page);
	}
	
	@Override
	protected int getRecipeHeight() {
		return 94;
	}
	
	@Override
	protected void drawRecipe(GuiGraphics drawContext, RecipeHolder<EnchanterRecipe> recipeEntry, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		EnchanterRecipe recipe = recipeEntry.value();
		Level world = Minecraft.getInstance().level;
		if (world == null) return;
		
		RenderSystem.enableBlend();
		drawContext.blit(BACKGROUND_TEXTURE, recipeX, recipeY, 0, 0, 100, 80, 256, 256);
		
		renderTitle(drawContext, recipeY, second);
		
		// the ingredients
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		
		// surrounding input slots
		parentScreen.renderIngredient(drawContext, recipeX + 13, recipeY, mouseX, mouseY, ingredients.get(1));
		parentScreen.renderIngredient(drawContext, recipeX + 37, recipeY, mouseX, mouseY, ingredients.get(2));
		parentScreen.renderIngredient(drawContext, recipeX + 53, recipeY + 16, mouseX, mouseY, ingredients.get(3));
		parentScreen.renderIngredient(drawContext, recipeX + 53, recipeY + 40, mouseX, mouseY, ingredients.get(4));
		parentScreen.renderIngredient(drawContext, recipeX + 37, recipeY + 56, mouseX, mouseY, ingredients.get(5));
		parentScreen.renderIngredient(drawContext, recipeX + 13, recipeY + 56, mouseX, mouseY, ingredients.get(6));
		parentScreen.renderIngredient(drawContext, recipeX - 3, recipeY + 40, mouseX, mouseY, ingredients.get(7));
		parentScreen.renderIngredient(drawContext, recipeX - 3, recipeY + 16, mouseX, mouseY, ingredients.get(8));
		
		// center input slot
		parentScreen.renderIngredient(drawContext, recipeX + 25, recipeY + 28, mouseX, mouseY, ingredients.get(0));
		
		// Knowledge Gem and Enchanter
		ItemStack knowledgeDropStackWithXP = KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience(), true);
		parentScreen.renderItemStack(drawContext, recipeX + 81, recipeY + 9, mouseX, mouseY, knowledgeDropStackWithXP);
		parentScreen.renderItemStack(drawContext, recipeX + 81, recipeY + 46, mouseX, mouseY, ENCHANTER);
		
		// the output
		parentScreen.renderItemStack(drawContext, recipeX + 81, recipeY + 31, mouseX, mouseY, recipe.getResultItem(world.registryAccess()));
	}
	
}
