package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.matchbooks.recipe.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.patchouli.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.gui.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;

import java.util.*;

public class PageFusionShrine extends PageGatedRecipeSingle<FusionShrineRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/fusion_shrine.png");
	
	public PageFusionShrine() {
		super(SpectrumRecipeTypes.FUSION_SHRINE);
	}
	
	@Override
	protected ItemStack getRecipeOutput(World world, FusionShrineRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput(world.getRegistryManager());
		}
	}
	
	@Override
	protected void drawRecipe(DrawContext drawContext, World world, @NotNull FusionShrineRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY) {
		RenderSystem.enableBlend();
		drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX - 2, recipeY - 2, 0, 0, 104, 97, 128, 256);
		
		parent.drawCenteredStringNoShadow(drawContext, getTitle().asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredients
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		int startX = Math.max(-5, 30 - ingredients.size() * 8);
		for (int i = 0; i < ingredients.size(); i++) {
			PatchouliHelper.renderIngredientStack(drawContext, parent, recipeX + startX + i * 16, recipeY + 3, mouseX, mouseY, ingredients.get(i));
		}
		
		if (recipe.getFluidInput() != Fluids.EMPTY) {
			Item fluidBucketItem = recipe.getFluidInput().getBucketItem();
			if (fluidBucketItem != null) {
				// the shrine
				parent.renderItemStack(drawContext, recipeX + 14, recipeY + 31, mouseX, mouseY, recipe.createIcon());
				
				// the fluid as a bucket
				ItemStack fluidBucketItemStack = new ItemStack(fluidBucketItem);
				parent.renderItemStack(drawContext, recipeX + 30, recipeY + 31, mouseX, mouseY, fluidBucketItemStack);
				
			}
		} else {
			// the shrine
			parent.renderItemStack(drawContext, recipeX + 22, recipeY + 31, mouseX, mouseY, recipe.createIcon());
		}
		
		// the output
		parent.renderItemStack(drawContext, recipeX + 78, recipeY + 31, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));
	}
	
	@Override
	protected int getRecipeHeight() {
		return 68;
	}
	
}