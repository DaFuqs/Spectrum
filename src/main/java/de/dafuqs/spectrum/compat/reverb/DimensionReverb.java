package de.dafuqs.spectrum.compat.reverb;

import de.dafuqs.reverb.*;
import de.dafuqs.reverb.sound.*;
import de.dafuqs.reverb.sound.reverb.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;

import java.util.*;

public class DimensionReverb {
	
	public static void setup() {
		if (SpectrumConfig.CONFIG.DimensionReverbDecayTime.get() > 0 || SpectrumConfig.CONFIG.DimensionReverbDensity.get() > 0) {
			// TODO: port
			/*Registry.register(Reverb.SOUND_EFFECTS, SpectrumDimensions.DIMENSION_ID, new SoundEffects(
					Optional.of(new StaticReverbEffect.Builder()
							.setDecayTime(SpectrumConfig.CONFIG.DimensionReverbDecayTime)
							.setDensity(SpectrumConfig.CONFIG.DimensionReverbDensity).build()
					), Optional.empty(), Optional.empty()));*/
		}
	}
	
}
