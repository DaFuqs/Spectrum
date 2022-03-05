package de.dafuqs.spectrum.mixin.client.enchantment_descriptions;

import de.dafuqs.spectrum.enchantments.SpectrumEnchantment;
import net.darkhax.enchdesc.DescriptionManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * If Enchantment descriptions is installed:
 * Obfuscate descriptions for Enchantments that the user does not have discovered yet
 */
@Pseudo
@Mixin(value = DescriptionManager.class, remap = false)
public class DescriptionManagerMixin {
/*
	private static final Language spectrum$language = Language.getInstance();
	
	@Inject(at = @At("RETURN"), method= "Lnet/darkhax/enchdesc/DescriptionManager;getDescription(L;)L;", cancellable = true)
	private static void spectrum$obfuscateEnchDescDescription(Enchantment ench, CallbackInfoReturnable<Text> cir) {
		if(ench instanceof SpectrumEnchantment spectrumEnchantment) {
			if(!spectrumEnchantment.canEntityUse(MinecraftClient.getInstance().player)) {
				cir.setReturnValue(new LiteralText(spectrum$language.get(((TranslatableText) cir.getReturnValue()).getKey())).formatted(Formatting.OBFUSCATED).formatted(Formatting.DARK_GRAY));
			}
		}
	}
*/
}
