package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.api.recipe.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class GatedSpectrumEmiRecipe<T extends GatedRecipe<?>> extends SpectrumEmiRecipe {
	
	public static final Text SECRET = Text.translatable("container.spectrum.rei.pedestal_crafting.secret_recipe");
	public static final Text SECRET_HINT = Text.translatable("container.spectrum.rei.pedestal_crafting.secret_recipe.hint");
	
	public final @Nullable Text secretHintText;
	
	public final T recipe;

	public GatedSpectrumEmiRecipe(EmiRecipeCategory category, T recipe, int width, int height) {
		super(category, recipe.getRecipeTypeUnlockIdentifier(), recipe.getId(), width, height);
		this.recipe = recipe;
		this.outputs = List.of(EmiStack.of(recipe.getOutput(getRegistryManager())));
		this.secretHintText = recipe.getSecretHintText();
	}
	
	@Override
	public boolean isUnlocked() {
		return hasAdvancement(recipe.getRequiredAdvancementIdentifier()) && super.isUnlocked();
	}
	
	@Override
	public boolean hideCraftable() {
		return recipe.isSecret() || super.hideCraftable();
	}
	
	@Override
	public void addWidgets(WidgetHolder widgets) {
		if (recipe.isSecret() && isUnlocked()) {
			if (secretHintText == null) {
				widgets.addText(SECRET, getDisplayWidth() / 2, getDisplayHeight() / 2, 0x3f3f3f, false).horizontalAlign(TextWidget.Alignment.CENTER);
			} else {
				widgets.addText(SECRET_HINT, getDisplayWidth() / 2, getDisplayHeight() / 2 - 8, 0x3f3f3f, false).horizontalAlign(TextWidget.Alignment.CENTER);
				widgets.addText(secretHintText, getDisplayWidth() / 2, getDisplayHeight() / 2 + 2, 0x3f3f3f, false).horizontalAlign(TextWidget.Alignment.CENTER);
			}
		} else {
			super.addWidgets(widgets);
		}
	}
	
}