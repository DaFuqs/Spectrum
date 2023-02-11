package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.REI.GatedSpectrumDisplay;
import de.dafuqs.spectrum.compat.REI.REIHelper;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.helpers.LoreHelper;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.spawner.SpawnerChangeRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class SpiritInstillingDisplay extends GatedSpectrumDisplay {
	
	protected final float experience;
	protected final int craftingTime;
	
	public SpiritInstillingDisplay(@NotNull SpiritInstillerRecipe recipe) {
		super(recipe, REIHelper.toEntryIngredients(recipe.getIngredientStacks()), Collections.singletonList(buildOutput(recipe)));
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
	}
	
	public static EntryIngredient buildOutput(SpiritInstillerRecipe recipe) {
		if (recipe instanceof SpawnerChangeRecipe spawnerChangeRecipe) {
			ItemStack outputStack = recipe.getOutput();
			LoreHelper.setLore(outputStack, spawnerChangeRecipe.getOutputLoreText());
			return EntryIngredients.of(outputStack);
		} else {
			return EntryIngredients.of(recipe.getOutput());
		}
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.SPIRIT_INSTILLER;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, SpiritInstillerRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}