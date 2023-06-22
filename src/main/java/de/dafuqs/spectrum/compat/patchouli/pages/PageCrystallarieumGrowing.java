package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import net.minecraft.block.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;

import java.util.*;

public class PageCrystallarieumGrowing extends PageGatedRecipe<CrystallarieumRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/crystallarieum.png");
	private static BookTextRenderer textRenderer;
	
	public PageCrystallarieumGrowing() {
		super(SpectrumRecipeTypes.CRYSTALLARIEUM);
	}
	
	@Override
	protected ItemStack getRecipeOutput(World world, CrystallarieumRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput(world.getRegistryManager());
		}
	}
	
	@Override
	protected void drawRecipe(MatrixStack ms, @NotNull CrystallarieumRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(ms, recipeX - 3, recipeY, 0, 0, 53, 25, 128, 128);
		
		parent.drawCenteredStringNoShadow(ms, getTitle().asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredient
		Ingredient ingredient = recipe.getIngredientStack();
		parent.renderIngredient(ms, recipeX, recipeY + 5, mouseX, mouseY, ingredient);
		
		// growth stages
		Iterator<BlockState> it = recipe.getGrowthStages().iterator();
		BlockState growthState = it.next();
		parent.renderItemStack(ms, recipeX + 23, recipeY - 2, mouseX, mouseY, growthState.getBlock().asItem().getDefaultStack());
		int x = 0;
		while (it.hasNext()) {
			parent.renderItemStack(ms, recipeX + 52 + 16 * x, recipeY + 4, mouseX, mouseY, it.next().getBlock().asItem().getDefaultStack());
			x++;
		}
		
		// crystallarieum
		parent.renderItemStack(ms, recipeX + 23, recipeY + 8, mouseX, mouseY, recipe.createIcon());
		
		// catalyst text
		if (textRenderer == null) {
			textRenderer = new BookTextRenderer(parent, Text.translatable("container.spectrum.patchouli.crystallarieum.catalyst"), 0, 38);
		}
		textRenderer.render(ms, mouseX, mouseY);
		
		// the catalysts
		x = 0;
		int startX = 26;
		int offsetPerReagent = 18;
		for (CrystallarieumCatalyst catalyst : recipe.getCatalysts()) {
			int offsetX = recipeX + startX + offsetPerReagent * x;
			parent.renderIngredient(ms, recipeX + startX + offsetPerReagent * x, recipeY + 27, mouseX, mouseY, catalyst.ingredient);
			
			float growthAcceleration = catalyst.growthAccelerationMod;
			float inkConsumption = catalyst.inkConsumptionMod;
			float consumeChance = catalyst.consumeChancePerSecond;
			
			RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
			RenderSystem.enableBlend();
			int offsetU = growthAcceleration == 1 ? 97 : growthAcceleration >= 6 ? 85 : growthAcceleration > 1 ? 67 : growthAcceleration <= 0.25 ? 79 : 73;
			DrawableHelper.drawTexture(ms, offsetX + 5, recipeY + 45, offsetU, 0, 6, 6, 128, 128);
			
			offsetU = inkConsumption == 1 ? 97 : inkConsumption >= 8 ? 85 : inkConsumption > 1 ? 67 : inkConsumption <= 0.25 ? 79 : 73;
			DrawableHelper.drawTexture(ms, offsetX + 5, recipeY + 54, offsetU, 6, 6, 6, 128, 128);
			
			offsetU = consumeChance == 0 ? 97 : consumeChance >= 0.2 ? 85 : consumeChance >= 0.05 ? 67 : 91;
			DrawableHelper.drawTexture(ms, offsetX + 5, recipeY + 63, offsetU, 6, 6, 6, 128, 128);
			
			x++;
		}
	}
	
	@Override
	protected int getRecipeHeight() {
		return 90;
	}
	
}