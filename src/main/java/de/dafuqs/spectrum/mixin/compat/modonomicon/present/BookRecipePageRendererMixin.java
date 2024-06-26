package de.dafuqs.spectrum.mixin.compat.modonomicon.present;

import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.modonomicon.client.render.page.BookPageRenderer;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BookRecipePageRenderer.class)
public abstract class BookRecipePageRendererMixin extends BookPageRenderer<BookRecipePage<?>> {

    public BookRecipePageRendererMixin(BookRecipePage page) {
        super(page);
    }

    @ModifyArg(method = "getClickedComponentStyleAt", at = @At(value = "INVOKE", target = "Lcom/klikli_dev/modonomicon/client/render/page/BookRecipePageRenderer;getClickedComponentStyleAtForTextHolder(Lcom/klikli_dev/modonomicon/book/BookTextHolder;IIIDD)Lnet/minecraft/text/Style;"), index = 1)
    private int spectrum$getClickedComponent$modifyX(int x) {
        return x + parentScreen.getBook().getBookTextOffsetX();
    }

    @ModifyArg(method = "getClickedComponentStyleAt", at = @At(value = "INVOKE", target = "Lcom/klikli_dev/modonomicon/client/render/page/BookRecipePageRenderer;getClickedComponentStyleAtForTextHolder(Lcom/klikli_dev/modonomicon/book/BookTextHolder;IIIDD)Lnet/minecraft/text/Style;"), index = 2)
    private int spectrum$getClickedComponent$modifyY(int y) {
        return y + parentScreen.getBook().getBookTextOffsetY();
    }

    @ModifyArg(method = "getClickedComponentStyleAt", at = @At(value = "INVOKE", target = "Lcom/klikli_dev/modonomicon/client/render/page/BookRecipePageRenderer;getClickedComponentStyleAtForTextHolder(Lcom/klikli_dev/modonomicon/book/BookTextHolder;IIIDD)Lnet/minecraft/text/Style;"), index = 3)
    private int spectrum$getClickedComponent$modifyWidth(int width) {
        return width + parentScreen.getBook().getBookTextOffsetWidth() - parentScreen.getBook().getBookTextOffsetX();
    }

}
