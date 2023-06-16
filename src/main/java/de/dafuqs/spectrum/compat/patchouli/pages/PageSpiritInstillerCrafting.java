package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.patchouli.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.registries.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;

import java.util.*;

public class PageSpiritInstillerCrafting extends PageGatedRecipe<SpiritInstillerRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/spirit_instiller.png");
	private static final ItemStack ITEM_BOWL_STACK = SpectrumBlocks.ITEM_BOWL_CALCITE.asItem().getDefaultStack();
	
	public PageSpiritInstillerCrafting() {
		super(SpectrumRecipeTypes.SPIRIT_INSTILLING);
	}
	
	@Override
	protected ItemStack getRecipeOutput(SpiritInstillerRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput();
		}
	}
	
	@Override
	protected void drawRecipe(MatrixStack ms, @NotNull SpiritInstillerRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(ms, recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);
		
		parent.drawCenteredStringNoShadow(ms, getTitle().asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredients
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		PatchouliHelper.renderIngredientStack(parent, ms, recipeX + 3, recipeY + 8, mouseX, mouseY, ingredients.get(1)); // left
		PatchouliHelper.renderIngredientStack(parent, ms, recipeX + 23, recipeY + 11, mouseX, mouseY, ingredients.get(0)); // center
		PatchouliHelper.renderIngredientStack(parent, ms, recipeX + 44, recipeY + 8, mouseX, mouseY, ingredients.get(2)); // right
		
		// spirit instiller
		parent.renderItemStack(ms, recipeX + 23, recipeY + 25, mouseX, mouseY, recipe.createIcon());
		
		// item bowls
		parent.renderItemStack(ms, recipeX + 3, recipeY + 25, mouseX, mouseY, ITEM_BOWL_STACK);
		parent.renderItemStack(ms, recipeX + 44, recipeY + 25, mouseX, mouseY, ITEM_BOWL_STACK);
		
		// the output
		parent.renderItemStack(ms, recipeX + 79, recipeY + 8, mouseX, mouseY, recipe.getOutput());
	}
	
	@Override
	protected int getRecipeHeight() {
		return 58;
	}
	
}