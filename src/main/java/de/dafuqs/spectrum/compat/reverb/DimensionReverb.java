package de.dafuqs.spectrum.compat.reverb;

import de.dafuqs.reverb.*;
import de.dafuqs.reverb.sound.*;
import de.dafuqs.reverb.sound.reverb.*;
import de.dafuqs.spectrum.deeper_down.*;
import net.fabricmc.api.*;
import net.minecraft.util.registry.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class DimensionReverb {

	public static void setup() {
		Registry.register(Reverb.SOUND_EFFECTS, DDDimension.DEEPER_DOWN_DIMENSION_ID, new SoundEffects(
				Optional.of(new StaticReverbEffect.Builder().setDecayTime(8.0F).setDensity(0.5F).build()),
				Optional.empty(),
				Optional.empty()));
	}

}
