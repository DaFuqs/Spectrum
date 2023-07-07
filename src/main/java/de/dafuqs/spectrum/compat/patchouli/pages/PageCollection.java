package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;
import net.minecraft.client.util.math.*;
import net.minecraft.command.*;
import net.minecraft.command.argument.*;
import net.minecraft.item.*;
import net.minecraft.util.registry.*;
import vazkii.patchouli.api.*;
import vazkii.patchouli.client.book.*;
import vazkii.patchouli.client.book.gui.*;
import vazkii.patchouli.client.book.page.abstr.*;

import java.util.*;

public class PageCollection extends PageWithText {
	
	private static final int ENTRIES_PER_ROW = 6;
	
	String title;
	IVariable items;
	
	transient List<ItemStack> stacks;
	
	@Override
	public void build(BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(entry, builder, pageNum);
		
		stacks = new ArrayList<>();
		for (IVariable item : items.asList()) {
			String stackString = item.asString();
			ItemStack stack;
			try {
				stack = new ItemStackArgumentType(new CommandRegistryAccess(DynamicRegistryManager.of(Registry.REGISTRIES))).parse(new StringReader(stackString)).createStack(1, false);
			} catch (CommandSyntaxException e) {
				PatchouliAPI.LOGGER.warn("Unable to parse stack {} in collection page", stackString);
				continue;
			}
			stacks.add(stack);
			entry.addRelevantStack(builder, stack, pageNum);
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
		int firstNonFullRowIndex = (stacks.size()) / ENTRIES_PER_ROW;
		int unusedEntriesInLastRow = ENTRIES_PER_ROW - (stacks.size() % ENTRIES_PER_ROW);
		for (ItemStack stack : stacks) {
			column++;
			if (column == ENTRIES_PER_ROW) {
				column = 0;
				row++;
			}
			int startX = 5 + column * 18;
			if (row == firstNonFullRowIndex) {
				startX += unusedEntriesInLastRow * 9;
			}
			parent.renderItemStack(ms, startX, startY + row * 18, mouseX, mouseY, stack);
		}
		
		if (!text.asString().isEmpty()) {
			GuiBook.drawSeparator(ms, book, 0, startY + 20 + row * 18);
		}
		
		super.render(ms, mouseX, mouseY, pticks);
	}
	
	@Override
	public int getTextHeight() {
		boolean hasTitle = title != null && !title.isEmpty();
		return 8 + (hasTitle ? 18 : 0) + (int) Math.ceil(stacks.size() / (float) ENTRIES_PER_ROW) * 18;
	}
	
}