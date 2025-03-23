package de.dafuqs.spectrum.compat.modonomicon;

import com.klikli_dev.modonomicon.api.multiblock.*;
import com.klikli_dev.modonomicon.client.gui.book.*;
import com.klikli_dev.modonomicon.client.render.*;
import de.dafuqs.matchbooks.recipe.*;
import net.minecraft.client.gui.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ModonomiconHelper {
	
	public static void renderIngredientStack(DrawContext drawContext, BookContentScreen parentScreen, int x, int y, int mouseX, int mouseY, IngredientStack ingredientStack) {
		List<ItemStack> stacks = ingredientStack.getStacks();
		if (!stacks.isEmpty()) {
			parentScreen.renderItemStack(drawContext, x, y, mouseX, mouseY, stacks.get(parentScreen.ticksInBook / 20 % stacks.size()));
		}
	}
	
	public static void renderMultiblock(Multiblock multiblock, Text text, BlockPos pos, BlockRotation rotation) {
		MultiblockPreviewRenderer.setMultiblock(multiblock, text, false);
		MultiblockPreviewRenderer.anchorTo(pos, rotation);
	}
	
	/**
	 * Clears multiblock if the currently rendered one matches the one in the argument
	 * If null is passed, any multiblock will get cleared
	 */
	public static void clearRenderedMultiblock(@Nullable Multiblock multiblock) {
		Multiblock currentlyRenderedMultiblock = MultiblockPreviewRenderer.getMultiblock();
		if (currentlyRenderedMultiblock == null || currentlyRenderedMultiblock != multiblock) {
			return;
		}
		MultiblockPreviewRenderer.setMultiblock(null, Text.empty(), false);
	}
	
}
