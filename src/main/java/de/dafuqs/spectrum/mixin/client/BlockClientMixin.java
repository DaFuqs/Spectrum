package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.progression.ClientBlockCloaker;
import net.minecraft.block.Block;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockClientMixin {
	
	@Inject(method = "getName()Lnet/minecraft/text/MutableText;", at = @At("RETURN"), cancellable = true)
	private void getCloakedName(CallbackInfoReturnable<MutableText> callbackInfoReturnable) {
		Block thisBlock = (Block) (Object) this;
		if (ClientBlockCloaker.isCloaked(thisBlock)) {
			// Get the localized name of the block and scatter it using §k to make it unreadable
			Language language = Language.getInstance();
			LiteralText newText = new LiteralText("§k" + language.get(thisBlock.getTranslationKey()));
			
			callbackInfoReturnable.setReturnValue(newText);
		}
	}
	
}
