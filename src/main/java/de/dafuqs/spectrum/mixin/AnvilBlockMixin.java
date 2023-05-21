package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(AnvilBlock.class)
public abstract class AnvilBlockMixin {
	
	@Inject(at = @At("HEAD"), method = "getLandingState(Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;", cancellable = true)
	private static void makeBedrockAnvilUnbreakable(BlockState fallingState, CallbackInfoReturnable<BlockState> callbackInfoReturnable) {
		if (fallingState.isOf(SpectrumBlocks.BEDROCK_ANVIL)) {
			callbackInfoReturnable.setReturnValue(fallingState);
		}
	}
}
