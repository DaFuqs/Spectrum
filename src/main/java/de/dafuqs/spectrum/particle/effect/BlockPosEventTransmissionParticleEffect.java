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

public class BlockPosEventTransmissionParticleEffect extends SimpleTransmissionParticleEffect {
	
	public static final Codec<BlockPosEventTransmissionParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> instance.group(PositionSource.CODEC.fieldOf("destination").forGetter((effect) -> effect.destination), Codec.INT.fieldOf("arrival_in_ticks").forGetter((vibrationParticleEffect) -> vibrationParticleEffect.arrivalInTicks)).apply(instance, BlockPosEventTransmissionParticleEffect::new));
	public static final Factory<BlockPosEventTransmissionParticleEffect> FACTORY = new Factory<>() {
		@Override
		public BlockPosEventTransmissionParticleEffect read(ParticleType<BlockPosEventTransmissionParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			float f = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float g = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float h = (float) stringReader.readDouble();
			stringReader.expect(' ');
			int i = stringReader.readInt();
			BlockPos blockPos = new BlockPos(f, g, h);
			return new BlockPosEventTransmissionParticleEffect(new BlockPositionSource(blockPos), i);
		}
		
		@Override
		public BlockPosEventTransmissionParticleEffect read(ParticleType<BlockPosEventTransmissionParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			PositionSource positionSource = PositionSourceType.read(packetByteBuf);
			int i = packetByteBuf.readVarInt();
			return new BlockPosEventTransmissionParticleEffect(positionSource, i);
		}
	};
	
	public BlockPosEventTransmissionParticleEffect(PositionSource positionSource, int arrivalInTicks) {
		super(positionSource, arrivalInTicks);
	}
	
	@Override
	public ParticleType getType() {
		return SpectrumParticleTypes.BLOCK_POS_EVENT_TRANSMISSION;
	}
	
}
