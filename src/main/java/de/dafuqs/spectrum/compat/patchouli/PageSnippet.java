package de.dafuqs.spectrum.compat.patchouli;

import net.minecraft.client.util.math.*;
import vazkii.patchouli.client.book.*;
import vazkii.patchouli.client.book.page.abstr.*;

// TODO
public class PageSnippet extends PageWithText {
	
	String title;
	
	@Override
	public void build(BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(entry, builder, pageNum);
	}
	
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
		super.render(ms, mouseX, mouseY, pticks);
	}
	
	@Override
	public int getTextHeight() {
		return 40;
	}
	
}