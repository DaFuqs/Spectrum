package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.cinderhearth.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.gui.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;

import java.util.*;

public class PageCinderhearthSmelting extends PageGatedRecipeSingle<CinderhearthRecipe> {
	
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
	protected void drawRecipe(DrawContext drawContext, World world, @NotNull CinderhearthRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY) {
		RenderSystem.enableBlend();
		
		List<Pair<ItemStack, Float>> possibleOutputs = recipe.getOutputsWithChance(world.getRegistryManager());
		recipeX = Math.max(recipeX, recipeX + 26 - possibleOutputs.size() * 10);
		
		int backgroundTextureWidth = 34 + possibleOutputs.size() * 24;
		drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX - 1, recipeY - 2, 0, 0, backgroundTextureWidth, 45, 128, 128);
		
		parent.drawCenteredStringNoShadow(drawContext, titleText.asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredient
		Ingredient ingredient = recipe.getIngredients().get(0);
		parent.renderIngredient(drawContext, recipeX + 2, recipeY + 7, mouseX, mouseY, ingredient);
		
		// cinderhearth
		parent.renderItemStack(drawContext, recipeX + 21, recipeY + 26, mouseX, mouseY, recipe.createIcon());
		
		// outputs
		int chanceTextIndex = 0;
		for (int i = 0; i < possibleOutputs.size(); i++) {
			Pair<ItemStack, Float> possibleOutput = possibleOutputs.get(i);
			int x = recipeX + 37 + i * 23;
			parent.renderItemStack(drawContext, x, recipeY + 6, mouseX, mouseY, possibleOutput.getLeft());
			
			if (possibleOutput.getRight() < 1.0F) {
				if (chanceTextRenders.size() < chanceTextIndex + 1) {
					chanceTextRenders.add(new BookTextRenderer(parent, Text.literal((int) (possibleOutput.getRight() * 100) + "%"), x, recipeY + 24));
				}
				chanceTextRenders.get(chanceTextIndex).render(drawContext, mouseX, mouseY);
				chanceTextIndex++;
			}
		}
	}
	
	@Override
	protected int getRecipeHeight() {
		return 58;
	}
	
}