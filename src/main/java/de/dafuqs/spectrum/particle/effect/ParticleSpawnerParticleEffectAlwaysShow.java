package de.dafuqs.spectrum.particle.effect;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Function7;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.AbstractDustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;

public class ParticleSpawnerParticleEffectAlwaysShow extends ParticleSpawnerParticleEffect {
	
	public static final Codec<ParticleSpawnerParticleEffectAlwaysShow> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Vec3f.CODEC.fieldOf("color").forGetter((particleSpawnerParticleEffect) -> {
			return particleSpawnerParticleEffect.color;
		}), Codec.STRING.fieldOf("texture_identifier_string").forGetter((particleSpawnerParticleEffect) -> {
			return particleSpawnerParticleEffect.textureIdentifier.toString();
		}), Codec.FLOAT.fieldOf("scale").forGetter((particleSpawnerParticleEffect) -> {
			return particleSpawnerParticleEffect.scale;
		}), Codec.INT.fieldOf("lifetime_ticks").forGetter((particleSpawnerParticleEffect) -> {
			return particleSpawnerParticleEffect.lifetimeTicks;
		}), Codec.FLOAT.fieldOf("gravity").forGetter((particleSpawnerParticleEffect) -> {
			return particleSpawnerParticleEffect.gravity;
		}), Codec.BOOL.fieldOf("collisions").forGetter((particleSpawnerParticleEffect) -> {
			return particleSpawnerParticleEffect.collisions;
		}), Codec.BOOL.fieldOf("glow_in_the_dark").forGetter((particleSpawnerParticleEffect) -> {
			return particleSpawnerParticleEffect.glowInTheDark;
		})).apply(instance, (Function7) (ParticleSpawnerParticleEffectAlwaysShow::new));
	});
	
	public static final ParticleEffect.Factory<ParticleSpawnerParticleEffectAlwaysShow> FACTORY = new ParticleEffect.Factory<>() {
		public ParticleSpawnerParticleEffectAlwaysShow read(ParticleType<ParticleSpawnerParticleEffectAlwaysShow> particleType, StringReader stringReader) throws CommandSyntaxException {
			Vec3f color = AbstractDustParticleEffect.readColor(stringReader);
			stringReader.expect(' ');
			Identifier textureIdentifier = new Identifier(stringReader.readString());
			stringReader.expect(' ');
			float scale = stringReader.readFloat();
			stringReader.expect(' ');
			int lifetimeTicks = stringReader.readInt();
			stringReader.expect(' ');
			float gravity = stringReader.readFloat();
			stringReader.expect(' ');
			boolean collisions = stringReader.readBoolean();
			boolean glowInTheDark = stringReader.readBoolean();
			
			return new ParticleSpawnerParticleEffectAlwaysShow(textureIdentifier, gravity, color, scale, lifetimeTicks, collisions, glowInTheDark);
		}
		
		public ParticleSpawnerParticleEffectAlwaysShow read(ParticleType<ParticleSpawnerParticleEffectAlwaysShow> particleType, PacketByteBuf packetByteBuf) {
			Vec3f color = AbstractDustParticleEffect.readColor(packetByteBuf);
			Identifier textureIdentifier = packetByteBuf.readIdentifier();
			float scale = packetByteBuf.readFloat();
			int lifetimeTicks = packetByteBuf.readInt();
			float gravity = packetByteBuf.readFloat();
			boolean collisions = packetByteBuf.readBoolean();
			boolean glowInTheDark = packetByteBuf.readBoolean();
			
			return new ParticleSpawnerParticleEffectAlwaysShow(textureIdentifier, gravity, color, scale, lifetimeTicks, collisions, glowInTheDark);
		}
	};
	
	public ParticleSpawnerParticleEffectAlwaysShow(Identifier textureIdentifier, float gravity, Vec3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowInTheDark) {
		super(textureIdentifier, gravity, color, scale, lifetimeTicks, collisions, glowInTheDark);
	}
	
	public ParticleSpawnerParticleEffectAlwaysShow(Identifier textureIdentifier, Vec3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowInTheDark) {
		super(textureIdentifier, color, scale, lifetimeTicks, collisions, glowInTheDark);
	}
	
	public ParticleSpawnerParticleEffectAlwaysShow(Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
		super(o, o1, o2, o3, o4, o5, o6);
	}
	
	public String asString() {
		return String.valueOf(Registry.PARTICLE_TYPE.getId(this.getType()));
	}
	
	public ParticleType getType() {
		return SpectrumParticleTypes.PARTICLE_SPAWNER_ALWAYS_SHOW;
	}
	
}
