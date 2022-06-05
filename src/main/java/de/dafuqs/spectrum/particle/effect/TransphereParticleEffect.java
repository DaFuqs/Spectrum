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

public class TransphereParticleEffect implements ParticleEffect {
	
	public static final Codec<TransphereParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Transphere.CODEC.fieldOf("transphere").forGetter((transphereParticleEffect) -> {
		return transphereParticleEffect.transphere;
	})).apply(instance, (Function) (TransphereParticleEffect::new)));
	
	public static final Factory<TransphereParticleEffect> FACTORY = new Factory<>() {
		public TransphereParticleEffect read(ParticleType<TransphereParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
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
			int p = stringReader.readInt();
			BlockPos blockPos = new BlockPos(f, g, h);
			BlockPos blockPos2 = new BlockPos(i, j, k);
			return new TransphereParticleEffect(new Transphere(blockPos, new BlockPositionSource(blockPos2), l, p));
		}
		
		public TransphereParticleEffect read(ParticleType<TransphereParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			Transphere transphere = Transphere.readFromBuf(packetByteBuf);
			return new TransphereParticleEffect(transphere);
		}
	};
	
	private final Transphere transphere;
	
	public TransphereParticleEffect(Transphere transphere) {
		this.transphere = transphere;
	}
	
	public TransphereParticleEffect(Object transphere) {
		this((Transphere) transphere);
	}
	
	public void write(PacketByteBuf buf) {
		Transphere.writeToBuf(buf, this.transphere);
	}
	
	public String asString() {
		BlockPos blockPos = this.transphere.getOrigin();
		double d = blockPos.getX();
		double e = blockPos.getY();
		double f = blockPos.getZ();
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %d", Registry.PARTICLE_TYPE.getId(this.getType()), d, e, f, d, e, f, this.transphere.getArrivalInTicks());
	}
	
	public ParticleType<TransphereParticleEffect> getType() {
		return SpectrumParticleTypes.TRANSPHERE;
	}
	
	public Transphere getTransphere() {
		return this.transphere;
	}
}
