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

public class ColoredSparkleRisingParticleEffect implements ParticleOptions {
	
	public static final ColoredSparkleRisingParticleEffect BLACK = new ColoredSparkleRisingParticleEffect(InkColors.BLACK_COLOR);
	public static final ColoredSparkleRisingParticleEffect BLUE = new ColoredSparkleRisingParticleEffect(InkColors.BLUE_COLOR);
	public static final ColoredSparkleRisingParticleEffect BROWN = new ColoredSparkleRisingParticleEffect(InkColors.BROWN_COLOR);
	public static final ColoredSparkleRisingParticleEffect CYAN = new ColoredSparkleRisingParticleEffect(InkColors.CYAN_COLOR);
	public static final ColoredSparkleRisingParticleEffect GRAY = new ColoredSparkleRisingParticleEffect(InkColors.GRAY_COLOR);
	public static final ColoredSparkleRisingParticleEffect GREEN = new ColoredSparkleRisingParticleEffect(InkColors.GREEN_COLOR);
	public static final ColoredSparkleRisingParticleEffect LIGHT_BLUE = new ColoredSparkleRisingParticleEffect(InkColors.LIGHT_BLUE_COLOR);
	public static final ColoredSparkleRisingParticleEffect LIGHT_GRAY = new ColoredSparkleRisingParticleEffect(InkColors.LIGHT_GRAY_COLOR);
	public static final ColoredSparkleRisingParticleEffect LIME = new ColoredSparkleRisingParticleEffect(InkColors.LIME_COLOR);
	public static final ColoredSparkleRisingParticleEffect MAGENTA = new ColoredSparkleRisingParticleEffect(InkColors.MAGENTA_COLOR);
	public static final ColoredSparkleRisingParticleEffect ORANGE = new ColoredSparkleRisingParticleEffect(InkColors.ORANGE_COLOR);
	public static final ColoredSparkleRisingParticleEffect PINK = new ColoredSparkleRisingParticleEffect(InkColors.PINK_COLOR);
	public static final ColoredSparkleRisingParticleEffect PURPLE = new ColoredSparkleRisingParticleEffect(InkColors.PURPLE_COLOR);
	public static final ColoredSparkleRisingParticleEffect RED = new ColoredSparkleRisingParticleEffect(InkColors.RED_COLOR);
	public static final ColoredSparkleRisingParticleEffect WHITE = new ColoredSparkleRisingParticleEffect(InkColors.WHITE_COLOR);
	public static final ColoredSparkleRisingParticleEffect YELLOW = new ColoredSparkleRisingParticleEffect(InkColors.YELLOW_COLOR);
	
	public static final MapCodec<ColoredSparkleRisingParticleEffect> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			ExtraCodecs.VECTOR3F.fieldOf("color").forGetter((effect) -> effect.color)
	).apply(instance, ColoredSparkleRisingParticleEffect::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, ColoredSparkleRisingParticleEffect> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.VECTOR3F, (effect) -> effect.color,
			ColoredSparkleRisingParticleEffect::new
	);
	
	private final Vector3f color;
	
	public ColoredSparkleRisingParticleEffect(int color) {
		this.color = SpectrumColorHelper.colorIntToVec(color);
	}
	
	public ColoredSparkleRisingParticleEffect(Vector3f color) {
		this.color = color;
	}
	
	public ParticleType<ColoredSparkleRisingParticleEffect> getType() {
		return SpectrumParticleTypes.COLORED_SPARKLE_RISING;
	}
	
	public Vector3f getColor() {
		return this.color;
	}
	
	public static ParticleOptions of(int color) {
		return new ColoredSparkleRisingParticleEffect(color);
	}
	
}
