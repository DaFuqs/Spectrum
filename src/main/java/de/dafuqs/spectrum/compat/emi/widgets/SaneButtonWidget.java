package de.dafuqs.spectrum.compat.emi.widgets;

import com.mojang.blaze3d.systems.*;
import dev.emi.emi.api.widget.*;
import dev.emi.emi.runtime.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.tooltip.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;

import java.util.*;
import java.util.function.*;

public class SaneButtonWidget extends ButtonWidget implements WidgetTooltipHolder<SaneButtonWidget> {
	
	private BiFunction<Integer, Integer, List<ClientTooltipComponent>> tooltipSupplier = (mouseX, mouseY) -> List.of();
	
	public SaneButtonWidget(int x, int y, int width, int height, int u, int v, ResourceLocation texture, BooleanSupplier isActive, ClickAction action) {
		super(x, y, width, height, u, v, texture, isActive, action);
	}
	
	@Override
	public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		EmiDrawContext context = EmiDrawContext.wrap(draw);
		context.drawTexture(texture, this.x, this.y, this.u, this.v, this.width, this.height);
	}
	
	@Override
	public SaneButtonWidget tooltip(BiFunction<Integer, Integer, List<ClientTooltipComponent>> tooltipSupplier) {
		this.tooltipSupplier = tooltipSupplier;
		return this;
	}
	
	public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
		return tooltipSupplier.apply(mouseX, mouseY);
	}
	
}
