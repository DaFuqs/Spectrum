package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(TransparentBlock.class)
public abstract class TransparentBlockMixin {
	
	@Inject(method = "isSideInvisible", at = @At("HEAD"), cancellable = true)
	public void dontRenderVanillaPlayerOnlyGlass(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (this.equals(Blocks.GLASS) && stateFrom.getBlock().equals(SpectrumBlocks.VANILLA_SEMI_PERMEABLE_GLASS) ||
				this.equals(Blocks.TINTED_GLASS) && stateFrom.getBlock().equals(SpectrumBlocks.TINTED_SEMI_PERMEABLE_GLASS)) {
			callbackInfoReturnable.setReturnValue(true);
		}
	}
	
}