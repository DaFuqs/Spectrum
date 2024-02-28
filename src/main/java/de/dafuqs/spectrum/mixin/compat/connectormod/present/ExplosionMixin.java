package de.dafuqs.spectrum.mixin.compat.connectormod.present;


import de.dafuqs.spectrum.api.block.*;
import net.minecraft.block.*;
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
