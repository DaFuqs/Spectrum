package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.util.*;
import org.lwjgl.openal.*;

public class DimensionReverb {
	
	public static void setup() {

	}
	
	public static class SourceEffects {
		
		public static int effect = -1;
		public static int filter = -1;
		public static int slot = -1;
		
		public static void updateSlots() {
			try {
				effect = EXTEfx.alGenEffects();
				filter = EXTEfx.alGenFilters();
				slot = EXTEfx.alGenAuxiliaryEffectSlots();
			} catch (Throwable t) {
				SpectrumCommon.LOGGER.warn("Error updating dimension reverb. No audio devices present?");
			}
		}
		
		public static void tick(SoundInstance soundInstance, int sourceID) {
			Minecraft client = Minecraft.getInstance();
			
			ClientLevel level = client.level;
			if (level == null) {
				return;
			}
			if (level.dimension().location() != SpectrumDimensionKeys.DIMENSION_ID) {
				return;
			}
			
			float reverbTime = SpectrumConfig.CONFIG.DimensionReverbDecayTime.get().floatValue();
			float reverbDensity = SpectrumConfig.CONFIG.DimensionReverbDensity.get().floatValue();
			if (!(reverbTime > 0) && !(reverbDensity > 0)) {
				return;
			}
			
			String soundInstanceIdPath = soundInstance.getLocation().getPath();
			if (soundInstanceIdPath.contains("ui.") ||
					soundInstanceIdPath.contains("music.") ||
					soundInstanceIdPath.contains("block.lava.pop") ||
					soundInstanceIdPath.contains("weather.") ||
					soundInstanceIdPath.startsWith("atmosfera") ||
					soundInstanceIdPath.startsWith("dynmus")) {
				return;
			}
			
			for (int i = 0; i < 2; i++) {
				AL11.alSourcei(sourceID, EXTEfx.AL_DIRECT_FILTER, 0);
				
				if (effect == -1 || filter == -1 || slot == -1) {
					updateSlots();
				}
				
				EXTEfx.alAuxiliaryEffectSlotf(slot, EXTEfx.AL_EFFECTSLOT_GAIN, 0);
				EXTEfx.alEffecti(effect, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_REVERB);
				EXTEfx.alEffectf(effect, EXTEfx.AL_REVERB_DECAY_TIME, Mth.clamp(reverbTime, EXTEfx.AL_REVERB_MIN_DECAY_TIME, EXTEfx.AL_REVERB_MAX_DECAY_TIME));
				EXTEfx.alEffectf(effect, EXTEfx.AL_REVERB_DENSITY, Mth.clamp(reverbDensity, EXTEfx.AL_REVERB_MIN_DENSITY, EXTEfx.AL_REVERB_MAX_DENSITY));
				
				EXTEfx.alAuxiliaryEffectSlotf(slot, EXTEfx.AL_EFFECTSLOT_GAIN, 0);
				EXTEfx.alAuxiliaryEffectSloti(slot, EXTEfx.AL_EFFECTSLOT_EFFECT, effect);
				EXTEfx.alAuxiliaryEffectSlotf(slot, EXTEfx.AL_EFFECTSLOT_GAIN, 1);
				
				EXTEfx.alAuxiliaryEffectSloti(slot, EXTEfx.AL_EFFECTSLOT_EFFECT, effect);
				EXTEfx.alAuxiliaryEffectSlotf(slot, EXTEfx.AL_EFFECTSLOT_GAIN, 1);
				AL11.alSourcei(sourceID, EXTEfx.AL_DIRECT_FILTER, filter);
				AL11.alSource3i(sourceID, EXTEfx.AL_AUXILIARY_SEND_FILTER, slot, 0, 0);
				
				int error = AL11.alGetError();
				if (error != AL11.AL_NO_ERROR) {
					SpectrumCommon.LOGGER.error("OpenAl Error {}", error);
				}
			}
		}
		
	}
	
}
