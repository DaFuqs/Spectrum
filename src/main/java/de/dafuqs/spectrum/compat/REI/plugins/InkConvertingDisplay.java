package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.recipe.ink_converting.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InkConvertingDisplay extends GatedSpectrumDisplay {
	
	protected final InkColor color;
	protected final long amount;
	
	public InkConvertingDisplay(@NotNull InkConvertingRecipe recipe) {
		super(recipe, EntryIngredients.ofIngredients(recipe.getIngredients()), List.of());
		this.color = recipe.getInkColor();
		this.amount = recipe.getInkAmount();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.INK_CONVERTING;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, InkConvertingRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
	
}