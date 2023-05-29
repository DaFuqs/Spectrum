package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;
import vazkii.patchouli.client.book.page.abstr.*;

public abstract class PagePotionWorkshop extends PageDoubleRecipeRegistry<PotionWorkshopRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/potion_workshop.png");
	
	public PagePotionWorkshop(RecipeType recipeType) {
		super(recipeType);
	}
	
	@Override
	protected ItemStack getRecipeOutput(World world, PotionWorkshopRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput(world.getRegistryManager());
		}
	}
	
	@Override
	protected void drawRecipe(MatrixStack ms, @NotNull PotionWorkshopRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(ms, recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);
		
		parent.drawCenteredStringNoShadow(ms, getTitle(second).asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredients
		DefaultedList<Ingredient> ingredients = recipe.getIngredients();
		parent.renderIngredient(ms, recipeX + 20, recipeY + 62, mouseX, mouseY, ingredients.get(0));
		parent.renderIngredient(ms, recipeX + 58, recipeY + 5, mouseX, mouseY, ingredients.get(1));
		parent.renderIngredient(ms, recipeX + 20, recipeY + 9, mouseX, mouseY, ingredients.get(2));
		parent.renderIngredient(ms, recipeX + 3, recipeY + 32, mouseX, mouseY, ingredients.get(3));
		parent.renderIngredient(ms, recipeX + 37, recipeY + 32, mouseX, mouseY, ingredients.get(4));
		
		// the potion workshop
		parent.renderItemStack(ms, recipeX + 82, recipeY + 42, mouseX, mouseY, recipe.createIcon());
		
		// the output
		parent.renderItemStack(ms, recipeX + 82, recipeY + 24, mouseX, mouseY, recipe.getOutput(DynamicRegistryManager.EMPTY));
	}
	
	@Override
	protected int getRecipeHeight() {
		return 97;
	}
	
}