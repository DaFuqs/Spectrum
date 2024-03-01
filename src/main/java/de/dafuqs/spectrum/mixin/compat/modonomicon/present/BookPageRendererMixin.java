package de.dafuqs.spectrum.mixin.compat.modonomicon.present;

import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.RenderedBookTextHolder;
import com.klikli_dev.modonomicon.book.page.BookPage;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.klikli_dev.modonomicon.client.gui.book.markdown.MarkdownComponentRenderUtils;
import com.klikli_dev.modonomicon.client.render.page.BookPageRenderer;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconGuidebookProvider;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookPageRenderer.class)
public class BookPageRendererMixin {

    private static float SCALE = 0.8f;

    @Shadow(remap = false)
    protected BookPage page;
    @Shadow(remap = false)
    protected BookContentScreen parentScreen;
    @Shadow
    protected TextRenderer font;

    @Inject(method = "renderBookTextHolder(Lnet/minecraft/client/gui/DrawContext;Lcom/klikli_dev/modonomicon/book/BookTextHolder;III)V", at = @At(value = "HEAD"), cancellable = true)
    private void spectrum$resizeTextStart(DrawContext guiGraphics, BookTextHolder text, int x, int y, int width, CallbackInfo ci) {
        if (page.getBook().getId().equals(ModonomiconGuidebookProvider.GUIDEBOOK_ID)) {
            x += parentScreen.getBook().getBookTextOffsetX();

            y += parentScreen.getBook().getBookTextOffsetY();
            y /= SCALE;

            width += parentScreen.getBook().getBookTextOffsetWidth();
            width -= parentScreen.getBook().getBookTextOffsetX(); //always remove the offset x from the width to avoid overflow
            width /= SCALE;

            guiGraphics.getMatrices().push();
            guiGraphics.getMatrices().scale(SCALE, SCALE, 0);
            BookPageRenderer.renderBookTextHolder(guiGraphics, text, font, x, y, width);
            guiGraphics.getMatrices().pop();
            ci.cancel();
        }
    }

    @ModifyVariable(method = "getClickedComponentStyleAtForTextHolder", at = @At(value = "HEAD"), ordinal = 1, argsOnly = true)
    private int spectrum$getClickedComponent$modifyY(int y) {
        return (int) (y / SCALE);
    }

    @ModifyVariable(method = "getClickedComponentStyleAtForTextHolder", at = @At(value = "HEAD"), ordinal = 2, argsOnly = true)
    private int spectrum$getClickedComponent$modifyWidth(int width) {
        return (int) (width / SCALE);
    }

    @ModifyVariable(method = "getClickedComponentStyleAtForTextHolder", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private double spectrum$getClickedComponent$modifyMouseX(double pMouseX) {
        return pMouseX / SCALE;
    }

    @ModifyVariable(method = "getClickedComponentStyleAtForTextHolder", at = @At(value = "HEAD"), ordinal = 1, argsOnly = true)
    private double spectrum$getClickedComponent$modifyMouseY(double pMouseY) {
        return pMouseY / SCALE;
    }

}
