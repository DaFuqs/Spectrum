package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.matchbooks.recipe.*;
import net.minecraft.client.gui.*;
import net.minecraft.item.*;
import vazkii.patchouli.client.book.gui.*;

import java.util.*;

public class PatchouliHelper {
	
	public static void renderIngredientStack(DrawContext dc, GuiBookEntry bookEntry, int x, int y, int mouseX, int mouseY, IngredientStack ingr) {
		List<ItemStack> stacks = ingr.getStacks();
		if (!stacks.isEmpty()) {
			bookEntry.renderItemStack(dc, x, y, mouseX, mouseY, stacks.get(bookEntry.ticksInBook / 20 % stacks.size()));
		}
	}
	
}
