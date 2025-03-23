package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.*;
import de.dafuqs.spectrum.compat.REI.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.client.registry.display.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public abstract class GatedDisplayCategory<T extends GatedRecipeDisplay> implements DisplayCategory<T> {
	
	public static final Text HIDDEN_LINE_1 = Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1");
	public static final Text HIDDEN_LINE_2 = Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2");
	
	public static final Text SECRET = Text.translatable("container.spectrum.rei.pedestal_crafting.secret_recipe");
	public static final Text SECRET_HINT = Text.translatable("container.spectrum.rei.pedestal_crafting.secret_recipe.hint");
	
	@Override
	public List<Widget> setupDisplay(@NotNull T display, @NotNull Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - getDisplayHeight() / 2 + 5);
		List<Widget> widgets = Lists.newArrayList();
		
		widgets.add(Widgets.createRecipeBase(bounds));
		
		if (display.isUnlocked()) {
			if (display.isSecret()) {
				if (display.getSecretHintText() == null) {
					widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), bounds.getCenterY() - 9), SECRET).centered().color(0x3f3f3f).noShadow());
				} else {
					widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), bounds.getCenterY() - 9), SECRET_HINT).centered().color(0x3f3f3f).noShadow());
					widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), bounds.getCenterY() + 1), display.getSecretHintText()).centered().color(0x3f3f3f).noShadow());
				}
			} else {
				setupWidgets(startPoint, bounds, widgets, display);
			}
		} else {
			widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), bounds.getCenterY() - 9), HIDDEN_LINE_1).centered().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), bounds.getCenterY() + 1), HIDDEN_LINE_2).centered().color(0x3f3f3f).noShadow());
		}
		
		return widgets;
	}
	
	public abstract void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull T display);
	
	// Special handling for "1 second"
	// Looks nicer
	protected static Text getCraftingTimeText(int time) {
		if (time == 20) {
			return Text.translatable("container.spectrum.rei.crafting_time_one_second", 1);
		} else {
			return Text.translatable("container.spectrum.rei.crafting_time", (time / 20));
		}
	}
	
	// Special handling for "1 second"
	// Looks nicer
	protected static Text getCraftingTimeText(int time, float xp) {
		if (time == 20) {
			return Text.translatable("container.spectrum.rei.crafting_time_one_second_and_xp", 1, xp);
		} else {
			return Text.translatable("container.spectrum.rei.crafting_time_and_xp", (time / 20), xp);
		}
	}
	
}
