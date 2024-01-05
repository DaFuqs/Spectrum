package de.dafuqs.spectrum.mixin;

import com.mojang.datafixers.util.Pair;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Explosion.class)
public class ExplosionMixin {
	
	@Shadow
	@Final
	private DamageSource damageSource;
	
	@Inject(method = "affectWorld(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractFireBlock;getState(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	private void spectrum$modifyExplosion(boolean particles, CallbackInfo ci) {
		if (this.damageSource.isOf(SpectrumDamageTypes.INCANDESCENCE)) {
			PrimordialFireBlock.EXPLOSION_CAUSES_PRIMORDIAL_FIRE_FLAG = true;
		}
	}
	
}
