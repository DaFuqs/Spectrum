package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.blaze3d.systems.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import vazkii.patchouli.client.book.*;
import vazkii.patchouli.client.book.gui.*;
import vazkii.patchouli.client.book.page.abstr.*;

public class PageSnippet extends PageWithText {
	
	String title;
	String resource_path;
	int resource_width;
	int resource_height;
	int texture_x;
	int texture_y;
	int texture_width;
	int texture_height;
	
	@Override
	public void build(World world, BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(world, entry, builder, pageNum);
	}
	
	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float pticks) {
		super.render(drawContext, mouseX, mouseY, pticks);
		
		parent.drawCenteredStringNoShadow(drawContext, title == null ? "" : i18n(title), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
		GuiBook.drawSeparator(drawContext, book, 0, 12 + texture_height);
		
		RenderSystem.enableBlend();
		drawContext.drawTexture(new Identifier(resource_path), 58 - texture_width / 2, 10, texture_x, texture_y, texture_width, texture_height, resource_width, resource_height);
	}
	
	@Override
	public int getTextHeight() {
		return 20 + texture_height;
	}
	
}