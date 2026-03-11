package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public class MobEffectHelper {
	
	private static final float ADDITIONAL_EFFECT_DURATION_MODIFIER_PER_LEVEL = 0.25F;
	
	public static boolean canBeExtended(Holder<MobEffect> statusEffect) {
		return !statusEffect.is(SpectrumMobEffectTags.NO_DURATION_EXTENSION);
	}
	
	public static int getExtendedDuration(int originalDuration, int prolongingAmplifier) {
		return (int) (originalDuration * (1 + ADDITIONAL_EFFECT_DURATION_MODIFIER_PER_LEVEL * (1 + prolongingAmplifier)));
	}

    public static void clearRandomEffect(@NotNull LivingEntity entity, Predicate<MobEffectInstance> effectPredicate) {
        Collection<MobEffectInstance> currentEffects = entity.getActiveEffects();
        List<MobEffectInstance> toRemove = new ArrayList<>();
        for (MobEffectInstance instance : currentEffects) {
            if (effectPredicate.test(instance)) {
                toRemove.add(instance);
            }
        }
        
        if (toRemove.isEmpty()) {
            return;
        }
        
        Level world = entity.level();
        int randomIndex = world.random.nextInt(toRemove.size());
        entity.removeEffect(toRemove.get(randomIndex).getEffect());
    }

    public static void clearEffects(@NotNull LivingEntity entity, Predicate<MobEffectInstance> effectPredicate) {
        Set<Holder<MobEffect>> effectsToRemove = new HashSet<>();
        for (MobEffectInstance instance : entity.getActiveEffects()) {
            if (effectPredicate.test(instance)) {
                effectsToRemove.add(instance.getEffect());
            }
        }
        
        for (Holder<MobEffect> effect : effectsToRemove) {
            entity.removeEffect(effect);
        }
    }

    public static void shortenEffects(@NotNull LivingEntity entity, Predicate<MobEffectInstance> effectPredicate) {
        for (MobEffectInstance instance : entity.getActiveEffects()) {
            if (effectPredicate.test(instance)) {
				shortenEffect(entity, instance);
            }
        }
    }
	
	public static void shortenEffect(LivingEntity livingEntity, MobEffectInstance instance) {
		// new duration = duration - 1min OR duration * 0.4, whichever is the smaller reduction
		int duration = instance.getDuration();
		((StatusEffectInstanceAccessor) instance).setDuration(Math.max(duration - 1200, (int) (duration * 0.4)));
		if (livingEntity.level() instanceof ServerLevel serverWorld) {
			serverWorld.getChunkSource().broadcastAndSend(livingEntity, new ClientboundUpdateMobEffectPacket(livingEntity.getId(), instance, false));
		}
	}

    public enum RenderType {
		GUI_LARGE,
		GUI_SMALL,
		HUD_DEFAULT,
		HUD_AMBIENT
	}
	
	public record StatusEffectBackground(ResourceLocation guiLarge, ResourceLocation guiSmall, ResourceLocation hudDefault, ResourceLocation hudAmbient) {
		
		public StatusEffectBackground(String name) {
			this(SpectrumCommon.locate("container/inventory/" + name + "_effect_background_gui_large"),
					SpectrumCommon.locate("container/inventory/" + name + "_effect_background_gui_small"),
					SpectrumCommon.locate("hud/" + name + "_effect_background_hud_default"),
					SpectrumCommon.locate("hud/" + name + "_effect_background_hud_ambient"));
		}
		
		public ResourceLocation get(RenderType type) {
			return switch (type) {
				case GUI_LARGE -> guiLarge;
				case GUI_SMALL -> guiSmall;
				case HUD_DEFAULT -> hudDefault;
				case HUD_AMBIENT -> hudAmbient;
			};
		}
	}
	
	private static final StatusEffectBackground DIVINITY = new StatusEffectBackground("divinity");
	private static final StatusEffectBackground SOPORIFIC = new StatusEffectBackground("soporific");
	private static final StatusEffectBackground NIGHT_ALCHEMY = new StatusEffectBackground("night_alchemy");
	
	public static ResourceLocation getTextureLocation(ResourceLocation original, MobEffectInstance effect, RenderType renderType) {
		var type = effect.getEffect();
		
		if (type == SpectrumMobEffects.DIVINITY)
			return DIVINITY.get(renderType);
		
		if (type.is(SpectrumMobEffectTags.NIGHT_ALCHEMY))
			return NIGHT_ALCHEMY.get(renderType);
		
		if (type.is(SpectrumMobEffectTags.SOPORIFIC)) {
			return SOPORIFIC.get(renderType);
		}
		
		return original;
	}
	
}
