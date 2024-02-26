package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.client.render.page.BookTextPageRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookStatusEffectPage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;

public class BookStatusEffectPageRenderer extends BookTextPageRenderer {

    private final Sprite statusEffectSprite;

    public BookStatusEffectPageRenderer(BookStatusEffectPage page) {
        super(page);
        StatusEffect statusEffect = Registries.STATUS_EFFECT.get(page.getStatusEffectId());
        this.statusEffectSprite = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(statusEffect);
    }

    @Override
    public int getTextY() {
        return 50;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float pticks) {
        RenderSystem.enableBlend();
        drawContext.drawSprite(49, 24, 0, 18, 18, statusEffectSprite);

        super.render(drawContext, mouseX, mouseY, pticks);
    }

}
