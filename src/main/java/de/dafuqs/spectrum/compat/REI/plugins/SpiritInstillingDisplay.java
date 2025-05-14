package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.dynamic.spawner_manipulation.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.display.basic.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpiritInstillingDisplay extends GatedSpectrumDisplay {
	
	protected final float experience;
	protected final int craftingTime;
	
	public SpiritInstillingDisplay(@NotNull RecipeHolder<SpiritInstillerRecipe> recipe) {
		super(recipe, REIHelper.toEntryIngredients(recipe.value().getIngredientStacks()), Collections.singletonList(buildOutput(recipe.value())));
		this.experience = recipe.value().getExperience();
		this.craftingTime = recipe.value().getCraftingTime();
	}
	
	public static EntryIngredient buildOutput(SpiritInstillerRecipe recipe) {
		if (recipe instanceof SpawnerChangeRecipe spawnerChangeRecipe) {
			ItemStack outputStack = recipe.getResultItem(BasicDisplay.registryAccess());
			LoreHelper.setLore(outputStack, spawnerChangeRecipe.getOutputLoreText());
			return EntryIngredients.of(outputStack);
		} else {
			return EntryIngredients.of(recipe.getResultItem(BasicDisplay.registryAccess()));
		}
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.SPIRIT_INSTILLER;
	}
	
	@Override
    public boolean isUnlocked() {
		Minecraft client = Minecraft.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, SpiritInstillerRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}
