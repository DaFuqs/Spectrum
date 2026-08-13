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
	
	@Inject(method = "isRainingAt", at = @At("HEAD"), cancellable = true)
	public void forcePermanentRain(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		var biome = biomeManager.getBiome(pos);
		if (biome.is(SpectrumBiomeKeys.DEEP_DRIPSTONE_CAVES) || biome.is(SpectrumBiomeKeys.DRAGONROT_SWAMP))
			cir.setReturnValue(true);
	}
}
