package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.emi.SpectrumEmiRecipe;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import org.apache.commons.compress.utils.*;

import java.util.*;
import java.util.function.*;

public class SpectrumWorldInteractionRecipe extends EmiWorldInteractionRecipe {
	private final List<Identifier> requiredAdvancementIdentifier = Lists.newArrayList();
	
	protected SpectrumWorldInteractionRecipe(Builder builder) {
		super(builder.superbuilder);
		requiredAdvancementIdentifier.addAll(builder.requiredAdvancementIdentifier);
	}
	
	public static Builder customBuilder() {
		return new Builder();
	}
	
	public boolean hasAdvancement(Identifier advancement) {
		MinecraftClient client = MinecraftClient.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, advancement);
	}
	
	public boolean isUnlocked() {
		if (requiredAdvancementIdentifier.isEmpty()) return true;
		for (Identifier id : requiredAdvancementIdentifier) {
			if (!hasAdvancement(id)) return false;
		}
		return true;
	}
	
	@Override
	public int getDisplayWidth() {
		if (isUnlocked()) {
			return super.getDisplayWidth();
		} else {
			return 125;
		}
	}
	
	@Override
	public int getDisplayHeight() {
		if (isUnlocked()) {
			return super.getDisplayHeight();
		} else {
			return 18;
		}
	}
	
	@Override
	public void addWidgets(WidgetHolder widgets) {
		if (!isUnlocked()) {
			widgets.addText(SpectrumEmiRecipe.HIDDEN_LINE_1, getDisplayWidth() / 2, getDisplayHeight() / 2 - 9, 0x3f3f3f, false).horizontalAlign(TextWidget.Alignment.CENTER);
			widgets.addText(SpectrumEmiRecipe.HIDDEN_LINE_2, getDisplayWidth() / 2, getDisplayHeight() / 2 + 1, 0x3f3f3f, false).horizontalAlign(TextWidget.Alignment.CENTER);
		} else {
			super.addWidgets(widgets);
		}
	}
	
	public static class Builder {
		//Use a combinatorial relationship to build the parent class builder because its constructor is private.
		private final EmiWorldInteractionRecipe.Builder superbuilder;
		private final List<Identifier> requiredAdvancementIdentifier = Lists.newArrayList();
		
		private Builder() {
			this.superbuilder = EmiWorldInteractionRecipe.builder();
		}
		
		public SpectrumWorldInteractionRecipe build() {
			return new SpectrumWorldInteractionRecipe(this);
		}
		
		public Builder id(Identifier id) {
			superbuilder.id(id);
			return this;
		}
		
		public Builder leftInput(EmiIngredient stack) {
			superbuilder.leftInput(stack);
			return this;
		}
		
		public Builder leftInput(EmiIngredient stack, Function<SlotWidget, SlotWidget> mutator) {
			superbuilder.leftInput(stack, mutator);
			return this;
		}
		
		public Builder rightInput(EmiIngredient stack, boolean catalyst) {
			superbuilder.rightInput(stack, catalyst);
			return this;
		}
		
		public Builder rightInput(EmiIngredient stack, boolean catalyst, Function<SlotWidget, SlotWidget> mutator) {
			superbuilder.rightInput(stack, catalyst, mutator);
			return this;
		}
		
		public Builder output(EmiStack stack) {
			superbuilder.output(stack);
			return this;
		}
		
		public Builder output(EmiStack stack, Function<SlotWidget, SlotWidget> mutator) {
			superbuilder.output(stack, mutator);
			return this;
		}
		
		public Builder supportsRecipeTree(boolean supportsRecipeTree) {
			superbuilder.supportsRecipeTree(supportsRecipeTree);
			return this;
		}
		
		public Builder requiredAdvancement(Identifier advId) {
			requiredAdvancementIdentifier.add(advId);
			return this;
		}
	}
}
