package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.mixin.accessors.LivingEntityAccessor;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.network.*;
import net.minecraft.entity.damage.DamageSource;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @WrapWithCondition(method = "updateHealth", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hurtTime*:I", opcode = Opcodes.PUTFIELD))
    private boolean shouldTiltScreen(ClientPlayerEntity player, int newValue) {
        System.out.println(((LivingEntityAccessor) player).getLastDamageSource());
        return ((LivingEntityAccessor) player).getLastDamageSource() == null
                || !((LivingEntityAccessor) player).getLastDamageSource()
                        .isIn(SpectrumDamageTypeTags.USES_SET_HEALTH);
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void setDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        ((LivingEntityAccessor) this).setLastDamageSource(source);
        return;
    }
}
