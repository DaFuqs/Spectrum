package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.gui.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;

public class PageEnchanterRecipe extends PageGatedRecipeSingle<EnchanterRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/enchanter_crafting.png");
	
	public PageEnchanterRecipe() {
		super(SpectrumRecipeTypes.ENCHANTER);
	}
	
	@Override
	protected ItemStack getRecipeOutput(World world, EnchanterRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput(world.getRegistryManager());
		}
	}
	
	@Override
	protected void drawRecipe(DrawContext drawContext, World world, @NotNull EnchanterRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY) {
		RenderSystem.enableBlend();
		drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX, recipeY, 0, 0, 100, 80, 256, 256);
		
		parent.drawCenteredStringNoShadow(drawContext, getTitle().asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredients
		DefaultedList<Ingredient> ingredients = recipe.getIngredients();
		
		int ingredientX = recipeX - 3;
		
		// surrounding input slots
		parent.renderIngredient(drawContext, ingredientX + 16, recipeY, mouseX, mouseY, ingredients.get(1));
		parent.renderIngredient(drawContext, ingredientX + 40, recipeY, mouseX, mouseY, ingredients.get(2));
		parent.renderIngredient(drawContext, ingredientX + 56, recipeY + 16, mouseX, mouseY, ingredients.get(3));
		parent.renderIngredient(drawContext, ingredientX + 56, recipeY + 40, mouseX, mouseY, ingredients.get(4));
		parent.renderIngredient(drawContext, ingredientX + 40, recipeY + 56, mouseX, mouseY, ingredients.get(5));
		parent.renderIngredient(drawContext, ingredientX + 16, recipeY + 56, mouseX, mouseY, ingredients.get(6));
		parent.renderIngredient(drawContext, ingredientX, recipeY + 40, mouseX, mouseY, ingredients.get(7));
		parent.renderIngredient(drawContext, ingredientX, recipeY + 16, mouseX, mouseY, ingredients.get(8));
		
		// center input slot
		parent.renderIngredient(drawContext, ingredientX + 28, recipeY + 28, mouseX, mouseY, ingredients.get(0));
		
		// Knowledge Gem and Enchanter
		ItemStack knowledgeDropStackWithXP = KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience(), true);
		parent.renderItemStack(drawContext, recipeX + 81, recipeY + 9, mouseX, mouseY, knowledgeDropStackWithXP);
		parent.renderItemStack(drawContext, recipeX + 81, recipeY + 46, mouseX, mouseY, SpectrumBlocks.ENCHANTER.asItem().getDefaultStack());
		
		// the output
		parent.renderItemStack(drawContext, recipeX + 81, recipeY + 31, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));
	}
	
	@Override
	protected int getRecipeHeight() {
		return 94;
	}
	
}