package de.dafuqs.spectrum.compat.patchouli.pages;

import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.world.World;
import vazkii.patchouli.api.*;
import vazkii.patchouli.client.book.*;
import vazkii.patchouli.client.book.gui.*;
import vazkii.patchouli.client.book.page.abstr.*;

import java.util.*;

public class PageCollection extends PageWithText {
	
	private static final int ITEMS_PER_ROW = 6;
	
	String title;
	IVariable items;
	
	transient List<Ingredient> stacks;
	
	@Override
	public void build(World world, BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(world, entry, builder, pageNum);
		
		stacks = new ArrayList<>();
		for (IVariable item : items.asList()) {
			Ingredient ingredient = item.as(Ingredient.class);
			stacks.add(ingredient);
			for (ItemStack stack : ingredient.getMatchingStacks()) {
				entry.addRelevantStack(builder, stack, pageNum);
			}
		}
	}
	
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
		super.render(ms, mouseX, mouseY, pticks);
		
		boolean hasTitle = title != null && !title.isEmpty();
		if (hasTitle) {
			parent.drawCenteredStringNoShadow(ms, i18n(title), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
			GuiBook.drawSeparator(ms, book, 0, 12);
		}
		
		int startY = hasTitle ? 18 : 0;
		int row = 0;
		int column = -1;
		int firstNonFullRowIndex = (stacks.size()) / ITEMS_PER_ROW;
		int unusedEntriesInLastRow = ITEMS_PER_ROW - (stacks.size() % ITEMS_PER_ROW);
		for (Ingredient ingredient : stacks) {
			column++;
			if (column == ITEMS_PER_ROW) {
				column = 0;
				row++;
			}
			int startX = 5 + column * 18;
			if (row == firstNonFullRowIndex) {
				startX += unusedEntriesInLastRow * 9;
			}
			parent.renderIngredient(ms, startX, startY + row * 18, mouseX, mouseY, ingredient);
		}
		
		if (!text.asString().isEmpty()) {
			GuiBook.drawSeparator(ms, book, 0, startY + 20 + row * 18);
		}
		
		super.render(ms, mouseX, mouseY, pticks);
	}
	
	@Override
	public int getTextHeight() {
		boolean hasTitle = title != null && !title.isEmpty();
		return 8 + (hasTitle ? 18 : 0) + (int) Math.ceil(stacks.size() / (float) ITEMS_PER_ROW) * 18;
	}
	
}