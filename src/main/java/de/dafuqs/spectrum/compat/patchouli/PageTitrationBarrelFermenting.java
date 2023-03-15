package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.util.math.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;
import vazkii.patchouli.client.book.page.abstr.*;

import java.util.*;

public class PageTitrationBarrelFermenting extends PageDoubleRecipeRegistry<ITitrationBarrelRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/titration_barrel.png");
	
	private transient BookTextRenderer textRenderer;
	private transient BookTextRenderer textRenderer2;
	
	public PageTitrationBarrelFermenting() {
		super(SpectrumRecipeTypes.TITRATION_BARREL);
	}
	
	@Override
	protected ItemStack getRecipeOutput(ITitrationBarrelRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput();
		}
	}
	
	@Override
	protected void drawRecipe(MatrixStack ms, @NotNull ITitrationBarrelRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(ms, recipeX - 2, recipeY - 2, 0, 0, 100, 32, 128, 256);
		
		parent.drawCenteredStringNoShadow(ms, getTitle(second).asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		Fluid fluid = recipe.getFluidInput();
		boolean usesFluid = fluid != Fluids.EMPTY;
		IngredientStack bucketStack = IngredientStack.EMPTY;
		if (usesFluid) {
			bucketStack = IngredientStack.of(Ingredient.ofStacks(recipe.getFluidInput().getBucketItem().getDefaultStack()));
		}
		
		// the ingredients
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		int ingredientSize = ingredients.size();
		int startX = recipeX + Math.max(-5, 15 - ingredientSize * 10);
		int startY = recipeY + (ingredientSize > 2 ? 0 : 10);
		int ingredientSizeWithFluid = usesFluid ? ingredientSize + 1 : ingredientSize;
		for (int i = 0; i < ingredientSizeWithFluid; i++) {
			IngredientStack currentIngredient = i == ingredientSize ? bucketStack : ingredients.get(i);
			int yOffset;
			int xOffset;
			if (i < 3) {
				xOffset = i * 18;
				yOffset = 0;
			} else {
				xOffset = (i - 3) * 18;
				yOffset = 18;
			}
			PatchouliHelper.renderIngredientStack(parent, ms, startX + xOffset, startY + yOffset, mouseX, mouseY, currentIngredient);
		}
		
		// the titration barrel / tapping ingredient
		if (recipe.getTappingItem() == Items.AIR) {
			parent.renderItemStack(ms, recipeX + 54, recipeY + 20, mouseX, mouseY, recipe.createIcon());
		} else {
			parent.renderItemStack(ms, recipeX + 54, recipeY + 20, mouseX, mouseY, recipe.getTappingItem().getDefaultStack());
		}
		
		// the output
		parent.renderItemStack(ms, recipeX + 78, recipeY + 10, mouseX, mouseY, recipe.getOutput());
		
		// the duration
		if (second) {
			if (textRenderer2 == null) {
				MutableText text = TitrationBarrelRecipe.getDurationText(recipe.getMinFermentationTimeHours(), recipe.getFermentationData());
				textRenderer2 = new BookTextRenderer(parent, text, 0, recipeY + 40);
			}
			textRenderer2.render(ms, mouseX, mouseY);
		} else {
			if (textRenderer == null) {
				MutableText text = TitrationBarrelRecipe.getDurationText(recipe.getMinFermentationTimeHours(), recipe.getFermentationData());
				textRenderer = new BookTextRenderer(parent, text, 0, recipeY + 40);
			}
			textRenderer.render(ms, mouseX, mouseY);
		}
	}
	
	@Override
	protected int getRecipeHeight() {
		return 66;
	}
	
}