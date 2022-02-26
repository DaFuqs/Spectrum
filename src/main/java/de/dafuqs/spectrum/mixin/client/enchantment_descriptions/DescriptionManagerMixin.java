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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * If Enchantment descriptions is installed:
 * Obfuscate descriptions for Enchantments that the user does not have discovered yet
 */
@Mixin(DescriptionManager.class)
public class DescriptionManagerMixin {
	
	/*private static final Language language = Language.getInstance();

	// TODO
	@Inject(at = @At("RETURN"), method= "Lnet/darkhax/enchdesc/DescriptionManager;getDescription(L;)L;", cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void getDescription(Enchantment ench, CallbackInfoReturnable<Text> cir) {
		if(ench instanceof SpectrumEnchantment spectrumEnchantment) {
			if(!spectrumEnchantment.canEntityUse(MinecraftClient.getInstance().player)) {
				cir.setReturnValue(new LiteralText(language.get(((TranslatableText) cir.getReturnValue()).getKey())).formatted(Formatting.OBFUSCATED).formatted(Formatting.DARK_GRAY));
			}
		}
	}*/

}
