package de.dafuqs.spectrum.mixin.client.enchantment_descriptions;

import net.darkhax.enchdesc.DescriptionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * If Enchantment descriptions is installed:
 * Obfuscate descriptions for Enchantments that the user does not have discovered yet
 */
@Pseudo
@Mixin(value = DescriptionManager.class, remap = false)
public class DescriptionManagerMixin {

	/*private static final Language spectrum$language = Language.getInstance();
	
	@Inject(at = @At("HEAD"), method= "Lnet/darkhax/enchdesc/DescriptionManager;getDescription(L;)L;", cancellable = true)
	private void spectrum$obfuscateEnchDescDescription(Enchantment ench, CallbackInfoReturnable<MutableText> cir) {
		if(ench instanceof SpectrumEnchantment spectrumEnchantment) {
			if(!spectrumEnchantment.canEntityUse(MinecraftClient.getInstance().player)) {
				cir.setReturnValue(new LiteralText(spectrum$language.get(((TranslatableText) cir.getReturnValue()).getKey())).formatted(Formatting.OBFUSCATED).formatted(Formatting.DARK_GRAY));
			}
		}
	}*/
	
}
