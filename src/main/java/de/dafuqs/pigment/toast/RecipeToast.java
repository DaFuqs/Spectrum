package de.dafuqs.pigment.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.pigment.PigmentCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RecipeToast implements Toast {

    private final Identifier TEXTURE = new Identifier(PigmentCommon.MOD_ID, "textures/gui/toasts.png");
    private final ItemStack itemStack;

    public RecipeToast(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static void showRecipeToast(MinecraftClient client, ItemStack itemStack) {
        client.getToastManager().add(new RecipeToast(itemStack));
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        Text title = new TranslatableText("pigment.toast.recipe_unlocked.title");
        Text text = this.itemStack.getName();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        manager.drawTexture(matrices, 0, 0, 0, 32, this.getWidth(), this.getHeight());

        manager.getGame().textRenderer.draw(matrices, title, 30.0F, 7.0F, 3289650); // => #323232: dark gray
        manager.getGame().textRenderer.draw(matrices, text, 30.0F, 18.0F, 0);

        manager.getGame().getItemRenderer().renderInGui(itemStack, 8, 8);
        return startTime >= 5000L ? Visibility.HIDE : Visibility.SHOW;
    }

}
