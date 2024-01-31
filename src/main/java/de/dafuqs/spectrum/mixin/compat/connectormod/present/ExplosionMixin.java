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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow
    @Final
    private World world;

    @Unique
    private BlockState blockState;
    @Unique
    private BlockPos blockPos;


    @ModifyVariable(method = "affectWorld(Z)V", at = @At(value = "STORE"), ordinal = 0)
    public BlockState snagBlockState(BlockState state){
        return blockState = state;
    }

    @ModifyVariable(method = "affectWorld(Z)V", at = @At(value = "STORE"), ordinal = 0)
    public BlockPos snagBlockPos(BlockPos pos){
        return blockPos = pos;
    }

    @Inject(method = "affectWorld(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", shift = At.Shift.BEFORE))
    public void applyExplosionEffects(boolean particles, CallbackInfo ci){
        if(blockState.getBlock() instanceof ExplosionAware explosionAware) {
            explosionAware.beforeDestroyedByExplosion(world, blockPos, blockState, (Explosion) (Object) this);
            this.world.setBlockState(blockPos, explosionAware.getStateForExplosion(this.world, blockPos, blockState), 3);
        }
    }


}
