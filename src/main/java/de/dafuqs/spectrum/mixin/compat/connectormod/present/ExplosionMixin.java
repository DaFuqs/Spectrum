package de.dafuqs.spectrum.mixin.compat.connectormod.present;


import de.dafuqs.spectrum.blocks.ExplosionAware;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow
    @Final
    private World world;

    @Inject(method = "affectWorld(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    public void applyExplosionEffects(boolean particles, CallbackInfo ci, boolean bl, ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList, boolean bl2, ObjectListIterator var5, BlockPos blockPos, BlockState blockState){
        if(blockState.getBlock() instanceof ExplosionAware explosionAware) {
            explosionAware.beforeDestroyedByExplosion(world, blockPos, blockState, (Explosion) (Object) this);
            this.world.setBlockState(blockPos, explosionAware.getStateForExplosion(this.world, blockPos, blockState), 3);
        }
    }


}
