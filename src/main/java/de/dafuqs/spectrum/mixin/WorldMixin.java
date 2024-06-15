package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.progression.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.server.network.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(World.class)
public abstract class WorldMixin {
	
	// using a mixin additional to net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents,
	// since the fabric api event does not trigger for indirect breaks, like via projectile
	@Inject(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void breakBlock(BlockPos pos, boolean drop, Entity breakingEntity, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir, BlockState state) {
		if (breakingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumAdvancementCriteria.BLOCK_BROKEN.trigger(serverPlayerEntity, state);
		}
	}
	
}
