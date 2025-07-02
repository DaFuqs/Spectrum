package de.dafuqs.spectrum.compat.modonomicon;

import com.klikli_dev.modonomicon.api.multiblock.*;
import com.klikli_dev.modonomicon.client.gui.book.entry.*;
import com.klikli_dev.modonomicon.client.render.*;
import de.dafuqs.spectrum.api.recipe.*;
import net.minecraft.client.gui.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ModonomiconHelper {
	
	public static void renderIngredientStack(GuiGraphics drawContext, BookEntryScreen parentScreen, int x, int y, int mouseX, int mouseY, IngredientStack ingredientStack) {
		List<ItemStack> stacks = ingredientStack.getMatchingStacks();
		if (!stacks.isEmpty()) {
			parentScreen.renderItemStack(drawContext, x, y, mouseX, mouseY, stacks.get(parentScreen.getTicksInBook() / 20 % stacks.size()));
		}
	}
	
	public static void renderMultiblock(Multiblock multiblock, Component text, BlockPos pos, Rotation rotation) {
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
		MultiblockPreviewRenderer.setMultiblock(null, Component.empty(), false);
	}
	
}
