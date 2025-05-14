package de.dafuqs.spectrum.particle.effect;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import org.joml.*;

public record DynamicParticleEffect(ParticleType<?> particleType, float gravity, Vector3f color, float scale,
									int lifetimeTicks, boolean collisions, boolean glowing, boolean alwaysShow) implements ParticleOptions {
	
	public static final MapCodec<DynamicParticleEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BuiltInRegistries.PARTICLE_TYPE.byNameCodec().fieldOf("particle_type").forGetter(c -> c.particleType),
			Codec.FLOAT.fieldOf("gravity").forGetter(c -> c.gravity),
			ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(c -> c.color),
			Codec.FLOAT.fieldOf("scale").forGetter(c -> c.scale),
			Codec.INT.fieldOf("lifetime_ticks").forGetter(c -> c.lifetimeTicks),
			Codec.BOOL.fieldOf("collisions").forGetter(c -> c.collisions),
			Codec.BOOL.fieldOf("glow_in_the_dark").forGetter(c -> c.glowing),
			Codec.BOOL.optionalFieldOf("always_show", false).forGetter(c -> c.alwaysShow)
	).apply(i, DynamicParticleEffect::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, DynamicParticleEffect> PACKET_CODEC = PacketCodecHelper.tuple(
			ByteBufCodecs.registry(Registries.PARTICLE_TYPE), c -> c.particleType,
			ByteBufCodecs.FLOAT, c -> c.gravity,
			ByteBufCodecs.VECTOR3F, c -> c.color,
			ByteBufCodecs.FLOAT, c -> c.scale,
			ByteBufCodecs.VAR_INT, c -> c.lifetimeTicks,
			ByteBufCodecs.BOOL, c -> c.collisions,
			ByteBufCodecs.BOOL, c -> c.glowing,
			ByteBufCodecs.BOOL, c -> c.alwaysShow,
			DynamicParticleEffect::new
	);
	
	public DynamicParticleEffect(float gravity, Vector3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowing) {
		this(SpectrumParticleTypes.SHOOTING_STAR, gravity, color, scale, lifetimeTicks, collisions, glowing);
	}
	
	public DynamicParticleEffect(float gravity, Vector3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowing, boolean alwaysShow) {
		this(SpectrumParticleTypes.SHOOTING_STAR, gravity, color, scale, lifetimeTicks, collisions, glowing, alwaysShow);
	}
	
	public DynamicParticleEffect(ParticleType<?> particleType, float gravity, Vector3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowing) {
		this(particleType, gravity, color, scale, lifetimeTicks, collisions, glowing, false);
	}
	
	@Override
	public ParticleType<?> getType() {
		return alwaysShow ? SpectrumParticleTypes.DYNAMIC_ALWAYS_SHOW : SpectrumParticleTypes.DYNAMIC;
	}
	
	@Override
	public String toString() {
		return String.valueOf(BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()));
	}
	
}
