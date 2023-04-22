package de.dafuqs.spectrum.compat.reverb;

import de.dafuqs.reverb.*;
import de.dafuqs.reverb.sound.*;
import de.dafuqs.reverb.sound.reverb.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.deeper_down.*;
import net.fabricmc.api.*;
import net.minecraft.util.registry.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class DimensionReverb {

	public static void setup() {
		if (SpectrumCommon.CONFIG.DimensionReverbDecayTime > 0 || SpectrumCommon.CONFIG.DimensionReverbDensity > 0) {
			Registry.register(Reverb.SOUND_EFFECTS, DDDimension.DIMENSION_ID, new SoundEffects(
					Optional.of(new StaticReverbEffect.Builder()
							.setDecayTime(SpectrumCommon.CONFIG.DimensionReverbDecayTime)
							.setDensity(SpectrumCommon.CONFIG.DimensionReverbDensity).build()
					), Optional.empty(), Optional.empty()));
		}
	}

}
