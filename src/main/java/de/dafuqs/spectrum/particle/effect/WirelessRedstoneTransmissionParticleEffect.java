package de.dafuqs.spectrum.particle.effect;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.BlockPositionSource;

import java.util.Locale;
import java.util.function.Function;

public class WirelessRedstoneTransmissionParticleEffect implements ParticleEffect {
	
	public static final Codec<WirelessRedstoneTransmissionParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(WirelessRedstoneTransmission.CODEC.fieldOf("wireless_redstone_transmission").forGetter((itemTransferParticleEffect) -> {
			return itemTransferParticleEffect.wirelessRedstoneTransmission;
		})).apply(instance, (Function) (WirelessRedstoneTransmissionParticleEffect::new));
	});
	public static final Factory<WirelessRedstoneTransmissionParticleEffect> FACTORY = new Factory<>() {
		public WirelessRedstoneTransmissionParticleEffect read(ParticleType<WirelessRedstoneTransmissionParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			float f = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float g = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float h = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float i = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float j = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float k = (float) stringReader.readDouble();
			stringReader.expect(' ');
			int l = stringReader.readInt();
			BlockPos blockPos = new BlockPos(f, g, h);
			BlockPos blockPos2 = new BlockPos(i, j, k);
			return new WirelessRedstoneTransmissionParticleEffect(new WirelessRedstoneTransmission(blockPos, new BlockPositionSource(blockPos2), l));
		}
		
		public WirelessRedstoneTransmissionParticleEffect read(ParticleType<WirelessRedstoneTransmissionParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			ItemTransfer itemTransfer = ItemTransfer.readFromBuf(packetByteBuf);
			return new WirelessRedstoneTransmissionParticleEffect(itemTransfer);
		}
	};
	
	private final WirelessRedstoneTransmission wirelessRedstoneTransmission;
	
	public WirelessRedstoneTransmissionParticleEffect(WirelessRedstoneTransmission wirelessRedstoneTransmission) {
		this.wirelessRedstoneTransmission = wirelessRedstoneTransmission;
	}
	
	public WirelessRedstoneTransmissionParticleEffect(Object wirelessRedstoneTransmission) {
		this((WirelessRedstoneTransmission) wirelessRedstoneTransmission);
	}
	
	public void write(PacketByteBuf buf) {
		WirelessRedstoneTransmission.writeToBuf(buf, this.wirelessRedstoneTransmission);
	}
	
	public String asString() {
		BlockPos blockPos = this.wirelessRedstoneTransmission.getOrigin();
		double d = blockPos.getX();
		double e = blockPos.getY();
		double f = blockPos.getZ();
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %d", Registry.PARTICLE_TYPE.getId(this.getType()), d, e, f, d, e, f, this.wirelessRedstoneTransmission.getArrivalInTicks());
	}
	
	public ParticleType<WirelessRedstoneTransmissionParticleEffect> getType() {
		return SpectrumParticleTypes.WIRELESS_REDSTONE_TRANSMISSION;
	}
	
	public WirelessRedstoneTransmission getWirelessRedstoneTransmission() {
		return this.wirelessRedstoneTransmission;
	}
}
