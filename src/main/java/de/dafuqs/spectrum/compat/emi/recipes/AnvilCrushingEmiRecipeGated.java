package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.TextWidget.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

public class AnvilCrushingEmiRecipeGated extends GatedSpectrumEmiRecipe<AnvilCrushingRecipe> {
	private final static Identifier WALL_TEXTURE = SpectrumCommon.locate("textures/gui/container/anvil_crushing.png");
	
	public AnvilCrushingEmiRecipeGated(AnvilCrushingRecipe recipe) {
		super(SpectrumEmiRecipeCategories.ANVIL_CRUSHING, null, recipe, 116, 64);
		this.inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addSlot(inputs.get(0), 21, 30);
		widgets.addTexture(WALL_TEXTURE, 0, 0, 16, 48, 0, 0); // dirt wall
		widgets.addSlot(EmiStack.of(Items.ANVIL), 21, 10).drawBack(false);
		widgets.addTexture(WALL_TEXTURE, 22, 1, 16, 16, 16, 1); // falling stripes for anvil
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 50, 16);
		widgets.addSlot(outputs.get(0), 90, 11).large(true).recipeContext(this);
		
		widgets.addText(Text.translatable("container.spectrum.rei.anvil_crushing.plus_xp", recipe.getExperience()),
				width, 40, 0x3f3f3f, false).horizontalAlign(Alignment.END);
		
		widgets.addText(getForceText(), width / 2, 54, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
	}

	public Text getForceText() {
		if (recipe.getCrushedItemsPerPointOfDamage() >= 1) {
			return Text.translatable("container.spectrum.rei.anvil_crushing.low_force_required");
		} else if (recipe.getCrushedItemsPerPointOfDamage() >= 0.5) {
			return Text.translatable("container.spectrum.rei.anvil_crushing.medium_force_required");
		} else {
			return Text.translatable("container.spectrum.rei.anvil_crushing.high_force_required");
		}
	}
}
