package de.dafuqs.spectrum.registries;

import net.minecraft.client.sound.MusicType;
import net.minecraft.sound.MusicSound;

public class SpectrumMusicType extends MusicType {
	
	public static MusicSound SPECTRUM_THEME;
	public static MusicSound BOSS_THEME;
	public static MusicSound DEEPER_DOWN_THEME;
	
	public static void register() {
		SPECTRUM_THEME = new MusicSound(SpectrumSoundEvents.SPECTRUM_THEME, 6000, 24000, false);
		BOSS_THEME = new MusicSound(SpectrumSoundEvents.BOSS_THEME, 0, 0, true);
		DEEPER_DOWN_THEME = new MusicSound(SpectrumSoundEvents.DEEPER_DOWN_THEME, 6000, 24000, false);
	}
	
}
