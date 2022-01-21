package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.items.magic_items.KnowledgeGemItem;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.registries.SpectrumItems;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EnchanterRecipeDisplay<R extends EnchanterRecipe> implements SimpleGridMenuDisplay, GatedRecipeDisplay {
	
	protected final List<EntryIngredient> inputs; // first input is the center, all others around clockwise
	protected final EntryIngredient output;
	protected final int requiredExperience;
	protected final int craftingTime;
	protected final Identifier requiredAdvancementIdentifier;
	
	public EnchanterRecipeDisplay(@NotNull EnchanterRecipe recipe) {
		this.inputs = recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new));
		this.inputs.add(EntryIngredients.of(KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience())));
		this.output = EntryIngredients.of(recipe.getOutput());
		this.requiredExperience = recipe.getRequiredExperience();
		this.craftingTime = recipe.getCraftingTime();
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
		return SpectrumPlugins.ENCHANTER;
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