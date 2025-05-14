package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Level.class)
public abstract class WorldMixin {
	
	@Shadow
	@Final
	private BiomeManager biomeManager;
	
	// using a mixin additional to net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents,
	// since the fabric api event does not trigger for indirect breaks, like via projectile
	@Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"))
	public void breakBlock(BlockPos pos, boolean drop, Entity breakingEntity, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir, @Local BlockState state) {
		if (breakingEntity instanceof ServerPlayer serverPlayerEntity) {
			SpectrumAdvancementCriteria.BLOCK_BROKEN.trigger(serverPlayerEntity, state);
		}
	}
	
	@Inject(method = "isRainingAt", at = @At("HEAD"), cancellable = true)
	public void forcePermanentRain(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		var biome = biomeManager.getBiome(pos);
		if (biome.is(SpectrumBiomes.DEEP_DRIPSTONE_CAVES) || biome.is(SpectrumBiomes.DRAGONROT_SWAMP))
			cir.setReturnValue(true);
	}
}
