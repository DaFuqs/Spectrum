package de.dafuqs.spectrum.compat.emi.widgets;

import com.mojang.blaze3d.systems.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.client.render.*;
import net.minecraft.util.*;

import java.util.*;
import java.util.function.*;

public class SaneButtonWidget extends ButtonWidget implements WidgetTooltipHolder<SaneButtonWidget> {
	
	private BiFunction<Integer, Integer, List<TooltipComponent>> tooltipSupplier = (mouseX, mouseY) -> List.of();
	
	public SaneButtonWidget(int x, int y, int width, int height, int u, int v, Identifier texture, BooleanSupplier isActive, ClickAction action) {
		super(x, y, width, height, u, v, texture, isActive, action);
	}
	
	@Override
	public void render(DrawContext draw, int mouseX, int mouseY, float delta) {
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		draw.drawTexture(texture, this.x, this.y, this.u, this.v, this.width, this.height);
	}
	
	@Override
	public SaneButtonWidget tooltip(BiFunction<Integer, Integer, List<TooltipComponent>> tooltipSupplier) {
		this.tooltipSupplier = tooltipSupplier;
		return this;
	}
	
	public List<TooltipComponent> getTooltip(int mouseX, int mouseY) {
		return tooltipSupplier.apply(mouseX, mouseY);
	}
	
}
