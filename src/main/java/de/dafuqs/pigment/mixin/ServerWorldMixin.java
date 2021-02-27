package de.dafuqs.pigment.mixin;

import de.dafuqs.pigment.blocks.gravity.GravityBlockEntity;
import de.dafuqs.pigment.registries.PigmentEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Shadow
    private int idleTimeout;

    @Inject(at = @At(value = "RETURN"), method = "tick")
    void postEntityTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        if (this.idleTimeout < 300) {
            List<? extends GravityBlockEntity> list2 = ((ServerWorld)(Object) this).getEntitiesByType(PigmentEntityTypes.GRAVITY_BLOCK, Entity::isAlive);

            for (GravityBlockEntity entry : list2) {
                entry.postTickEntities();
            }
        }
    }
}