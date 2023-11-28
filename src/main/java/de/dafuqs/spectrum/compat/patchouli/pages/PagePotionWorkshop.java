package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.client.gui.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;

import java.util.*;

public abstract class PagePotionWorkshop<T extends PotionWorkshopRecipe> extends PageGatedRecipeSingle<T> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/potion_workshop.png");
	
	public PagePotionWorkshop(RecipeType<T> recipeType) {
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
	protected void drawRecipe(DrawContext drawContext, World world, @NotNull PotionWorkshopRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		RenderSystem.enableBlend();
		drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);
		
		parent.drawCenteredStringNoShadow(drawContext, getTitle().asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredients
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		renderIngredientStack(drawContext, recipeX + 20, recipeY + 62, mouseX, mouseY, ingredients.get(0));
		renderIngredientStack(drawContext, recipeX + 58, recipeY + 5, mouseX, mouseY, ingredients.get(1));
		renderIngredientStack(drawContext, recipeX + 20, recipeY + 9, mouseX, mouseY, ingredients.get(2));
		renderIngredientStack(drawContext, recipeX + 3, recipeY + 32, mouseX, mouseY, ingredients.get(3));
		renderIngredientStack(drawContext, recipeX + 37, recipeY + 32, mouseX, mouseY, ingredients.get(4));
		
		// the potion workshop
		parent.renderItemStack(drawContext, recipeX + 82, recipeY + 42, mouseX, mouseY, recipe.createIcon());
		
		// the output
		parent.renderItemStack(drawContext, recipeX + 82, recipeY + 24, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));
	}
	
	@Override
	protected int getRecipeHeight() {
		return 97;
	}
	
	private void renderIngredientStack(DrawContext graphics, int x, int y, int mouseX, int mouseY, IngredientStack ingredientStack) {
		List<ItemStack> stacks = ingredientStack.getStacks();
		if (!stacks.isEmpty()) {
			parent.renderItemStack(graphics, x, y, mouseX, mouseY, stacks.get((parent.getTicksInBook() / 20) % stacks.size()));
		}
	}

}