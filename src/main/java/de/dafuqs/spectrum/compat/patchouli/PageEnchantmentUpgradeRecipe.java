package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.magic_items.KnowledgeGemItem;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

import java.util.ArrayList;
import java.util.List;

public class PageEnchantmentUpgradeRecipe extends PageDoubleRecipeRegistry<EnchantmentUpgradeRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/patchouli/enchanter_crafting.png");
	
	public PageEnchantmentUpgradeRecipe() {
		super(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE);
	}
	
	@Override
	protected ItemStack getRecipeOutput(EnchantmentUpgradeRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput();
		}
	}
	
	@Override
	protected void drawRecipe(MatrixStack ms, @NotNull EnchantmentUpgradeRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(ms, recipeX, recipeY, 0, 0, 100, 80, 256, 256);
		
		parent.drawCenteredStringNoShadow(ms, getTitle(second).asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredients
		DefaultedList<Ingredient> ingredients = recipe.getIngredients();
		
		int ingredientX = recipeX - 3;
		int ingredientY = recipeY;
		
		// surrounding input slots
		List<ItemStack> inputStacks = new ArrayList<>();
		int requiredItemCountSplit = recipe.getRequiredItemCount() / 8;
		int requiredItemCountModulo = recipe.getRequiredItemCount() % 8;
		for (int i = 0; i < 8; i++) {
			int addAmount = i < requiredItemCountModulo ? 1 : 0;
			inputStacks.add(new ItemStack(recipe.getRequiredItem(), requiredItemCountSplit + addAmount));
		}
		
		parent.renderItemStack(ms, ingredientX + 16, ingredientY, mouseX, mouseY, inputStacks.get(0));
		parent.renderItemStack(ms, ingredientX + 40, ingredientY, mouseX, mouseY, inputStacks.get(1));
		parent.renderItemStack(ms, ingredientX + 56, ingredientY + 16, mouseX, mouseY, inputStacks.get(2));
		parent.renderItemStack(ms, ingredientX + 56, ingredientY + 40, mouseX, mouseY, inputStacks.get(3));
		parent.renderItemStack(ms, ingredientX + 40, ingredientY + 56, mouseX, mouseY, inputStacks.get(4));
		parent.renderItemStack(ms, ingredientX + 16, ingredientY + 56, mouseX, mouseY, inputStacks.get(5));
		parent.renderItemStack(ms, ingredientX, ingredientY + 40, mouseX, mouseY, inputStacks.get(6));
		parent.renderItemStack(ms, ingredientX, ingredientY + 16, mouseX, mouseY, inputStacks.get(7));
		
		// center input slot
		parent.renderIngredient(ms, ingredientX + 28, ingredientY + 28, mouseX, mouseY, ingredients.get(0));
		
		// Knowledge Gem and Enchanter
		ItemStack knowledgeDropStackWithXP = KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience());
		parent.renderItemStack(ms, recipeX + 81, recipeY + 9, mouseX, mouseY, knowledgeDropStackWithXP);
		parent.renderItemStack(ms, recipeX + 81, recipeY + 46, mouseX, mouseY, SpectrumBlocks.ENCHANTER.asItem().getDefaultStack());
		
		// the output
		parent.renderItemStack(ms, recipeX + 81, recipeY + 31, mouseX, mouseY, recipe.getOutput());
	}
	
	@Override
	protected int getRecipeHeight() {
		return 94;
	}
	
}