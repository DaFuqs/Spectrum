package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.api.distmarker.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@OnlyIn(Dist.CLIENT)
@Mixin(HalfTransparentBlock.class)
public abstract class TranslucentBlockMixin {
	
	@Inject(method = "skipRendering", at = @At("HEAD"), cancellable = true)
	public void spectrum$dontRenderVanillaPlayerOnlyGlass(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (state.is(Blocks.GLASS) && stateFrom.is(SpectrumBlocks.SEMI_PERMEABLE_GLASS)
				|| state.is(Blocks.TINTED_GLASS) && stateFrom.is(SpectrumBlocks.TINTED_SEMI_PERMEABLE_GLASS))
			callbackInfoReturnable.setReturnValue(true);
	}
	
}