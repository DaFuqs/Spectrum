package de.dafuqs.spectrum.inventories;

import com.klikli_dev.modonomicon.api.multiblock.*;
import com.klikli_dev.modonomicon.client.render.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.*;

public class PedestalScreen extends AbstractContainerScreen<PedestalScreenHandler> {
	
	public static final ResourceLocation BACKGROUND1 = SpectrumCommon.locate("textures/gui/container/pedestal1.png");
	public static final ResourceLocation BACKGROUND2 = SpectrumCommon.locate("textures/gui/container/pedestal2.png");
	public static final ResourceLocation BACKGROUND3 = SpectrumCommon.locate("textures/gui/container/pedestal3.png");
	public static final ResourceLocation BACKGROUND4 = SpectrumCommon.locate("textures/gui/container/pedestal4.png");
	private final boolean structureUpdateAvailable;
	private final ResourceLocation backgroundTexture;
	private final PedestalRecipeTier maxPedestalRecipeTierForVariant;
	final int informationIconX = 95;
	final int informationIconY = 55;
	
	public PedestalScreen(PedestalScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
		this.imageHeight = 194;
		this.maxPedestalRecipeTierForVariant = this.menu.getPedestalRecipeTier();
		this.backgroundTexture = getBackgroundTextureForTier(this.maxPedestalRecipeTierForVariant);
		PedestalRecipeTier maxPedestalRecipeTier = this.menu.getMaxPedestalRecipeTier();
		this.structureUpdateAvailable = this.maxPedestalRecipeTierForVariant != maxPedestalRecipeTier;
	}
	
	@Contract(pure = true)
	public static ResourceLocation getBackgroundTextureForTier(@NotNull PedestalRecipeTier recipeTier) {
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
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (imageWidth - font.width(title)) / 2; // 8;
		int titleY = 7;
		Component title = this.title;
		int inventoryX = 8;
		int intInventoryY = 100;
		var tr = this.font;
		
		drawContext.drawString(tr, title, titleX, titleY, RenderHelper.GREEN_COLOR, false);
		drawContext.drawString(tr, this.playerInventoryTitle, inventoryX, intInventoryY, RenderHelper.GREEN_COLOR, false);
		
		// if structure could be improved:
		// show red blinking information icon
		if (structureUpdateAvailable) {
			if (minecraft != null && minecraft.level != null && (minecraft.level.getGameTime() >> 4) % 2 == 0) {
				drawContext.drawString(tr, "ℹ", informationIconX, informationIconY, 11010048, false);
			} else {
				drawContext.drawString(tr, "ℹ", informationIconX, informationIconY, 16252928, false);
			}
		}
	}
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
		// background
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		drawContext.blit(backgroundTexture, x, y, 0, 0, imageWidth, imageHeight);
		
		// crafting arrow
		boolean isCrafting = this.menu.isCrafting();
		if (isCrafting) {
			int progressWidth = (this.menu).getCraftingProgress();
			// x+y: destination, u+v: original coordinates in texture file
			drawContext.blit(backgroundTexture, x + 88, y + 37, 176, 0, progressWidth + 1, 16);
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		Minecraft client = Minecraft.getInstance();
		if (mouseOverInformationIcon((int) mouseX, (int) mouseY)) {
			Multiblock currentMultiBlock = MultiblockPreviewRenderer.getMultiblock();
			Multiblock multiblockToDisplay = SpectrumMultiblocks.get(maxPedestalRecipeTierForVariant.getStructureID(client.player));
			if (currentMultiBlock == multiblockToDisplay) {
				ModonomiconHelper.clearRenderedMultiblock(currentMultiBlock);
			} else {
				ModonomiconHelper.renderMultiblock(multiblockToDisplay, maxPedestalRecipeTierForVariant.getStructureText(), this.menu.getBlockPos().below(2), Rotation.NONE);
			}
			return true;
		} else {
			return super.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext, mouseX, mouseY, delta);
		super.render(drawContext, mouseX, mouseY, delta);
		
		if (mouseOverInformationIcon(mouseX, mouseY)) {
			drawContext.renderTooltip(this.font, Component.translatable("multiblock.spectrum.pedestal.upgrade_available"), mouseX, mouseY);
		} else {
			renderTooltip(drawContext, mouseX, mouseY);
		}
	}
	
	private boolean mouseOverInformationIcon(int mouseX, int mouseY) {
		return structureUpdateAvailable && mouseX > leftPos + informationIconX - 2 && mouseX < leftPos + informationIconX + 10 && mouseY > topPos + informationIconY - 2 && mouseY < topPos + informationIconY + 10;
	}
	
}
