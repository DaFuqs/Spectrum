package de.dafuqs.spectrum.sound.music;

import net.minecraft.client.sound.AudioStream;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.nio.ByteBuffer;

public record StreamMimic(AudioFormat format, ByteBuffer buffer) implements AudioStream {

    @Override
    public void close() throws IOException {}

    @Override
    public AudioFormat getFormat() {
        return format;
    }

    @Override
    public ByteBuffer getBuffer(int size) throws IOException {
        return buffer;
    }
}
