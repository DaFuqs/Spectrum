package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public class PotionWorkshopEmiRecipeGated extends GatedSpectrumEmiRecipe<PotionWorkshopRecipe> {
	private final static Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/potion_workshop_3_slots.png");
	
	public PotionWorkshopEmiRecipeGated(EmiRecipeCategory category, PotionWorkshopRecipe recipe) {
		super(category, PotionWorkshopRecipe.UNLOCK_IDENTIFIER, recipe, 112, 66);
		
		this.inputs = recipe.getIngredientStacks().stream().map(s -> EmiIngredient.of(s.getStacks().stream().map(EmiStack::of).toList())).toList();
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addSlot(inputs.get(0), 18, 48);
		widgets.addSlot(inputs.get(1), 65, 4);
		widgets.addSlot(inputs.get(2), 18, 0);
		widgets.addSlot(inputs.get(3), 0, 24);
		widgets.addSlot(inputs.get(4), 36, 24);
		
		widgets.addSlot(outputs.get(0), 94, 24).recipeContext(this);
		
		// bubbles
		widgets.addTexture(BACKGROUND_TEXTURE, 21, 20, 11, 27, 176, 0);
		widgets.addFillingArrow(62, 25, recipe.getCraftingTime() * 50);
		
		// description text
		Text text = Text.translatable("container.spectrum.rei.potion_workshop.crafting_time", (recipe.getCraftingTime() / 20));
		widgets.addText(text, 40, 53, 0x3f3f3f, false);
	}
}
