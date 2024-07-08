package de.dafuqs.spectrum.sound.music;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.mixin.client.accessors.SoundManagerAccessor;
import de.dafuqs.spectrum.mixin.client.accessors.SoundSystemAccessor;
import de.dafuqs.spectrum.mixin.client.accessors.SourceManagerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Predicate;

public class ControlledAudioInstance extends AbstractSoundInstance implements TickableSoundInstance {

    private final Identifier location;
    private final SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
    @NotNull
    private Optional<CachedAudioStream> cachedAudioStream = Optional.empty();

    protected ControlledAudioInstance(SoundEvent sound, SoundCategory category, Random random) {
        super(sound, category, random);
        var soundId = sound.getId();
        location = new Identifier(soundId.getNamespace(), "sounds/" + soundId.getPath().replace(".", "/") + ".ogg");
        relative = true;
    }

    //@Override
    //public CompletableFuture<AudioStream> getAudioStream(SoundLoader loader, Identifier id, boolean repeatInstantly) {
    //    return CompletableFuture.supplyAsync(() -> {
    //        try {
    //            var stream = SpectrumCommon.resourceLoader.getResourceFactory().open(location);
    //            var dynamicStream = new CachedAudioStream(stream);
    //            cachedAudioStream = Optional.of(dynamicStream);
    //            return dynamicStream;
    //        } catch (IOException e) {
    //            throw new CompletionException(e);
    //        }
    //    }, Util.getMainWorkerExecutor());
    //}

    public boolean isDoneYielding() {
        return isDone();
    }

    //@Override
    //public float getVolume() {
    //    var client = MinecraftClient.getInstance();
    //    return super.getVolume() * (float) (Math.sin((client.world.getTime() + client.getTickDelta()) / (Math.PI * 2)) * 0.5 + 0.5);
    //}

    public @NotNull Optional<CachedAudioStream> getCachedAudioStream() {
        return cachedAudioStream;
    }

    @Override
    public boolean isDone() {
        return cachedAudioStream
                .map(CachedAudioStream::getBuffer)
                .map(Predicate.not(ByteBuffer::hasRemaining)::test)
                .orElse(true);
    }

    public void stop() {}

    @Override
    public void tick() {

        if (MinecraftClient.getInstance().world.getTime() % 50 != 0)
            return;

        //cachedAudioStream.ifPresent(stream -> {
        //    var buffer = stream.getBuffer();
        //    buffer.position(random.nextInt(Math.max(1, stream.getByteDuration())));
        //    var sources = ((SoundSystemAccessor) ((SoundManagerAccessor) soundManager).getSoundSystem()).getSources();
        //    Optional<Channel.SourceManager> sourceManager = Optional.ofNullable(sources.get(this));
//
        //    sourceManager
        //            .map(manager -> ((SourceManagerAccessor) manager).getSource())
        //            .ifPresent(source -> source.setStream(stream));
        //});
    }

    public Random getRandom() {
        return random;
    }
}
