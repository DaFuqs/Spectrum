package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InkConvertingDisplay extends GatedSpectrumDisplay {
	
	protected final InkColor color;
	protected final long amount;
	
	public InkConvertingDisplay(@NotNull RecipeHolder<InkConvertingRecipe> recipe) {
		super(recipe, EntryIngredients.ofIngredients(recipe.value().getIngredients()), List.of());
		this.color = recipe.value().getInkColor();
		this.amount = recipe.value().getInkAmount();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.INK_CONVERTING;
	}
	
	@Override
    public boolean isUnlocked() {
		Minecraft client = Minecraft.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, InkConvertingRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}
