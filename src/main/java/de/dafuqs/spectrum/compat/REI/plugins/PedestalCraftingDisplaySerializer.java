package de.dafuqs.spectrum.compat.REI.plugins;

import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PedestalCraftingDisplaySerializer {
	
	public static BasicDisplay.Serializer<PedestalCraftingDisplay> serializer() {
		return BasicDisplay.Serializer.of((input, output, location, tag) -> {
			float experience = tag.getFloat("Experience");
			int craftingTime = tag.getInt("CraftingTime");
			int width = tag.getInt("Width");
			int height = tag.getInt("Height");
			String recipeTier = tag.getString("RecipeTier");
			Identifier requiredAdvancementIdentifier = Identifier.tryParse(tag.getString("Advancement"));
			return simple(input, output, width, height, experience, craftingTime, requiredAdvancementIdentifier, recipeTier);
		}, (display, tag) -> {
			tag.putFloat("Experience", display.experience);
			tag.putInt("CraftingTime", display.craftingTime);
			tag.putString("RecipeTier", display.pedestalRecipeTier.toString());
			tag.putString("Advancement", display.requiredAdvancementIdentifier.toString());
			tag.putInt("Width", display.getWidth());
			tag.putInt("Height", display.getHeight());
		});
	}
	
	static @NotNull PedestalCraftingDisplay simple(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int width, int height, float experience, int craftingTime, Identifier requiredAdvancementIdentifier, String recipeTier) {
		return new PedestalCraftingDisplay(inputs, outputs, width, height, experience, craftingTime, requiredAdvancementIdentifier, recipeTier);
	}

	public static int getSlotWithSize(int recipeWidth, int index) {
		int x = index % recipeWidth;
		int y = (index - x) / recipeWidth;
		return 3 * y + x;
	}
	
}
