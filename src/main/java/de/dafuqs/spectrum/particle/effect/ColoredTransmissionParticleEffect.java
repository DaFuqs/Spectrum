package de.dafuqs.spectrum.particle.effect;

import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.network.*;
import net.minecraft.particle.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;

public class ColoredTransmissionParticleEffect extends SimpleTransmissionParticleEffect {
	
	public static final Codec<ColoredTransmissionParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> instance.group(PositionSource.CODEC.fieldOf("destination").forGetter((effect) -> effect.destination), Codec.INT.fieldOf("arrival_in_ticks").forGetter((effect) -> effect.arrivalInTicks), Codec.INT.fieldOf("dye_color").forGetter((effect) -> effect.dyeColor.getId())).apply(instance, ColoredTransmissionParticleEffect::new));
	public static final ParticleEffect.Factory<ColoredTransmissionParticleEffect> FACTORY = new ParticleEffect.Factory<>() {
		@Override
		public ColoredTransmissionParticleEffect read(ParticleType<ColoredTransmissionParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			float f = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float g = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float h = (float) stringReader.readDouble();
			stringReader.expect(' ');
			int i = stringReader.readInt();
			int dyeColorId = stringReader.readInt();
			BlockPos blockPos = BlockPos.ofFloored(f, g, h);
			return new ColoredTransmissionParticleEffect(new BlockPositionSource(blockPos), i, dyeColorId);
		}
		
		@Override
		public ColoredTransmissionParticleEffect read(ParticleType<ColoredTransmissionParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			PositionSource positionSource = PositionSourceType.read(packetByteBuf);
			int i = packetByteBuf.readVarInt();
			int dyeColorId = packetByteBuf.readVarInt();
			return new ColoredTransmissionParticleEffect(positionSource, i, dyeColorId);
		}
	};
	
	public final DyeColor dyeColor;
	
	public ColoredTransmissionParticleEffect(PositionSource positionSource, int arrivalInTicks, int dyeColorId) {
		super(positionSource, arrivalInTicks);
		this.dyeColor = DyeColor.byId(dyeColorId);
	}
	
	public ColoredTransmissionParticleEffect(PositionSource positionSource, int arrivalInTicks, DyeColor dyeColor) {
		super(positionSource, arrivalInTicks);
		this.dyeColor = dyeColor;
	}
	
	@Override
	public ParticleType<ColoredTransmissionParticleEffect> getType() {
		return SpectrumParticleTypes.COLORED_TRANSMISSION;
	}
	
	public DyeColor getDyeColor() {
		return dyeColor;
	}
	
}
