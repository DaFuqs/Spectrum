package de.dafuqs.spectrum.compat.patchouli.pages;

import com.google.gson.annotations.*;
import com.mojang.blaze3d.systems.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.effect.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;
import vazkii.patchouli.client.book.*;
import vazkii.patchouli.client.book.gui.*;
import vazkii.patchouli.client.book.page.abstr.*;

public class PageStatusEffect extends PageWithText {
	
	String title;
	@SerializedName("status_effect_id")
	String statusEffectId;
	transient StatusEffect statusEffect;
	transient Sprite statusEffectSprite;
	
	@Override
	public void build(World world, BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(world, entry, builder, pageNum);
		statusEffect = Registries.STATUS_EFFECT.get(new Identifier(statusEffectId));
		statusEffectSprite = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(statusEffect);
	}
	
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
		RenderSystem.setShaderTexture(0, statusEffectSprite.getContents().getId());
		RenderSystem.enableBlend();
		DrawableHelper.drawSprite(ms, 49, 14, 0, 18, 18, statusEffectSprite);
		RenderSystem.setShaderTexture(0, book.craftingTexture);
		DrawableHelper.drawTexture(ms, GuiBook.PAGE_WIDTH / 2 - 66 / 2, 10, 0, 128 - 26, 68, 28, 128, 256);
		
		Text toDraw;
		if (title != null && !title.isEmpty()) {
			toDraw = i18nText(title);
		} else {
			toDraw = statusEffect.getName();
		}
		parent.drawCenteredStringNoShadow(ms, toDraw.asOrderedText(), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
		
		super.render(ms, mouseX, mouseY, pticks);
	}
	
	@Override
	public int getTextHeight() {
		return 40;
	}
	
}