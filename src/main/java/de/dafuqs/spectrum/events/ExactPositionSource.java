package de.dafuqs.spectrum.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;

import java.util.Optional;

public class ExactPositionSource implements PositionSource {
    
    public static final Codec<ExactPositionSource> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Vec3d.CODEC.fieldOf("pos").forGetter((blockPositionSource) -> {
            return blockPositionSource.pos;
        })).apply(instance, ExactPositionSource::new);
    });
    
    final Vec3d pos;

    public ExactPositionSource(Vec3d pos) {
        this.pos = pos;
    }

    public Optional<Vec3d> getPos(World world) {
        return Optional.of(this.pos);
    }

    public PositionSourceType<?> getType() {
        return PositionSourceType.BLOCK;
    }

    public static class Type implements PositionSourceType<ExactPositionSource> {
        public Type() {
        }

        public ExactPositionSource readFromBuf(PacketByteBuf packetByteBuf) {
            return new ExactPositionSource(new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble()));
        }

        public void writeToBuf(PacketByteBuf packetByteBuf, ExactPositionSource blockPositionSource) {
            packetByteBuf.writeDouble(blockPositionSource.pos.x);
            packetByteBuf.writeDouble(blockPositionSource.pos.y);
            packetByteBuf.writeDouble(blockPositionSource.pos.z);
        }

        public Codec<ExactPositionSource> getCodec() {
            return ExactPositionSource.CODEC;
        }
    }
}
