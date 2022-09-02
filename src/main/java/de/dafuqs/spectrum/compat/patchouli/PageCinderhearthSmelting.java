package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.cinderhearth.CinderhearthRecipe;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

import java.util.ArrayList;
import java.util.List;

public class PageCinderhearthSmelting extends PageDoubleRecipeRegistry<CinderhearthRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/cinderhearth.png");
	
	transient List<BookTextRenderer> chanceTextRenders = new ArrayList<>();
	
	public PageCinderhearthSmelting() {
		super(SpectrumRecipeTypes.CINDERHEARTH);
	}
	
	@Override
	protected ItemStack getRecipeOutput(CinderhearthRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput();
		}
	}
	
	@Override
	protected void drawRecipe(MatrixStack ms, @NotNull CinderhearthRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		RenderSystem.enableBlend();
		
		List<Pair<ItemStack, Float>> possibleOutputs = recipe.getOutputsWithChance();
		recipeX = Math.max(recipeX, recipeX + 26 - possibleOutputs.size() * 10);
		
		int backgroundTextureWidth = 34 + possibleOutputs.size() * 24;
		DrawableHelper.drawTexture(ms, recipeX - 1, recipeY - 2, 0, 0, backgroundTextureWidth, 45, 128, 128);
		
		parent.drawCenteredStringNoShadow(ms, getTitle(second).asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredient
		Ingredient ingredient = recipe.getIngredients().get(0);
		parent.renderIngredient(ms, recipeX + 2, recipeY + 7, mouseX, mouseY, ingredient);
		
		// cinderhearth
		parent.renderItemStack(ms, recipeX + 21, recipeY + 26, mouseX, mouseY, recipe.createIcon());
		
		// outputs
		int chanceTextIndex = 0;
		for(int i = 0; i < possibleOutputs.size(); i++) {
			Pair<ItemStack, Float> possibleOutput = possibleOutputs.get(i);
			int x = recipeX + 37 + i * 23;
			parent.renderItemStack(ms, x, recipeY + 6, mouseX, mouseY, possibleOutput.getLeft());
			
			if(possibleOutput.getRight() < 1.0F) {
				if(chanceTextRenders.size() < chanceTextIndex + 1) {
					chanceTextRenders.add(new BookTextRenderer(parent, Text.literal((int) (possibleOutput.getRight() * 100) + "%"), x, recipeY + 24));
				}
				chanceTextRenders.get(chanceTextIndex).render(ms, mouseX, mouseY);
				chanceTextIndex++;
			}
		}
	}
	
	@Override
	protected int getRecipeHeight() {
		return 58;
	}
	
}