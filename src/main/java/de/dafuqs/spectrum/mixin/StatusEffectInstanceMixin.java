package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import de.dafuqs.spectrum.api.status_effect.Incurable;
import de.dafuqs.spectrum.mixin.accessors.StatusEffectInstanceAccessor;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin implements StatusEffectInstanceAccessor, Incurable {

    @Unique
    private boolean incurable;

    @Inject(method = "copyFrom", at = @At("HEAD"))
    public void writeIncurable(StatusEffectInstance that, CallbackInfo ci) {
        ((Incurable) this).spectrum$setIncurable( ((Incurable) that).spectrum$isIncurable());
    }

    @Inject(method = "writeTypelessNbt", at = @At("HEAD"))
    public void writeIncurable(NbtCompound nbt, CallbackInfo ci) {
        if (incurable)
            nbt.putBoolean("Incurable", incurable);
    }

    @ModifyReturnValue(method = "fromNbt(Lnet/minecraft/entity/effect/StatusEffect;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/effect/StatusEffectInstance;", at = @At("RETURN"))
    private static StatusEffectInstance readIncurable(StatusEffectInstance original, @Local(argsOnly = true) NbtCompound nbt) {
        if (nbt.contains("Incurable") && nbt.getBoolean("Incurable"))
            ((Incurable) original).spectrum$setIncurable(true);
        return original;
    }

    @Inject(method = "upgrade", at = @At("RETURN"))
    private void readIncurable(StatusEffectInstance that, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) LocalBooleanRef changed) {
        if (((Incurable) this).spectrum$isIncurable() != ((Incurable) that).spectrum$isIncurable()) {
            ((Incurable) this).spectrum$setIncurable(((Incurable) that).spectrum$isIncurable());
            changed.set(true);
        }
    }

    @Override
    public void spectrum$setIncurable(boolean incurable) {
        this.incurable = incurable;
    }

    @Override
    public boolean spectrum$isIncurable() {
        return incurable;
    }
}
