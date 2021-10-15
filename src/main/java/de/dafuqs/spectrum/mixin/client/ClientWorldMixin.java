package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.entity.entity.GravityBlockEntity;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

    @Shadow public abstract Iterable<Entity> getEntities();

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;tickBlockEntities()V"), method = "tickEntities")
    void postEntityTick(CallbackInfo ci) {
        for (Entity entry : getEntities()) {
            if (entry instanceof GravityBlockEntity entity) {
                entity.postTickEntities();
            }
            if (entry instanceof GravityBlockEntity.PostTicker entity) {
                entity.postTick();
            }
        }
    }

}