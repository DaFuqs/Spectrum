package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageWithText;

public class PageIllustration extends BookPage {

	String resource_path;
	int resource_width;
	int resource_height;
	int texture_x;
	int texture_y;
	int texture_width;
	int texture_height;
	int y_offset;
	
	@Override
	public void build(World world, BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(world, entry, builder, pageNum);
	}
	
	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float pticks) {
		super.render(drawContext, mouseX, mouseY, pticks);
		
		RenderSystem.enableBlend();
		drawContext.drawTexture(new Identifier(resource_path), 58 - texture_width / 2, y_offset, texture_x, texture_y, texture_width, texture_height, resource_width, resource_height);
	}
	
}