package de.dafuqs.spectrum.particle.effect;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;

public class ColoredTransmissionParticleEffect extends TransmissionParticleEffect {
	
	public static final Codec<ColoredTransmissionParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(PositionSource.CODEC.fieldOf("destination").forGetter((effect) -> {
			return effect.destination;
		}), Codec.INT.fieldOf("arrival_in_ticks").forGetter((effect) -> {
			return effect.arrivalInTicks;
		}), Codec.INT.fieldOf("dye_color").forGetter((effect) -> {
			return effect.dyeColor.getId();
		})).apply(instance, ColoredTransmissionParticleEffect::new);
	});
	public static final ParticleEffect.Factory<ColoredTransmissionParticleEffect> FACTORY = new ParticleEffect.Factory<>() {
		public ColoredTransmissionParticleEffect read(ParticleType<ColoredTransmissionParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			float f = (float)stringReader.readDouble();
			stringReader.expect(' ');
			float g = (float)stringReader.readDouble();
			stringReader.expect(' ');
			float h = (float)stringReader.readDouble();
			stringReader.expect(' ');
			int i = stringReader.readInt();
			int dyeColorId = stringReader.readInt();
			BlockPos blockPos = new BlockPos(f, g, h);
			return new ColoredTransmissionParticleEffect(new BlockPositionSource(blockPos), i, dyeColorId);
		}
		
		public ColoredTransmissionParticleEffect read(ParticleType<ColoredTransmissionParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			PositionSource positionSource = PositionSourceType.read(packetByteBuf);
			int i = packetByteBuf.readVarInt();
			int dyeColorId = packetByteBuf.readVarInt();
			return new ColoredTransmissionParticleEffect(positionSource, i, dyeColorId);
		}
	};
	
	public DyeColor dyeColor;
	
	public ColoredTransmissionParticleEffect(PositionSource positionSource, int arrivalInTicks, int dyeColorId) {
		super(positionSource, arrivalInTicks);
		this.dyeColor = DyeColor.byId(dyeColorId);
	}
	
	public ColoredTransmissionParticleEffect(PositionSource positionSource, int arrivalInTicks, DyeColor dyeColor) {
		super(positionSource, arrivalInTicks);
		this.dyeColor = dyeColor;
	}
	
	public ParticleType<ColoredTransmissionParticleEffect> getType() {
		return SpectrumParticleTypes.COLORED_TRANSMISSION;
	}
	
	public DyeColor getDyeColor() {
		return dyeColor;
	}
	
}
