package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import dev.emi.emi.api.recipe.*;
import net.minecraft.client.*;

public class EnchantmentUpgradeEmiRecipeGated extends EnchanterEmiRecipeGated {
	
	protected boolean requiresOverEnchanting;
	
	public EnchantmentUpgradeEmiRecipeGated(EmiRecipeCategory category, EnchantmentUpgradeRecipe recipe) {
		super(category, recipe);
		this.requiresOverEnchanting = recipe.requiresUnlockedOverEnchanting();
	}
	
	@Override
	public boolean isUnlocked() {
		if (!super.isUnlocked()) {
			return false;
		}
		if (requiresOverEnchanting) {
			return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, EnchanterBlockEntity.OVERENCHANTING_ADVANCEMENT_IDENTIFIER);
		}
		return true;
	}
	
}
