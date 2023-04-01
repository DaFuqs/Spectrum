package de.dafuqs.spectrum.particle.effect;

import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.network.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;

public class HummingstoneTransmissionParticleEffect extends SimpleTransmissionParticleEffect {
	
	public static final Codec<HummingstoneTransmissionParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			PositionSource.CODEC.fieldOf("destination").forGetter((effect) -> effect.destination),
			Codec.INT.fieldOf("arrival_in_ticks").forGetter((effect) -> effect.arrivalInTicks)
	).apply(instance, HummingstoneTransmissionParticleEffect::new));
	
	public static final Factory<HummingstoneTransmissionParticleEffect> FACTORY = new Factory<>() {
		public HummingstoneTransmissionParticleEffect read(ParticleType<HummingstoneTransmissionParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			float f = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float g = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float h = (float) stringReader.readDouble();
			stringReader.expect(' ');
			int i = stringReader.readInt();
			BlockPos blockPos = new BlockPos(f, g, h);
			return new HummingstoneTransmissionParticleEffect(new BlockPositionSource(blockPos), i);
		}
		
		public HummingstoneTransmissionParticleEffect read(ParticleType<HummingstoneTransmissionParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			PositionSource positionSource = PositionSourceType.read(packetByteBuf);
			int i = packetByteBuf.readVarInt();
			return new HummingstoneTransmissionParticleEffect(positionSource, i);
		}
	};
	
	public HummingstoneTransmissionParticleEffect(PositionSource positionSource, int arrivalInTicks) {
		super(positionSource, arrivalInTicks);
	}
	
	public ParticleType<HummingstoneTransmissionParticleEffect> getType() {
		return SpectrumParticleTypes.HUMMINGSTONE_TRANSMISSION;
	}
	
}
