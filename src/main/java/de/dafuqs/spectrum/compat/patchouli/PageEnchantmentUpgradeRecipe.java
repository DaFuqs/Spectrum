package de.dafuqs.spectrum.compat.patchouli;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;
import vazkii.patchouli.client.book.page.abstr.*;

import java.util.*;

public class PageEnchantmentUpgradeRecipe extends PageDoubleRecipeRegistry<EnchantmentUpgradeRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/enchanter_crafting.png");
	
	public PageEnchantmentUpgradeRecipe() {
		super(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE);
	}
	
	@Override
	protected ItemStack getRecipeOutput(World world, EnchantmentUpgradeRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput(world.getRegistryManager());
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
		
		// surrounding input slots
		List<ItemStack> inputStacks = new ArrayList<>();
		int requiredItemCountSplit = recipe.getRequiredItemCount() / 8;
		int requiredItemCountModulo = recipe.getRequiredItemCount() % 8;
		for (int i = 0; i < 8; i++) {
			int addAmount = i < requiredItemCountModulo ? 1 : 0;
			inputStacks.add(new ItemStack(recipe.getRequiredItem(), requiredItemCountSplit + addAmount));
		}
		
		parent.renderItemStack(ms, ingredientX + 16, recipeY, mouseX, mouseY, inputStacks.get(0));
		parent.renderItemStack(ms, ingredientX + 40, recipeY, mouseX, mouseY, inputStacks.get(1));
		parent.renderItemStack(ms, ingredientX + 56, recipeY + 16, mouseX, mouseY, inputStacks.get(2));
		parent.renderItemStack(ms, ingredientX + 56, recipeY + 40, mouseX, mouseY, inputStacks.get(3));
		parent.renderItemStack(ms, ingredientX + 40, recipeY + 56, mouseX, mouseY, inputStacks.get(4));
		parent.renderItemStack(ms, ingredientX + 16, recipeY + 56, mouseX, mouseY, inputStacks.get(5));
		parent.renderItemStack(ms, ingredientX, recipeY + 40, mouseX, mouseY, inputStacks.get(6));
		parent.renderItemStack(ms, ingredientX, recipeY + 16, mouseX, mouseY, inputStacks.get(7));
		
		// center input slot
		parent.renderIngredient(ms, ingredientX + 28, recipeY + 28, mouseX, mouseY, ingredients.get(0));
		
		// Knowledge Gem and Enchanter
		ItemStack knowledgeDropStackWithXP = KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience(), true);
		parent.renderItemStack(ms, recipeX + 81, recipeY + 9, mouseX, mouseY, knowledgeDropStackWithXP);
		parent.renderItemStack(ms, recipeX + 81, recipeY + 46, mouseX, mouseY, SpectrumBlocks.ENCHANTER.asItem().getDefaultStack());
		
		// the output
		parent.renderItemStack(ms, recipeX + 81, recipeY + 31, mouseX, mouseY, recipe.getOutput(DynamicRegistryManager.EMPTY));
	}
	
	@Override
	protected int getRecipeHeight() {
		return 94;
	}
	
}