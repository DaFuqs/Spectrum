package de.dafuqs.spectrum.inventories.widgets;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.function.*;

import static de.dafuqs.spectrum.helpers.RenderHelper.*;


public class ColorSelectionWidget extends AbstractWidget {
	
	protected final BaseInkBlockEntity<?> blockEntity;
	
	@Nullable
	private Consumer<Optional<Holder<InkColor>>> changedListener;
	
	final List<Tuple<InkColor, Boolean>> usableColors = new ArrayList<>(); // stores if a certain color should be displayed
	
	final int selectedIndexX;
	final int selectedIndexY;
	
	public ColorSelectionWidget(int x, int y, int selectedIndexX, int selectedIndexY, BaseInkBlockEntity<?> blockEntity) {
		this(x, y, selectedIndexX, selectedIndexY, blockEntity, blockEntity.getInkStorage().acceptedColors());
	}
	
	public ColorSelectionWidget(int x, int y, int selectedIndexX, int selectedIndexY, BaseInkBlockEntity<?> blockEntity, Iterable<InkColor> availableColors) {
		super(x, y, 56, 14, Component.literal(""));
		this.blockEntity = blockEntity;
		this.selectedIndexX = selectedIndexX;
		this.selectedIndexY = selectedIndexY;
		
		for (InkColor inkColor : availableColors) {
			usableColors.add(new Tuple<>(inkColor, AdvancementHelper.hasAdvancementClient(inkColor.getRequiredAdvancement())));
		}
	}
	
	public void setChangedListener(@Nullable Consumer<Optional<Holder<InkColor>>> changedListener) {
		this.changedListener = changedListener;
	}
	
	private void onChanged(Optional<Holder<InkColor>> newColor) {
		if (this.changedListener != null) {
			this.changedListener.accept(newColor);
		}
	}
	
	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return super.isMouseOver(mouseX, mouseY) || (this.active && this.visible && isUnselection(mouseX, mouseY));
	}
	
	@Override
	protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
		// draw selection icons
		int i = -1;
		int currentX = this.getX() + 1;
		int currentY = this.getY() + 1;
		for (var color : usableColors) {
			if (color.getB()) {
				fillQuad(context.pose(), currentX, currentY, 5, 5, color.getA().getColorVec());
			}
			i = i + 1;
			currentX = currentX + 7;
			if (i == 7) {
				currentY = currentY + 7;
				currentX = this.getX() + 1;
			}
		}
		
		this.blockEntity.getSelectedColor().ifPresent(inkColor -> fillQuad(context.pose(), selectedIndexX, selectedIndexY, 4, 4, inkColor.value().getColorVec()));
	}
	
	@Override
	@SuppressWarnings("DataFlowIssue")
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		Minecraft client = Minecraft.getInstance();
		
		if (isUnselection(mouseX, mouseY)) {
			client.player.playSound(SpectrumSoundEvents.BUTTON_CLICK, 1.0F, 1.0F);
			onChanged(Optional.empty());
		}
		
		boolean colorSelectionClicked = mouseX >= (double) this.getX() && mouseX < (double) (this.getX() + this.width) && mouseY >= (double) this.getY() && mouseY < (double) (this.getY() + this.height);
		if (colorSelectionClicked && button == 0) {
			int xOffset = Mth.floor(mouseX) - this.getX();
			int yOffset = Mth.floor(mouseY) - this.getY();
			
			int horizontalColorOffset = xOffset / 7;
			int verticalColorOffset = yOffset / 7;
			int newColorIndex = horizontalColorOffset + verticalColorOffset * 8;
			
			var clickedColor = usableColors.get(newColorIndex);
			if (clickedColor.getB()) {
				client.player.playSound(SpectrumSoundEvents.BUTTON_CLICK, 1.0F, 1.0F);
				onChanged(Optional.of(SpectrumRegistries.INK_COLOR.wrapAsHolder(clickedColor.getA())));
			} else {
				client.player.playSound(SpectrumSoundEvents.USE_FAIL, 1.0F, 1.0F);
				onChanged(Optional.empty());
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void updateWidgetNarration(NarrationElementOutput builder) { }
	
	private boolean isUnselection(double mouseX, double mouseY) {
		return mouseX >= (double) selectedIndexX && mouseX < (double) (selectedIndexX + 4) && mouseY >= (double) selectedIndexY && mouseY < (double) (selectedIndexY + 4);
	}
	
	public void drawMouseoverTooltip(GuiGraphics drawContext, int mouseX, int mouseY) {
		Minecraft client = Minecraft.getInstance();
		if (isUnselection(mouseX, mouseY)) {
			drawContext.renderTooltip(client.font, List.of(Component.translatable("spectrum.tooltip.ink_powered.unselect_color")), Optional.empty(), getX(), getY());
		} else {
			int xOffset = Mth.floor(mouseX) - this.getX();
			int yOffset = Mth.floor(mouseY) - this.getY();
			
			int horizontalColorOffset = xOffset / 7;
			int verticalColorOffset = yOffset / 7;
			int newColorIndex = horizontalColorOffset + verticalColorOffset * 8;
			
			if(newColorIndex < 0 || newColorIndex >= usableColors.size()) {
				return;
			}
			
			var hoveredColor = usableColors.get(newColorIndex);
			if (hoveredColor.getB()) {
				drawContext.renderTooltip(client.font, List.of(hoveredColor.getA().getName()), Optional.empty(), getX(), getY());
			} else {
				drawContext.renderTooltip(client.font, List.of(Component.translatable("spectrum.tooltip.ink_powered.unselect_color")), Optional.empty(), getX(), getY());
			}
		}
	}
	
}
