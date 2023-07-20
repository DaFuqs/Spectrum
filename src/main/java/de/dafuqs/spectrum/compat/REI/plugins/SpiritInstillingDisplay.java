package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.spawner.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import org.jetbrains.annotations.*;

import java.util.*;

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
			ItemStack outputStack = recipe.getOutput(DynamicRegistryManager.EMPTY);
			LoreHelper.setLore(outputStack, spawnerChangeRecipe.getOutputLoreText());
			return EntryIngredients.of(outputStack);
		} else {
			return EntryIngredients.of(recipe.getOutput(DynamicRegistryManager.EMPTY));
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