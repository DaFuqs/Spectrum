package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.client.gui.book.entry.*;
import com.klikli_dev.modonomicon.data.*;
import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.cinderhearth.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class BookCinderhearthSmeltingPageRenderer extends BookGatedRecipePageRenderer<CinderhearthRecipe, BookGatedRecipePage<CinderhearthRecipe>> {
	
	private static final ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/cinderhearth.png");
	
	private @Nullable List<BookTextHolder> chanceTexts1 = null;
	private @Nullable List<BookTextHolder> chanceTexts2 = null;
	
	public BookCinderhearthSmeltingPageRenderer(BookGatedRecipePage<CinderhearthRecipe> page) {
		super(page);
	}
	
	@Override
	public void onBeginDisplayPage(BookEntryScreen parentScreen, int left, int top) {
		super.onBeginDisplayPage(parentScreen, left, top);
		
		if (chanceTexts1 == null) {
			if (page.getRecipe1() != null)
				chanceTexts1 = createChanceTexts(page.getRecipe1().value());
		}
		if (chanceTexts2 == null) {
			if (page.getRecipe2() != null)
				chanceTexts2 = createChanceTexts(page.getRecipe2().value());
		}
	}
	
	private @Nullable List<BookTextHolder> createChanceTexts(CinderhearthRecipe recipe) {
		Level world = Minecraft.getInstance().level;
		if (world == null) return null;
		
		ResourceLocation font = BookDataManager.Client.get().safeFont(this.page.getBook().getFont());
		
		List<BookTextHolder> chanceTexts = new ArrayList<>();
		List<StackWithChance> possibleOutputs = recipe.getResultsWithChance();
		
		int chanceTextIndex = 0;
		for (StackWithChance possibleOutput : possibleOutputs) {
			if (possibleOutput.chance() < 1.0F) {
				if (chanceTexts.size() < chanceTextIndex + 1) {
					chanceTexts.add(new BookTextHolder(Component.literal(String.format("%f.2%%", possibleOutput.chance() * 100)).withStyle(s -> s.withFont(font))));
				}
				chanceTextIndex++;
			}
		}
		
		return chanceTexts;
	}
	
	@Override
	protected int getRecipeHeight() {
		return 54;
	}
	
	@Override
	protected void drawRecipe(GuiGraphics drawContext, RecipeHolder<CinderhearthRecipe> recipeEntry, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		CinderhearthRecipe recipe = recipeEntry.value();
		Level world = Minecraft.getInstance().level;
		if (world == null) return;
		
		RenderSystem.enableBlend();
		
		List<StackWithChance> possibleOutputs = recipe.getResultsWithChance();
		recipeX = Math.max(recipeX, recipeX + 26 - possibleOutputs.size() * 10);
		
		int backgroundTextureWidth = 34 + possibleOutputs.size() * 24;
		drawContext.blit(BACKGROUND_TEXTURE, recipeX - 1, recipeY - 2, 0, 0, backgroundTextureWidth, 45, 128, 128);
		
		renderTitle(drawContext, recipeY, second);
		
		// the ingredient
		var ingredientStack = recipe.getIngredientStacks().getFirst();
		parentScreen.renderIngredient(drawContext, recipeX + 2, recipeY + 7, mouseX, mouseY, ingredientStack.getIngredient(), ingredientStack.getCount());
		
		// cinderhearth
		parentScreen.renderItemStack(drawContext, recipeX + 21, recipeY + 26, mouseX, mouseY, recipe.getToastSymbol());
		
		// outputs
		int chanceTextIndex = 0;
		for (int i = 0; i < possibleOutputs.size(); i++) {
			StackWithChance possibleOutput = possibleOutputs.get(i);
			int x = recipeX + 37 + i * 23;
			parentScreen.renderItemStack(drawContext, x, recipeY + 6, mouseX, mouseY, possibleOutput.stack());
			
			if (possibleOutput.chance() < 1.0F) {
				var chance = second ? chanceTexts2 : chanceTexts1;
				
				if (chance == null)
					continue;
				
				BookTextHolder chanceText = chance.get(chanceTextIndex);
				renderBookTextHolder(drawContext, chanceText, x, recipeY + 24, BookEntryScreen.PAGE_WIDTH);
				chanceTextIndex++;
			}
		}
	}
	
}
