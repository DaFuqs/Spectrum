package de.dafuqs.spectrum.mixin.compat.connectormod.absent;

import de.dafuqs.spectrum.api.block.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow
    @Final
    private World world;
    @ModifyArg(method = "affectWorld(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"), index = 1)
    private BlockState spectrum$modifyExplosion(BlockPos pos, BlockState state, int flags) {
        BlockState stateAtPos = world.getBlockState(pos);
        if(stateAtPos.getBlock() instanceof ExplosionAware explosionAware) {
            explosionAware.beforeDestroyedByExplosion(world, pos, stateAtPos, (Explosion) (Object) this);
            return explosionAware.getStateForExplosion(this.world, pos, stateAtPos);
        }
        return state;
    }
}
