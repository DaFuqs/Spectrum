package de.dafuqs.spectrum.compat.emi;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget.Alignment;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class SpectrumBaseEmiRecipe implements EmiRecipe {
	private static final Text HIDDEN_LINE_1 = Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1");
	private static final Text HIDDEN_LINE_2 = Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2");
	public final EmiRecipeCategory category;
	public final Identifier unlock, id;
	public final int width, height;
	public List<EmiIngredient> input = List.of();
	public List<EmiStack> output = List.of();

	public SpectrumBaseEmiRecipe(EmiRecipeCategory category, Identifier unlock, Identifier id, int width, int height) {
		this.category = category;
		this.unlock = unlock;
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public boolean isUnlocked() {
		if (unlock != null && !hasAdvancement(unlock)) {
			return false;
		}
		return true;
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
		return id;
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
}
