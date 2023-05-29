package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.cinderhearth.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;
import vazkii.patchouli.client.book.page.abstr.*;

import java.util.*;

public class PageCinderhearthSmelting extends PageDoubleRecipeRegistry<CinderhearthRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/cinderhearth.png");
	
	final transient List<BookTextRenderer> chanceTextRenders = new ArrayList<>();
	
	public PageCinderhearthSmelting() {
		super(SpectrumRecipeTypes.CINDERHEARTH);
	}
	
	@Override
	protected ItemStack getRecipeOutput(World world, CinderhearthRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput(world.getRegistryManager());
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
		for (int i = 0; i < possibleOutputs.size(); i++) {
			Pair<ItemStack, Float> possibleOutput = possibleOutputs.get(i);
			int x = recipeX + 37 + i * 23;
			parent.renderItemStack(ms, x, recipeY + 6, mouseX, mouseY, possibleOutput.getLeft());
			
			if (possibleOutput.getRight() < 1.0F) {
				if (chanceTextRenders.size() < chanceTextIndex + 1) {
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