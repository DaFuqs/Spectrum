package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public class BookEnchanterUpgradingPageRenderer extends BookGatedRecipePageRenderer<EnchantmentUpgradeRecipe, BookGatedRecipePage<EnchantmentUpgradeRecipe>> {
	
	private static final ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/modonomicon/enchanter_crafting.png");
	private static final ItemStack ENCHANTER = SpectrumBlocks.ENCHANTER.asItem().getDefaultInstance();
	
	public BookEnchanterUpgradingPageRenderer(BookGatedRecipePage<EnchantmentUpgradeRecipe> page) {
		super(page);
	}
	
	@Override
	protected int getRecipeHeight() {
		return 94;
	}
	
	@Override
	protected void drawRecipe(GuiGraphics drawContext, RecipeHolder<EnchantmentUpgradeRecipe> recipeEntry, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		EnchantmentUpgradeRecipe recipe = recipeEntry.value();
		Level world = Minecraft.getInstance().level;
		if (world == null) return;
		
		RenderSystem.enableBlend();
		drawContext.blit(BACKGROUND_TEXTURE, recipeX, recipeY, 0, 0, 100, 80, 256, 256);
		
		renderTitle(drawContext, recipeY, second);
		
		// the ingredients
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		
		// surrounding input slots
		EnchantmentUpgradeRecipe.LevelData levelOneData = recipe.getForSourceLevel(1);
		IngredientStack bowlStack = IngredientStack.of(levelOneData.ingredient(), levelOneData.countPerBowl());
		ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 13, recipeY, mouseX, mouseY, bowlStack);
		ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 37, recipeY, mouseX, mouseY, bowlStack);
		ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 53, recipeY + 16, mouseX, mouseY, bowlStack);
		ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 53, recipeY + 40, mouseX, mouseY, bowlStack);
		ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 37, recipeY + 56, mouseX, mouseY, bowlStack);
		ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX + 13, recipeY + 56, mouseX, mouseY, bowlStack);
		ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX - 3, recipeY + 40, mouseX, mouseY, bowlStack);
		ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, recipeX - 3, recipeY + 16, mouseX, mouseY, bowlStack);
		
		// center input slot
		parentScreen.renderIngredient(drawContext, recipeX + 25, recipeY + 28, mouseX, mouseY, ingredients.getFirst());
		
		// Knowledge Gem and Enchanter
		ItemStack knowledgeDropStackWithXP = KnowledgeGemItem.getKnowledgeDropStackWithXP(levelOneData.experience(), true);
		parentScreen.renderItemStack(drawContext, recipeX + 81, recipeY + 9, mouseX, mouseY, knowledgeDropStackWithXP);
		parentScreen.renderItemStack(drawContext, recipeX + 81, recipeY + 46, mouseX, mouseY, ENCHANTER);
		
		// the output
		parentScreen.renderItemStack(drawContext, recipeX + 81, recipeY + 31, mouseX, mouseY, recipe.getResultItem(world.registryAccess()));
	}
	
}
