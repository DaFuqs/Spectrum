package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.api.recipe.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.*;

import java.util.*;

public abstract class GatedSpectrumEmiRecipe<T extends GatedRecipe<?>> extends SpectrumEmiRecipe {
	
	public static final Component SECRET = Component.translatable("container.spectrum.rei.pedestal_crafting.secret_recipe");
	public static final Component SECRET_HINT = Component.translatable("container.spectrum.rei.pedestal_crafting.secret_recipe.hint");
	
	public final @Nullable Component secretHintText;
	
	public final T recipe;
	
	public GatedSpectrumEmiRecipe(EmiRecipeCategory category, RecipeHolder<T> entry, int width, int height) {
		super(category, entry.value().getRecipeTypeUnlockIdentifier(), entry.id(), width, height);
		this.recipe = entry.value();
		this.outputs = List.of(EmiStack.of(entry.value().getResultItem(getRegistryManager())));
		this.secretHintText = entry.value().getSecretHintText(getId());
	}
	
	@Override
	public boolean isUnlocked() {
		return hasAdvancement(recipe.getRequiredAdvancementIdentifier().orElse(null)) && super.isUnlocked();
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
