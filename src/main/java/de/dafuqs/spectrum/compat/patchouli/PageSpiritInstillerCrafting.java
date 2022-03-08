package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public class PageSpiritInstillerCrafting extends PageDoubleRecipeRegistry<SpiritInstillerRecipe> {

	private static final Identifier BACKGROUND_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/patchouli/spirit_instiller.png");
	private static final ItemStack ITEM_BOWL_STACK = SpectrumBlocks.ITEM_BOWL_CALCITE.asItem().getDefaultStack();
	
	public PageSpiritInstillerCrafting() {
		super(SpectrumRecipeTypes.SPIRIT_INSTILLER_RECIPE);
	}

	@Override
	protected ItemStack getRecipeOutput(SpiritInstillerRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput();
		}
	}

	@Override
	protected void drawRecipe(MatrixStack ms, @NotNull SpiritInstillerRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(ms, recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);

		parent.drawCenteredStringNoShadow(ms, getTitle(second).asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);

		// the ingredients
		DefaultedList<Ingredient> ingredients = recipe.getIngredients();
		parent.renderIngredient(ms, recipeX + 3, recipeY + 8, mouseX, mouseY, ingredients.get(0)); // left
		parent.renderIngredient(ms, recipeX + 44, recipeY + 8, mouseX, mouseY, ingredients.get(1)); // right
		parent.renderIngredient(ms, recipeX + 23, recipeY + 11, mouseX, mouseY, ingredients.get(2)); // center
		
		// spirit instiller
		parent.renderItemStack(ms, recipeX + 23, recipeY + 24, mouseX, mouseY, recipe.createIcon());
		
		// item bowls
		parent.renderItemStack(ms, recipeX + 3, recipeY + 22, mouseX, mouseY, ITEM_BOWL_STACK);
		parent.renderItemStack(ms, recipeX + 44, recipeY + 22, mouseX, mouseY, ITEM_BOWL_STACK);
		
		// the output
		parent.renderItemStack(ms, recipeX + 79, recipeY + 8, mouseX, mouseY, recipe.getOutput());
	}

	@Override
	protected int getRecipeHeight() {
		return 57;
	}

}