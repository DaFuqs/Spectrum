package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.titration_barrel.ITitrationBarrelRecipe;
import de.dafuqs.spectrum.recipe.titration_barrel.TitrationBarrelRecipe;
import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

import java.util.List;

public class PageTitrationBarrelFermenting extends PageDoubleRecipeRegistry<ITitrationBarrelRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/patchouli/titration_barrel.png");
	private static final IngredientStack WATER_BUCKET = IngredientStack.of(Ingredient.ofStacks(Items.WATER_BUCKET.getDefaultStack()));
	
	private static BookTextRenderer textRenderer;
	
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
		DrawableHelper.drawTexture(ms, recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);
		
		parent.drawCenteredStringNoShadow(ms, getTitle(second).asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredients
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		int ingredientSize = ingredients.size();
		int startX = recipeX + Math.max(-5, 15 - ingredientSize * 10);
		int startY = recipeY + (ingredientSize > 2 ? 0 : 10);
		for (int i = 0; i < ingredientSize + 1; i++) {
			IngredientStack currentIngredient = i == ingredientSize ? WATER_BUCKET : ingredients.get(i);
			int yOffset;
			int xOffset;
			if(i < 3) {
				xOffset = i * 18;
				yOffset = 0;
			} else {
				xOffset = (i - 3) * 18;
				yOffset = 18;
			}
			PatchouliHelper.renderIngredientStack(parent, ms, startX + xOffset, startY + yOffset, mouseX, mouseY, currentIngredient);
		}
		
		// the titration barrel / tapping ingredient
		if(recipe.getTappingIngredient().isEmpty()) {
			parent.renderItemStack(ms, recipeX + 54, recipeY + 20, mouseX, mouseY, recipe.createIcon());
		} else {
			parent.renderIngredient(ms, recipeX + 54, recipeY + 20, mouseX, mouseY, recipe.getTappingIngredient());
		}
		
		// the output
		parent.renderItemStack(ms, recipeX + 78, recipeY + 10, mouseX, mouseY, recipe.getOutput());
		
		// the duration
		if(textRenderer == null) {
			MutableText text = TitrationBarrelRecipe.getDurationText(recipe.getMinFermentationTimeHours(), recipe.getFermentationData());
			textRenderer = new BookTextRenderer(parent, text, 0, recipeY + 42);
		}
		textRenderer.render(ms, mouseX, mouseY);
	}
	
	@Override
	protected int getRecipeHeight() {
		return 66;
	}
	
}