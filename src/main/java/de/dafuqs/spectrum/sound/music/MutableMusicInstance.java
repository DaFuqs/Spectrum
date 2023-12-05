package de.dafuqs.spectrum.sound.music;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.mixin.client.accessors.SoundLoaderAccessor;
import net.fabricmc.fabric.api.client.sound.v1.FabricSoundInstance;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.AudioStream;
import net.minecraft.client.sound.SoundLoader;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class MutableMusicInstance extends AbstractSoundInstance implements FabricSoundInstance {

    private final Identifier location;
    private Optional<DynamicAudioStream> cachedAudioStream;

    protected MutableMusicInstance(SoundEvent sound, SoundCategory category, Random random) {
        super(sound, category, random);
        var soundId = sound.getId();
        location = new Identifier(soundId.getNamespace(), "sounds/" + soundId.getPath().replace(".", "/") + ".ogg");
        relative = true;
    }
    @Override
    public CompletableFuture<AudioStream> getAudioStream(SoundLoader loader, Identifier id, boolean repeatInstantly) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var stream = SpectrumCommon.resourceLoader.getResourceFactory().open(location);
                var dynamicStream = new DynamicAudioStream(stream);
                cachedAudioStream = Optional.of(dynamicStream);
                return dynamicStream;
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, Util.getMainWorkerExecutor());
    }

    public Optional<DynamicAudioStream> getCachedAudioStream() {
        return cachedAudioStream;
    }
}
