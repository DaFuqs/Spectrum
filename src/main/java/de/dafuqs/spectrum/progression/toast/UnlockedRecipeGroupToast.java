package de.dafuqs.spectrum.progression.toast;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.sound.*;
import net.minecraft.client.toast.*;
import net.minecraft.client.util.math.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class UnlockedRecipeGroupToast implements Toast {
	
	private final Identifier TEXTURE = SpectrumCommon.locate("textures/gui/toasts.png");
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
	
	public static void showRecipeToast(@NotNull MinecraftClient client, ItemStack itemStack, Text title) {
		Text text = getTextForItemStack(itemStack);
		client.getToastManager().add(new UnlockedRecipeGroupToast(title, text, new ArrayList<>() {{
			add(itemStack);
		}}));
	}
	
	public static void showRecipeGroupToast(@NotNull MinecraftClient client, String groupName, List<ItemStack> itemStacks, Text title) {
		Text text = Text.translatable("recipeGroup.spectrum." + groupName);
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
				return Text.translatable(firstEnchantment.getKey().getTranslationKey());
			}
		} else if (itemStack.isOf(Items.POTION)) {
			// special handling for potions
			// use the name of the first custom potion effect
			List<StatusEffectInstance> effects = PotionUtil.getCustomPotionEffects(itemStack);
			if (effects.size() > 0) {
				return Text.translatable(effects.get(0).getTranslationKey()).append(" ").append(Text.translatable("item.minecraft.potion"));
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
