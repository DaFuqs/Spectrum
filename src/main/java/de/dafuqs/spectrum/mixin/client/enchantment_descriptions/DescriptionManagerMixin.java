package de.dafuqs.spectrum.mixin.client.enchantment_descriptions;

import net.darkhax.enchdesc.DescriptionManager;
import org.spongepowered.asm.mixin.Mixin;

/**
 * If Enchantment descriptions is installed:
 * Obfuscate descriptions for Enchantments that the user does not have discovered yet
 */
@Mixin(DescriptionManager.class)
public class DescriptionManagerMixin {
	
	// TODO
	// breaks in prod
	/*private static final Language language = Language.getInstance();
	
	@Inject(at = @At("RETURN"), method= "Lnet/darkhax/enchdesc/DescriptionManager;getDescription(L;)L;", cancellable = true, remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void getDescription(Enchantment ench, CallbackInfoReturnable<Text> cir) {
		if(ench instanceof SpectrumEnchantment spectrumEnchantment) {
			if(!spectrumEnchantment.canEntityUse(MinecraftClient.getInstance().player)) {
				cir.setReturnValue(new LiteralText(language.get(((TranslatableText) cir.getReturnValue()).getKey())).formatted(Formatting.OBFUSCATED).formatted(Formatting.DARK_GRAY));
			}
		}
	}*/

}
