package de.dafuqs.spectrum.compat.patchouli;

import net.id.incubus_core.recipe.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import vazkii.patchouli.client.book.gui.*;

import java.util.*;

public class PatchouliHelper {
	
	public static void renderIngredientStack(DrawContext dc, GuiBookEntry bookEntry, int x, int y, int mouseX, int mouseY, IngredientStack ingr) {
		List<ItemStack> stacks = ingr.getStacks();
		if (!stacks.isEmpty()) {
			bookEntry.renderItemStack(dc, x, y, mouseX, mouseY, stacks.get(bookEntry.ticksInBook / 20 % stacks.size()));
		}
	}

	public static void drawBookBackground(Identifier texture, DrawContext drawContext, int recipeX, int recipeY) {
		drawContext.drawTexture(texture, recipeX - 2, recipeY - 2, 0, 0, 100, 32, 128, 256);
	}
	
}
