package de.dafuqs.pigment.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.pigment.PigmentCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.toast.AdvancementToast;
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
public class RecipeToast implements Toast {

    private final Identifier TEXTURE = new Identifier(PigmentCommon.MOD_ID, "textures/gui/toasts.png");
    private final ItemStack itemStack;
    private final SoundEvent soundEvent;
    private boolean soundPlayed;

    public RecipeToast(ItemStack itemStack, SoundEvent soundEvent) {
        this.itemStack = itemStack;
        this.soundEvent = soundEvent;
        this.soundPlayed = false;
    }

    public static void showRecipeToast(MinecraftClient client, ItemStack itemStack, SoundEvent soundEvent) {
        client.getToastManager().add(new RecipeToast(itemStack, soundEvent));
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

        if (!this.soundPlayed && startTime > 0L) {
            this.soundPlayed = true;
            if(this.soundEvent != null) {
                manager.getGame().getSoundManager().play(PositionedSoundInstance.master(this.soundEvent, 1.0F, 1.0F));
            }
        }

        manager.getGame().getItemRenderer().renderInGui(itemStack, 8, 8);
        return startTime >= 5000L ? Visibility.HIDE : Visibility.SHOW;
    }

}
