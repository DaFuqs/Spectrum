package de.dafuqs.spectrum.particle.effect;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import org.joml.*;

public class ColoredFallingSporeBlossomParticleEffect implements ParticleOptions {
	
	public static final ColoredFallingSporeBlossomParticleEffect BLACK = new ColoredFallingSporeBlossomParticleEffect(InkColors.BLACK_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect BLUE = new ColoredFallingSporeBlossomParticleEffect(InkColors.BLUE_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect BROWN = new ColoredFallingSporeBlossomParticleEffect(InkColors.BROWN_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect CYAN = new ColoredFallingSporeBlossomParticleEffect(InkColors.CYAN_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect GRAY = new ColoredFallingSporeBlossomParticleEffect(InkColors.GRAY_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect GREEN = new ColoredFallingSporeBlossomParticleEffect(InkColors.GREEN_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect LIGHT_BLUE = new ColoredFallingSporeBlossomParticleEffect(InkColors.LIGHT_BLUE_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect LIGHT_GRAY = new ColoredFallingSporeBlossomParticleEffect(InkColors.LIGHT_GRAY_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect LIME = new ColoredFallingSporeBlossomParticleEffect(InkColors.LIME_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect MAGENTA = new ColoredFallingSporeBlossomParticleEffect(InkColors.MAGENTA_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect ORANGE = new ColoredFallingSporeBlossomParticleEffect(InkColors.ORANGE_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect PINK = new ColoredFallingSporeBlossomParticleEffect(InkColors.PINK_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect PURPLE = new ColoredFallingSporeBlossomParticleEffect(InkColors.PURPLE_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect RED = new ColoredFallingSporeBlossomParticleEffect(InkColors.RED_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect WHITE = new ColoredFallingSporeBlossomParticleEffect(InkColors.WHITE_COLOR);
	public static final ColoredFallingSporeBlossomParticleEffect YELLOW = new ColoredFallingSporeBlossomParticleEffect(InkColors.YELLOW_COLOR);
	
	public static final MapCodec<ColoredFallingSporeBlossomParticleEffect> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			ExtraCodecs.VECTOR3F.fieldOf("color").forGetter((effect) -> effect.color)
	).apply(instance, ColoredFallingSporeBlossomParticleEffect::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, ColoredFallingSporeBlossomParticleEffect> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.VECTOR3F, (effect) -> effect.color,
			ColoredFallingSporeBlossomParticleEffect::new
	);
	
	private final Vector3f color;
	
	public ColoredFallingSporeBlossomParticleEffect(int color) {
		this.color = SpectrumColorHelper.colorIntToVec(color);
	}
	
	public ColoredFallingSporeBlossomParticleEffect(Vector3f color) {
		this.color = color;
	}
	
	public ParticleType<ColoredFallingSporeBlossomParticleEffect> getType() {
		return SpectrumParticleTypes.COLORED_FALLING_SPORE_BLOSSOM;
	}
	
	public Vector3f getColor() {
		return this.color;
	}
	
}
