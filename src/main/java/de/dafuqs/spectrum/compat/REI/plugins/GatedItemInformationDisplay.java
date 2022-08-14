package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.recipe.DescriptiveGatedRecipe;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GatedItemInformationDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	protected final List<EntryIngredient> input;
	protected final Identifier requiredAdvancementIdentifier;
	protected final Item item;
	protected final Text description;
	
	public GatedItemInformationDisplay(DescriptiveGatedRecipe recipe) {
		super(recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new)), Collections.emptyList());
		this.input = recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new));
		this.requiredAdvancementIdentifier = recipe.getRequiredAdvancementIdentifier();
		this.item = recipe.getItem();
		this.description = recipe.getDescription();
	}
	
	@Override
	public List<EntryIngredient> getInputEntries() {
		if (this.isUnlocked()) {
			return input;
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<EntryIngredient> getOutputEntries() {
		return new ArrayList<>();
	}
	
	public boolean isUnlocked() {
		if (this.input == null) {
			return true;
		} else {
			return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER || isUnlockedClient();
		}
	}
	
	@Environment(EnvType.CLIENT)
	public boolean isUnlockedClient() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier);
	}
	
	public Item getItem() {
		return this.item;
	}
	
	public Text getDescription() {
		return this.description;
	}
	
}