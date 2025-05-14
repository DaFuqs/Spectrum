package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.effect.*;

public class StatusEffectHelper {
	
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
	private static final StatusEffectBackground INCURABLE = new StatusEffectBackground("incurable");
	private static final StatusEffectBackground NIGHT_ALCHEMY = new StatusEffectBackground("night_alchemy");
	
	public static ResourceLocation getTextureLocation(ResourceLocation original, MobEffectInstance effect, RenderType renderType) {
		var type = effect.getEffect();
		
		if (type == SpectrumStatusEffects.DIVINITY)
			return DIVINITY.get(renderType);
		
		if (isIncurable(effect) && type != SpectrumStatusEffects.ETERNAL_SLUMBER && type != SpectrumStatusEffects.FATAL_SLUMBER) {
			return INCURABLE.get(renderType);
		}
		
		if (type.is(SpectrumStatusEffectTags.NIGHT_ALCHEMY))
			return NIGHT_ALCHEMY.get(renderType);
		
		return original;
	}
	
	//TODO this needs a better name. What even is this.
	//Also why is that not a tag?
	public static boolean isIncurable(MobEffectInstance instance) {
		var type = instance.getEffect();
		if (type == SpectrumStatusEffects.ETERNAL_SLUMBER || type == SpectrumStatusEffects.FATAL_SLUMBER)
			return false;
		
		return instance.spectrum$isIncurable();
	}
}
