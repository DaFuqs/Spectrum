package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import de.dafuqs.spectrum.registries.*;
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
		MinecraftClient client = MinecraftClient.getInstance();
		if (!super.isUnlocked()) {
			return false;
		}
		if (requiresOverEnchanting) {
			return AdvancementHelper.hasAdvancement(client.player, SpectrumAdvancements.OVERENCHANTING);
		}
		return true;
	}
	
}
