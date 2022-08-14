package de.dafuqs.spectrum.items;

import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;

public class SpectrumMusicDiscItem extends MusicDiscItem {

	public SpectrumMusicDiscItem(int comparatorOutput, SoundEvent sound, Settings settings, Integer length) {
		super(comparatorOutput, sound, settings, length);
	}
	
}