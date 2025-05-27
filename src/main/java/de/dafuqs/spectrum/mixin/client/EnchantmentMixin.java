package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.enchantment.*;
import net.minecraft.registry.entry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Environment(EnvType.CLIENT)
@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	
	@ModifyReturnValue(method = "getName", at = @At("RETURN"))
	private static Text spectrum$obfuscateEnchantmentNames(Text original, RegistryEntry<Enchantment> enchantment, int level) {
		if (enchantment.getIdAsString().startsWith("spectrum")) {
			MutableText text = original.copy();
			if (SpectrumEnchantmentHelper.canEntityUse(MinecraftClient.getInstance().player, enchantment.getIdAsString())) {
				return text;
			}
			if (SpectrumCommon.CONFIG.NameForUnrevealedEnchantments.isBlank() && text instanceof MutableText mutableText) {
				return mutableText.formatted(Formatting.byCode('k'));
			}
			else {
				return Text.literal(SpectrumCommon.CONFIG.NameForUnrevealedEnchantments).setStyle(text.getStyle());
			}
		}
		return original;
	}
	
}
