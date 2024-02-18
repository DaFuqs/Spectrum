package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.patchouli.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.gui.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;

import java.util.*;

public class PageSpiritInstillerCrafting extends PageGatedRecipeSingle<SpiritInstillerRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/spirit_instiller.png");
	private static final ItemStack ITEM_BOWL_STACK = SpectrumBlocks.ITEM_BOWL_CALCITE.asItem().getDefaultStack();
	
	public PageSpiritInstillerCrafting() {
		super(SpectrumRecipeTypes.SPIRIT_INSTILLING);
	}
	
	@Override
	protected ItemStack getRecipeOutput(World world, SpiritInstillerRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput(world.getRegistryManager());
		}
	}
	
	@Override
	protected void drawRecipe(DrawContext drawContext, World world, @NotNull SpiritInstillerRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY) {
		RenderSystem.enableBlend();
		drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);
		
		parent.drawCenteredStringNoShadow(drawContext, getTitle().asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredients
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		PatchouliHelper.renderIngredientStack(drawContext, parent, recipeX + 3, recipeY + 8, mouseX, mouseY, ingredients.get(1)); // left
		PatchouliHelper.renderIngredientStack(drawContext, parent, recipeX + 23, recipeY + 11, mouseX, mouseY, ingredients.get(0)); // center
		PatchouliHelper.renderIngredientStack(drawContext, parent, recipeX + 44, recipeY + 8, mouseX, mouseY, ingredients.get(2)); // right
		
		// spirit instiller
		parent.renderItemStack(drawContext, recipeX + 23, recipeY + 25, mouseX, mouseY, recipe.createIcon());
		
		// item bowls
		parent.renderItemStack(drawContext, recipeX + 3, recipeY + 25, mouseX, mouseY, ITEM_BOWL_STACK);
		parent.renderItemStack(drawContext, recipeX + 44, recipeY + 25, mouseX, mouseY, ITEM_BOWL_STACK);
		
		// the output
		parent.renderItemStack(drawContext, recipeX + 79, recipeY + 8, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));
	}
	
	@Override
	protected int getRecipeHeight() {
		return 58;
	}
	
}