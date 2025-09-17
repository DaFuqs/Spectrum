package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.*;
import com.klikli_dev.modonomicon.client.gui.book.entry.*;
import com.klikli_dev.modonomicon.data.*;
import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class BookCrystallarieumGrowingPageRenderer extends BookGatedRecipePageRenderer<CrystallarieumRecipe, BookGatedRecipePage<CrystallarieumRecipe>> {
	
	private static final ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/crystallarieum.png");
	
	private static BookTextHolder catalystText;
	private BookTextHolder craftingTimeText1 = null;
	private BookTextHolder craftingTimeText2 = null;
	
	public BookCrystallarieumGrowingPageRenderer(BookGatedRecipePage<CrystallarieumRecipe> page) {
		super(page);
		
		ResourceLocation font = BookDataManager.Client.get().safeFont(this.page.getBook().getFont());
		
		if (catalystText == null) {
			catalystText = new BookTextHolder(Component.translatable("container.spectrum.modonomicon.crystallarieum.catalyst").withStyle(s -> s.withFont(font)));
		}
		
		if (page.getRecipe1() != null) {
			craftingTimeText1 = new BookTextHolder(Component.translatable(page.getRecipe1().value().growsWithoutCatalyst()
					? "container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds_catalyst_optional"
					: "container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds", page.getRecipe1().value().getSecondsPerGrowthStage()).withStyle(s -> s.withFont(font)));
		}
		
		if (page.getRecipe2() != null) {
			craftingTimeText2 = new BookTextHolder(Component.translatable(page.getRecipe2().value().growsWithoutCatalyst()
					? "container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds_catalyst_optional"
					: "container.spectrum.rei.crystallarieum.crafting_time_per_stage_seconds", page.getRecipe2().value().getSecondsPerGrowthStage()).withStyle(s -> s.withFont(font)));
		}
	}
	
	@Override
	protected int getRecipeHeight() {
		return 100;
	}
	
	@Override
	protected void drawRecipe(GuiGraphics drawContext, RecipeHolder<CrystallarieumRecipe> recipeEntry, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		CrystallarieumRecipe recipe = recipeEntry.value();
		Level world = Minecraft.getInstance().level;
		if (world == null) return;
		recipeY += 2;
		
		RenderSystem.enableBlend();
		
		renderTitle(drawContext, recipeY, second);
		
		// the ingredient
		int startX = 26;
		int offsetPerReagent = 18;
		Ingredient ingredient = recipe.getIngredientStack();
		parentScreen.renderIngredient(drawContext, recipeX + startX, recipeY + 5, mouseX, mouseY, ingredient);
		parentScreen.renderFluidStack(drawContext, recipeX + startX - offsetPerReagent - 4, recipeY + 5, mouseX, mouseY, new FabricFluidHolder(recipe.getFluidMedium(), 1000));
		drawContext.blit(BACKGROUND_TEXTURE, recipeX + startX - offsetPerReagent - 7, recipeY + 1, 0, 0, 53, 25, 128, 128);
		
		
		// growth stages
		Iterator<BlockState> it = recipe.getGrowthStages().iterator();
		BlockState growthState = it.next();
		parentScreen.renderItemStack(drawContext, recipeX + startX + offsetPerReagent, recipeY - 1, mouseX, mouseY, growthState.getBlock().asItem().getDefaultInstance());
		int x = 0;
		while (it.hasNext()) {
			parentScreen.renderItemStack(drawContext, recipeX + 62 + offsetPerReagent * x, recipeY + 4, mouseX, mouseY, it.next().getBlock().asItem().getDefaultInstance());
			x++;
		}
		
		// crystallarieum
		parentScreen.renderItemStack(drawContext, recipeX + startX + offsetPerReagent, recipeY + 8, mouseX, mouseY, SpectrumBlocks.CRYSTALLARIEUM.asStackWithColor(recipe.getInkColor()));
		
		// catalyst text
		renderBookTextHolder(drawContext, catalystText, 0, 42, BookEntryScreen.PAGE_WIDTH);
		renderBookTextHolder(drawContext, second ? craftingTimeText2 : craftingTimeText1, 0, 82, BookEntryScreen.PAGE_WIDTH);
		
		// the catalysts
		x = 0;
		recipeY += 4;
		for (CrystallarieumCatalyst catalyst : recipe.getCatalysts()) {
			int offsetX = recipeX + startX + offsetPerReagent * x;
			parentScreen.renderIngredient(drawContext, recipeX + startX + offsetPerReagent * x, recipeY + 27, mouseX, mouseY, catalyst.ingredient());
			
			RenderSystem.enableBlend();
			int offset = 0;
			float accel = catalyst.growthAccelerationMod();
			
			if (accel > 0.2) {
				if (accel >= 5)
					offset = 7 * 4;
				else if (accel > 1)
					offset = 7 * 3;
				else if (accel == 1)
					offset = 7 * 2;
				else if (accel < 1)
					offset = 7;
			}
			drawContext.blit(BACKGROUND_TEXTURE, offsetX + 5, recipeY + 45, 7, 7, 70 + offset, 0, 7, 7, 128, 128);
			
			float drain = catalyst.inkConsumptionMod();
			
			if (drain >= 5)
				offset = 0;
			else if (drain > 1)
				offset = 7;
			else if (drain == 1)
				offset = 7 * 2;
			else if (drain < 0.2)
				offset = 7 * 4;
			else if (drain < 1)
				offset = 7 * 3;
			
			drawContext.blit(BACKGROUND_TEXTURE, offsetX + 5, recipeY + 54, 7, 7, 70 + offset, 7, 7, 7, 128, 128);
			
			float chance = catalyst.consumeChancePerSecond();
			
			if (chance >= 0.25)
				offset = 0;
			else if (chance < 0.0001)
				offset = 7 * 4;
			else if (chance <= 0.02)
				offset = 7 * 3;
			else if (chance < 0.05)
				offset = 7 * 2;
			else if (chance < 0.25)
				offset = 7;
			
			drawContext.blit(BACKGROUND_TEXTURE, offsetX + 5, recipeY + 63, 7, 7, 70 + offset, 14, 7, 7, 128, 128);
			
			x++;
		}
	}
	
}
