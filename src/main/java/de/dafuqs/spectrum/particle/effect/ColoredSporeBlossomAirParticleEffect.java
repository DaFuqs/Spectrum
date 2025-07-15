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

public class ColoredSporeBlossomAirParticleEffect implements ParticleOptions {
	
	public static final ColoredSporeBlossomAirParticleEffect BLACK = new ColoredSporeBlossomAirParticleEffect(InkColors.BLACK_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect BLUE = new ColoredSporeBlossomAirParticleEffect(InkColors.BLUE_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect BROWN = new ColoredSporeBlossomAirParticleEffect(InkColors.BROWN_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect CYAN = new ColoredSporeBlossomAirParticleEffect(InkColors.CYAN_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect GRAY = new ColoredSporeBlossomAirParticleEffect(InkColors.GRAY_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect GREEN = new ColoredSporeBlossomAirParticleEffect(InkColors.GREEN_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect LIGHT_BLUE = new ColoredSporeBlossomAirParticleEffect(InkColors.LIGHT_BLUE_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect LIGHT_GRAY = new ColoredSporeBlossomAirParticleEffect(InkColors.LIGHT_GRAY_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect LIME = new ColoredSporeBlossomAirParticleEffect(InkColors.LIME_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect MAGENTA = new ColoredSporeBlossomAirParticleEffect(InkColors.MAGENTA_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect ORANGE = new ColoredSporeBlossomAirParticleEffect(InkColors.ORANGE_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect PINK = new ColoredSporeBlossomAirParticleEffect(InkColors.PINK_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect PURPLE = new ColoredSporeBlossomAirParticleEffect(InkColors.PURPLE_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect RED = new ColoredSporeBlossomAirParticleEffect(InkColors.RED_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect WHITE = new ColoredSporeBlossomAirParticleEffect(InkColors.WHITE_COLOR);
	public static final ColoredSporeBlossomAirParticleEffect YELLOW = new ColoredSporeBlossomAirParticleEffect(InkColors.YELLOW_COLOR);
	
	public static final MapCodec<ColoredSporeBlossomAirParticleEffect> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			ExtraCodecs.VECTOR3F.fieldOf("color").forGetter((effect) -> effect.color)
	).apply(instance, ColoredSporeBlossomAirParticleEffect::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, ColoredSporeBlossomAirParticleEffect> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.VECTOR3F, (effect) -> effect.color,
			ColoredSporeBlossomAirParticleEffect::new
	);
	
	private final Vector3f color;
	
	public ColoredSporeBlossomAirParticleEffect(int color) {
		this.color = SpectrumColorHelper.colorIntToVec(color);
	}
	
	public ColoredSporeBlossomAirParticleEffect(Vector3f color) {
		this.color = color;
	}
	
	public ParticleType<ColoredSporeBlossomAirParticleEffect> getType() {
		return SpectrumParticleTypes.COLORED_SPORE_BLOSSOM_AIR;
	}
	
	public Vector3f getColor() {
		return this.color;
	}
	
}
