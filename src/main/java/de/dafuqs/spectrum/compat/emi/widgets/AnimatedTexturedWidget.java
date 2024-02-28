package de.dafuqs.spectrum.compat.emi.widgets;

import com.mojang.blaze3d.systems.*;
import dev.emi.emi.api.widget.*;
import dev.emi.emi.runtime.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import java.util.*;
import java.util.function.*;

public class AnimatedTexturedWidget extends Widget implements WidgetTooltipHolder<AnimatedTexturedWidget> {
	
	protected final Identifier texture;
	protected final int x, y;
	protected final int textureWidth, textureHeight;
	
	private final int animationCount;
	private final double animationDurationMS;
	
	private BiFunction<Integer, Integer, List<TooltipComponent>> tooltipSupplier = (mouseX, mouseY) -> List.of();
	
	public AnimatedTexturedWidget(Identifier texture, int x, int y, int textureWidth, int textureHeight, int animationDurationMS) {
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
	public AnimatedTexturedWidget tooltip(BiFunction<Integer, Integer, List<TooltipComponent>> tooltipSupplier) {
		this.tooltipSupplier = tooltipSupplier;
		return this;
	}
	
	@Override
	public List<TooltipComponent> getTooltip(int mouseX, int mouseY) {
		return tooltipSupplier.apply(mouseX, mouseY);
	}
	
	@Override
	public void render(DrawContext draw, int mouseX, int mouseY, float delta) {
		EmiDrawContext context = EmiDrawContext.wrap(draw);
		
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		if (animationDurationMS > 0) {
			int index = MathHelper.ceil((System.currentTimeMillis() / (animationDurationMS / animationCount) % animationCount));
			context.drawTexture(texture, x, y, 0, textureWidth, index * textureWidth, textureWidth, textureWidth, textureWidth, textureHeight);
		} else {
			context.drawTexture(texture, x, y, 0, textureWidth, textureWidth, textureWidth, textureWidth, textureWidth, textureHeight);
		}
	}
	
}
