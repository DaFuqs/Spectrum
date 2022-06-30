package de.dafuqs.spectrum.compat.patchouli;

import com.google.gson.annotations.SerializedName;
import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

import java.util.HashMap;
import java.util.Map;

public class PageChecklist extends BookPage {
	
	protected IVariable text;
	transient BookTextRenderer textRender;
	
	@SerializedName("checklist") Map<String, String> checklist = new HashMap<>();
	
	@Override
	public void onDisplayed(GuiBookEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);
		
		StringBuilder stringBuilder = new StringBuilder();
		if (text == null) {
			text = IVariable.wrap("");
		} else {
			stringBuilder.append(text.asString());
		}
		
		for(Map.Entry<String, String> entry : checklist.entrySet()) {
			String value = entry.getValue();
			if(AdvancementHelper.hasAdvancementClient(Identifier.tryParse(entry.getKey()))) {
				stringBuilder.append("$(li)");
				stringBuilder.append("$(m)");
				stringBuilder.append(value);
				stringBuilder.append("$()");
			} else {
				stringBuilder.append("$(li)");
				stringBuilder.append(value);
				stringBuilder.append("$()");
			}
		}
		
		textRender = new BookTextRenderer(parent, IVariable.wrap(stringBuilder.toString()).as(Text.class), 0, getTextHeight());
	}
	
	public int getTextHeight() {
		return 0;
	}
	
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
		super.render(ms, mouseX, mouseY, pticks);
		
		textRender.render(ms, mouseX, mouseY);
	}
	
}