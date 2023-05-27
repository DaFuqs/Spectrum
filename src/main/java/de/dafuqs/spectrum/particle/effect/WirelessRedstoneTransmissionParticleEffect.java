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

public class WirelessRedstoneTransmissionParticleEffect extends SimpleTransmissionParticleEffect {
	
	public static final Codec<WirelessRedstoneTransmissionParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(PositionSource.CODEC.fieldOf("destination").forGetter((effect) -> {
			return effect.destination;
		}), Codec.INT.fieldOf("arrival_in_ticks").forGetter((vibrationParticleEffect) -> {
			return vibrationParticleEffect.arrivalInTicks;
		})).apply(instance, WirelessRedstoneTransmissionParticleEffect::new);
	});
	
	public static final ParticleEffect.Factory<WirelessRedstoneTransmissionParticleEffect> FACTORY = new ParticleEffect.Factory<>() {
		public WirelessRedstoneTransmissionParticleEffect read(ParticleType<WirelessRedstoneTransmissionParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			float f = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float g = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float h = (float) stringReader.readDouble();
			stringReader.expect(' ');
			int i = stringReader.readInt();
			BlockPos blockPos = BlockPos.ofFloored(f, g, h);
			return new WirelessRedstoneTransmissionParticleEffect(new BlockPositionSource(blockPos), i);
		}
		
		public WirelessRedstoneTransmissionParticleEffect read(ParticleType<WirelessRedstoneTransmissionParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			PositionSource positionSource = PositionSourceType.read(packetByteBuf);
			int i = packetByteBuf.readVarInt();
			return new WirelessRedstoneTransmissionParticleEffect(positionSource, i);
		}
	};
	
	public WirelessRedstoneTransmissionParticleEffect(PositionSource positionSource, int arrivalInTicks) {
		super(positionSource, arrivalInTicks);
	}
	
	public ParticleType<WirelessRedstoneTransmissionParticleEffect> getType() {
		return SpectrumParticleTypes.WIRELESS_REDSTONE_TRANSMISSION;
	}
	
}
