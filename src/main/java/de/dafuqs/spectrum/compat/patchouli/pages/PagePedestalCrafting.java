package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.compat.patchouli.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.gui.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;

import java.util.*;

public class PagePedestalCrafting extends PageGatedRecipeSingle<PedestalRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE1 = SpectrumCommon.locate("textures/gui/patchouli/pedestal_crafting1.png");
	private static final Identifier BACKGROUND_TEXTURE2 = SpectrumCommon.locate("textures/gui/patchouli/pedestal_crafting2.png");
	private static final Identifier BACKGROUND_TEXTURE3 = SpectrumCommon.locate("textures/gui/patchouli/pedestal_crafting3.png");
	private static final Identifier BACKGROUND_TEXTURE4 = SpectrumCommon.locate("textures/gui/patchouli/pedestal_crafting4.png");
	
	public PagePedestalCrafting() {
		super(SpectrumRecipeTypes.PEDESTAL);
	}

	@Override
	protected ItemStack getRecipeOutput(World world, PedestalRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput(world.getRegistryManager());
		}
	}
	
	@Override
	protected void drawRecipe(DrawContext drawContext, World world, @NotNull PedestalRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY) {
		RenderSystem.enableBlend();
		drawContext.drawTexture(getBackgroundTextureForTier(recipe.getTier()), recipeX - 2, recipeY - 2, 0, 0, 106, 97, 128, 256);
		
		parent.drawCenteredStringNoShadow(drawContext, getTitle().asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		parent.renderItemStack(drawContext, recipeX + 78, recipeY + 22, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));
		
		switch (recipe.getTier()) {
			case COMPLEX ->
					drawGemstonePowderSlots(drawContext, recipe, recipe.getTier().getAvailableGemstoneColors(), 3, recipeX, recipeY, mouseX, mouseY);
			case ADVANCED ->
					drawGemstonePowderSlots(drawContext, recipe, recipe.getTier().getAvailableGemstoneColors(),12, recipeX, recipeY, mouseX, mouseY);
			default ->
					drawGemstonePowderSlots(drawContext, recipe, recipe.getTier().getAvailableGemstoneColors(),22, recipeX, recipeY, mouseX, mouseY);
		}
		
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		int wrap = recipe.getWidth();
		for (int i = 0; i < ingredients.size(); i++) {
			PatchouliHelper.renderIngredientStack(drawContext, parent, recipeX + (i % wrap) * 19 + 3, recipeY + (i / wrap) * 19 + 3, mouseX, mouseY, ingredients.get(i));
		}
	}
	
	@Contract(pure = true)
	private Identifier getBackgroundTextureForTier(@NotNull PedestalRecipeTier pedestalRecipeTier) {
		switch (pedestalRecipeTier) {
			case BASIC -> {
				return BACKGROUND_TEXTURE1;
			}
			case SIMPLE -> {
				return BACKGROUND_TEXTURE2;
			}
			case ADVANCED -> {
				return BACKGROUND_TEXTURE3;
			}
			default -> {
				return BACKGROUND_TEXTURE4;
			}
		}
	}
	
	@Override
	protected int getRecipeHeight() {
		return 108;
	}
	
	private void drawGemstonePowderSlots(DrawContext drawContext, PedestalRecipe recipe, GemstoneColor @NotNull [] colors, int startX, int recipeX, int recipeY, int mouseX, int mouseY) {
		int h = 0;
		for (GemstoneColor color : colors) {
			int amount = recipe.getPowderInputs().getOrDefault(color, 0);
			if (amount > 0) {
				ItemStack stack = color.getGemstonePowderItem().getDefaultStack();
				stack.setCount(amount);
				parent.renderItemStack(drawContext, recipeX + startX + h * 19, recipeY + 72, mouseX, mouseY, stack);
			}
			h++;
		}
	}
	
	
}