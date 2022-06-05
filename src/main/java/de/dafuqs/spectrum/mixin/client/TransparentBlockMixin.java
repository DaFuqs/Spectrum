package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TransparentBlock;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TransparentBlock.class)
public abstract class TransparentBlockMixin {
	
	@Inject(method = "isSideInvisible", at = @At("HEAD"), cancellable = true)
	public void dontRenderVanillaPlayerOnlyGlass(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (this.equals(Blocks.GLASS) && stateFrom.getBlock().equals(SpectrumBlocks.VANILLA_PLAYER_ONLY_GLASS) ||
				this.equals(Blocks.TINTED_GLASS) && stateFrom.getBlock().equals(SpectrumBlocks.TINTED_PLAYER_ONLY_GLASS)) {
			callbackInfoReturnable.setReturnValue(true);
		}
	}
	
}