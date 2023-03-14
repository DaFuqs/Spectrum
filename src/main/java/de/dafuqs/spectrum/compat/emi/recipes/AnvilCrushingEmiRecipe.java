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

public class AnvilCrushingEmiRecipe extends SpectrumEmiRecipe<AnvilCrushingRecipe> {
	private final static Identifier WALL_TEXTURE = SpectrumCommon.locate("textures/gui/container/anvil_crushing.png");

	public AnvilCrushingEmiRecipe(AnvilCrushingRecipe recipe) {
		super(SpectrumEmiRecipeCategories.ANVIL_CRUSHING, recipe, 116, 64);
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addSlot(EmiStack.of(Items.ANVIL), 21, 10).drawBack(false);
		widgets.addSlot(input.get(0), 21, 30);
		widgets.addSlot(output.get(0), 90, 11).output(true).recipeContext(this);
		
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 50, 16); // dirt wall
		widgets.addTexture(WALL_TEXTURE, 0, 0, 16, 48, 0, 0); // falling stripes for anvil
		widgets.addTexture(WALL_TEXTURE, 20, 0, 16, 16, 16, 1);
		
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
