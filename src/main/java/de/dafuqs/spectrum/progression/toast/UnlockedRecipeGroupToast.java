package de.dafuqs.spectrum.progression.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class UnlockedRecipeGroupToast implements Toast {
	
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
	
	public static void showRecipeToast(@NotNull MinecraftClient client, ItemStack itemStack, TranslatableText title) {
		Text text = getTextForItemStack(itemStack);
		client.getToastManager().add(new UnlockedRecipeGroupToast(title, text, new ArrayList<>() {{
			add(itemStack);
		}}));
	}
	
	public static void showRecipeGroupToast(@NotNull MinecraftClient client, String groupName, List<ItemStack> itemStacks, TranslatableText title) {
		Text text = new TranslatableText("recipeGroup.spectrum." + groupName);
		client.getToastManager().add(new UnlockedRecipeGroupToast(title, text, itemStacks));
	}
	
	public static Text getTextForItemStack(@NotNull ItemStack itemStack) {
		if (itemStack.isOf(Items.ENCHANTED_BOOK)) {
			// special handling for enchanted books
			// Instead of the text "enchanted book" the toast will
			// read the first stored enchantment in the book
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(itemStack);
			if (enchantments.size() > 0) {
				Map.Entry<Enchantment, Integer> firstEnchantment = enchantments.entrySet().iterator().next();
				return new TranslatableText(firstEnchantment.getKey().getTranslationKey());
			}
		} else if (itemStack.isOf(Items.POTION)) {
			// special handling for potions
			// use the name of the first custom potion effect
			List<StatusEffectInstance> effects = PotionUtil.getCustomPotionEffects(itemStack);
			if (effects.size() > 0) {
				return new TranslatableText(effects.get(0).getTranslationKey()).append(" ").append(new TranslatableText("item.minecraft.potion"));
			}
		}
		return itemStack.getName();
	}
	
	@Override
	public Visibility draw(MatrixStack matrices, @NotNull ToastManager manager, long startTime) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		manager.drawTexture(matrices, 0, 0, 0, 32, this.getWidth(), this.getHeight());
		
		manager.getClient().textRenderer.draw(matrices, title, 30.0F, 7.0F, 3289650); // => #323232: dark gray
		manager.getClient().textRenderer.draw(matrices, text, 30.0F, 18.0F, 0);
		
		long toastTimeMilliseconds = SpectrumCommon.CONFIG.ToastTimeMilliseconds;
		if (!this.soundPlayed && startTime > 0L) {
			this.soundPlayed = true;
			if (this.soundEvent != null) {
				manager.getClient().getSoundManager().play(PositionedSoundInstance.master(this.soundEvent, 1.0F, 1.0F));
			}
		}
		
		int itemStackIndex = (int) (startTime / Math.max(1, toastTimeMilliseconds / this.itemStacks.size()) % this.itemStacks.size());
		ItemStack currentItemStack = itemStacks.get(itemStackIndex);
		manager.getClient().getItemRenderer().renderInGui(currentItemStack, 8, 8);
		
		return startTime >= toastTimeMilliseconds ? Visibility.HIDE : Visibility.SHOW;
	}
	
}
