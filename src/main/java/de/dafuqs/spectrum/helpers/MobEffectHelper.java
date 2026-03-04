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

public class MobEffectHelper {
	
	private static final float ADDITIONAL_EFFECT_DURATION_MODIFIER_PER_LEVEL = 0.25F;
	
	public static boolean canBeExtended(Holder<MobEffect> statusEffect) {
		return !statusEffect.is(SpectrumMobEffectTags.NO_DURATION_EXTENSION);
	}
	
	public static int getExtendedDuration(int originalDuration, int prolongingAmplifier) {
		return (int) (originalDuration * (1 + ADDITIONAL_EFFECT_DURATION_MODIFIER_PER_LEVEL * (1 + prolongingAmplifier)));
	}
	
	public static boolean isStrongSleepEffect(MobEffectInstance instance) {
		return instance.getEffect() == SpectrumMobEffects.ETERNAL_SLUMBER || instance.getEffect() == SpectrumMobEffects.FATAL_SLUMBER;
	}
	
	public static boolean isStrongSleepEffect(InkPoweredStatusEffectInstance instance) {
		return isStrongSleepEffect(instance.getStatusEffectInstance());
	}
	
	public static void cutDuration(LivingEntity instance, MobEffectInstance effect) {
		// new duration = duration - 1min OR duration * 0.4, whichever is the smaller reduction
		int duration = effect.getDuration();
		((StatusEffectInstanceAccessor) effect).setDuration(Math.max(duration - 1200, (int) (duration * 0.4)));
		if (instance.level() instanceof ServerLevel serverWorld) {
			serverWorld.getChunkSource().broadcastAndSend(instance, new ClientboundUpdateMobEffectPacket(instance.getId(), effect, false));
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
	private static final StatusEffectBackground SEVERE = new StatusEffectBackground("severe");
	private static final StatusEffectBackground NIGHT_ALCHEMY = new StatusEffectBackground("night_alchemy");
	
	public static ResourceLocation getTextureLocation(ResourceLocation original, MobEffectInstance effect, RenderType renderType) {
		var type = effect.getEffect();
		
		if (type == SpectrumMobEffects.DIVINITY)
			return DIVINITY.get(renderType);

		if (isSevere(effect) && type != SpectrumMobEffects.ETERNAL_SLUMBER && type != SpectrumMobEffects.FATAL_SLUMBER) {
			return SEVERE.get(renderType);
		}
		
		if (type.is(SpectrumMobEffectTags.NIGHT_ALCHEMY))
			return NIGHT_ALCHEMY.get(renderType);
		
		return original;
	}
	
	public static boolean isSevere(MobEffectInstance instance) {
		var type = instance.getEffect();
		if (type.is(SpectrumMobEffectTags.CANNOT_BE_SEVERE))
			return false;
		return instance.spectrum$isSevere();
	}
	
}
