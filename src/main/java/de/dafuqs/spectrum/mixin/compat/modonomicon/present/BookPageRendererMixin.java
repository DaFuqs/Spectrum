package de.dafuqs.spectrum.mixin.compat.modonomicon.present;

import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.page.BookPage;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.render.page.BookPageRenderer;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconGuidebookProvider;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookPageRenderer.class)
public class BookPageRendererMixin {

    @Shadow
    protected BookPage page;
    @Shadow
    protected BookContentScreen parentScreen;
    @Shadow
    protected TextRenderer font;

    @Inject(method = "renderBookTextHolder(Lnet/minecraft/client/gui/DrawContext;Lcom/klikli_dev/modonomicon/book/BookTextHolder;III)V", at = @At(value = "HEAD"), cancellable = true)
    private void spectrum$resizeTextStart(DrawContext guiGraphics, BookTextHolder text, int x, int y, int width, CallbackInfo ci) {
        if (page.getBook().getId().equals(ModonomiconGuidebookProvider.GUIDEBOOK_ID)) {
            float scale = 0.8f;

            x += parentScreen.getBook().getBookTextOffsetX();

            y += parentScreen.getBook().getBookTextOffsetY();
            y *= 1 / scale;

            width += parentScreen.getBook().getBookTextOffsetWidth();
            width -= parentScreen.getBook().getBookTextOffsetX(); //always remove the offset x from the width to avoid overflow
            width *= 1 / scale;

            guiGraphics.getMatrices().push();
            guiGraphics.getMatrices().scale(scale, scale, 0);
            BookPageRenderer.renderBookTextHolder(guiGraphics, text, font, x, y, width);
            guiGraphics.getMatrices().pop();
            ci.cancel();
        }
    }

}
