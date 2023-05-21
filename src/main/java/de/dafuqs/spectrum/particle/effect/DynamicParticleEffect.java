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
import net.minecraft.util.registry.*;

public class DynamicParticleEffect implements ParticleEffect {
	
	public static final Codec<DynamicParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Vec3f.CODEC.fieldOf("color").forGetter((effect) -> effect.color), Codec.STRING.fieldOf("particle_type").forGetter((effect) -> effect.particleTypeIdentifier.toString()), Codec.FLOAT.fieldOf("scale").forGetter((effect) -> effect.scale), Codec.INT.fieldOf("lifetime_ticks").forGetter((effect) -> effect.lifetimeTicks), Codec.FLOAT.fieldOf("gravity").forGetter((effect) -> effect.gravity), Codec.BOOL.fieldOf("collisions").forGetter((effect) -> effect.collisions), Codec.BOOL.fieldOf("glow_in_the_dark").forGetter((effect) -> effect.glowing)).apply(instance, DynamicParticleEffect::new));
	
	public static final ParticleEffect.Factory<DynamicParticleEffect> FACTORY = new ParticleEffect.Factory<>() {
		@Override
		public DynamicParticleEffect read(ParticleType<DynamicParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
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
			
			return new DynamicParticleEffect(textureIdentifier, gravity, color, scale, lifetimeTicks, collisions, glowInTheDark);
		}
		
		@Override
		public DynamicParticleEffect read(ParticleType<DynamicParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			Vec3f color = AbstractDustParticleEffect.readColor(packetByteBuf);
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
	public Vec3f color;
	public float scale;
	public int lifetimeTicks;
	public float gravity;
	public boolean collisions;
	public boolean glowing;
	
	public DynamicParticleEffect(float gravity, Vec3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowing) {
		this(SpectrumParticleTypes.SHOOTING_STAR, gravity, color, scale, lifetimeTicks, collisions, glowing);
	}
	
	public DynamicParticleEffect(Vec3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowing) {
		this(SpectrumParticleTypes.SHOOTING_STAR, 1.0, color, scale, lifetimeTicks, collisions, glowing);
	}
	
	public DynamicParticleEffect(ParticleType<?> particleType, Vec3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowing) {
		this(particleType, 1.0, color, scale, lifetimeTicks, collisions, glowing);
	}
	
	public DynamicParticleEffect(ParticleType<?> particleType, float gravity, Vec3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowing) {
		this.particleTypeIdentifier = Registry.PARTICLE_TYPE.getId(particleType);
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
		buf.writeFloat(this.color.getX());
		buf.writeFloat(this.color.getY());
		buf.writeFloat(this.color.getZ());
		buf.writeFloat(this.scale);
		buf.writeInt(this.lifetimeTicks);
		buf.writeBoolean(this.collisions);
		buf.writeBoolean(this.glowing);
	}
	
	@Override
	public String asString() {
		return String.valueOf(Registry.PARTICLE_TYPE.getId(this.getType()));
	}
	
	@Override
	public ParticleType<DynamicParticleEffect> getType() {
		return SpectrumParticleTypes.DYNAMIC;
	}
	
	public float getGravity() {
		return this.gravity;
	}
}
