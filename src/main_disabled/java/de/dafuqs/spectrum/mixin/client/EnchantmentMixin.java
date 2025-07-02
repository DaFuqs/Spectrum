package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.api.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.enchantment.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Environment(EnvType.CLIENT)
@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	
	@ModifyReturnValue(method = "getFullname(Lnet/minecraft/core/Holder;I)Lnet/minecraft/network/chat/Component;", at = @At("RETURN"))
	private static Component spectrum$obfuscateEnchantmentNames(Component original, Holder<Enchantment> enchantment, int level) {
		if (enchantment.getRegisteredName().startsWith(SpectrumCommon.MOD_ID)) {
			MutableComponent text = original.copy();
			if (SpectrumEnchantmentHelper.canEntityUse(Minecraft.getInstance().player, enchantment.getRegisteredName())) {
				return text;
			}
			if (SpectrumCommon.CONFIG.NameForUnrevealedEnchantments.isBlank() && text instanceof MutableComponent mutableText) {
				return mutableText.withStyle(ChatFormatting.OBFUSCATED);
			} else {
				return Component.literal(SpectrumCommon.CONFIG.NameForUnrevealedEnchantments).setStyle(text.getStyle());
			}
		}
		return original;
	}
	
}
