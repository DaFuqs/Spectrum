package de.dafuqs.spectrum.events;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.helpers.*;
import io.netty.buffer.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class ExactPositionSource implements PositionSource {
	
	public static final MapCodec<ExactPositionSource> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			Vec3.CODEC.fieldOf("pos").forGetter((blockPositionSource) -> blockPositionSource.pos)
	).apply(instance, ExactPositionSource::new));
	
	public static final StreamCodec<ByteBuf, ExactPositionSource> PACKET_CODEC = StreamCodec.composite(PacketCodecHelper.VEC3D, (source) -> source.pos, ExactPositionSource::new);
	
	final Vec3 pos;
	
	public ExactPositionSource(Vec3 pos) {
		this.pos = pos;
	}
	
	@Override
	public Optional<Vec3> getPosition(Level world) {
		return Optional.of(this.pos);
	}
	
	@Override
	public PositionSourceType<?> getType() {
		return SpectrumPositionSources.EXACT;
	}
	
	public static class Type implements PositionSourceType<ExactPositionSource> {
		public Type() {
		}
		
		public MapCodec<ExactPositionSource> codec() {
			return ExactPositionSource.CODEC;
		}
		
		public StreamCodec<ByteBuf, ExactPositionSource> streamCodec() {
			return ExactPositionSource.PACKET_CODEC;
		}
	}
	
}
