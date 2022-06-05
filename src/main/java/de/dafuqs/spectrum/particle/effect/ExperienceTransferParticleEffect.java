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

public class ExperienceTransferParticleEffect implements ParticleEffect {
	
	public static final Codec<ExperienceTransferParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(ExperienceTransfer.CODEC.fieldOf("experience_transfer").forGetter((experienceTransferParticleEffect) -> {
			return experienceTransferParticleEffect.experienceTransfer;
		})).apply(instance, (Function) (ExperienceTransferParticleEffect::new));
	});
	public static final Factory<ExperienceTransferParticleEffect> FACTORY = new Factory<>() {
		public ExperienceTransferParticleEffect read(ParticleType<ExperienceTransferParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
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
			return new ExperienceTransferParticleEffect(new ExperienceTransfer(blockPos, new BlockPositionSource(blockPos2), l));
		}
		
		public ExperienceTransferParticleEffect read(ParticleType<ExperienceTransferParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			ExperienceTransfer experienceTransfer = ExperienceTransfer.readFromBuf(packetByteBuf);
			return new ExperienceTransferParticleEffect(experienceTransfer);
		}
	};
	
	private final ExperienceTransfer experienceTransfer;
	
	public ExperienceTransferParticleEffect(ExperienceTransfer experienceTransfer) {
		this.experienceTransfer = experienceTransfer;
	}
	
	public ExperienceTransferParticleEffect(Object experienceOrbTransfer) {
		this((ExperienceTransfer) experienceOrbTransfer);
	}
	
	public void write(PacketByteBuf buf) {
		ExperienceTransfer.writeToBuf(buf, this.experienceTransfer);
	}
	
	public String asString() {
		BlockPos blockPos = this.experienceTransfer.getOrigin();
		double d = blockPos.getX();
		double e = blockPos.getY();
		double f = blockPos.getZ();
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %d", Registry.PARTICLE_TYPE.getId(this.getType()), d, e, f, d, e, f, this.experienceTransfer.getArrivalInTicks());
	}
	
	public ParticleType<ExperienceTransferParticleEffect> getType() {
		return SpectrumParticleTypes.EXPERIENCE_TRANSFER;
	}
	
	public ExperienceTransfer getExperienceTransfer() {
		return this.experienceTransfer;
	}
}
