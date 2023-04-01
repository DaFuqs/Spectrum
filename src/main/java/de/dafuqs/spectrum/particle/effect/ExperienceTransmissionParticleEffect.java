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

public class ExperienceTransmissionParticleEffect extends SimpleTransmissionParticleEffect {
	
	public static final Codec<ExperienceTransmissionParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(PositionSource.CODEC.fieldOf("destination").forGetter((effect) -> {
			return effect.destination;
		}), Codec.INT.fieldOf("arrival_in_ticks").forGetter((vibrationParticleEffect) -> {
			return vibrationParticleEffect.arrivalInTicks;
		})).apply(instance, ExperienceTransmissionParticleEffect::new);
	});
	public static final Factory<ExperienceTransmissionParticleEffect> FACTORY = new Factory<>() {
		public ExperienceTransmissionParticleEffect read(ParticleType<ExperienceTransmissionParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			float f = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float g = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float h = (float) stringReader.readDouble();
			stringReader.expect(' ');
			int i = stringReader.readInt();
			BlockPos blockPos = new BlockPos(f, g, h);
			return new ExperienceTransmissionParticleEffect(new BlockPositionSource(blockPos), i);
		}
		
		public ExperienceTransmissionParticleEffect read(ParticleType<ExperienceTransmissionParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			PositionSource positionSource = PositionSourceType.read(packetByteBuf);
			int i = packetByteBuf.readVarInt();
			return new ExperienceTransmissionParticleEffect(positionSource, i);
		}
	};
	
	public ExperienceTransmissionParticleEffect(PositionSource positionSource, int arrivalInTicks) {
		super(positionSource, arrivalInTicks);
	}
	
	public ParticleType getType() {
		return SpectrumParticleTypes.EXPERIENCE_TRANSMISSION;
	}
	
}
