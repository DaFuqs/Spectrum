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

public class DynamicParticleEffectAlwaysShow extends DynamicParticleEffect {
	
	public static final Codec<DynamicParticleEffectAlwaysShow> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Vec3f.CODEC.fieldOf("color").forGetter((effect) -> effect.color), Codec.STRING.fieldOf("particle_type_identifier").forGetter((effect) -> effect.particleTypeIdentifier.toString()), Codec.FLOAT.fieldOf("scale").forGetter((effect) -> effect.scale), Codec.INT.fieldOf("lifetime_ticks").forGetter((effect) -> effect.lifetimeTicks), Codec.FLOAT.fieldOf("gravity").forGetter((effect) -> effect.gravity), Codec.BOOL.fieldOf("collisions").forGetter((effect) -> effect.collisions), Codec.BOOL.fieldOf("glow_in_the_dark").forGetter((effect) -> effect.glowing)).apply(instance, DynamicParticleEffectAlwaysShow::new));
	
	public static final ParticleEffect.Factory<DynamicParticleEffectAlwaysShow> FACTORY = new ParticleEffect.Factory<>() {
		@Override
		public DynamicParticleEffectAlwaysShow read(ParticleType<DynamicParticleEffectAlwaysShow> particleType, StringReader stringReader) throws CommandSyntaxException {
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
			
			return new DynamicParticleEffectAlwaysShow(textureIdentifier, gravity, color, scale, lifetimeTicks, collisions, glowInTheDark);
		}
		
		@Override
		public DynamicParticleEffectAlwaysShow read(ParticleType<DynamicParticleEffectAlwaysShow> particleType, PacketByteBuf packetByteBuf) {
			Vec3f color = AbstractDustParticleEffect.readColor(packetByteBuf);
			Identifier textureIdentifier = packetByteBuf.readIdentifier();
			float scale = packetByteBuf.readFloat();
			int lifetimeTicks = packetByteBuf.readInt();
			float gravity = packetByteBuf.readFloat();
			boolean collisions = packetByteBuf.readBoolean();
			boolean glowInTheDark = packetByteBuf.readBoolean();
			
			return new DynamicParticleEffectAlwaysShow(textureIdentifier, gravity, color, scale, lifetimeTicks, collisions, glowInTheDark);
		}
	};
	
	public DynamicParticleEffectAlwaysShow(float gravity, Vec3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowInTheDark) {
		super(gravity, color, scale, lifetimeTicks, collisions, glowInTheDark);
	}
	
	public DynamicParticleEffectAlwaysShow(Vec3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowInTheDark) {
		super(color, scale, lifetimeTicks, collisions, glowInTheDark);
	}
	
	public DynamicParticleEffectAlwaysShow(ParticleType<?> particleType, Vec3f color, float scale, int lifetimeTicks, boolean collisions, boolean glowInTheDark) {
		super(particleType, 1.0, color, scale, lifetimeTicks, collisions, glowInTheDark);
	}
	
	protected DynamicParticleEffectAlwaysShow(Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
		super(o, o1, o2, o3, o4, o5, o6);
	}
	
	@Override
	public String asString() {
		return String.valueOf(Registry.PARTICLE_TYPE.getId(this.getType()));
	}
	
	@Override
	public ParticleType getType() {
		return SpectrumParticleTypes.DYNAMIC_ALWAYS_SHOW;
	}
	
}
