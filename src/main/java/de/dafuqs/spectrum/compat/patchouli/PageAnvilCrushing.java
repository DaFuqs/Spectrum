package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public class PageAnvilCrushing extends PageDoubleRecipeRegistry<AnvilCrushingRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/anvil_crushing.png");
	
	public PageAnvilCrushing() {
		super(SpectrumRecipeTypes.ANVIL_CRUSHING);
	}
	
	@Override
	protected ItemStack getRecipeOutput(AnvilCrushingRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput();
		}
	}
	
	@Override
	protected void drawRecipe(MatrixStack ms, @NotNull AnvilCrushingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		RenderSystem.enableBlend();
		
		// dirt  wall
		DrawableHelper.drawTexture(ms, recipeX, recipeY + 4, 0, 0, 84, 48, 256, 256);
		
		parent.drawCenteredStringNoShadow(ms, getTitle(second).asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredients
		Ingredient ingredient = recipe.getIngredients().get(0);
		parent.renderIngredient(ms, recipeX + 16, recipeY + 35, mouseX, mouseY, ingredient);
		
		// the anvil
		parent.renderItemStack(ms, recipeX + 16, recipeY + 15, mouseX, mouseY, recipe.createIcon());
		
		// the output
		parent.renderItemStack(ms, recipeX + 64, recipeY + 29, mouseX, mouseY, recipe.getOutput());
	}
	
	@Override
	protected int getRecipeHeight() {
		return 73;
	}
	
}