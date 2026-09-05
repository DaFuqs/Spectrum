package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.crafting.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import it.unimi.dsi.fastutil.ints.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.display.*;
import me.shedaniel.rei.api.common.display.basic.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.entry.type.*;
import me.shedaniel.rei.api.common.transfer.info.*;
import me.shedaniel.rei.api.common.transfer.info.simple.*;
import me.shedaniel.rei.api.common.util.*;
import me.shedaniel.rei.plugin.common.*;
import me.shedaniel.rei.plugin.common.displays.crafting.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class GatedCraftingDisplay extends GatedSpectrumDisplay {
	
	protected final int width;
	public boolean shapeless;
	
	public GatedCraftingDisplay(RecipeHolder<GatedCraftingRecipe> recipe) {
		super(recipe, EntryIngredients.ofIngredients(recipe.value().getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.value().getResultItem(BasicDisplay.registryAccess()))));
		if(recipe.value() instanceof ShapedGatedCraftingRecipe shapedGatedCraftingRecipe) {
			this.width = shapedGatedCraftingRecipe.getWidth();
			this.shapeless = false;
		} else {
			this.width = 3;
			this.shapeless = true;
		}
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.GATED_CRAFTING;
	}
	
	public List<InputIngredient<EntryStack<?>>> getInputIngredients(int craftingWidth, int craftingHeight) {
		Map<IntIntPair, InputIngredient<EntryStack<?>>> grid = new HashMap<>();
		
		List<EntryIngredient> inputEntries = getInputEntries();
		for (int i = 0; i < inputEntries.size(); i++) {
			EntryIngredient stacks = inputEntries.get(i);
			if (stacks.isEmpty()) {
				continue;
			}
			int index = DefaultCraftingDisplay.getSlotWithSize(width, i, craftingWidth);
			int x = i % width;
			int y = i / width;
			grid.put(new IntIntImmutablePair(x, y), InputIngredient.of(index, 3 * y + x, stacks));
		}
		
		List<InputIngredient<EntryStack<?>>> list = new ArrayList<>(craftingWidth * craftingHeight);
		for (int i = 0, n = craftingWidth * craftingHeight; i < n; i++) {
			list.add(InputIngredient.empty(i));
		}
		
		for (int x = 0; x < craftingWidth; x++) {
			for (int y = 0; y < craftingHeight; y++) {
				InputIngredient<EntryStack<?>> ingredient = grid.get(new IntIntImmutablePair(x, y));
				if (ingredient != null) {
					int index = craftingWidth * y + x;
					list.set(index, ingredient);
				}
			}
		}
		
		return list;
	}
	
}
