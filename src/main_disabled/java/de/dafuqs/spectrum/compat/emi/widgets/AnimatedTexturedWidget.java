package de.dafuqs.spectrum.compat.emi.widgets;

import com.mojang.blaze3d.systems.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.tooltip.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;

import java.util.*;
import java.util.function.*;

public class AnimatedTexturedWidget extends Widget implements WidgetTooltipHolder<AnimatedTexturedWidget> {
	
	protected final ResourceLocation texture;
	protected final int x, y;
	protected final int textureWidth, textureHeight;
	
	private final int animationCount;
	private final double animationDurationMS;
	
	private BiFunction<Integer, Integer, List<ClientTooltipComponent>> tooltipSupplier = (mouseX, mouseY) -> List.of();
	
	public AnimatedTexturedWidget(ResourceLocation texture, int x, int y, int textureWidth, int textureHeight, int animationDurationMS) {
		super();
		
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		
		this.animationDurationMS = animationDurationMS;
		this.animationCount = textureHeight / textureWidth;
	}
	
	@Override
	public Bounds getBounds() {
		return new Bounds(x, y, textureWidth, textureWidth);
	}
	
	@Override
	public AnimatedTexturedWidget tooltip(BiFunction<Integer, Integer, List<ClientTooltipComponent>> tooltipSupplier) {
		this.tooltipSupplier = tooltipSupplier;
		return this;
	}
	
	@Override
	public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
		return tooltipSupplier.apply(mouseX, mouseY);
	}
	
	@Override
	public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		if (animationDurationMS > 0) {
			int index = Mth.ceil((System.currentTimeMillis() / (animationDurationMS / animationCount) % animationCount));
			draw.blit(texture, x, y, 0, textureWidth, index * textureWidth, textureWidth, textureWidth, textureWidth, textureHeight);
		} else {
			draw.blit(texture, x, y, 0, textureWidth, textureWidth, textureWidth, textureWidth, textureWidth, textureHeight);
		}
	}
	
}
