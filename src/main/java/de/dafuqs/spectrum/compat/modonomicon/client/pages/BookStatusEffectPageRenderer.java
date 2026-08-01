package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.client.render.page.*;
import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.compat.modonomicon.pages.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.effect.*;

import java.util.*;

public class BookStatusEffectPageRenderer extends BookTextPageRenderer {
	
	private final TextureAtlasSprite statusEffectSprite;
	
	public BookStatusEffectPageRenderer(BookStatusEffectPage page) {
		super(page);
		Optional<Holder.Reference<MobEffect>> statusEffect = BuiltInRegistries.MOB_EFFECT.getHolder(page.getStatusEffectId());
		if(statusEffect.isEmpty()) {
			this.statusEffectSprite = Minecraft.getInstance().getMobEffectTextures().get(MobEffects.FIRE_RESISTANCE); // TODO: render missing image instead?
		} else {
			this.statusEffectSprite = Minecraft.getInstance().getMobEffectTextures().get(statusEffect.get());
		}
	}
	
	@Override
	public int getTextY() {
		return 50;
	}
	
	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float ticks) {
		super.render(drawContext, mouseX, mouseY, ticks);
		
		RenderSystem.enableBlend();
		drawContext.blit(49, 24, 0, 18, 18, statusEffectSprite);
	}
	
}
