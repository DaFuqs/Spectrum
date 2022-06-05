package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.magic_items.KnowledgeGemItem;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public class PageEnchanterRecipe extends PageDoubleRecipeRegistry<EnchanterRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/patchouli/enchanter_crafting.png");
	
	public PageEnchanterRecipe() {
		super(SpectrumRecipeTypes.ENCHANTER);
	}
	
	@Override
	protected ItemStack getRecipeOutput(EnchanterRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput();
		}
	}
	
	@Override
	protected void drawRecipe(MatrixStack ms, @NotNull EnchanterRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(ms, recipeX, recipeY, 0, 0, 100, 80, 256, 256);
		
		parent.drawCenteredStringNoShadow(ms, getTitle(second).asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredients
		DefaultedList<Ingredient> ingredients = recipe.getIngredients();
		
		int ingredientX = recipeX - 3;
		int ingredientY = recipeY;
		
		// surrounding input slots
		parent.renderIngredient(ms, ingredientX + 16, ingredientY, mouseX, mouseY, ingredients.get(1));
		parent.renderIngredient(ms, ingredientX + 40, ingredientY, mouseX, mouseY, ingredients.get(2));
		parent.renderIngredient(ms, ingredientX + 56, ingredientY + 16, mouseX, mouseY, ingredients.get(3));
		parent.renderIngredient(ms, ingredientX + 56, ingredientY + 40, mouseX, mouseY, ingredients.get(4));
		parent.renderIngredient(ms, ingredientX + 40, ingredientY + 56, mouseX, mouseY, ingredients.get(5));
		parent.renderIngredient(ms, ingredientX + 16, ingredientY + 56, mouseX, mouseY, ingredients.get(6));
		parent.renderIngredient(ms, ingredientX, ingredientY + 40, mouseX, mouseY, ingredients.get(7));
		parent.renderIngredient(ms, ingredientX, ingredientY + 16, mouseX, mouseY, ingredients.get(8));
		
		// center input slot
		parent.renderIngredient(ms, ingredientX + 28, ingredientY + 28, mouseX, mouseY, ingredients.get(0));
		
		// Knowledge Gem and Enchanter
		ItemStack knowledgeDropStackWithXP = KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience());
		parent.renderItemStack(ms, recipeX + 81, recipeY + 9, mouseX, mouseY, knowledgeDropStackWithXP);
		parent.renderItemStack(ms, recipeX + 81, recipeY + 46, mouseX, mouseY, SpectrumBlocks.ENCHANTER.asItem().getDefaultStack());
		
		// the output
		parent.renderItemStack(ms, recipeX + 81, recipeY + 31, mouseX, mouseY, recipe.getOutput());
	}
	
	@Override
	protected int getRecipeHeight() {
		return 94;
	}
	
}