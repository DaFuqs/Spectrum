package de.dafuqs.spectrum.compat.modonomicon;

import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import de.dafuqs.matchbooks.recipe.IngredientStack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ModonomiconHelper {

	public static void renderIngredientStack(DrawContext drawContext, BookContentScreen parentScreen, int x, int y, int mouseX, int mouseY, IngredientStack ingredientStack) {
		List<ItemStack> stacks = ingredientStack.getStacks();
		if (!stacks.isEmpty()) {
			parentScreen.renderItemStack(drawContext, x, y, mouseX, mouseY, stacks.get(parentScreen.ticksInBook / 20 % stacks.size()));
		}
	}
	
}
