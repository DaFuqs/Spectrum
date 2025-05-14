package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(AnvilBlock.class)
public abstract class AnvilBlockMixin {
	
	@Inject(at = @At("HEAD"), method = "damage", cancellable = true)
	private static void makeBedrockAnvilUnbreakable(BlockState fallingState, CallbackInfoReturnable<BlockState> callbackInfoReturnable) {
		if (fallingState.is(SpectrumBlocks.BEDROCK_ANVIL)) {
			callbackInfoReturnable.setReturnValue(fallingState);
		}
	}
}
