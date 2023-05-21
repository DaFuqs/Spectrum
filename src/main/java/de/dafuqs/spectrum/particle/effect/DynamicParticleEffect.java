package de.dafuqs.spectrum.particle.effect;

import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.network.*;
import net.minecraft.particle.*;
import net.minecraft.util.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.registry.*;
import org.joml.Vector3f;

public class DynamicParticleEffect implements ParticleEffect {
	
	public static final Codec<DynamicParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codecs.VECTOR_3F.fieldOf("color").forGetter((effect) -> {
			return effect.color;
		}), Codec.STRING.fieldOf("particle_type").forGetter((effect) -> {
			return effect.particleTypeIdentifier.toString();
		}), Codec.FLOAT.fieldOf("scale").forGetter((effect) -> {
			return effect.scale;
		}), Codec.INT.fieldOf("lifetime_ticks").forGetter((effect) -> {
			return effect.lifetimeTicks;
		}), Codec.FLOAT.fieldOf("gravity").forGetter((effect) -> {
			return effect.gravity;
		}), Codec.BOOL.fieldOf("collisions").forGetter((effect) -> {
			return effect.collisions;
		}), Codec.BOOL.fieldOf("glow_in_the_dark").forGetter((effect) -> {
			return effect.glowing;
		})).apply(instance, DynamicParticleEffect::new);
	});
	
	public static final ParticleEffect.Factory<DynamicParticleEffect> FACTORY = new ParticleEffect.Factory<>() {
		@Override
		public DynamicParticleEffect read(ParticleType<DynamicParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			Vector3f color = AbstractDustParticleEffect.readColor(stringReader);
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
			
			return new DynamicParticleEffect(textureIdentifier, gravity, color, scale, lifetimeTicks, collisions, glowInTheDark);
		}
		
		@Override
		public DynamicParticleEffect read(ParticleType<DynamicParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			Vector3f color = AbstractDustParticleEffect.readColor(packetByteBuf);
			Identifier textureIdentifier = packetByteBuf.readIdentifier();
			float scale = packetByteBuf.readFloat();
			int lifetimeTicks = packetByteBuf.readInt();
			float gravity = packetByteBuf.readFloat();
			boolean collisions = packetByteBuf.readBoolean();
			boolean glowInTheDark = packetByteBuf.readBoolean();
			
			return new DynamicParticleEffect(textureIdentifier, gravity, color, scale, lifetimeTicks, collisions, glowInTheDark);
		}
	};
	
	public Identifier particleTypeIdentifier;
	public Vector3f color;
	public float scale;
	public int lifetimeTicks;
	public float gravity;
	public boolean collisions;
	public boolean glowing;
	
	public DynamicParticleEffect(float gravity, Vector3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowing) {
		this(SpectrumParticleTypes.SHOOTING_STAR, gravity, color, scale, lifetimeTicks, collisions, glowing);
	}
	
	public DynamicParticleEffect(Vector3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowing) {
		this(SpectrumParticleTypes.SHOOTING_STAR, 1.0, color, scale, lifetimeTicks, collisions, glowing);
	}
	
	public DynamicParticleEffect(ParticleType<?> particleType, Vector3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowing) {
		this(particleType, 1.0, color, scale, lifetimeTicks, collisions, glowing);
	}
	
	public DynamicParticleEffect(ParticleType<?> particleType, float gravity, Vector3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowing) {
		this.particleTypeIdentifier = Registries.PARTICLE_TYPE.getId(particleType);
		this.gravity = gravity;
		this.color = color;
		this.scale = scale;
		this.lifetimeTicks = lifetimeTicks;
		this.collisions = collisions;
		this.glowing = glowing;
	}
	
	protected DynamicParticleEffect(Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
		new DynamicParticleEffect(o, o1, o2, o3, o4, o5, o6);
	}
	
	@Override
	public void write(PacketByteBuf buf) {
		buf.writeString(this.particleTypeIdentifier.toString());
		buf.writeFloat(this.gravity);
		buf.writeFloat(this.color.x());
		buf.writeFloat(this.color.y());
		buf.writeFloat(this.color.z());
		buf.writeFloat(this.scale);
		buf.writeInt(this.lifetimeTicks);
		buf.writeBoolean(this.collisions);
		buf.writeBoolean(this.glowing);
	}
	
	@Override
	public String asString() {
		return String.valueOf(Registries.PARTICLE_TYPE.getId(this.getType()));
	}
	
	@Override
	public ParticleType<DynamicParticleEffect> getType() {
		return SpectrumParticleTypes.DYNAMIC;
	}
	
	public float getGravity() {
		return this.gravity;
	}
}
