package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.api.recipe.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public abstract class GatedSpectrumEmiRecipe<T extends GatedRecipe<?>> extends SpectrumEmiRecipe {
	
	public static final Component SECRET = Component.translatable("container.spectrum.rei.pedestal_crafting.secret_recipe");
	public static final Component SECRET_HINT = Component.translatable("container.spectrum.rei.pedestal_crafting.secret_recipe.hint");
	
	public final @Nullable ResourceLocation revealSecretAdvancement;
	public final @Nullable Component secretHintText;
	public final T recipe;
	
	public GatedSpectrumEmiRecipe(EmiRecipeCategory category, RecipeHolder<T> entry, int width, int height) {
		super(category, entry.value().getRecipeTypeUnlockIdentifier(), entry.id(), width, height);
		this.recipe = entry.value();
		
		this.outputs = buildOutputs(entry.value());
		
		this.revealSecretAdvancement = entry.value().getRevealSecretAdvancement().orElse(null);
		this.secretHintText = entry.value().getSecretHintText(getId());
	}
	
	public List<EmiStack> buildOutputs(T recipe) {
		List<EmiStack> out = new ArrayList<>();
		out.add(EmiStack.of(recipe.getResultItem(getRegistryManager())));
		out.addAll(recipe.getAdditionalResults().stream().map(EmiStack::of).toList());
		return out;
	}
	
	@Override
	public boolean isUnlocked() {
		return hasAdvancement(recipe.getRequiredAdvancement().orElse(null)) && super.isUnlocked();
	}
	
	public boolean isSecret() {
		return revealSecretAdvancement != null && !hasAdvancement(revealSecretAdvancement);
	}
	
	@Override
	public void addWidgets(WidgetHolder widgets) {
		if (isUnlocked() && isSecret()) {
			if (secretHintText == null) {
				widgets.addText(SECRET, getDisplayWidth() / 2, getDisplayHeight() / 2 - 4, 0x3f3f3f, false).horizontalAlign(TextWidget.Alignment.CENTER);
			} else {
				widgets.addText(SECRET_HINT, getDisplayWidth() / 2, getDisplayHeight() / 2 - 8, 0x3f3f3f, false).horizontalAlign(TextWidget.Alignment.CENTER);
				widgets.addText(secretHintText, getDisplayWidth() / 2, getDisplayHeight() / 2 + 2, 0x3f3f3f, false).horizontalAlign(TextWidget.Alignment.CENTER);
			}
		} else {
			super.addWidgets(widgets);
		}
	}
	
}
