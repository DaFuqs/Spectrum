package de.dafuqs.spectrum.progression.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
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
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class UnlockedRecipeGroupToast implements Toast {

	public enum UnlockedRecipeToastType {
		PEDESTAL,
		FUSION_SHRINE;
	}

	private final Identifier TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/toasts.png");
	private final Text title;
	private final Text text;
	private final List<ItemStack> itemStacks;
	private final SoundEvent soundEvent = SpectrumSoundEvents.NEW_RECIPE;
	private boolean soundPlayed;

	public UnlockedRecipeGroupToast(Text title, Text text, List<ItemStack> itemStacks) {
		this.title = title;
		this.text = text;
		this.itemStacks = itemStacks;
		this.soundPlayed = false;
	}

	public static void showRecipeToast(MinecraftClient client, ItemStack itemStack, UnlockedRecipeToastType type) {
		Text title;
		if(type == UnlockedRecipeToastType.PEDESTAL) {
			title = new TranslatableText("spectrum.toast.pedestal_recipe_unlocked.title");
		} else {
			title = new TranslatableText("spectrum.toast.fusion_shrine_recipe_unlocked.title");
		}
		Text text = itemStack.getName();
		client.getToastManager().add(new UnlockedRecipeGroupToast(title, text, new ArrayList<>() {{ add(itemStack); }}));
	}

	public static void showRecipeGroupToast(MinecraftClient client, String groupName, List<ItemStack> itemStacks, UnlockedRecipeToastType type) {
		Text title;
		if(type == UnlockedRecipeToastType.PEDESTAL) {
			title = new TranslatableText("spectrum.toast.pedestal_recipes_unlocked.title");
		} else {
			title = new TranslatableText("spectrum.toast.fusion_shrine_recipes_unlocked.title");
		}
		Text text = new TranslatableText("recipeGroup.spectrum." + groupName);
		client.getToastManager().add(new UnlockedRecipeGroupToast(title, text, itemStacks));
	}

	@Override
	public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		manager.drawTexture(matrices, 0, 0, 0, 32, this.getWidth(), this.getHeight());

		manager.getClient().textRenderer.draw(matrices, title, 30.0F, 7.0F, 3289650); // => #323232: dark gray
		manager.getClient().textRenderer.draw(matrices, text, 30.0F, 18.0F, 0);

		if (!this.soundPlayed && startTime > 0L) {
			this.soundPlayed = true;
			if(this.soundEvent != null) {
				manager.getClient().getSoundManager().play(PositionedSoundInstance.master(this.soundEvent, 1.0F, 1.0F));
			}
		}

		int itemStackIndex = (int) (startTime / Math.max(1, 5000 / this.itemStacks.size()) % this.itemStacks.size());
		ItemStack currentItemStack = itemStacks.get(itemStackIndex);
		manager.getClient().getItemRenderer().renderInGui(currentItemStack, 8, 8);

		return startTime >= 5000L ? Visibility.HIDE : Visibility.SHOW;
	}

}
