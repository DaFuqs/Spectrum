package de.dafuqs.spectrum.compat.patchouli;

import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

import java.util.List;

public class PatchouliHelper {
	
	public static void renderIngredientStack(GuiBookEntry bookEntry, MatrixStack ms, int x, int y, int mouseX, int mouseY, IngredientStack ingr) {
		List<ItemStack> stacks = ingr.getStacks();
		if (!stacks.isEmpty()) {
			bookEntry.renderItemStack(ms, x, y, mouseX, mouseY, stacks.get(bookEntry.ticksInBook / 20 % stacks.size()));
		}
	}
	
}
