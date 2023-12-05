package de.dafuqs.spectrum.sound.music;

import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class SpectrumMusicManager {

    private static SpectrumMusicManager manager;
    private final MinecraftClient client;
    private Optional<SoundInstance> activeMusic = Optional.empty();

    private SpectrumMusicManager(MinecraftClient client) {
        this.client = client;
    }

    public void tick() {

        if (activeMusic.isEmpty())
            return;

        var musicSound = (MutableMusicInstance) activeMusic.get();
        var cachedStream = musicSound.getCachedAudioStream();

        if (cachedStream.isPresent()) {
            var stream = cachedStream.get();
            //client.getSoundManager().
        }


    }

    public void start() {
        var sound = new MutableMusicInstance(SpectrumSoundEvents.TEST_MUSIC, SoundCategory.MUSIC, SoundInstance.createRandom());
        activeMusic = Optional.of(sound);
        client.getSoundManager().play(sound);
    }

    public static void create(MinecraftClient client) {
        manager = new SpectrumMusicManager(client);
    }

    public static SpectrumMusicManager getInstance() {
        return manager;
    }
}
