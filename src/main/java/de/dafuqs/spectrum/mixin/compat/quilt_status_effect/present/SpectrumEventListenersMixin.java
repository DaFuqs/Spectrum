package de.dafuqs.spectrum.mixin.compat.quilt_status_effect.present;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.api.status_effect.Incurable;
import de.dafuqs.spectrum.mixin.accessors.StatusEffectInstanceAccessor;
import de.dafuqs.spectrum.registries.SpectrumEventListeners;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Mixin(value = SpectrumEventListeners.class, remap = false)
public class SpectrumEventListenersMixin {
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "register", at = @At("TAIL"))
    private static void spectrum$registerQuiltEvents(CallbackInfo ci) {
        try {
            Method triStateValueOf = Class.forName("org.quiltmc.qsl.base.api.util.TriState").getMethod("valueOf", String.class);

            Class<?> shouldRemoveClass = Class.forName("org.quiltmc.qsl.entity.effect.api.StatusEffectEvents$ShouldRemove");

            Object shouldRemoveProxy = Proxy.newProxyInstance(
                    SpectrumEventListeners.class.getClassLoader(),
                    new Class[] { shouldRemoveClass },
                    (proxy, method, args) -> {
                        if (method.getName().equals("onRemoved")) {
                            LivingEntity entity = (LivingEntity) args[0];
                            StatusEffectInstance effect = (StatusEffectInstance) args[1];

                            if (Incurable.isIncurable(effect) && !affectedByImmunity(entity, effect.getAmplifier())) {
                                if (effect.getDuration() > 1200) {
                                    ((StatusEffectInstanceAccessor) effect).setDuration(effect.getDuration() - 1200);
                                    if (!entity.getWorld().isClient()) {
                                        ((ServerWorld) entity.getWorld()).getChunkManager().sendToNearbyPlayers(entity, new EntityStatusEffectS2CPacket(entity.getId(), effect));
                                    }
                                }
                                return triStateValueOf.invoke(null, "FALSE");
                            }
                            return triStateValueOf.invoke(null, "DEFAULT");
                        }

                        return proxy;
                    }
            );

            Field shouldRemoveField = Class.forName("org.quiltmc.qsl.entity.effect.api.StatusEffectEvents").getField("SHOULD_REMOVE");
            shouldRemoveField.getDeclaringClass().getMethod("register", shouldRemoveClass).invoke(shouldRemoveField.get(null), shouldRemoveClass.cast(shouldRemoveProxy));
        } catch (Exception e) {
            SpectrumCommon.LOGGER.warn("[Spectrum] Quilt integration failed to load", e);
        }
    }

    @Unique
    private static boolean affectedByImmunity(LivingEntity instance, int amplifier) {
        var immunity = instance.getStatusEffect(SpectrumStatusEffects.IMMUNITY);
        var cost = 1200 + 600 * amplifier;

        if (immunity != null && immunity.getDuration() >= cost) {
            ((StatusEffectInstanceAccessor) immunity).setDuration(Math.max(5, immunity.getDuration() - cost));
            if (!instance.getWorld().isClient()) {
                ((ServerWorld) instance.getWorld()).getChunkManager().sendToNearbyPlayers(instance, new EntityStatusEffectS2CPacket(instance.getId(), immunity));
            }
            return true;
        }
        return false;
    }
}
