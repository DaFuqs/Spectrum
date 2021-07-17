package de.dafuqs.pigment.misc;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.pigment.PigmentCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class RevelationToast implements Toast {

    private final Identifier TEXTURE = new Identifier(PigmentCommon.MOD_ID, "textures/gui/toasts.png");
    private final ItemStack itemStack;
    private final SoundEvent soundEvent;
    private boolean soundPlayed;

    public RevelationToast(ItemStack itemStack, SoundEvent soundEvent) {
        this.itemStack = itemStack;
        this.soundEvent = soundEvent;
        this.soundPlayed = false;
    }

    public static void showRevelationToast(MinecraftClient client, ItemStack itemStack, SoundEvent soundEvent) {
        client.getToastManager().add(new RevelationToast(itemStack, soundEvent));
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        Text title = new TranslatableText("pigment.toast.revelation.title");
        Text text = new TranslatableText("pigment.toast.revelation.text");

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        manager.drawTexture(matrices, 0, 0, 0, 0, this.getWidth(), this.getHeight());

        List<OrderedText> wrappedText = manager.getGame().textRenderer.wrapLines(text, 125);
        List<OrderedText> wrappedTitle = manager.getGame().textRenderer.wrapLines(title, 125);
        int l;
        if (startTime < 3000L) {
            l = MathHelper.floor(MathHelper.clamp((float)(3000L - startTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
            int var10000 = this.getHeight() / 2;
            int var10001 = wrappedTitle.size();
            int m = var10000 - var10001 * 9 / 2;

            for(Iterator<OrderedText> var12 = wrappedTitle.iterator(); var12.hasNext(); m += 9) {
                OrderedText orderedText = var12.next();
                manager.getGame().textRenderer.draw(matrices, orderedText, 30.0F, (float)m, 3289650 | l);
            }
        } else {
            l = MathHelper.floor(MathHelper.clamp((float)(startTime - 3000L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
            int var10000 = this.getHeight() / 2;
            int var10001 = wrappedText.size();
            int m = var10000 - var10001 * 9 / 2;

            for(Iterator<OrderedText> var12 = wrappedText.iterator(); var12.hasNext(); m += 9) {
                OrderedText orderedText = var12.next();
                manager.getGame().textRenderer.draw(matrices, orderedText, 30.0F, (float)m, l);
            }
        }

        if (!this.soundPlayed && startTime > 0L) {
            this.soundPlayed = true;
            if(this.soundEvent != null) {
                manager.getGame().getSoundManager().play(PositionedSoundInstance.master(this.soundEvent, 1.0F, 1.0F));
            }
        }

        manager.getGame().getItemRenderer().renderInGui(itemStack, 8, 12);
        return startTime >= 6000 ? Visibility.HIDE : Visibility.SHOW;
    }

    public int getWidth() {
        return 160;
    }

    public int getHeight() {
        return 40;
    }

}
