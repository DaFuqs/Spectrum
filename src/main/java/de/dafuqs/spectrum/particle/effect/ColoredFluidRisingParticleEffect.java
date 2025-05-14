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
import org.jetbrains.annotations.*;
import org.joml.*;

public class ColoredFluidRisingParticleEffect implements ParticleOptions {
	
	public static final ColoredFluidRisingParticleEffect BLACK = new ColoredFluidRisingParticleEffect(InkColors.BLACK_COLOR);
	public static final ColoredFluidRisingParticleEffect BLUE = new ColoredFluidRisingParticleEffect(InkColors.BLUE_COLOR);
	public static final ColoredFluidRisingParticleEffect BROWN = new ColoredFluidRisingParticleEffect(InkColors.BROWN_COLOR);
	public static final ColoredFluidRisingParticleEffect CYAN = new ColoredFluidRisingParticleEffect(InkColors.CYAN_COLOR);
	public static final ColoredFluidRisingParticleEffect GRAY = new ColoredFluidRisingParticleEffect(InkColors.GRAY_COLOR);
	public static final ColoredFluidRisingParticleEffect GREEN = new ColoredFluidRisingParticleEffect(InkColors.GREEN_COLOR);
	public static final ColoredFluidRisingParticleEffect LIGHT_BLUE = new ColoredFluidRisingParticleEffect(InkColors.LIGHT_BLUE_COLOR);
	public static final ColoredFluidRisingParticleEffect LIGHT_GRAY = new ColoredFluidRisingParticleEffect(InkColors.LIGHT_GRAY_COLOR);
	public static final ColoredFluidRisingParticleEffect LIME = new ColoredFluidRisingParticleEffect(InkColors.LIME_COLOR);
	public static final ColoredFluidRisingParticleEffect MAGENTA = new ColoredFluidRisingParticleEffect(InkColors.MAGENTA_COLOR);
	public static final ColoredFluidRisingParticleEffect ORANGE = new ColoredFluidRisingParticleEffect(InkColors.ORANGE_COLOR);
	public static final ColoredFluidRisingParticleEffect PINK = new ColoredFluidRisingParticleEffect(InkColors.PINK_COLOR);
	public static final ColoredFluidRisingParticleEffect PURPLE = new ColoredFluidRisingParticleEffect(InkColors.PURPLE_COLOR);
	public static final ColoredFluidRisingParticleEffect RED = new ColoredFluidRisingParticleEffect(InkColors.RED_COLOR);
	public static final ColoredFluidRisingParticleEffect WHITE = new ColoredFluidRisingParticleEffect(InkColors.WHITE_COLOR);
	public static final ColoredFluidRisingParticleEffect YELLOW = new ColoredFluidRisingParticleEffect(InkColors.YELLOW_COLOR);
	
	public static final MapCodec<ColoredFluidRisingParticleEffect> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			ExtraCodecs.VECTOR3F.fieldOf("color").forGetter((effect) -> effect.color)
	).apply(instance, ColoredFluidRisingParticleEffect::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, ColoredFluidRisingParticleEffect> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.VECTOR3F, (effect) -> effect.color,
			ColoredFluidRisingParticleEffect::new
	);
	
	private final Vector3f color;
	
	public ColoredFluidRisingParticleEffect(int color) {
		this.color = SpectrumColorHelper.colorIntToVec(color);
	}
	
	public ColoredFluidRisingParticleEffect(Vector3f color) {
		this.color = color;
	}
	
	public ParticleType<ColoredFluidRisingParticleEffect> getType() {
		return SpectrumParticleTypes.COLORED_FLUID_RISING;
	}
	
	public Vector3f getColor() {
		return this.color;
	}
	
	public static @NotNull ParticleOptions of(int color) {
		return new ColoredFluidRisingParticleEffect(color);
	}
	
}
