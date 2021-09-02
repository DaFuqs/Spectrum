package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlockEntity;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class PedestalScreen extends HandledScreen<PedestalScreenHandler> {

    public static final Identifier BACKGROUND1 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/pedestal1.png");
    public static final Identifier BACKGROUND2 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/pedestal2.png");
    public static final Identifier BACKGROUND3 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/pedestal3.png");
    public static final Identifier BACKGROUND4 = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/pedestal4.png");

    private final Identifier backgroundTexture;
    private final PedestalBlock.PedestalVariant pedestalVariant;
    private final PedestalRecipeTier maxPedestalRecipeTierForVariant;
    private final boolean structureUpdateAvailable;

    public PedestalScreen(PedestalScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        this.backgroundHeight = 194;

        backgroundTexture = getBackgroundTextureForVariant(handler.getVariant());

        this.pedestalVariant = handler.getVariant();

        this.maxPedestalRecipeTierForVariant = PedestalBlockEntity.getHighestAvailableRecipeTierForVariant(pedestalVariant);
        PedestalRecipeTier maxPedestalRecipeTier = handler.getMaxPedestalRecipeTier();
        this.structureUpdateAvailable = this.maxPedestalRecipeTierForVariant != maxPedestalRecipeTier;
    }

    public static Identifier getBackgroundTextureForVariant(PedestalBlock.PedestalVariant pedestalVariant) {
        switch (pedestalVariant) {
            case MOONSTONE -> {
                return BACKGROUND4;
            }
            case ONYX -> {
                return BACKGROUND3;
            }
            case ALL_BASIC -> {
                return BACKGROUND2;
            }
            default -> {
                return BACKGROUND1;
            }
        }
    }

    public static Identifier getBackgroundTextureForTier(PedestalRecipeTier recipeTier) {
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
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        // draw "title" and "inventory" texts
        int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2; // 8;
        int titleY = 7;
        Text title = this.title;
        int inventoryX = 8;
        int intInventoryY = 100;

        this.textRenderer.draw(matrices, title, titleX, titleY, 4210752);
        this.textRenderer.draw(matrices, this.playerInventoryTitle, inventoryX, intInventoryY, 4210752);

        // if structure could be improved: show blinking information icon
        if(structureUpdateAvailable) {
            if((client.world.getTime() >> 4) % 2 == 0) {
                this.textRenderer.draw(matrices, "ℹ", 130, 66, 11010048);
            } else {
                this.textRenderer.draw(matrices, "ℹ", 130, 66, 16252928);
            }
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, backgroundTexture);

        // background
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // crafting arrow
        boolean isCrafting = this.handler.isCrafting();
        if(isCrafting) {
            int progressWidth = (this.handler).getCraftingProgress();
            // x+y: destination, u+v: original coordinates in texture file
            this.drawTexture(matrices, x + 88, y + 37, 176, 0, progressWidth + 1, 16);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(mouseOverInformationIcon((int) mouseX,(int) mouseY)) {
            IMultiblock currentMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
            IMultiblock multiblockToDisplay = PatchouliAPI.get().getMultiblock(SpectrumMultiblocks.getDisplayStructureIdentifierForTier(maxPedestalRecipeTierForVariant));
            if(currentMultiBlock == multiblockToDisplay) {
                PatchouliAPI.get().clearMultiblock();
            } else {
                PatchouliAPI.get().showMultiblock(multiblockToDisplay, SpectrumMultiblocks.getStructureText(maxPedestalRecipeTierForVariant), this.handler.getPedestalPos().down(2) , BlockRotation.NONE);
            }
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

        if(mouseOverInformationIcon(mouseX, mouseY)) {
            this.renderTooltip(matrices, new LiteralText("Structure update available!"), mouseX, mouseY);
        } else {
            drawMouseoverTooltip(matrices, mouseX, mouseY);
        }
    }

    private boolean mouseOverInformationIcon(int mouseX, int mouseY) {
        return structureUpdateAvailable && mouseX > x+128 && mouseX < x+140 && mouseY > y+64 && mouseY < y+74;
    }

}