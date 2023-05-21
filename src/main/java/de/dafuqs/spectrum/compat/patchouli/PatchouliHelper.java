package de.dafuqs.spectrum.compat.patchouli;

import net.id.incubus_core.recipe.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import vazkii.patchouli.client.book.gui.*;

import java.util.*;

public class PatchouliHelper {
	
	public static void renderIngredientStack(GuiBookEntry bookEntry, MatrixStack ms, int x, int y, int mouseX, int mouseY, IngredientStack ingr) {
		List<ItemStack> stacks = ingr.getStacks();
		if (!stacks.isEmpty()) {
			bookEntry.renderItemStack(ms, x, y, mouseX, mouseY, stacks.get(bookEntry.ticksInBook / 20 % stacks.size()));
		}
	}
	
}
