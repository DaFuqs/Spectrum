package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.TextWidget.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class SpectrumEmiRecipe implements EmiRecipe {
	public static final Text HIDDEN_LINE_1 = Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1");
	public static final Text HIDDEN_LINE_2 = Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2");
	public final EmiRecipeCategory category;
	public final Identifier recipeTypeUnlockIdentifier, recipeIdentifier;
	public final int width, height;
	public List<EmiIngredient> input = List.of();
	public List<EmiStack> output = List.of();
	
	public SpectrumEmiRecipe(EmiRecipeCategory category, Identifier recipeTypeUnlockIdentifier, Identifier recipeIdentifier, int width, int height) {
		this.category = category;
		this.recipeTypeUnlockIdentifier = recipeTypeUnlockIdentifier;
		this.recipeIdentifier = recipeIdentifier;
		this.width = width;
		this.height = height;
	}
	
	public boolean isUnlocked() {
		return recipeTypeUnlockIdentifier == null || hasAdvancement(recipeTypeUnlockIdentifier);
	}
	
	public boolean hasAdvancement(Identifier advancement) {
		MinecraftClient client = MinecraftClient.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, advancement);
	}
	
	protected static Text getCraftingTimeText(int time) {
		if (time == 20) {
			return Text.translatable("container.spectrum.rei.enchanting.crafting_time_one_second", 1);
		} else {
			return Text.translatable("container.spectrum.rei.enchanting.crafting_time", (time / 20));
		}
	}
	
	protected static Text getCraftingTimeText(int time, float experience) {
		// special handling for "1 second". Looks nicer
		if (time == 20) {
			return Text.translatable("container.spectrum.rei.pedestal_crafting.crafting_time_one_second_and_xp", 1, experience);
		} else {
			return Text.translatable("container.spectrum.rei.pedestal_crafting.crafting_time_and_xp", (time / 20), experience);
		}
	}
	
	public abstract void addUnlockedWidgets(WidgetHolder widgets);
	
	@Override
	public EmiRecipeCategory getCategory() {
		return category;
	}
	
	@Override
	public @Nullable Identifier getId() {
		return recipeIdentifier;
	}
	
	@Override
	public List<EmiIngredient> getInputs() {
		return input;
	}
	
	@Override
	public List<EmiStack> getOutputs() {
		return output;
	}
	
	@Override
	public int getDisplayWidth() {
		if (isUnlocked()) {
			return width;
		} else {
			MinecraftClient client = MinecraftClient.getInstance();
			return Math.max(client.textRenderer.getWidth(HIDDEN_LINE_1), client.textRenderer.getWidth(HIDDEN_LINE_2)) + 8;
		}
	}
	
	@Override
	public int getDisplayHeight() {
		if (isUnlocked()) {
			return height;
		} else {
			return 32;
		}
	}
	
	@Override
	public void addWidgets(WidgetHolder widgets) {
		if (!isUnlocked()) {
			widgets.addText(HIDDEN_LINE_1, getDisplayWidth() / 2, getDisplayHeight() / 2 - 8, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
			widgets.addText(HIDDEN_LINE_2, getDisplayWidth() / 2, getDisplayHeight() / 2 + 2, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
		} else {
			addUnlockedWidgets(widgets);
		}
	}
	
	@Override
	public boolean supportsRecipeTree() {
		return EmiRecipe.super.supportsRecipeTree() && isUnlocked();
	}
	
	@Override
	public boolean hideCraftable() {
		return SpectrumCommon.CONFIG.REIListsRecipesAsNotUnlocked && !isUnlocked();
	}
}
