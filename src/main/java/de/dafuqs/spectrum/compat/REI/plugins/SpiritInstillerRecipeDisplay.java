package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.REIHelper;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.helpers.LoreHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.recipe.spirit_instiller.ISpiritInstillerRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.spawner.SpawnerChangeRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static de.dafuqs.spectrum.recipe.spirit_instiller.ISpiritInstillerRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER;

public class SpiritInstillerRecipeDisplay implements SimpleGridMenuDisplay, GatedRecipeDisplay {
	
	protected final List<EntryIngredient> craftingInputs;
	protected final EntryIngredient output;
	protected final float experience;
	protected final int craftingTime;
	protected final Identifier requiredAdvancementIdentifier;

	public SpiritInstillerRecipeDisplay(@NotNull ISpiritInstillerRecipe recipe) {
		this.craftingInputs = recipe.getIngredientStacks().stream().map(REIHelper::ofIngredientStack).collect(Collectors.toCollection(ArrayList::new));
		
		if(recipe instanceof SpawnerChangeRecipe spawnerChangeRecipe) {
			ItemStack outputStack = recipe.getOutput();
			LoreHelper.setLore(outputStack, spawnerChangeRecipe.getOutputLoreText());
			this.output = EntryIngredients.of(outputStack);
		} else {
			this.output = EntryIngredients.of(recipe.getOutput());
		}
		this.experience = recipe.getExperience();
		this.craftingTime = recipe.getCraftingTime();
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		if(this.isUnlocked()) {
			return craftingInputs;
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		if(this.isUnlocked() || SpectrumCommon.CONFIG.REIListsRecipesAsNotUnlocked) {
			return Collections.singletonList(output);
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.SPIRIT_INSTILLER;
	}

	public boolean isUnlocked() {
		PlayerEntity player = MinecraftClient.getInstance().player;
		return Support.hasAdvancement(player, UNLOCK_ADVANCEMENT_IDENTIFIER) && Support.hasAdvancement(player, this.requiredAdvancementIdentifier);
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