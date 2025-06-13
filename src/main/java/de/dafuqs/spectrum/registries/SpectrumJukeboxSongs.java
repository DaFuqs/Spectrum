package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;

@SuppressWarnings("unused")
public class SpectrumJukeboxSongs {
	
	public static final ResourceKey<JukeboxSong> CREDITS = of("credits");
	public static final ResourceKey<JukeboxSong> DISCOVERY = of("discovery");
	public static final ResourceKey<JukeboxSong> DIVINITY = of("divinity");
	public static final ResourceKey<JukeboxSong> MUSIC_LE_FISHE_AU_CHOCOLAT = of("le_fishe_au_chocolat");
	
	private static ResourceKey<JukeboxSong> of(String id) {
		return ResourceKey.create(Registries.JUKEBOX_SONG, SpectrumCommon.locate(id));
    }

}
