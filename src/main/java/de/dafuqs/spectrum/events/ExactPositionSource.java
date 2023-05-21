package de.dafuqs.spectrum.events;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

import java.util.*;

public class ExactPositionSource implements PositionSource {
	
	public static final Codec<ExactPositionSource> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Vec3d.CODEC.fieldOf("pos").forGetter((positionSource) -> positionSource.pos)).apply(instance, ExactPositionSource::new));
	
	final Vec3d pos;
	
	public ExactPositionSource(Vec3d pos) {
		this.pos = pos;
	}
	
	@Override
	public Optional<Vec3d> getPos(World world) {
		return Optional.of(this.pos);
	}
	
	@Override
	public PositionSourceType<?> getType() {
		return SpectrumPositionSources.EXACT;
	}
	
	public static class Type implements PositionSourceType<ExactPositionSource> {
		public Type() {
		}
		
		@Override
		public ExactPositionSource readFromBuf(PacketByteBuf packetByteBuf) {
			return new ExactPositionSource(new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble()));
		}
		
		@Override
		public void writeToBuf(PacketByteBuf packetByteBuf, ExactPositionSource blockPositionSource) {
			packetByteBuf.writeDouble(blockPositionSource.pos.x);
			packetByteBuf.writeDouble(blockPositionSource.pos.y);
			packetByteBuf.writeDouble(blockPositionSource.pos.z);
		}
		
		@Override
		public Codec<ExactPositionSource> getCodec() {
			return ExactPositionSource.CODEC;
		}
	}
	
}
