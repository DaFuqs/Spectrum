package de.dafuqs.pigment;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.List;

public class RevelationToast implements Toast {

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
        int i = 16776960;

        manager.getGame().getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        manager.drawTexture(matrices, 0, 0, 0, 0, this.getWidth(), this.getHeight());

        List<OrderedText> list = manager.getGame().textRenderer.wrapLines(title, 125);
        if (list.size() == 1) {
            manager.getGame().textRenderer.draw(matrices, text, 30.0F, 7.0F, i | -16777216);
            manager.getGame().textRenderer.draw(matrices, list.get(0), 30.0F, 18.0F, -1);
        } else {
            int l;
            if (startTime < 1500L) {
                l = MathHelper.floor(MathHelper.clamp((float)(1500L - startTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                manager.getGame().textRenderer.draw(matrices, text, 30.0F, 11.0F, i | l);
            } else {
                l = MathHelper.floor(MathHelper.clamp((float)(startTime - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                int var10000 = this.getHeight() / 2;
                int var10001 = list.size();
                manager.getGame().textRenderer.getClass();
                int m = var10000 - var10001 * 9 / 2;

                for(Iterator<OrderedText> var12 = list.iterator(); var12.hasNext(); m += 9) {
                    OrderedText orderedText = var12.next();
                    manager.getGame().textRenderer.draw(matrices, orderedText, 30.0F, (float)m, 16777215 | l);
                    manager.getGame().textRenderer.getClass();
                }
            }
        }

        if (!this.soundPlayed && startTime > 0L) {
            this.soundPlayed = true;
            if(this.soundEvent != null) {
                manager.getGame().getSoundManager().play(PositionedSoundInstance.master(this.soundEvent, 1.0F, 1.0F));
            }
        }

        manager.getGame().getItemRenderer().renderInGui(itemStack, 8, 8);
        return startTime >= 10000L ? Visibility.HIDE : Visibility.SHOW;
    }

}
