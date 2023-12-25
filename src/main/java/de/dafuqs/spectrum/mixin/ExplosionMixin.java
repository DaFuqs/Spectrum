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
	
	@Shadow
	@Final
	private World world;
	
	@Inject(method = "affectWorld(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractFireBlock;getState(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	private void spectrum$modifyExplosion(boolean particles, CallbackInfo ci) {
		if (this.damageSource.isOf(SpectrumDamageTypes.INCANDESCENCE)) {
			PrimordialFireBlock.EXPLOSION_CAUSES_PRIMORDIAL_FIRE_FLAG = true;
		}
	}

	@Inject(method = "affectWorld(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onDestroyedByExplosion(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/explosion/Explosion;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void spectrum$modifyExplosion(boolean particles, CallbackInfo ci, boolean bl, ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList, boolean bl2, ObjectListIterator var5, BlockPos blockPos, BlockState blockState) {
		if(blockState.getBlock() instanceof ExplosionAware explosionAware) {
			explosionAware.beforeDestroyedByExplosion(world, blockPos, blockState, (Explosion) (Object) this);
			this.world.setBlockState(blockPos, explosionAware.getStateForExplosion(this.world, blockPos, blockState), 3);
		}
		this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
	}
	
}
