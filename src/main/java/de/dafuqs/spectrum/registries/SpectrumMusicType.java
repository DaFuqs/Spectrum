package de.dafuqs.spectrum.registries;

import net.minecraft.client.sound.*;
import net.minecraft.registry.*;
import net.minecraft.sound.*;

public class SpectrumMusicType extends MusicType {
	
	public static MusicSound SPECTRUM_THEME;
	public static MusicSound DEEPER_DOWN_THEME;
	
	public static void register() {
		SPECTRUM_THEME = new MusicSound(Registries.SOUND_EVENT.getEntry(SpectrumSoundEvents.SPECTRUM_THEME), 6000, 24000, false);
		DEEPER_DOWN_THEME = new MusicSound(Registries.SOUND_EVENT.getEntry(SpectrumSoundEvents.DEEPER_DOWN_THEME), 6000, 24000, false);
	}
	
}
