package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnchantmentUpgradeRecipeDisplay<R extends EnchantmentUpgradeRecipe> implements SimpleGridMenuDisplay, GatedRecipeDisplay {
	
	protected final int requiredExperience;
	protected final int requiredItemCount;
	@Nullable
	protected final Identifier requiredAdvancementIdentifier;
	
	protected final List<EntryIngredient> inputs;
	protected final EntryIngredient output;
	
	public EnchantmentUpgradeRecipeDisplay(@NotNull EnchantmentUpgradeRecipe recipe) {
		this.inputs = new ArrayList<>();
		this.inputs.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(0))); // the center stack
		
		int requiredItemCountSplit = recipe.getRequiredItemCount() / 8;
		int requiredItemCountModulo = recipe.getRequiredItemCount() % 8;
		for(int i = 0; i < 8; i++) {
			int addAmount = i < requiredItemCountModulo ? 1 : 0;
			this.inputs.add(EntryIngredients.of(new ItemStack(recipe.getRequiredItem(), requiredItemCountSplit + addAmount)));
		}
		
		this.inputs.add(EnchanterRecipeDisplay.getKnowledgeDropIngredient(recipe.getRequiredExperience()));
		this.output = EntryIngredients.of(recipe.getOutput());
		this.requiredItemCount = recipe.getRequiredItemCount();
		
		this.requiredExperience = recipe.getRequiredExperience();
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		if(this.isUnlocked()) {
			return inputs;
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		if(this.isUnlocked()) {
			return Collections.singletonList(output);
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.ENCHANTMENT_UPGRADE;
	}

	public boolean isUnlocked() {
		return Support.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
	}

	@Override
	public int getWidth() {
		return 3;
	}

	@Override
	public int getHeight() {
		return 3;
	}

}