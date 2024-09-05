package de.dafuqs.spectrum.registries;

import net.minecraft.client.sound.*;
import net.minecraft.registry.*;
import net.minecraft.sound.*;

public class SpectrumMusicType extends MusicType {
	
	public static MusicSound DEEPER_DOWN;
	
	public static void register() {
		DEEPER_DOWN = new MusicSound(Registries.SOUND_EVENT.getEntry(SpectrumSoundEvents.DEEPER_DOWN), 12000, 24000, false);
	}
	
}
