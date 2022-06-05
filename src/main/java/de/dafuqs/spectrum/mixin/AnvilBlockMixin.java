package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
	
	@Inject(at = @At("HEAD"), method = "getLandingState(Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;", cancellable = true)
	private static void makeBedrockAnvilUnbreakable(BlockState fallingState, CallbackInfoReturnable<BlockState> callbackInfoReturnable) {
		if (fallingState.isOf(SpectrumBlocks.BEDROCK_ANVIL)) {
			callbackInfoReturnable.setReturnValue(fallingState);
		}
	}
}
