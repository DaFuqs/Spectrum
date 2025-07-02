package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Explosion.class)
public class ExplosionMixin {
	
	@Shadow
	@Final
	private DamageSource damageSource;
	
	@Inject(method = "finalizeExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BaseFireBlock;getState(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
	private void spectrum$modifyExplosion(boolean particles, CallbackInfo ci) {
		if (this.damageSource.is(SpectrumDamageTypes.INCANDESCENCE)) {
			PrimordialFireBlock.EXPLOSION_CAUSES_PRIMORDIAL_FIRE_FLAG = true;
		}
	}
	
}
