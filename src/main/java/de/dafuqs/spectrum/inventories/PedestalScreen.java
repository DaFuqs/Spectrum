package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.helpers.RenderHelper;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.entity.player.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.api.*;

public class PedestalScreen extends HandledScreen<PedestalScreenHandler> {
	
	public static final Identifier BACKGROUND1 = SpectrumCommon.locate("textures/gui/container/pedestal1.png");
	public static final Identifier BACKGROUND2 = SpectrumCommon.locate("textures/gui/container/pedestal2.png");
	public static final Identifier BACKGROUND3 = SpectrumCommon.locate("textures/gui/container/pedestal3.png");
	public static final Identifier BACKGROUND4 = SpectrumCommon.locate("textures/gui/container/pedestal4.png");
	private final Identifier backgroundTexture;
	private final PedestalRecipeTier maxPedestalRecipeTierForVariant;
	private final boolean structureUpdateAvailable;
	final int informationIconX = 95;
	final int informationIconY = 55;
	
	public PedestalScreen(PedestalScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 194;
		
		this.maxPedestalRecipeTierForVariant = handler.getPedestalRecipeTier();
		this.backgroundTexture = getBackgroundTextureForTier(this.maxPedestalRecipeTierForVariant);
		PedestalRecipeTier maxPedestalRecipeTier = handler.getMaxPedestalRecipeTier();
		this.structureUpdateAvailable = this.maxPedestalRecipeTierForVariant != maxPedestalRecipeTier;
	}

	@Contract(pure = true)
	public static Identifier getBackgroundTextureForTier(@NotNull PedestalRecipeTier recipeTier) {
		switch (recipeTier) {
			case COMPLEX -> {
				return BACKGROUND4;
			}
			case ADVANCED -> {
				return BACKGROUND3;
			}
			case SIMPLE -> {
				return BACKGROUND2;
			}
			default -> {
				return BACKGROUND1;
			}
		}
	}
	
	@Override
	protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2; // 8;
		int titleY = 7;
		Text title = this.title;
		int inventoryX = 8;
		int intInventoryY = 100;
		var tr = this.textRenderer;

		drawContext.drawText(tr, title, titleX, titleY, RenderHelper.GREEN_COLOR, false);
		drawContext.drawText(tr, this.playerInventoryTitle, inventoryX, intInventoryY, RenderHelper.GREEN_COLOR, false);
		
		// if structure could be improved:
		// show red blinking information icon
		if (structureUpdateAvailable) {
			if (client != null && (client.world.getTime() >> 4) % 2 == 0) {
				drawContext.drawText(tr, "ℹ", informationIconX, informationIconY, 11010048, false);
			} else {
				drawContext.drawText(tr, "ℹ", informationIconX, informationIconY, 16252928, false);
			}
		}
	}
	
	@Override
	protected void drawBackground(DrawContext drawContext, float delta, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		// background
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawContext.drawTexture(backgroundTexture, x, y, 0, 0, backgroundWidth, backgroundHeight);
		
		// crafting arrow
		boolean isCrafting = this.handler.isCrafting();
		if (isCrafting) {
			int progressWidth = (this.handler).getCraftingProgress();
			// x+y: destination, u+v: original coordinates in texture file
			drawContext.drawTexture(backgroundTexture, x + 88, y + 37, 176, 0, progressWidth + 1, 16);
		}
	}
	
	@Override
	@SuppressWarnings("resource")
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (mouseOverInformationIcon((int) mouseX, (int) mouseY)) {
			IMultiblock currentMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
			IMultiblock multiblockToDisplay = PatchouliAPI.get().getMultiblock(SpectrumMultiblocks.getDisplayStructureIdentifierForTier(maxPedestalRecipeTierForVariant, MinecraftClient.getInstance().player));
			if (currentMultiBlock == multiblockToDisplay) {
				PatchouliAPI.get().clearMultiblock();
			} else {
				PatchouliAPI.get().showMultiblock(multiblockToDisplay, SpectrumMultiblocks.getPedestalStructureText(maxPedestalRecipeTierForVariant), this.handler.getPedestalPos().down(2), BlockRotation.NONE);
			}
			return true;
		} else {
			return super.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext);
		super.render(drawContext, mouseX, mouseY, delta);
		
		if (mouseOverInformationIcon(mouseX, mouseY)) {
			drawContext.drawTooltip(this.textRenderer, Text.translatable("multiblock.spectrum.pedestal.upgrade_available"), mouseX, mouseY);
		} else {
			drawMouseoverTooltip(drawContext, mouseX, mouseY);
		}
	}
	
	private boolean mouseOverInformationIcon(int mouseX, int mouseY) {
		return structureUpdateAvailable && mouseX > x + informationIconX - 2 && mouseX < x + informationIconX + 10 && mouseY > y + informationIconY - 2 && mouseY < y + informationIconY + 10;
	}
	
}